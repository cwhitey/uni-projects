//
//      FIT2070 - Assignment 3
//
//      Callum White - 24571520
//
//      assignment3.c - Calculate the sum of a range of numbers using a specified number of threads
//
//      To compile:
//              gcc -pthread -o assignment3 assignment3.c
//
//      To run:
//              ./assignment3 <number of threads> <increment for each thread>
//              IF THESE TWO PARAMETERS ARE NOT PROVIDED, THE PROGRAM WILL GIVE YOU A SEGMENTATION FAULT.
//

#include <pthread.h>
#include <stdio.h>
#include <semaphore.h>
#include <sys/time.h>
#include <errno.h>

//function prototype
void *addValues(void*);

//the main function
int main(int argc, char *argv[])
{
    //create timeval structs to record the total time the program takes to run;
    struct timeval totalTimeStart;
    struct timeval totalTimeFinish;
    //get current time
    gettimeofday(&totalTimeStart, NULL);

    //retrieve the values passed as parameters when the program was executed by the user
    long numThreads = atoi(argv[1]);
    long increment = atoi(argv[2]);

    //we need a list of threads so we can loop through 
    pthread_t threads[numThreads];

    //inform the user of the range that will have its sum calculated
    long mult = numThreads * increment;
    printf("Retrieving the summation of integers in the range of (0, %lu)\n", mult-1);

    //create an array of structs so we can calculate how long each thread takes to run
    struct timeval timeStart[numThreads];
    //array of structs to be used to compare against the start time of a thread
    struct timeval timeFinish[numThreads];

    long a=0;
    long b=increment;
    int err;
    //two-dimensional array of thread parameter arrays, to solve the issue of threads receiving non-unique parameter values (see report).
    long argList[numThreads][3];
    long x=0;
    //fill our two-dimensional array with the start and finish values for each thread
    //(each immediate element of the array is an array of {a, b, threadNum}, where a is the start value and b-1 is the end value)
    for(; x<numThreads; x++){
        argList[x][0] = a;
        argList[x][1] = b;
        argList[x][2] = x+1;
        a = b;
        b = b + increment;
    }
    //create each thread and, if successful, record the start time of the thread
    for(x=0; x<numThreads; x++){
        printf("Summing %lu to %lu\n", argList[x][0], argList[x][1]-1);
        err = pthread_create(&threads[x], NULL, addValues, &argList[x]);
        if(err)
        {
            printf("\nError in creating the addValues thread: ERROR code %d \n", err);
            return 1;
        }
    } 
    long sumOfIncrement=0;
    double num1, num2;
    //void* variable for pthread_join to put each thread's result in
    void *returnedVal;
    for(x=0; x<numThreads; x++){
        //wait for thread to finish;
        pthread_join(threads[x], &returnedVal);       

        //add increment to running total of sum from all threads
        sumOfIncrement = sumOfIncrement + (long)returnedVal;
    }
    //print result
    printf("The sum of integers from 0 to %lu is: %lu\n", mult-1, sumOfIncrement);

    //get current time and calculate total running time
    gettimeofday(&totalTimeFinish, NULL);
    num1 = totalTimeStart.tv_sec+(totalTimeStart.tv_usec/1000000.0);
    num2 = totalTimeFinish.tv_sec+(totalTimeFinish.tv_usec/1000000.0);
    //print total running time
    printf("Total running time: %.6lf seconds\n", num2-num1);
}

//to be run as a thread
//calculates the sum of the integers whithin a given range
//returns the sum
void *addValues(void *data)
{
    //convert void* parameter into a format we can index as a list of integers
    long *input = (long*)data;
    long x=input[0];
    long sum=0;

    //set up timer
    struct timeval timeStart, timeFinish;
    double num1, num2;
    gettimeofday(&timeStart, NULL);

    //calculate sum
    for(; x<(input[1]); x++){
        sum=sum+x;
    }

    //calculate current time
    gettimeofday(&timeFinish, NULL);
    //calculate difference between start time and finish time of the thread
    num1 = timeStart.tv_sec+(timeStart.tv_usec/1000000.0);
    num2 = timeFinish.tv_sec+(timeFinish.tv_usec/1000000.0);
    printf("Thread %lu returned %lu and took %.6lf seconds\n", input[2], sum, num2-num1);

    //return a pointer to the sum
    long *sumPointer = sum;
    return sumPointer;
}
