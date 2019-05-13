/*

Name :- Siddharth Nahar
Entry No :- 2016csb1043
Purpose :- Creating a Kernel Module for Linked List utilites.
Date :- 8/9/19

*/
#include <linux/init.h>
#include <linux/kernel.h>
#include <linux/module.h>


/* This function is called when the module is loaded. */
int simple_init(void){

	printk(KERN_INFO "Loading Module Hello World\n");
	printk(KERN_INFO "Hello World ! This is my first Kernel Module \n");

	return 0;
}

/* This function is called when the module is removed. */
void simple_exit(void){

	printk(KERN_INFO "Hello World Module Removed Successfully\n");

}

/* Macros for registering module entry and exit points. */
module_init(simple_init);
module_exit(simple_exit);
MODULE_LICENSE("GPL");
MODULE_DESCRIPTION("HelloWorld Module");
MODULE_AUTHOR("Siddharth");
