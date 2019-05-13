'''

Name :- Siddharth Nahar
Entry No :- 2016csb1043
Date :- 5/11/18

Purpose :- 

	1. Contains Structure and Complete Page Table
	2. It devises Page Table size and apply replace ment Policies

'''

from PageTable.invEntry import Entry
from math import log2
from sys import maxsize as MAX_INT

class InvertedPageTable:

	#Contains basic elements required for Page table
	def __init__(self,sizeAddress,sizeRAM,sizePage,policy):

		'''
			*Store All Basic Variables Required
			*Ram Size,Address bits,Replacement Policy to use
		'''

		self.sizeAddress = sizeAddress
		self.sizeRAM = sizeRAM
		self.sizePage = sizePage
		self.policy = policy

		self.pageFaultRate = 0
		#Table Length For 
		self.lengthTable = self.sizeRAM*1024/self.sizePage

		#Table initialisation as empty list
		self.table = list()
		self.count = 0

#-----------------------------------------------------------------------------------------

	#Conversion from hex to binary with leading zeros

	def hex2bin(self,Addr):

		#List Of Decimal Places
		hexRep = ['0000','0001','0010','0011',
			  '0100','0101','0110','0111',
			  '1000','1001','1010','1011',
			  '1100','1101','1110','1111']

		#Get binary representation for each character in Addr
		binString = ''
		for i in range(len(Addr)):
			binString = binString + hexRep[int(Addr[i],base=16)]

		return binString	

#------------------------------------------------------------------------------------------

	#Make all Addresses of Length of number of bits of Address
	def padWithZeros(self,Addr):

		Bits = int(self.sizeAddress/4)

		return Addr[2:].zfill(Bits)

#------------------------------------------------------------------------------------------

	#Get Page Number and Offset of Address
	def getPageOff(self,Addr):	

		#Get Number of Offset bits to Seperate
		bitsOffset = int(log2(self.sizePage) + 10)		
		
		'''
		index = self.sizeAddress - bitsOffset

		#Get Required PageNumber and Offset string
		PageNumber = Addr[:index]
		Offset = Addr[index:]
		'''
		
		index = 2**bitsOffset

		PageNumber = int(Addr/index)
		Offset = Addr%index

		return PageNumber,Offset

#--------------------------------------------------------------------------------------------

	#See for entry of this page
	def find(self,page):

		#Iterate through Inverted Page table for searching given page number and Increment its use
		for index,Obj in enumerate(self.table):

			if Obj.PageNumber == page :

				Obj.use = Obj.use + 1
				return index
		#If index not found return -1
		return -1

#----------------------------------------------------------------------------------------------------

	#FIFO Replacement Policy
	def FIFO(self,page):

		minIndex = 0
		minValue = MAX_INT

		#Iterate through Inverted Page table for searching given page number and Increment its use
		for index,Obj in enumerate(self.table):

			#Get Frame which was inserted earliest
			if(Obj.time <= minValue):

				minValue = Obj.time
				minIndex = index
		
		#Insert new Object at that index
		self.table[minIndex] = Entry(page,self.count)

		return minIndex

#-------------------------------------------------------------------------------------------------------

	#LFU replacement policy
	def LFU(self,page):

		minIndex = 0
		minValueUse = MAX_INT
		minTime = MAX_INT		

		#Iterate through Inverted Page table for searching given page number and Increment its use
		for index,Obj in enumerate(self.table):

			#Get Frame which was inserted earliest
			if(Obj.use < minValueUse):

				minValueUse = Obj.use
				minTime = Obj.time
				minIndex = index
			
			#If ,any frames are less use select one with earliest insertion
			elif(Obj.use == minValueUse):

				if(Obj.time <= minTime):

					minValueUse = Obj.use
					minTime = Obj.time
					minIndex = index
			else:
				continue
						
		#Insert new Object at that index
		self.table[minIndex] = Entry(page,self.count)

		return minIndex

#------------------------------------------------------------------------------------------------------

	#LFU replacement policy
	def MFU(self,page):

		minIndex = 0
		maxValueUse = -1
		minTime = MAX_INT		

		#Iterate through Inverted Page table for searching given page number and Increment its use
		for index,Obj in enumerate(self.table):

			#Get Frame which was inserted earliest
			if(Obj.use > maxValueUse):

				maxValueUse = Obj.use
				minTime = Obj.time
				minIndex = index
			
			#If ,any frames are more used select one with earliest insertion
			elif(Obj.use == maxValueUse):

				if(Obj.time <= minTime):

					maxValueUse = Obj.use
					minTime = Obj.time
					minIndex = index
			#Else continue
			else:
				continue
		
		#Insert new Object at that index
		self.table[minIndex] = Entry(page,self.count)

		return minIndex


#-------------------------------------------------------------------------------------------------------

	#Insertion Functinon in Table use all replacement policies
	def insert(self,Address):

		#Get Uniform Bits Address
		#Addr = self.padWithZeros(Address)
				
		#Addr = self.hex2bin(Addr)

		page,off = self.getPageOff(Address)
		isPageFault = "Yes"

		#Find frame number for finding page number
		frame = self.find(page)

		self.count = self.count + 1

		if(frame == -1):
			
			#If table is completely filled use replacement Policy for updating list
			if(len(self.table) == self.lengthTable):
				
				if(self.policy == "FIFO"):
					frame = self.FIFO(page)
				elif(self.policy == "LFU"):
					frame = self.LFU(page)
				else:
					frame = self.MFU(page)

			else:
				#If table is Not completely filled Append to list				
				entryObj = Entry(page,self.count)
				self.table.append(entryObj)

				frame = len(self.table) - 1

			self.pageFaultRate = self.pageFaultRate + 1

		else:

			isPageFault = "No"

		IntegerPage = int(page)
		Offset = int(off)

		print("For Address : " + str(Address) +"--------->>")
		print("Page Number : "+str(IntegerPage) +" Offset : "+str(Offset))
		print("Frame Number : "+str(frame)+" Offset : "+str(Offset))
		print("Page Fault : "+isPageFault)
		print("----------------------------------------")

#-------------------------------------------------------------------------------------------------------

	def getRate(self):

		return self.pageFaultRate/self.count
