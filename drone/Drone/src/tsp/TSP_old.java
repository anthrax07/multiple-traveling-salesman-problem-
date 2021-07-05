package tsp;
//http://sourcecodesforfree.blogspot.ca/2013/05/24-travelling-sales-person-problem-tsp.html
import graph.Node;

import java.util.*;
import java.text.*;


public class TSP_old {
	int n, tour[], finalCost;
	int weight[][];
	
	final int INF = 100000;

	public TSP_old(int w[][], int t) {
		n=t;
		tour = new int[n - 1];
		weight = new int[n][n];
		
		for(int i=0;i<n;i++){
			for(int j=0; j<n;j++){
				weight[i][j] = w[i][j];
			}
		}
		//System.out.println();
		//System.out.println("Starting node assumed to be node 1.");
		//eval();
	}

	public int COST(int currentNode, int inputSet[], int setSize) {
		if (setSize == 0)
			return weight[currentNode][0];
		int min = INF, minindex = 0;
		int setToBePassedOnToNextCallOfCOST[] = new int[n - 1];
		for (int i = 0; i < setSize; i++) {
			int k = 0;// initialise new set
			for (int j = 0; j < setSize; j++) {
				if (inputSet[i] != inputSet[j])
					setToBePassedOnToNextCallOfCOST[k++] = inputSet[j];
			}
			int temp = COST(inputSet[i], setToBePassedOnToNextCallOfCOST,
					setSize - 1);
			if ((weight[currentNode][inputSet[i]] + temp) < min) {
				min = weight[currentNode][inputSet[i]] + temp;
				minindex = inputSet[i];
			}
		}
		return min;
	}

	public int MIN(int currentNode, int inputSet[], int setSize) {
		if (setSize == 0)
			return weight[currentNode][0];
		int min = INF, minindex = 0;
		int setToBePassedOnToNextCallOfCOST[] = new int[n - 1];
		for (int i = 0; i < setSize; i++)// considers each node of inputSet
		{
			int k = 0;
			for (int j = 0; j < setSize; j++) {
				if (inputSet[i] != inputSet[j])
					setToBePassedOnToNextCallOfCOST[k++] = inputSet[j];
			}
			int temp = COST(inputSet[i], setToBePassedOnToNextCallOfCOST,
					setSize - 1);
			if ((weight[currentNode][inputSet[i]] + temp) < min) {
				min = weight[currentNode][inputSet[i]] + temp;
				minindex = inputSet[i];
			}
		}
		return minindex;
	}

	public void eval() {
		int dummySet[] = new int[n - 1];
		for (int i = 1; i < n; i++)
			dummySet[i - 1] = i;
		finalCost = COST(0, dummySet, n - 1);
		constructTour();
	}

	public void constructTour() {
		int previousSet[] = new int[n - 1];
		int nextSet[] = new int[n - 2];
		for (int i = 1; i < n; i++)
			previousSet[i - 1] = i;
		int setSize = n - 1;
		tour[0] = MIN(0, previousSet, setSize);
		for (int i = 1; i < n - 1; i++) {
			int k = 0;
			for (int j = 0; j < setSize; j++) {
				if (tour[i - 1] != previousSet[j])
					nextSet[k++] = previousSet[j];
			}
			--setSize;
			tour[i] = MIN(tour[i - 1], nextSet, setSize);
			for (int j = 0; j < setSize; j++)
				previousSet[j] = nextSet[j];
		}
	}

	public void display() {
		System.out.println();
		//System.out.print("The tour is 0-");
		for (int i = 0; i < n - 1; i++)
			System.out.print((tour[i]) + "-");
		//System.out.print("0");
		System.out.println();
		System.out.println("The final cost is " + finalCost);
	}

	public int[] getResult(){
		return tour.clone();
	}
	
	public int getFinalCost(){
		return finalCost;
	}
	//Receive and prepar the data. Create the adjacency matrix weighted by distance between each more connected and the other drone positions.
	public static int[][] preparData(Vector<Node> v, int more){
		v.add(0,new Node(100000,0,0));
		int w[][] = new int[more+1][more+1];
		for(int l=0; l<=more; l++){
			for(int c=0; c<=more; c++){
				if(l==c) 
					w[l][c]=0;
				else{
					double xa = v.get(l).getX();
					double ya = v.get(l).getY(); 
					double xb = v.get(c).getX();
					double yb = v.get(c).getY();
					
					double di = Math.sqrt(Math.pow(xa-xb,2) + Math.pow(ya-yb,2));
							
					w[l][c] =(int)di;		
					int teste = (int)v.get(l).distance(v.get(c));
					
				}
			}
		}
		v.remove(0);

		return w;
	}
	
	public static void main(String args[]) {
		/*int x[][] = { {0,0,0,0,0,0,0,0,0,0,0},
					  {0,0,228,424,228,408,445,67,593,550,331,550},
					  {0,228,0,228,169,182,335,182,391,324,300,394},
					  {0,424,228,0,228,201,192,361,169,182,271,182},
					  {0,228,169,228,0,313,218,161,391,394,134,324},
					  {0,408,182,201,313,0,384,364,301,174,416,379},
					  {0,445,335,192,218,384,0,379,270,362,150,134},
					  {0,67,182,361,161,364,379,0,530,496,270,483},
					  {0,593,391,169,391,301,270,530,0,161,400,161},
					  {0,550,324,182,394,174,362,496,161,0,453,296},
					  {0,331,300,271,134,416,150,270,400,453,0,283},
					  {0,550,394,182,324,379,134,483,161,296,283,0} };*/
		
		int x[][] = {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					 {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4},
					 {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4},
					 {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4},
					 {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4},
					 {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4},
					 {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4},
					 {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4},
					 {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4},
					 {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4},
					 {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4},
					 {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4},
					 {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4},
					 {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4},
					 {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4}};

		//TSP obj = new TSP(x,13);
		//obj.eval();
		//obj.display();
	}
}