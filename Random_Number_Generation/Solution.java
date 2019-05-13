/*

Name :- Siddharth Nahar
Entry No :- 2016csb1043
Date :- 7/10/18

*This file we generate numbers by multithreading
*Uses Device ,UniqueNumber files

*/

import dev.*;
import java.util.*;
import java.text.DecimalFormat;
import java.math.RoundingMode;
import java.io.*;

/*This class checks the nth unique number*/
class Special extends Thread{

	/*Common obj of Data Structure use to synchronize*/
	private UniqueNumber no;

	Special(UniqueNumber no){

		this.no = no;

	}

	public void run(){

		try{

			no.checkNumbers();


		}catch(Exception e){

			System.out.println(e.getMessage());
		}
	}

}

/*This class is running in parallel*/

/*Generates numbers using the given psuedcode*/
class MultiThread extends Thread{

	private UniqueNumber no;
	private Device dev;
	private int multi,n;

	/*Constructor which contains common object ,Multipliers length,Device object*/
	MultiThread(UniqueNumber no,Device dev,int multi,int n){

		this.no = no;
		this.dev = dev;
		this.multi = multi;
		this.n = n;
	}

	/*This function runs when start method is called*/
	public void run(){

		try{

			int done = 0;

			/*This is the psuedocode for number generation*/
			while(done == 0){

				/*Get the Base of number*/
				Tuple k = no.getBase();

				long base = k.base;
				int row = k.row;
				//System.out.println("Base Recieved = "+base+" Row number Recieved = "+row);

				/*This is stopping Criterias for loop*/
				if(no.length() >= n){

					done = 1;
					break;
				}

				if(base == -1)
					continue;

				if(row == -1){

					done = 1;
					break;

				}

				/*Create numbers by getting product by calling device.f*/
				for(int i = 0;i<multi;i++){

					if(no.finalCount == true){
		
						done = 1;
						break;

					}

					long nxt = dev.f(base,i);
					//System.out.println(nxt);
					no.recordInstance(nxt,row,i);
					
					
				}
			}


		}catch(Exception e){

			System.out.println("This is Message " +e.getMessage());

		}


	}

}

/*Main class consisting of main function*/
public class Solution{


	public static void main(String [] args) throws IOException{


		/*Reading from Configuration files*/
		Properties prop = new Properties();
		
		InputStream fileName = new FileInputStream("Device.config");

		prop.load(fileName);		

		/*Assigining Normal Variables*/
		int n,threads,bitReal=0;  //Nth unique number //Number of Threads

		/*Generating Random seed for creating Object*/

		long delay = Long.parseLong(prop.getProperty("ComputeDelay"));
		long bootDelay = Long.parseLong(prop.getProperty("BootDelay"));

		long seed = Long.parseLong(prop.getProperty("Seed"));

		//System.out.println(delay+ " "+bootDelay+" "+seed);
		/*
		Random rand = new Random();
		long seed = rand.nextLong();
		
		long delay = 3,bootDelay = 250;
		*/
		n = Integer.parseInt(args[0]);
		threads = Integer.parseInt(args[1]);

		if(args.length < 3){

			System.out.println("Less Number of Parameters Passed");
			return;
		}
		/*Assigining Command line Arguments*/
		if(args[2].equals("real"))
			bitReal = 0;
		else
			bitReal = 1;

		if(args.length == 4)
			delay = Long.parseLong(args[3]);
		else if(args.length == 5){

			delay = Long.parseLong(args[3]);
			bootDelay = Long.parseLong(args[4]);				

		}else if(args.length == 6){

			delay = Long.parseLong(args[3]);
			bootDelay = Long.parseLong(args[4]);	
			seed = Long.parseLong(args[5]);
		}else{		

		}

		
		Device dev;
		if(bitReal == 0)
			dev = new RealDevice(new RealDevice.DeviceConfig(delay,bootDelay,seed));
		else
			dev = new UnrealDevice(0);

		UniqueNumber no = new UniqueNumber(n,Device.MULTIPLIERS.length);

		List<MultiThread> mt = new ArrayList<MultiThread>();

		Special obj = new Special(no);

		/*Creating Array of objects of Multitreading*/
		for(int i = 0;i<threads;i++)
			mt.add(new MultiThread(no,dev,Device.MULTIPLIERS.length,n));


		/*Get the Start time of function*/
		double startTime = (System.nanoTime()*1.0)/1000000000;


		/*Starting Object of Threads*/
		for(int i = 0;i<threads;i++){
			mt.get(i).start();
		}

		obj.start();

		/*Wait for Joining of Objects*/
		for(int i = 0;i<threads;i++){

			try{
				mt.get(i).join();

				

			}catch(Exception e){

				System.out.println("This is message" +e.getMessage());
			}
		}

		try{
			obj.join();

		}catch(Exception e){

				System.out.println("This is message" +e.getMessage());
		}
	
		/*Get End time of Object and Print the Result*/
		double endTime = (System.nanoTime()*1.0)/1000000000;

		DecimalFormat df = new DecimalFormat("#.###");
		df.setRoundingMode(RoundingMode.CEILING);

		

		System.out.println("-----------------------------------------------\nRESULTS SUMMARY\n-----------------------------------------------");
		System.out.println("Target Count (n).................. : "+n);
		System.out.println("Number of Threads................. : "+threads);
		System.out.println("Time Taken........................ : "+df.format(endTime-startTime)+" sec");
		System.out.println("Resulting Number.................. : "+no.lastGenerated);
		System.out.println("Device invoked (approx.).......... : "+no.deviceInvoked + " times");
		//System.out.println("nth Unique is "+no.lastGenerated+" Total Time Taken = "+(endTime-startTime));
	}

}
