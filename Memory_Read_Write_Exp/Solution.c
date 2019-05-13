/*

Name :- Siddharth Nahar
Entry No :- 2016csb1043
Date :- 5/11/18
Purpose :- 

	1. Calculate Effective read/write Speed of Memory Access
	2. Use of memcpy and memset for reading or writing consecutive bytes

*/

/*Include all libraries required*/
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>

/*Read k consecutive bytes from pointe*/
double readBytes(unsigned char *Test,unsigned int TotSize,int k){
	
	clock_t t = clock();

	/*Memcpy for simultaneous reading of bytes*/
	char* tmp = malloc(k*sizeof(char));


	/*Increment by k bytes and use memcpy to read consecutive memory*/
	for(unsigned int index = 0;index < TotSize;index += k){
	
		/*Check for boundary condition*/
		unsigned int min = (index + k >= TotSize?(TotSize-index):k);

		memcpy(tmp, Test + index, min*sizeof(char));
	}

	t = clock() - t;

	free(tmp);
	/*Caclulate time in seconds*/
	return ((double)t)/CLOCKS_PER_SEC;
}

/*Write k consecutive bytes and calculate time*/
double writeBytes(unsigned char *Test,unsigned int TotSize,int k){

	unsigned int index = 0;

	clock_t t = clock();
	/*Increment by k bytes and use memset to allocate consecutive memory*/
	for(index = 0;index < TotSize;index += k){
	
		/*Check for boundary condition*/
		unsigned int min = ((index + k >= TotSize)?(TotSize-index):k);

		/*Memset for simultaneous allocation*/
		memset(Test + index, 0xAB, min*sizeof(char));
	}

	t = clock() - t;

	/*Caclulate time in seconds*/
	return ((double)t)/CLOCKS_PER_SEC; 
}

/*Main Run Function For C file to run*/
int main(){


	FILE *fp = fopen("Output.txt","w");
	/*Tests to be run on this size of array values*/
	int Nbytes[] = {10,20,40,80,160,320,640,1280};

	//int Nbytes[] = {320};
	int Nlen = 8;

	/*To check consecutive this many consecutive places*/
	int k[] = {10,20,30,40,50,60};

	int klen = 6;

	double maxSpeed = 0;

	double maxSpeedWrite = 0,maxSpeedRead = 0;
	int bestN = 0,bestK = 0;
	int bestNWrite = 0,bestKWrite = 0;
	int bestNRead = 0,bestKRead = 0;
	/*Run Loop for each Combination of N,K*/
	for(int i = 0;i < Nlen;i++){

		unsigned int TotSize = Nbytes[i]*1024*1024;  /*in MB*/

		//printf("%d",TotSize);
			
		/*Read/write operations are performed in bytes so create char pointer and allocate Heap memory*/
		unsigned char *Test = (unsigned char*)malloc(TotSize*sizeof(char));

		/*Operation 1 : Write single bytes to memory doesn't depend on k*/
		double Op1 = writeBytes(Test,TotSize,1);

		/*Operation 3 : Read single bytes from memory*/
		double Op3 = readBytes(Test,TotSize,1);		

		for(int j = 0;j < klen;j++){

			//printf("Started jth Iteration\n");

			fprintf(fp,"For N = %d MB and k = %d bytes :- \n",Nbytes[i],k[j]);
 			

			/*Operation 1 :- Initialization by single bytes*/
			fprintf(fp,"Time Taken after Writing Single bytes : %f secs and Effective Speed : %.2f MB/s\n",Op1,Nbytes[i]/Op1);

			/*Operation 2 :- Initializing K bytes*/
			double Op2 = writeBytes(Test,TotSize,k[j]);				
			fprintf(fp,"Time Taken after Writing %d bytes : %f secs and Effective Speed : %.2f MB/s\n",k[j],Op2,Nbytes[i]/Op2);

			/*Operation 3 :- Reading Single bytes from array*/
			fprintf(fp,"Time Taken after Reading Single bytes : %f secs and Effective Speed : %.2f MB/s\n",Op3,Nbytes[i]/Op3);

			/*Operation 4 :- Reading consecutive k bytes from memory*/
			double Op4 = readBytes(Test,TotSize,k[j]);
			fprintf(fp,"Time Taken after Reading %d bytes : %f secs and Effective Speed : %.2f MB/s\n",k[j],Op4,Nbytes[i]/Op4);

			

			double avgSpeed = (Nbytes[i])/(Op1+Op2+Op3+Op4);
			double avgSpeedWrite = (Nbytes[i])/(Op1 + Op2);
			double avgSpeedRead = (Nbytes[i])/(Op3 + Op4);

			/*Get Max of Total Average Speed*/
			if(avgSpeed >= maxSpeed){
			
				maxSpeed = avgSpeed;
				bestN = Nbytes[i];
				bestK = k[j];
			}

			/*Get Max of Total Writing Speed*/
			if(avgSpeedWrite >= maxSpeedWrite){
			
				maxSpeedWrite = avgSpeedWrite;
				bestNWrite = Nbytes[i];
				bestKWrite = k[j];
			}

			/*Get MAx of Total Reading Speed*/
			if(avgSpeedRead >= maxSpeedRead){
			
				maxSpeedRead = avgSpeedRead;
				bestNRead = Nbytes[i];
				bestKRead = k[j];
			}

			

			fprintf(fp,"Average Effective Speed read/write : %f MB/s\n",avgSpeed); 
			fprintf(fp,"Average Effective Speed write : %f MB/s\n",avgSpeedWrite); 
			fprintf(fp,"Average Effective Speed read : %f MB/s\n",avgSpeedRead); 
			fprintf(fp,"-------------------------------------------------------------------------------\n");
		}
	
		/*As much memory is being consumed free the pointer and reuse*/
		free(Test);
		fprintf(fp,"->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->\n");
		printf("Tests for %d MB data Completed , Outputs are Written in Output.txt file\n",Nbytes[i]);
	}

	printf("Best N,K pair Found is : (%d MB,%d bytes) and Effective Average Speed is : %.2f MB/s\n",bestN,bestK,maxSpeed);
	printf("Best N,K pair Found is : (%d MB,%d bytes) and Effective Writing Speed is : %.2f MB/s\n",bestNWrite,bestKWrite,maxSpeedWrite);
	printf("Best N,K pair Found is : (%d MB,%d bytes) and Effective Reading Speed is : %.2f MB/s\n",bestNRead,bestKRead,maxSpeedRead);

	fprintf(fp,"->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->\n");
	fprintf(fp,"Best N,K pair Found is : (%d MB,%d bytes) and Effective Average Speed is : %.2f MB/s\n",bestN,bestK,maxSpeed);
	fprintf(fp,"Best N,K pair Found is : (%d MB,%d bytes) and Effective Writing Speed is : %.2f MB/s\n",bestNWrite,bestKWrite,maxSpeedWrite);
	fprintf(fp,"Best N,K pair Found is : (%d MB,%d bytes) and Effective Reading Speed is : %.2f MB/s\n",bestNRead,bestKRead,maxSpeedRead);
}
