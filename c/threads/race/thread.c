//
//      pthread.c
//      Demonstration of multiple threads
//
//      To compile:
//              gcc  -lpthread -o thread thread.c
//
//      To run:
//              thread
//


#include <pthread.h>
#include <stdio.h>

pthread_t tid1, tid2;

// Function prototypes
void *pthread1(void *), *arg1;
void *pthread2(void *), *arg2;

// Now the main function. A function is called a method in Java
int main()
{
        int     err;

        // try to start pthread 1 by calling pthread_create()
        err = pthread_create(&tid1, NULL, pthread1, arg1);
        if (err)
         {
          printf("\n Error in creating the thread 1: ERROR code %d \n", err);
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
}


// this is the first thread - it just does a whole lot of counting to take up
// some CPU time
void *pthread1(void *param)
{
        float a;
        int i, j;

        a=0;
        printf("\nthread 1 is executing\n");

        for ( i=0; i<50000; i++)
                for ( j=0; j<20000; j++)
                {
                  a++;
                };

        // we don't bother to display the final value of a
        printf("\nthread 1 now terminating\n");
}


// this is the second thread - it too just does a whole lot of counting
// to waste some CPU time
void *pthread2(void *param)
{
        float a;
        int i,j;

        printf("\nthread 2  has started\n");

        for ( i=0; i<50000; i++)
                for ( j=0; j<20000; j++)
                {
                        a++;
                };

        // we don't bother to display the final value of a
        printf("\nthread 2 now terminating\n");
}
