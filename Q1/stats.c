#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

//Jared Enaje Assignment 3 Q1 Threads

int count = 0;
int average;
int maximum;
int minimum;

void *avgNum(void *arg){
    int *pVal = (int *) arg;
    int total = 0;
    for(int i = 0; i < count; i++){
        total = total + pVal[i];
    }//end for
    //printf("total = %d",total);
    average = (total/count);
    pthread_exit(0);
}//end average method

void *findMax(void *arg){
    int *pVal = (int *) arg;
    maximum = pVal[0];	
    for(int i = 0; i < count; i++){
	if(pVal[i] > maximum){
	maximum = pVal[i];
	}//end for
    }//end for
    pthread_exit(0);
}//end max method

void *findMin(void *arg){
    int *pVal = (int *) arg;
    minimum = pVal[0];	
    for(int i = 0; i < count; i++){
	if(pVal[i] < minimum){
	minimum = pVal[i];
	}//end for
    }//end for
    pthread_exit(0);
}//end min method

int main(int argc, char *argv[])
{

    //pass in array of ints
    int *nums = (int*)malloc((argc-1)*sizeof(int));

    for(int i = 1; i < argc; i++){
        nums[i-1] = strtol(argv[i],NULL,0);
        count++;
        //printf(" %d \n",nums[i-1]);
    }//end for

    pthread_t avgThread;
    pthread_t maxThread;
    pthread_t minThread;

    pthread_create(&avgThread,NULL,avgNum,(void*)nums);
    pthread_create(&maxThread,NULL,findMax,(void*)nums);
    pthread_create(&minThread,NULL,findMin,(void*)nums);

    pthread_join(avgThread,NULL);
    pthread_join(maxThread,NULL);    
    pthread_join(minThread,NULL);

    printf("The average value is %d \n", average);
    printf("The minimum value is %d \n", minimum);
    printf("The maximum value is %d \n", maximum);
    
    free(nums);
}
