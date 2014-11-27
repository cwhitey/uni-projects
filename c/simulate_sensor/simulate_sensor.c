//      FIT2070 - Assignment 2
//
//      Callum White - 24571520
//
//      assignment2.c - simulate sensor reading and writing
//
//      To compile:
//              gcc -pthread -o assignment2 assignment2.c
//
//      To run:
//              ./assignment2
//

#include <pthread.h>
#include <stdio.h>
#include <semaphore.h>

//defined variable for our buffer size
#define BUFFER_SIZE 50

pthread_t tid1, tid2, tid3;
// Function prototypes
void *updateValues(void *), *arg1;
void *readValues(void *), *arg2;
void *processValues(void *), *arg3;
sem_t sensorMutex, arrayMutex;

int light = 0;
int temp = 0;
int humid = 0;
int lightSum = 0;
int tempSum = 0;
int humidSum = 0;
int counter = 1;            //the total number of values that have been processed into the buffer

int arrayIndexEnd = 0;      //index of the last value added to the circular buffer
int arrayIndexStart = 0;    //index of the next item to grab out of the circular buffer
int numInBuffer = 0;        //total number of values currently in the buffer

//cicular buffers for each of the sensor values
int lightArray[BUFFER_SIZE];
int tempArray[BUFFER_SIZE];
int humidArray[BUFFER_SIZE];
int timeStamp[BUFFER_SIZE];

//booleans for use with our busy-wait's
_Bool valuesRead = 0; 
_Bool valuesGenerated = 0; 
_Bool valuesPrinted= 0;

//the main function
int main()
{
    int err;

    //initialise our semaphores
    sem_init(&sensorMutex, 0, 1);
    sem_init(&arrayMutex, 0, 1);

    valuesRead = 1;
    valuesGenerated = 0; 
    valuesPrinted = 1; 

    //initialise the random number generator
    srand(getpid());

    //try to start pthread 1 by calling pthread_create()
    printf ("Creating Update Values Thread\n");
    err = pthread_create(&tid1, NULL, updateValues, arg1);
    if(err)
    {
        printf ("\nError in creating the updateValues thread: ERROR code %d \n", err);
        return 1;
    }
    //try to start pthread 2 by calling pthread_create()
    printf ("Creating Read Values Thread\n");
    err = pthread_create(&tid2, NULL, readValues, arg2);
    if (err)
    {
        printf ("\nError in creating the readValues thread: ERROR code %d \n", err);
        return 1;
    }
    //try to start pthread 3 by calling pthread_create()
    printf ("Creating Print Values Thread\n");
    err = pthread_create(&tid3, NULL, processValues, arg3);
    if (err)
    {
        printf ("\nError in creating the processValues thread: ERROR code %d \n", err);
        return 1;
    }
    //wait for all threads to complete
    pthread_join(tid1, NULL);
    pthread_join(tid2, NULL);
    pthread_join(tid3, NULL);
}

//updates our three global variables to random values
void *updateValues(void *param)
{
    while(1){
        //busy-wait
        while (valuesRead == 0) {
        }
        //get the next values for light, humidity, and temperature, using the rand() function
        light = rand() % 100+1;
        temp = rand() % 100+1;
        humid = rand() % 100+1;
        //update busy-wait triggers
        valuesGenerated = 1;
        valuesRead = 0;
    }
}

//puts the three global variables into their respective buffers
void *readValues(void *param)
{
    while(1){
        //busy-wait
        while (valuesPrinted == 0) {
        }
        while (valuesGenerated == 0) {
        }
        //assign the read values to the array at the current position
        lightArray[arrayIndexEnd] = light;
        tempArray[arrayIndexEnd] = temp;
        humidArray[arrayIndexEnd] = humid;
        timeStamp[arrayIndexEnd] = counter;
        counter++;
        //update busy-wait triggers
        valuesRead = 1;
        valuesGenerated = 0;
        valuesPrinted = 0;
        //update the position of 'end' index of the circle buffers
        arrayIndexEnd = (arrayIndexEnd+1) % BUFFER_SIZE;
        if(numInBuffer < BUFFER_SIZE){
            numInBuffer++;
        } else{
            //here we must be overwriting a value. Move arrayIndexStart to next-oldest
            arrayIndexStart = (arrayIndexStart+1) % BUFFER_SIZE;
        }
    }
}

//print the next global variables in the buffer, along with the aggregate and the average of all values
void *processValues(void *param)
{
    while(1){
        //busy-wait
        while (valuesPrinted == 1) {
        }
        //print out all values
        int nextLight, nextTemp, nextHumid;
        //update values at the current 'start' position in the arrays so we can print them
        nextLight = lightArray[arrayIndexStart];
        lightSum += lightArray[arrayIndexStart];
        nextTemp = tempArray[arrayIndexStart];
        tempSum += tempArray[arrayIndexStart];
        nextHumid = humidArray[arrayIndexStart];
        humidSum += humidArray[arrayIndexStart];
        int currentTimeStamp = timeStamp[arrayIndexStart];
        //update the position of the 'start' index of the circle buffers
        arrayIndexStart = (arrayIndexStart+1) % BUFFER_SIZE;
        //decrement number of items in the buffers, as we have just used one
        numInBuffer--;
        //calculate averages
        int lightAvg, tempAvg, humidAvg;
        lightAvg = lightSum/currentTimeStamp;
        tempAvg = tempSum/currentTimeStamp;
        humidAvg = humidSum/currentTimeStamp;
        //print out all the values in a nice format
        printf("\t\tCurrent\t\tAggregate\tAverage");
        printf("\nLight:\t\t%d\t\t%d\t\t%d", nextLight, lightSum, lightAvg);
        printf("\nTemperature:\t%d\t\t%d\t\t%d", nextTemp, tempSum, tempAvg);
        printf("\nHumidity:\t%d\t\t%d\t\t%d", nextHumid, tempSum, humidAvg);
        printf("\n");
        printf("\n");
        //wait a bit so we can see the values printing out one by one (program still works normally if this is deleted)
        sleep(2);
        valuesPrinted = 1;
    }
}
