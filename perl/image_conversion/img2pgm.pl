#!/usr/bin/perl -w

#######################
# Author:Callum White #
# ID: 24571520        #
# Date: 30 May 2014   #
#######################

use autodie;
use PGMHelper qw(:All);

if(scalar @ARGV == 1){        #check number of arguments
  $filename = $ARGV[0];
  unless(-e $filename){       #check if file is valid.
    errorOut("Invalid input file.\n");
  }

  #get the information stored in the filename by using a method defined in PGMHelper
  @supportedExtensions = ('.img');
  ($outFilename, $columns, $rows) = getValuesFromName($filename, "Incorrect naming format. Expecting imgfilename_nrows_ncols.img\n", @supportedExtensions);
  $maxVal = 255;                                  #set maxval to 255 as we know there cannot be a value greater than that as .img files use 8 bits

  #construct header
  $header = getPGMHeader($columns, $rows, $maxVal);

  #output to new file
  `echo "$header" > $outFilename`;
  `cat $filename >> $outFilename`;

  print ".img file converted to .pgm file: ".$outFilename."\n";

} else {
  errorOut("Incorrect number of arguments.\nUsage: ./img2pgm.pl [filename_nrows_ncols.img]\n");
}
