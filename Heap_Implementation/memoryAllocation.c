/*

Name : Siddharth Nahar
Entry No : 2016csb1043
Date : 30/10/18
Purpose : Contains implementation of some functions

*/


/*Include all neccessary Headers*/
#include "listHeader.h"
#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>


/*Head of Linked List initialised to NULL and then used*/
struct Heap *head = NULL;


void printList(){

	struct Heap *tmp = head;
	while(tmp != NULL){

		printf("Size = %ld,Free = %d\n",tmp->size,tmp->free);
		tmp = tmp->next;
	}
}

/*Get pointer to address at present address + size*/
void *getIncrement(void* ptr,size_t size){

	char* tmp = (char*)ptr;
	
	/*Character pointer may be incremented by bytes*/
	tmp += size;

	return (void*)tmp;
}

/*Create extraSpace on Heap using sbrk*/
struct Heap* createNode(size_t size){

	/*Allocate size to MetaData*/
	struct Heap *node = sbrk(sizeof(struct Heap));

	/*Initialise all variables to its values*/
	node->size = size;
	node->free = false;
	node->prev = NULL;
	node->next = NULL;

	/*Allocate size to void* which stores the data*/
	node->block = sbrk(size);

	/*If sbrk returns -1 then It has reached limit  return -1*/
	if(node == (void*)-1 || node->block == (void*)-1)
		return NULL;

	return node;
}

/*Find Node in Linked List with size > required size*/
struct Heap* findFirstFit(size_t size){

	/*head of Linked List is Null ,increase size of heap and return that pointer*/
	if(head == NULL){

		head = createNode(size);
		return head;
	}
	
	/*Assign temproray node for traversal*/
	struct Heap* tmp = head;

	/*Condition till tmp == NULL or we find first fit free node in list*/
	while(!(tmp->size >= size && tmp->free == true)){ 

		if(tmp->next == NULL){

			tmp->next = createNode(size);
			tmp->next->prev = tmp;
			return tmp->next;

		}

		tmp = tmp->next;
	}


	/*If size is just sufficient results in unused space*/
	if(tmp->size < size + sizeof(struct Heap)){

		tmp->free = false;
		return tmp;
	}else{

		/*This case is when size is sufficiently larger*/

		/*Create Residual Node with size original size - size - MetaData*/
		struct Heap *node = (struct Heap*)getIncrement(tmp->block,size);
		node->size = tmp->size - size - sizeof(struct Heap);
		node->free = true;
		node->prev = tmp;
		node->next = tmp->next;
		/*Remaining block remains with residual node*/
		node->block = getIncrement(tmp->block,size + sizeof(struct Heap));

		/*Update nodes of original linked list*/
		if(tmp->next != NULL)
			tmp->next->prev = node;

		tmp->size = size;
		tmp->free = false;
		tmp->next = node;

		return tmp;
	}
}

/*To control internal fragmentation and freeing block*/
void freeBlock(void *ptr){

	if(head == NULL)
		return;

	
	struct Heap *tmp = head;

	/*Increment tmp till we find place of ptr to free*/
	while(ptr != tmp->block){

		/*If such pointer doesn't exist*/
		if(tmp->next == NULL)
			return;
		tmp = tmp->next;
	}

	tmp->free = true;

	/*Check if Next memory block is free or not*/
	if(tmp->next != NULL){

		/*If there Exists two Consecutive blocks Update tmp to total size*/
		if(tmp->next->free == true && getIncrement(tmp->block,tmp->size) == (void*)tmp->next){

			/*Next Meta Data Memory and block memory is collasesd*/
			tmp->size  += tmp->next->size + sizeof(struct Heap);
			if(tmp->next->next != NULL)
				tmp->next->next->prev = tmp;

			tmp->next = tmp->next->next;
		}
	}

	/*Check if Previous memory block is free or not Yet to be implemented*/
	if(tmp->prev != NULL){	

		/*If There Exists Previous block also empty and contiguous*/
		if(tmp->prev->free == true && getIncrement(tmp->prev->block,tmp->prev->size) == (void*)tmp){

			/*Update Previous Meta Data and links*/
			tmp->prev->size += tmp->size + sizeof(struct Heap);
			if(tmp->next != NULL)
				tmp->next->prev = tmp->prev;
		
			tmp->prev->next = tmp->next;

		}
	}
}

/*Function to get size allocated to this pointer*/
size_t getSizeofOriginal(void *ptr){

	struct Heap *tmp = head;

	while(tmp != NULL){

		if(tmp->block == ptr)
			return tmp->size;

		tmp = tmp->next;
	}

	return 0;
}


void *csl333_alloc(size_t size){

	/*Find First fit for memory It handles all the cases*/
	struct Heap *root = findFirstFit(size);

	return root->block;
}

/*Malloc Wrapper function self-created*/
void *csl333_malloc(size_t size){

	if(size <= 0)
		return NULL;

	/*Find First fit for memory It handles all the cases*/
	struct Heap *root = findFirstFit(size);

	/*Fill memory with 0*/
	memset(root->block,0,root->size);

	return root->block;
}

/*Free Wrapper Instead of Freeing memory to Os make it available for further use*/
void csl333_free(void *ptr){

	if(ptr == NULL)
		return;

	freeBlock(ptr);
}

/*Reallocation Wrapper for memory reallocation*/
void *csl333_realloc(void *ptr,size_t size){

	/*Considering Corner cases and handling the same*/
	if(ptr == NULL && size == 0)
		return NULL;
	else if(ptr == NULL)
		return csl333_malloc(size);
	else if(size == 0){

		csl333_free(ptr);	
		return NULL;
	}else{

		size_t original = getSizeofOriginal(ptr);

		/*If same new size is requested just pass that pointer back*/
		if(original == size)
			return ptr;

		/*If neither are null or 0 then first free the block then malloc the new size and copy content*/
		csl333_free(ptr);		
		void *addr = csl333_alloc(size);
		/*Copy memory from this pointer to current address*/
		/*If original size is greater than requested size just cpy only size content*/
		if(original < size)
			memcpy(addr,ptr,original);
		else
			memcpy(addr,ptr,size);		

		return addr;
	}
}
