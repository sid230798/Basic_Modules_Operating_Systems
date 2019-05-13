---------------------------------------------------------------

Name :- Siddharth Nahar
Entry No :- 2016csb1043
Date :- 4/9/18
Use :- Creates a Kernel Module using Linked List Data Structure.

------------------------------------------------------------------

Specifications :-

OS version :- Ubuntu 18.04
Kernel Version :- 4.17.0-041700-generic

-------------------------------------------------------------------

Compiling Module :- 

User@Name:~/Dir make

* Compiles the module and generates Object files.

-------------------------------------------------------------------

To insert Module :-

(ListModule)
User@Name:~/Dir	sudo insmod ListModule.ko
User@Name:~/Dir dmesg | tail -6

*Prints The 5 Date of Births and Loading module instruction

(HelloWorld)
User@Name:~/Dir	sudo insmod HelloWorld.ko
User@Name:~/Dir dmesg | tail -2

--------------------------------------------------------------------

To remove module :-

(ListModule)
User@Name:~/Dir sudo rmmod ListModule
User@Name:~/Dir	dmesg | tail -1

*Prints Success of Removing Modules

(HelloWorld)
User@Name:~/Dir sudo rmmod HelloWorld
User@Name:~/Dir	dmesg | tail -1

