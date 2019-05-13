/*

Name :- Siddharth Nahar
Entry No :- 2016csb1043
Purpose :- Creating a Kernel Module for Linked List utilites.
Date :- 8/9/19

*/
#include <linux/init.h>
#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/random.h>
#include <linux/slab.h> // Contains headers for kmalloc and kfree
/*List is header which contains function for add,del and traverse list*/
#include <linux/list.h>
/*Contains structure of list_head contains next and prev pointers*/
#include <linux/types.h>

/*Structure of Object conatins 3 data and 1 list Object*/
struct birthday{

	int day;
	int month;
	int year;
	struct list_head list;
};


/*Initialise head of Birthday list*/
static LIST_HEAD(birthday_list);

/* This function is called when the module is loaded. */
int simple_init(void)
{
	printk(KERN_INFO "Loading ListModule ....... Following are 5 Random BirthDates\n");

	struct birthday *tmp;

	int i = 5;
	while(i-- > 0){
	
		tmp = (struct birthday*)kmalloc(sizeof(struct birthday),GFP_KERNEL);
		/*Generate Random day,month and year*/
		int day = get_random_int()%28 + 1;
		int month = get_random_int()%12 + 1;
		int year = 1985 + get_random_int()%25;

		/*Iniaitalise structure with above data*/
		tmp->day = day;
		tmp->month = month;
		tmp->year = year;

		/*Initialise list_head data by below function*/
		INIT_LIST_HEAD(&tmp->list);
		/*Add to list tail by birthday_list head*/
		list_add_tail(&tmp->list,&birthday_list);
	}

	/*Traversing list and print to debug file*/
	struct birthday *person;
	int count = 1;

	list_for_each_entry(person,&birthday_list,list){

		printk(KERN_INFO "BirthDate of Person%d = %d/%d/%d\n",count,person->day,person->month,person->year);
		count++;
	}

	return 0;
}

/* This function is called when the module is removed. */
void simple_exit(void)
{
	struct birthday *ptr, *next;
	printk(KERN_INFO "Deallocating List Module ..............\n");
	list_for_each_entry_safe(ptr,next,&birthday_list,list) {

		/*ptr points to current cursor*/
		/*next points to next cursor*/
		/*Free List pointer and struct pointer*/
		list_del(&ptr->list);
		kfree(ptr);
	}

	printk(KERN_INFO "Module Removed Successfully\n");
}

/* Macros for registering module entry and exit points. */
module_init(simple_init);
module_exit(simple_exit);
MODULE_LICENSE("GPL");
MODULE_DESCRIPTION("LinkList Module");
MODULE_AUTHOR("Siddharth");
