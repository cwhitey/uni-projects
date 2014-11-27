//
//      race.c - demonstrate a Race Condition
//
//      To compile:
//              gcc -lpthread -o race race.c
//
//      To run:
//              race
//


#include <pthread.h>
#include <stdio.h>
#include <semaphore.h>

pthread_t tid1, tid2;

// Function prototypes
void *pthread1(void *), *arg1;
void *pthread2(void *), *arg2;

// This is the global variable shared by both threads, initialised to 50
// Both threads will try to update its value simultaneously
int     theValue = 50;
// Our mutex variable
//to use mutex... just lock it before variables are modified/read and then unlock it when done.
pthread_mutex_t   mutex = PTHREAD_MUTEX_INITIALIZER; //using mutex instead of semaphores so now the below code is broken


// Now the main function. A function is called a method in Java
int main()
{
        int     err;

        // initialise the random number generator
        srand (getpid());

        // initialise the semaphore
        sem_init(&mutex, 0, 1);

        // try to start pthread 1 by calling pthread_create()
        err = pthread_create(&tid1, NULL, pthread1, arg1);
        if(err)
         {
          printf ("\nError in creating the thread 1: ERROR code %d \n", err);
          return 1;
         }

        // try to start pthread 2 by calling pthread_create()
        err = pthread_create(&tid2, NULL, pthread2, arg1);
        if (err)
         {
          printf ("\nError in creating the thread 1: ERROR code %d \n", err);
          return 1;
         }

        // wait for both threads to complete
        pthread_join(tid1, NULL);
        pthread_join(tid2, NULL);

        // display the final value of variable theValue
       printf ("The final value of theValue is %d \n", theValue);
}


// this is the first thread - it increments the global variable theValue
void *pthread1(void *param)
{
        int a;
        printf("\nthread 1 started\n");
        //***** The critical section of thread 1
        sleep(rand() & 1);      // encourage race condition
        a = theValue;
        sleep(rand() & 1);      // encourage race condition
        a++;                    // increment the value of theValue by 1
        sleep(rand() & 1);      // encourage race condition
        theValue = a;
        //***** The end of the critical section of thread 1
        sem_post(&mutex);
        printf("\nthread 1 now terminating\n");
}


// this is the second thread - it decrements the global variable theValue
void *pthread2(void *param)
{
        int a;
        printf("\nthread 2 started\n");
        sem_wait(&mutex);
        //***** The critical section of thread 2
        sleep(rand() & 1);      // encourage race condition
        a = theValue;
        sleep(rand() & 1);      // encourage race condition
        a--;                    // decrement the value of theValue by 1
        sleep(rand() & 1);      // encourage race condition
        theValue = a;
        //***** The end of the critical section of thread 2
        sem_post(&mutex);
        printf("\nthread 2 now terminating\n");
}
