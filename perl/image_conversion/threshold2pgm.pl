#!/usr/bin/perl -w

#######################
# Author:Callum White #
# ID: 24571520        #
# Date: 30 May 2014   #
#######################

use autodie;
use File::Basename;
use PGMHelper qw(:All);

if(scalar @ARGV > 1){          #check number of arguments
  $filename = $ARGV[0];
  $threshVal = $ARGV[1];
  unless(-e $filename){        #check if file is valid.
    errorOut("Invalid input file.\n");
  }
  unless($threshVal >= 1 && $threshVal <= 99){
    errorOut("Invalid threshold value. Must be between 1 and 99 inclusive.");
  }

  @supportedExtensions = ('.img', '.smg');
  ($outFilename, $columns, $rows) = getValuesFromName($filename, "Incorrect naming format. Expecting filename_nrows_ncols.[is]mg\n", @supportedExtensions);
  $totalPixels = $columns * $rows;
	$totalBytes = $columns * $rows;

  #get file extension
  $fileExtension = $filename;
  $fileExtension =~ s/.*\.(.*)/.$1/;

  #read the entire file into a scalar variable using a method from PGMHelper.pm
  my $data = '';
  if($fileExtension eq '.smg'){
		getFileContentsAsString($filename, $data, $totalBytes);
	} elsif ($fileExtension eq '.img') {
		getFileContentsAsString($filename, $data, $totalPixels);
	}


  #check that the data is valid for a .smg file
  if($fileExtension eq '.smg' && (length($data) % 2) != 0){
    errorOut("File does not have an even number of bytes.\n");
  }

  #use hash %histogram to gather individual pixel frequencies
  %histogram = ();
  $current = '';
  $currentUnpacked = '';
  $strIndex = 0;
  $size = 0;
  for $i (0 .. (($totalPixels)-1)){
    if($fileExtension eq '.smg'){
      $strIndex = $i*2;
      $size = 2;
    } else {
      $strIndex = $i;
      $size = 1;
    }
    $current = substr($data, $strIndex, $size);    #get next byte from data string
    if($fileExtension eq '.smg'){
      $currentUnpacked = unpack('n', $current);
    } else {
      $currentUnpacked = unpack('C', $current);
    }
    if(exists($histogram{$currentUnpacked})){     #if current byte is in histogram already, increment the value
      $histogram{$currentUnpacked} += 1;
    } else {                              #otherwise, add it with a value of 1
      $histogram{$currentUnpacked} = 1;
    }
  }


  #get threshold level
  $threshLevel = $totalPixels * ($threshVal / 100);

  #iterate over the values of the histogram and assign values to a new histogram which will be used to modify the data
  $runningTotal = 0;
  %valuesToUse = ();
  $newVal = 0;
  $maxVal = 2**8-1;
  @keys = keys(%histogram);
  @sortedKeys = sort {$a <=> $b} @keys;
  for $k (@sortedKeys){
    $runningTotal += $histogram{$k};
    $valuesToUse{$k} = $newVal;
    if($runningTotal > $threshLevel){    #start thresholding to white if the threshold level has been met
      $newVal = $maxVal;
    }
  }

  #now form a new string which is a thresholded version of the original data
  $current = '';
  $currentUnpacked = '';
  $newData = '';
  $strIndex = 0;
  $size = 0;
  for $i (0 .. (($totalPixels)-1)){
    if($fileExtension eq '.smg'){
      $strIndex = $i*2;
      $size = 2;
    } else {
      $strIndex = $i;
      $size = 1;
    }
    $current = substr($data, $strIndex, $size);      #get next byte from data string
    if($fileExtension eq '.smg'){
      $currentUnpacked = unpack('n', $current);
    } else {
      $currentUnpacked = unpack('C', $current);
    }
    $newData .= pack('C', $valuesToUse{$currentUnpacked});
  }

  #create header
  $header = getPGMHeader($columns, $rows, $maxVal);

  #create proper outFilename
  $outFilename =~ s/(.*)\./$1_$threshVal./;

  #write the data to our output file
  outputDataToFile($outFilename, $header.$newData);

  print "$fileExtension file converted to .pgm file: ".$outFilename."\n";

} else {
  errorOut("Incorrect number of arguments.\nUsage: threshold2pgm [filename.[is]mg] threshold_percentage\n");
}
