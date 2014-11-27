#!/usr/bin/perl -w

#######################
# Author:Callum White #
# ID: 24571520        #
# Date: 30 May 2014   #
#######################

use autodie;
use File::Basename;
use PGMHelper;

if(scalar @ARGV == 1){        #check number of arguments
  $filename = $ARGV[0];
  unless(-e $filename){       #check if file is valid.
    errorOut("Invalid input file.");
  }
  #check that filename is in the correct format
  ($basename, $dirname, $fileExtension) = fileparse($filename, qr/\.[^.]*/);
  unless($fileExtension eq '.pgm'){
    errorOut("Incorrect file type. Expecting a file extension of '.pgm'. Got '$fileExtension'.");
  }

  #open the file in read mode
  open(my $file, "<", $filename);

  #validate the magic (we only want P5 .pgm's)
  $magic = <$file>;  #magic value
  chomp $magic;
  if($magic ne "P5"){
    errorOut("Incorrect PGM file type. Expecting P5. Got '$magic'.");
  }

  #pull apart the header
  while((scalar @values) < 3 && defined($input = <$file>)){  #loop until we've found three numbers
    if(substr($input, 0, 1) eq "#"){  #is comment
      print "Comment: $input";        #skip comment and get next line
    } else {
      #get numbers in line
      chomp $input;
      @splitInput = split(/ /, $input);
      for $i (0 .. $#splitInput){
        if(@values < 3){
          $values[scalar @values] = $splitInput[$i];
        }
      }
    }
  }

  $rows = $values[0];
  $columns = $values[1];
  $maxVal = $values[2];
  $totalBytes = $rows * $columns;

  #validate maximum pixel value (we can only support 255 or less)
  if($maxVal > 255){
    errorOut("Pixel values must take up no more than 1 byte to convert to a .img file");
  }

  #generate name to use for the output file
  $outFilename = $basename."_"."$rows"."_"."$columns".".img";
  `tail -c $totalBytes $filename > $outFilename`;    #get pixel bytes and push them into new file
  
  print ".pgm file converted to .img file: ".$outFilename."\n";

} else {
  errorOut("Incorrect number of arguments.\nUsage: ./pgm2img.pl [filename.pgm]");
}
