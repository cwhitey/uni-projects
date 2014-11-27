
#######################
# Author:Callum White #
# ID: 24571520        #
# Date: 30 May 2014   #
#######################

package PGMHelper;

use strict;
use Exporter;
use vars qw($VERSION @ISA @EXPORT @EXPORT_OK %EXPORT_TAGS);
use File::Basename;
use Scalar::Util qw(looks_like_number);

$VERSION     = 1.00;
@ISA         = qw(Exporter);
@EXPORT      = ();
@EXPORT_OK   = qw(errorOut getValuesFromName getPGMHeader outputDataToFile getFileContentsAsString);
%EXPORT_TAGS = ( DEFAULT  => [qw(&errorOut &outputDataToFile)],
                 All      => [qw(&errorOut &getValuesFromName &getPGMHeader &outputDataToFile &getFileContentsAsString)]);


#get values out of a filename to be used when constructing a PGM file
sub getValuesFromName {
  my $filename = $_[0];
  my $formatErrorMessage = $_[1];
  my @expectedExtensions = @_[2 .. ((scalar @_)-1)];  #count the rest of the arguments as 'allowed' extensions

  #check that filename is in the correct format
  (my $basename, my $dirname, my $fileExtension) = fileparse($filename, qr/\.[^.]*/);  #use fileparse from File::Basename
  unless($fileExtension ~~ @expectedExtensions){
    errorOut("Incorrect file type. Expecting a file extension of".join(',', @expectedExtensions).". Got '$fileExtension'.\n");
  }
  my @splitname = split('_', $basename);

  #die if basename was not in the desired format
  unless(@splitname >= 3){
    errorOut($formatErrorMessage);
  }
  my @values;
  my $i;
  for $i (0 .. 1){    #validate and then get number of rows and columns from name (last two elements)
    if(looks_like_number($splitname[$#splitname-$i])){
      $values[$i] = $splitname[$#splitname-$i];
    } else {
      errorOut($formatErrorMessage);
    }
  }
  my $outFilename = join('_', @splitname[0 .. $#splitname-2]).".pgm";
  my $rows = $values[0];
  my $columns = $values[1];

  return $outFilename, $rows, $columns;
}

sub getPGMHeader {
  return "P5\n#\n".$_[0]." ".$_[1]."\n".$_[2]."\n";
}

# outputDataToFile($filename, $data);
sub outputDataToFile {
  open(OUTFILE, '>', $_[0]);
  print OUTFILE $_[1];
  close(OUTFILE);
}

# getFileContentsAsString($filename, $dataVarDest, $length);
sub getFileContentsAsString {
	open(OUTFILE, '<', $_[0]);
	read OUTFILE, $_[1], $_[2];
	close(OUTFILE);
}

# Print the supplied error meessage and then exit with a 'success' value
sub errorOut { print($_[0]); exit 0; }

1;
