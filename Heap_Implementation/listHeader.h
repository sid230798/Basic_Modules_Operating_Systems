/*

Name : Siddharth Nahar
Entry No : 2016csb1043
Date : 30/10/18
Purpose : Contains Structure of Heap block

*/

#ifndef listHeader_H
#define listHeader_H

#include <stdbool.h>
#include <stddef.h>
#include <stdlib.h>

struct Heap{

	/*Contains Meta Data and block */

	size_t size; //Holds size of block it contains
	bool free;  //Variable to hold whether block is free or not
	
	/*Prev and next pointers for linked list*/

	struct Heap *prev;
	struct Heap *next;

	void* block; //Holds Address of corresponding block
};

/*Self-Implemented malloc function using sbrk*/
void *csl333_malloc(size_t size);

/*Freeing of memory instead of releasing reuse it*/
void csl333_free(void *ptr);

/*Reallocation of memory and copying original data*/
void *csl333_realloc(void *ptr,size_t size);

#endif
