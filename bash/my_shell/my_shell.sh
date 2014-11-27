#!/bin/bash

#Made by Callum White (24571520) on 8/9/2013

#ASSIGNMENT NOTES
#Thorough testing has been performed on this program.
#Various invalid inputs have been tried for both the main command and secondary parameters for each command.
#
#This program is able to detect wither the use running to program is running as root, or as a normal user.
#The prompt displays a different character for each type of user.
#
#For such a simple program, there isn't much that can be said about the efficiency of it.
#When the program receives input, it simply checks it and runs the appropriate bash command.
#It then waits for more input, until the user enters 'quit' and presses ENTER.
#With the current code structure, the program can easily be extended to accept more commands, and support extra SHELL features,
#such as piping into other commands, or pushing output into files.
#
#Thank you for viewing my assignment submission




processInput(){
	command="$1"
	command=$(echo $command | tr [A-Z] [a-z])		#change command input to lowercase so we can accept uppercase commands
	#inputs are sanitized by quoting then when they're used (e.g. "$@")
	#(otherwise a user could potentially delete the filesystem by typing "echo hi; rm -rf /")
	#the 'shift' command moves arguments along to the left (i.e. value in $2 becomes value in $1 and so on)
	case $command in
		"cd" )
			shift
			cmdCd "$@"
			;;
		"clr" )
			shift
			cmdClr "$@"
			;;
		"dir" )
			shift
			cmdDir "$@"
			;;
		"echo" )
			shift
			cmdEcho "$@"
			;;
		"environ" )
			shift
			cmdEnviron "$@"
			;;
		"help" )
			shift
			cmdHelp "$@"
			;;
		"pause" )
			shift
			cmdPause "$@"
			;;
		"quit" )
			shift
			cmdQuit "$@"
			;;
		* )								#if they enter an unknown command, print error message
			echo "-shell: $command: command not found"
			;;
	esac
}

################################
# Individual command functions #
################################

cmdCd(){								#change the working directory to whichever is specified
	if [[ $# -ge 1 ]]; then				#check there is at least one argument (if more than one, we just use the first arg)
		if [[ -d "$1" ]]; then			#check if first argument is a directory
			cd "$1"
		else
			echo "-shell: cd: $1: argument is not a directory"
		fi
	else								#if no arguments, print error message
		echo "-shell: cd: argument expected"
	fi
}

cmdClr(){								#clear all previous lines of the shell
	clear
}

cmdDir(){								#list all files and directories within a specified directory
	if [[ $# -ge 1 ]]; then				#check there is at least one argument (if more than one, we just use the first arg)
		if [[ -d "$1" ]]; then			#check if first argument is a directory
			ls "$1"
		else
			echo "-shell: cd: $1: argument is not a directory"
		fi
	else								#if only given one argument, list contents of working directory
		ls
	fi
}
cmdEcho(){								#echo all arguments
	echo "$@"
}

cmdEnviron(){							#use system command 'printenv' to display environment variables
	printenv
}

cmdHelp(){								#use 'less' to display our help.txt file (kept in the same file as this program)
	less help.txt
}

cmdPause(){								#use read to make it seem like the shell is paused (as there is no 'pause' command)
	read -p "Press ENTER to resume..."
}

cmdQuit(){								#print farewell message and exit with a success value of 0
	echo "Exiting..."
	exit 0
}


#######################
# Main body of script #
#######################

#print welcome message
echo ""
echo "Welcome to Callum's SHELL!"
echo "For help, type 'help' and press ENTER"
echo ""

#set the prompt character based off whether the user is admin or not
#(running this script with sudo will give us a '#' prompt character)
if [[ $EUID -ne 0 ]]; then					#check $EUID of user. (0 is admin, otherise they're just a normal user).
	promptChar="$"							#not running as admin
else
	promptChar="#"							#running as admin
fi

while [[ true ]]							#just loop forever. user can enter 'quit' to exit.	
do
	prompt="$PWD $(whoami)$promptChar"		#create prompt line - printed before the user enters a command
	read -p "$prompt " input				#ask for user input, and save it into $input
	processInput $input
done
