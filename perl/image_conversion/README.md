Name: Callum White
Student ID: 24571520

# General Information
* All tasks successfully run without errors.
* I have created a module called PGMHelper and used this for subroutines which would otherwise be defined in multiple files.
* I included a comment in my output PGM files because I found that the image editor I was using to render P5 PGM files (InkScape) would'nt load the file if it did not have a comment line.

# Permissions Information
If there are ever issues with running an executable, run:
   * chmod a+x [filename]

# Execution Information
* pgm2img.pl
  * Usage: ./pgm2img.pl [filename.pgm]
* img2pgm.pl
  * Usage: ./img2pgm.pl [filename_nrows_ncols.img]
* smg2pgm.pl
  * Usage: ./smg2pgm.pl [filename_nrows_ncols.smg] hilo | lohi | one_byte
  * All extra arguments are optional.
  * If no arguments are added, 'hilo' is used.
  * If both 'hilo' and 'lohi' are specified, 'lohi' is used.
* threshold2pgm.pl
  * Usage: threshold2pgm [filename.[is]mg] threshold_percentage
  * 'threshold_percentage' must be specified.
  * The input file can be either a .img or .smg file.

# Supported and Unsupported Functionality
* pgm2img.pl
  * All functionality supported.
  * The header and the image can be on one line. e.g. The following file is supported: P5 1 1 255 a
  * Includes error checking for the existence of the input file and the header format.
* img2pgm.pl
  * Does not need to store the .img file data before moving it to the new file.
  * Includes error checking for the existence of the input file.
  * Includes error checking for the filename format.
* smg2pgm.pl
  * Includes error checking for the existence of the input file.
  * Includes error checking for the filename format.
  * 'hilo', 'lohi', and 'one_byte' functionality supported.
  * 'lohi' byte swaps done by a simple single line regex.
  * Checks if the input filename's columns * rows information results in an even number of bytes, as .smg files should always have an even number of bytes.
* threshold2pgm.pl
  * Includes error checking for the existence of the input file.
  * Includes error checking for the filename format.
  * Will threshold the included image for any 'threshold_percentage' > 0 and < 100.
  * When given a .smg file, it also checks for an even amount of bytes, as smg2pgm does.

# Bugs and Limitations
* Bugs relevant to all tasks:
  * Unable to specify an output file location for any of the tasks. The output file just gets put in the current working directory of the program.

# Notes
* pgm2img.pl
  * Certain text editors add a newline at the end of the file after saving it. This was an issue for me when creating my own pgm files as pgm2img uses 'tail' to get the bytes, 
so this would grab the added newline and leave off some of the data. This isn't so much a bug, but more a weird issue I faced when testing this program. I didn't have this issue 
when the other tasks as then use read() and the number of columns and rows to get the information in the file, ignoring any extra jargon bytes that happen to be at the end of the 
file.
