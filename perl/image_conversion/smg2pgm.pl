#!/usr/bin/perl -w

#######################
# Author:Callum White #
# ID: 24571520        #
# Date: 30 May 2014   #
#######################

use autodie;
use PGMHelper qw(:All);

if(scalar @ARGV > 0){          #check number of arguments
  $filename = $ARGV[0];
  unless(-e $filename){        #check if file is valid.
    errorOut("Invalid input file.\n");
  }

  #get the information stored in the filename by using a method defined in PGMHelper
  @supportedExtensions = ('.smg');
  ($outFilename, $columns, $rows) = getValuesFromName($filename, "Incorrect naming format. Expecting smgfilename_nrows_ncols.img\n", @supportedExtensions);
  $totalPixels = $columns*$rows;
  $totalBytes = $totalPixels*2;

  #check extra options
  if(scalar @ARGV > 1){
    for $i (1 .. $#ARGV){
      if($ARGV[$i] eq "lohi"){
        $lohi = 1;
      } elsif($ARGV[$i] eq "one_byte") {
        $one_byte = 1;
      }
    }
  }

  #read the entire file into a scalar variable using a method from PGMHelper.pm
  my $smgData = '';
  getFileContentsAsString($filename, $smgData, $totalBytes);

  if((length($smgData) % 2) != 0){
    errorOut("File does not have an even number of bytes.\n");
  }

  #apply the conversions required for the lohi and one_byte options
  #lohi option
  if($lohi){                            #swap each pair of bytes around (the 's' modifier make sure this works for newlines)
    $smgData =~ s/(.)(.)/$2$1/msg;
  }

  #get the maxVal we're doing this after the lohi option has been process, as the maxVal would have changed.
  $maxVal = 0;
  $value = 0;
  for $i (0 .. (($totalPixels)-1)){
    $value = unpack('n', substr($smgData, $i*2, 2)); #get next pixel from data string
    if($value > $maxVal){
      $maxVal = $value;
	}
  }

  #one_byte option
  if($one_byte){                                     #for each two bytes, convert them to the nearest equivalent one byte value
    $reducedData = "";
    for $i (0 .. int((length($smgData)-1)/2)){
      $byteVal = unpack("n", substr($smgData, $i*2, 2));
      $byteVal = int((255 / $maxVal) * $byteVal);	  #dividing by maxVal instead of 2**16-1 so that we get the correct values

      #now assign $byteVal into a new string (it now shouldn't take up more than one byte)
      $byteVal = pack("C", $byteVal);

      $reducedData .= $byteVal;
    }
    $smgData = $reducedData;
    $maxVal = 255;
  }

  print("maxVal: ".$maxVal."\n");

  #create header
  $header = getPGMHeader($columns, $rows, $maxVal);

  #write the data to our output file
  outputDataToFile($outFilename, $header.$smgData);

  print ".smg file converted to .pgm file: ".$outFilename."\n";

} else {
  errorOut("Incorrect number of arguments.\nUsage: ./smg2pgm.pl [filename_nrows_ncols.smg] hilo | lohi | one_byte\n");
}
