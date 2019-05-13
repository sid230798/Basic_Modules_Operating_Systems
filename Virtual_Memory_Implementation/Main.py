'''

Name :- Siddharth Nahar
Entry No :- 2016csb1043
Date :- 5/11/18

Purpose :- 

	1. Contains Driver of Code and simulate function

'''

#Import Complete implementation of page table
from PageTable.table import InvertedPageTable
import sys
import re

#Simulate Function to implement
def simulate(sizeAddr,ram,pageSize,policy,pathFile):

	#Open File and Read all Hex Address
	filePtr = open(pathFile,'r')

	Table = InvertedPageTable(sizeAddr,ram,pageSize,policy)

	#Remove extra characters and insert in table
	for line in filePtr:
		line = line.strip()
		Table.insert(int(line))
	
	print("Page Fault Rate with "+policy+" policy : "+str(Table.getRate()))

#Run this code as driver if its run as first script
if __name__ == '__main__' :

	#Take Data file as command line argument
	pathFile = sys.argv[1]

	#Virtual Address size,Ram size,PAge size and Poilicy to use as input
	sizeAddr = input('Enter Virtual Address Size in n bits : ')
	ram = input('Enter RAM size in n MB : ')

	pageSize = input('Enter the Page Size or Frame Size in n KB : ')
	policy = input('Enter the Page Replacement Policy as FIFO or LFU or MFU : ')


	#Take all interger values from input as use
	sizeAddr = int(re.search(r'\d+', sizeAddr).group())
	ram = int(re.search(r'\d+', ram).group())

	pageSize = int(re.search(r'\d+', pageSize).group())

	#Call Simulate function
	simulate(sizeAddr,ram,pageSize,policy,pathFile)
