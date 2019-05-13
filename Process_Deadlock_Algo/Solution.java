/*

Name :- Siddharth Nahar
Entry No :- 2016csb1043
Date :- 15/10/18
Purpose :-
	
	1. Check for Deadlock among process at given time
	2. Read CSV file for input

*/


//Importing Libraries for Use
import java.util.*;
import java.io.*;
import com.opencsv.*;


/*Process class stores Allocated and Requested Resource by process*/
class Process{


	public List<Integer> P_Allocated,Q_Requested;
	public int marked;

	/*Constructor contains list of Allocated resource and requested resource*/
	Process(List<Integer> P_Allocated,List<Integer> Q_Requested,int marked){

		this.P_Allocated = P_Allocated;
		this.Q_Requested = Q_Requested;
		this.marked = marked;

	}

	/*Check For Process P_i(t) == 0 i.e process doesnt exist*/
	public boolean checkZero(){

		
		/*Check for process such that all resources are zero*/
		for(int i = 0;i<P_Allocated.size();i++){

			if(P_Allocated.get(i) != 0)
				return false;
		}

		return true;

	}

	/*Print Process Just for Check for correct implementation*/
	public void printProcess(){

		System.out.print("Allocated Resources : ");
		for(int i = 0;i<P_Allocated.size();i++)
			System.out.print(P_Allocated.get(i) + " ");

		System.out.println();

		System.out.print("Requested Resources : ");
		for(int i = 0;i<Q_Requested.size();i++)
			System.out.print(Q_Requested.get(i) + " ");

		System.out.println();

	}

}


/*Solution class conatining main function*/
public class Solution{

	/*This function takes Parameters as : 

		1. Available Resources.
		2. Requested Resources.
		3. HashMamp containing Deadlocked Resources


	  This Function insert in map the Resources present in deadlocked
	*/
	static void InsertResourceDeadLock(List<Integer> listAvailable,List<Integer> Q,HashMap<Integer,Integer> resDeadLock){

		for(int i = 0;i<listAvailable.size();i++){

			if(Q.get(i) > listAvailable.get(i)){

				/*Check if that resource is already present*/
				if(resDeadLock.containsKey(i+1))
					continue;
				else
					resDeadLock.put(i+1,1);

			}


		}

	}

	/*This function takes Parameters as :

		1. Available Resource
		2. Requested Resource of Process

		This function checks Q(i) <= W and return true

	*/
	static boolean check(List<Integer> listAvailable,List<Integer> req){

		for(int i = 0;i<listAvailable.size();i++){

			int a = req.get(i);
			int b = listAvailable.get(i);

			if(a > b)
				return false;
		}

		return true;


	}

	/*This function takes parameters as :

		1. Available Resources
		2. Allocated Resources

		This function updates Available Resource if Q(i) <= W

		W = W + P(i)

	*/
	static void update(List<Integer> listAvailable,List<Integer> up){

		for(int i = 0;i<listAvailable.size();i++){

			int a = up.get(i);
			int b = listAvailable.get(i);

			/*Updates value of resource by P(i)*/
			listAvailable.set(i,a+b);

		}


	}

	/*This function takes parameters as :

		1. Available Resource
		2. List of all Process

		* This function iterates through all Process
		* If Process is unmarked check Q(i) <= W,Update Available
		* Continue iterating till no process is found
	*/
	static boolean checkMarked(List<Integer> listAvailable,List<Process> p){

		boolean checkVal = false;
		for(int i = 0;i<p.size();i++){

			Process obj = p.get(i);
			if(obj.marked == 0){

				/*If object is unmarked Check for Q(i) <= W and Update W = W + P(i)*/
				if(check(listAvailable,obj.Q_Requested)){

					update(listAvailable,obj.P_Allocated);
					obj.marked = 1;
					checkVal = true;
				}


			}

		}		

		return checkVal;

	}
	

	/*Entry Point of Program MainFunction*/
	public static void main(String [] args)throws Exception{


		/*CSVReader file takes test file in csv and delimites by ","*/
		CSVReader reader = new CSVReader(new FileReader(args[0]));
		String[] line = null;

		List<Process> listProcess = new ArrayList<Process>();
		List<Integer> listAvailable = new ArrayList<Integer>();

		int index = 0;

		/*Allocate all Process and Allocate Resources and Requested Resources*/
		while((line = reader.readNext()) != null){


			Process p = null;
			List<Integer> alloc = new ArrayList<Integer>();
			List<Integer> req = new ArrayList<Integer>();

			if(index == 0){
			
				int length_Resources = line.length/3;
				for(int j = 0;j<3;j++){

					for(int k = 0;k<length_Resources;k++){

						//System.out.print(line[j*length_Resources + k]);
						if(j == 0)
							alloc.add(Integer.parseInt(line[j*length_Resources + k]));
						else if(j == 1)
							req.add(Integer.parseInt(line[j*length_Resources + k]));
						else
							listAvailable.add(Integer.parseInt(line[j*length_Resources + k]));

					}
				}

				p = new Process(alloc,req,0);
				

			}else{


				int length_Resources = line.length/2;
				for(int j = 0;j<2;j++){

					for(int k = 0;k<length_Resources;k++){

						if(j == 0)
							alloc.add(Integer.parseInt(line[j*length_Resources + k]));
						else
							req.add(Integer.parseInt(line[j*length_Resources + k]));

					}
				}

				p = new Process(alloc,req,0);


			}

			//System.out.println();
			listProcess.add(p);			
			index++;
		}

		for(int i = 0 ;i<listProcess.size();i++){

			Process obj = listProcess.get(i);
			if(obj.checkZero())
				obj.marked = 1;

		}

		/*Continue Process till we get Unmarked resource with Q(i)<=W*/
		while(checkMarked(listAvailable,listProcess));


		HashMap<Integer,Integer> proDeadLock = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> resDeadLock = new HashMap<Integer,Integer>();

		for(int i = 0 ;i<listProcess.size();i++){

			Process obj = listProcess.get(i);
			if(obj.marked == 0){

				proDeadLock.put(i+1,1);
				InsertResourceDeadLock(listAvailable,obj.Q_Requested,resDeadLock);

			}
		}


		/*Print the Process and Resources in DeadLocked*/
		if(proDeadLock.isEmpty())
			System.out.println("System State : Not in DeadLocked");

		else{

			System.out.println("System State : DeadLocked");
			System.out.print("Process in Deadlock : ");

			int count = 0;
			for(Integer key : proDeadLock.keySet()){
				
				if(count > 0)
					System.out.print(", ");
				System.out.print("P"+key);
				count++;
			}

			System.out.println(".");

			System.out.print("Resources in Deadlock : ");
			count  = 0;
			for(Integer key : resDeadLock.keySet()){
				
				if(count > 0)
					System.out.print(", ");
				System.out.print("R"+key);
				count++;
			}

			System.out.println(".");

		}

	}

}
