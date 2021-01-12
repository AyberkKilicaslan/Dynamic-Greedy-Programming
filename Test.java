import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Suleyman_Ayberk_Kilicaslan_2017510053 {

	//necessary variables
	private static int p = 5;
	private static int d = 5;
	private static int x = 3;
	private static int t = 2;
	private static int B = 100;
	private static int c = 3;
	public static ArrayList<Double> demands= new ArrayList<Double>();
	private static double[][] investment = new double[x][c];
	public static ArrayList<Double> garageCost = new ArrayList<Double>();

	public static void garageCostFile() throws IOException {
		File file = new File("garage_cost.txt"); 
	    BufferedReader br = new BufferedReader(new FileReader(file)); 
	    String line = br.readLine(); //skip first line unnecessary
	    String[] splittedLine = null; 
	    garageCost.add((double) 0);
	    while ((line = br.readLine()) != null) {
	    	splittedLine = line.split("\t");
	        garageCost.add((double) Integer.parseInt(splittedLine[1]));
	        
	    }
	    br.close();
	}

	public static void demandsFile() throws IOException {
        File file = new File("month_demand.txt"); 
        BufferedReader br = new BufferedReader(new FileReader(file)); 
        String line = br.readLine(); //skip first line unnecessary
        String[] splittedLine = null; 
        while ((line = br.readLine()) != null) {
        	splittedLine = line.split("\t");
            demands.add((double) Integer.parseInt(splittedLine[1]));
            
        }
        br.close();
    }
	
	public static void investmentFile(int x, int c) throws IOException {
        File file = new File("investment.txt"); 
        BufferedReader br = new BufferedReader(new FileReader(file)); 
        String line = br.readLine(); //to pass first line.
        String[] splittedLine = null; 
        int indexHolder = 0;
        while ((line = br.readLine()) != null && indexHolder < x) {
        	splittedLine = line.split("\t");
            for (int i = 1; i < c+1; i++) {
                investment[indexHolder][i-1] = (double)Integer.parseInt(splittedLine[i]);
            }
            indexHolder++;
        }
        br.close();
    }
	
	public static void Part1Dynamic() 
	{
		double totalCost = 0;   	//hold total cost for each step
		int availableCar = 0 ;			//variable for how many cars do we have
		double minCost = 0;
		int costMonth = 0;
		for (int i = 0; i < demands.size(); i++) 
		{
			costMonth += demands.get(i);
		}
		
		double [][] array = new double[x][costMonth+1];    //temp 2d array for dynamic
		
		for (int i = 0; i < array.length; i++) 
		{
			for (int j = 0; j < array[i].length; j++) 
			{
				if(i==0) 
				{
					//first line assigning with garageCost
					array[i][j] = garageCost.get(j);
				}
				
				else
				{
					if(p+availableCar <= demands.get(i-1)) {
						//pay for intern
						array[i][j] = (int) ((d*(demands.get(i-1) - p+availableCar)) + totalCost);
					}
					
					else 
					{
						//hold cars in garage 
						Double tempMoney = demands.get(i-1);
						array[i][j] = garageCost.get((int) (tempMoney - availableCar)) + totalCost;
					}
				}
			}
			
			minCost = 99999;
			for (int j = 0; j < array[i].length; j++) 
			{
				if(array[i][j]<minCost ) {
					minCost = array[i][j];
					totalCost = array[i][j];
					availableCar = j;
				}
			}
		}
		
		System.out.println("NOT WORKING TRUE !!! Part 1 Dynamic : " + minCost);
	}
	
	public static void Part1Greedy() 
	{
		int totalCost = 0;
		//we dont need garage cost on this part
		for (int i = 0; i < x; i++) 
			if(p<demands.get(i)) 
				totalCost += (demands.get(i) - p) * d;  //calculate how many intern we need
		
		System.out.println("Part 1 Greedy : " + totalCost);
	}
	
	public static void Part2Dynamic() {
		
		double [] [] array = new double [x][c];

		
		for (int i = 0; i < investment.length; i++) 
		{
			for (int j = 0; j < investment[i].length; j++) 
				
			{
				double halfMoney = demands.get(i) * B / 2;  //assign the half of the earning on that month
				
				if(i == 0) 
				{				
					//assign first months values
					array[i][j] = (halfMoney + (halfMoney * investment[i][j] / 100)) + halfMoney + ((demands.get(i+1)*B)/2);
				}
				
				else 
				{
					for (int j2 = 0; j2 < investment[i].length; j2++) 
					{
						double tempMoney = array[i-1][j2];   //hold previous months earning
						if(j2 == j)  //if company is same
						{
							if(tempMoney + ((tempMoney * investment[i][j2])/100) > array[i][j2] )
							{
								if(i+1 > x)  //if its last month, add last earning of the half
									array[i][j2] = tempMoney + (tempMoney * investment[i][j2] / 100) + (demands.get(x-1)* B / 2) + halfMoney;
								else
									array[i][j2] = tempMoney + (tempMoney * investment[i][j2] / 100) + (demands.get(i+1)*B/2) +halfMoney;
								
							}
						}
						
						else
						{	//different company
							if(((tempMoney - ((tempMoney) * t/100)) * (investment[i][j])/100) + tempMoney > array[i][j])
							{
								if(i+1 > x)
									array[i][j] = (tempMoney - ((tempMoney) * t/100)) * (investment[i][j] / 100)  +  (demands.get(x-1)* B / 2) + halfMoney;
								else
									array[i][j] = (tempMoney - ((tempMoney) * t/100)) * (investment[i][j] / 100)  +  (demands.get(i+1)*B/2+ halfMoney);
							}
							
						}
					}
				}
			}
			
		}
		
		//find max value of the last line of 2d array
		double finalMoney = 0;
		//defining max value
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				if(array[x-1][j]> finalMoney)
					finalMoney = array[x-1][j];
			}
			
			break;
		}
		
		System.out.println("Part 2 Dynamic : " + finalMoney);
		
	}
	
	public static void Part2Greedy() {
		double [] tempArray = new double[c];  //temp array for faiz values
		double money = B;  //startup money
		double finalMoney = 0;  //startup money
		double temp = 0;  //money temp for first line
		int company = 0;  //company index holder
		int countControl = 1;   //first line flag
		double tempMoney  = 0;   //next of the lines' money temp for compare
		for (int i = 0; i < investment.length; i++) 
		{
			//fill the temp array for each row
			for (int j = 0; j < investment[i].length; j++) 
			{
				tempArray[j] = investment[i][j];
			}
			//first line
			if(countControl == 1) 
			{
				double halfMoney = (demands.get(i) * money) / 2;
				for (int j = 0; j < tempArray.length; j++) 
				{
					if(halfMoney * tempArray[j] / 100 > temp)
					{
						temp = halfMoney * tempArray[j] / 100;
						company = j;
					}
				}
				finalMoney = temp+ halfMoney*2;
				
				countControl++;
			}
			//rest of the lines
			else
			{
				tempMoney = 0;
				double halfMoney = (demands.get(i) * money) / 2;
				finalMoney+=halfMoney;
				int tempCompany = 0;   //variable for money(!= j)
				for (int j = 0; j < tempArray.length; j++) 
				{
					if (j != company)
					{
						//System.out.println(finalMoney);
						//eðer þirket farklýysa bu sayýyý baþka bir temp deðerinde tut
						if((finalMoney-(finalMoney*(t/100))) * tempArray[j] / 100> tempMoney)
						{
							tempMoney = ((finalMoney-((finalMoney * t)/100)) * tempArray[j] / 100) + halfMoney +finalMoney;
							tempCompany = j;
						}
					}
					//ayný þirketse temp deðerinde tutmaya devam et
					else
					{
						temp = (finalMoney * tempArray[j] / 100) + halfMoney +finalMoney;
						company = j;
					}
				}
				
				//eðer ayný þirketteki deðeri daha yüksekse kalmaya devam et
				if(temp > tempMoney)
				{
					finalMoney = temp;
				}
				//eðer farklý þirkete geçiyorsa company deðerini güncelle
				else
				{
					finalMoney = tempMoney;
					company = tempCompany;
				}
			}
		}
		System.out.println("Part 2 Greedy : "+ finalMoney);
		//System.out.println("Final Company : "+ company);
	}
	
	public static void main(String[] args) throws IOException {
		
			//File reading and assigning them to array
			investmentFile(x,c);
			demandsFile();
			garageCostFile();
			
			//Functions and results
			Part1Dynamic();
			Part1Greedy();
			Part2Dynamic();
			Part2Greedy();
	}

}