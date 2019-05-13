'''

Name :- Siddharth Nahar
Entry No :- 2016csb1043
Date :- 5/11/18

Purpose :- 

	1. Contains Structure of Page Table Entry
	2. Class is defined to store which what to store

'''

#Class called Entry is defined

class Entry:

	#Structure contains page number of logical address space and offset
	def __init__(self,PageNumber,time):
		
		self.PageNumber = PageNumber
		self.time = time
		self.use = 1

	#Defining method for printing Object
	def __str__(self):

		print("Entry Contains Page Number as : "+str(self.PageNumber))
