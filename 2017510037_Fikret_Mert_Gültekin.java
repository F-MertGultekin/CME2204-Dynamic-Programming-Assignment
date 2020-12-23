import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Read {
	public static void main(String[] args) {



		int p = 2;
		int d=2;
		int x= 25;
		int t = 2;
		int B=100;
		int c = 6;
		int[][] proportion=new int[x][c];
		int[] demand=new int [x];
		try {
			File myObj = new File("garage_cost.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		try {
			File myObj = new File("investment.txt");
			Scanner myReader = new Scanner(myObj);
			myReader.nextLine();
			for (int i = 0; i < x; i++) {
				String data = myReader.nextLine();
				String arr[] = data.split("	");
				for (int j = 0; j < c; j++) {
					proportion[i][j]=Integer.parseInt(arr[j+1]);
				}

			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}


		try {
			File myObj = new File("month_demand.txt");
			Scanner myReader = new Scanner(myObj);
			myReader.nextLine();
			for (int i = 0; i < x; i++) {
				String data = myReader.nextLine();
				String arr[] = data.split("	");
				demand[i]=Integer.parseInt(arr[1]);

			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		double[][] bestConditions=new double[x][c];
		System.out.println();

		bestConditions=DynamicSolution(B,bestConditions, demand, t,proportion,c,x);

		double max =0;
		for (int j = 0; j < c; j++) {
			if(bestConditions[demand.length-1][j]>max) {
				max=bestConditions[demand.length-1][j];
			}
		}
		System.out.println("DP Results-Profit: " + ( max+demand[demand.length-1]*B/2) );
		System.out.println();
		int greedyCost=greedyCost(demand,p,d);
		double greedyProfit=greedyPart2(demand,B,t,proportion);
		System.out.println("Greedy Results: " +greedyProfit+" - "+greedyCost+" = "+( greedyProfit - greedyCost ) );
	}
	
	public static int greedyCost(int demand[],int p ,int d) {
		int cost =0;
		for (int i = 0; i < demand.length; i++) {
			if(demand[i]>p) {
				cost+=((demand[i]-p)*d);
			}
		}
		return cost;
	}
	public static double[][] DynamicSolution(int B,double[][]bestConditions,int demand[],int t,int[][]proportion,int c,int x){
		for (int i = 0; i <c; i++) {
			bestConditions[0][i]=(B*demand[0]/2)+((B*demand[0]/2)*proportion[0][i]/100);
		}
		for (int i = 1; i < x; i++) {
			double income=(B*demand[i]/2);

			double oldIncome=(B*demand[i-1]/2);

			for (int j = 0; j < c; j++) {
				for (int k = 0; k < c; k++) 
				{
					double money=bestConditions[i-1][k];
					double account=0;
					if(j!=k) 
					{
						money=money-(money*t/100);	
						account=money+income+oldIncome+((money+income+oldIncome)*proportion[i][j]/100);

					}
					else 
					{
						account=money+income+oldIncome+((money+income+oldIncome)*proportion[i][j]/100);
					}
					if(account>bestConditions[i][j]) 
					{
						bestConditions[i][j]=account;
					}
				}
			}
			

		}
		return bestConditions;
	}
	public static double greedyPart2(int demand[],int B,int t,int[][]proportion) {
		double Balance=0;
		double halfIncome=0;
		int PreSelect=0;
		double solution=0;
		for (int i = 0; i < demand.length; i++) {

			if(i==0) {
				double income=B*demand[i]/2;
				double[] findMax=findMax((income),proportion[i],0,0);
				PreSelect=(int) findMax[1];

				Balance= findMax[0];
				halfIncome=(B*demand[0]/2);
			}
			else 
			{
				double income=B*demand[i]/2;
				double[] findMax=findMax(Balance+halfIncome+(income),proportion[i],t,PreSelect);
				PreSelect=(int) findMax[1];
				if(i==demand.length-1) {
					findMax[0]+=B*demand[demand.length-1]/2;
					solution=findMax[0];
//						System.out.println(findMax[0]+" = Greedy Profit");
				}
				Balance= findMax[0];
				halfIncome=(B*demand[i]/2);
			}
		}
		return solution;

	}
	static double[] findMax(double money,int[] proportions,int t,int preSelect)//ikinci aydan itibaren hesaplancak 
	{
		double max=money;
		int selC = 0;
		for (int i = 0; i < proportions.length; i++) {
			double account=money;
			if(i!=preSelect) {
				account+=(account*proportions[i]/100)-(account*t/100);
			}
			else {
				account+=(account*proportions[i]/100);
			}
			if(account>max) {
				max=account;
				selC=i;
			}
		}
		double[] selP_C= {max,selC};

		return selP_C;

	}
}