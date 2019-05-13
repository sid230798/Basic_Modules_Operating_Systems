/*

Name :- Siddharth Nahar
Entry No :- 2016csb1043
Date :- 7/10/18

*This is Data Structure Designed for storing Unique numbers.
*Properties :

	1. No repetition of numbers.
	2. Must be saved in Order for getting base.
	3. Data Structure used is Two-Dim Array and predicted Row number
	4. HashMap use to judge uniqueNumber

*/


import java.util.*;
import java.lang.*;

/*This is class which is use to return from getBase Stores Base and row number where to insert*/
class Tuple{

	public int row;
	public long base;

	Tuple(int row,long base){

		this.row = row;
		this.base = base;
	}
}

/*Stores Value where it is stored in HashMap*/
class Pair{

	public int row,col;
	public long key;
	Pair(long key,int row,int col){

		this.key = key;
		this.row = row;
		this.col = col;
	}

}

/*Class which generates UniqueNumber and Stores and Verify different functions*/
public class UniqueNumber{

	/*HashMap stores all numbers and checks for uniqueness*/
	private HashMap<Long, Pair> uniqueHash;

	/*Array Stores Values by row number and Col number*/
	private long[][] baseValues;
	/*Use to check if that index is visited*/
	private boolean[][] baseVisited;

	/*Rows,number of columns*/
	private int rows,multi;
	private int baseRow,baseCol;
	private int rowIndex;

	/*Stores the Last number*/
	public long lastGenerated;
	public long deviceInvoked;
	public boolean finalCount;
	
	/*Constructor contains nth unique and number of multipliers*/
	UniqueNumber(int n,int multi){

		this.multi = multi;
		//int row_n = 2*(int)Math.ceil(n*1.0/multi);
		int row_n = n;

		this.rows = row_n;
		uniqueHash = new HashMap<Long, Pair>();

		/*Assigining memory and store values initially*/
		baseValues = new long[rows][multi];
		baseVisited = new boolean[rows][multi];

		for(int i = 0;i<rows;i++){
			for(int j = 0;j<multi;j++){
				baseValues[i][j] = -1;
				baseVisited[i][j] = false;
			}
		}
		rowIndex = 0;
		baseRow = 0;
		baseCol = 0;

		lastGenerated = 2;
		deviceInvoked = 0;
		finalCount = false;
		//System.out.println("No of Rows = "+rows);

	}

	/*Synchronized functions as multiple threas access at same time*/
	public synchronized void recordInstance(long val,int row,int col){

		/*
			1. Check if nummber is already present.
			2. If number is present but that number is after so swap values
		*/

		deviceInvoked++;

		if(uniqueHash.containsKey(val)){

			Pair pT = uniqueHash.get(val);
			
			if(pT.row > row){
			
				//System.out.println("Value Exchanged");
				baseValues[row][col] = val;
				baseValues[pT.row][pT.col] = -1;
				pT.row = row;
				pT.col = col;
				uniqueHash.put(val,pT);
			}

			baseVisited[row][col] = true;

			return;

		}else{

			/*If value is not present insert the value */
			if(row == -1)
				return;

			Pair pp = new Pair(val,row,col);
			baseValues[row][col] = val;
			baseVisited[row][col] = true;
			uniqueHash.put(val,pp);

			//lastGenerated = val;
			//System.out.println("This Values is inserted at "+row+" "+col+" "+val);
			return;
		}

	}

	/*Function to return next Oldest number*/
	public synchronized Tuple getBase(){

		if(baseCol == multi){

			baseRow++;
			baseCol = 0;
		}

		long value;
		int rowInsert=0;
		

		if(length() == 0){
			value = 2;
			rowInsert = 0;
		}
		else{
			/*Get the base if that value is visited and return values in Ordering*/

			
			if(baseRow < rows && baseCol < multi){
			
				if(baseVisited[baseRow][baseCol] == true){

					value = baseValues[baseRow][baseCol++];

					if(value != -1)
						rowInsert = ++rowIndex;
				}
				else
					value = -1;
				
				
			}
			else{
				value = -1;
				//System.out.println("This is the Problem");
			}
		}

		
	
		if(rowInsert >= rows)
			rowInsert = -1;

		return (new Tuple(rowInsert,value));
	}

	/*Function to get nth unique number*/
	public void checkNumbers(){

		int count = 0;
		int startRow = 0,startCol = 0;

		/*Iterate through two Dim array and if visited store that number*/
		while(count < rows){

			startRow = startRow%rows;
			startCol = startCol%multi;
	
			if(baseVisited[startRow][startCol]  == true){


				long val = baseValues[startRow][startCol];			

				if(val != -1){

					lastGenerated = val;
					count++;
					//finalCount = count;
				}

				//System.out.println("Visited Node : "+startRow + " "+startCol + " "+count);
				startCol++;
				if(startCol == multi)
					startRow++;

				//System.out.println("Visited Node : "+startRow + " "+startCol);

			}
		}

		finalCount = true;
	}

	public int length(){

		return uniqueHash.size();

	}
}
