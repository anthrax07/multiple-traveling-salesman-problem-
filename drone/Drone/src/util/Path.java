//This class Receives a Vector with the drone positions, another Vector with node positions, the radio range,
// a drone position and a sensor node. It returns the shortest path between these drone position and this
// node.

package util;

import java.util.Vector;
import graph.*;

public class Path {
	
	private static int smallest[]= new int[1000];
	private static int cs = Integer.MAX_VALUE;
	
	private static int visited[] = new int[1000];
	private static int cv = 0;
	
	private static int vend[] = new int[1000];
	private static int ce = 0;
	
	
	//Paramters: sensors, drone positions, initial drone position, final node, radio range
	//Returns a sequence off nodes forming the shortest path between begin and end.
/*
	public static int[] findShortestPath(Vector<Node>sensors, Vector<Node>dronepositions, int begin, int end, int radiorange){
		
		visited = new int[1000];
		smallest = new int[1000];
		cv=0;
		cs=Integer.MAX_VALUE;
		for(int a=0;a<1000;a++)
			vend[a]=-1;
		ce = 0;
		
		int numsensors = sensors.size();
		int numdp = dronepositions.size();
		
		int matrix[][] = new int[numsensors+numdp][numsensors+numdp];
		
		//Todas as distâncias infinitas
		for(int l=0; l<numsensors+numdp;l++){
			for(int c=0; c<numsensors+numdp;c++){
					matrix[l][c] = Integer.MAX_VALUE;
			}	
		}
		
		//node x node
		for(int l=0; l<numsensors;l++){
			for(int c=0; c<numsensors;c++){
				if(l==c) continue;
				Node auxl = sensors.get(l);
				Node auxc = sensors.get(c);
				if(distance(auxl,auxc)<=radiorange){
					matrix[l][c] = 1;		
					matrix[c][l] = 1;
				}
			}	
		}
		
		//node x node
		for(int l=numsensors; l<numsensors+numdp;l++){
			for(int c=0; c<numsensors;c++){
				if(l==c) continue;
				Node auxl = dronepositions.get(l-numsensors);
				Node auxc = sensors.get(c);
				if(distance(auxl,auxc)<=radiorange){
					matrix[l][c] = 1;
					matrix[c][l] = 1;
				}
			}	
		}
		
		//showMatrix(matrix, numsensors, numdp);

		//int x = shortesPath(begin, end, numsensors, matrix);
		int x = shortestPath(begin, end, numsensors, matrix);
		smallest[cs++] = end; 
		return smallest;
	}

*/	
	public static int[] teste(int begin, int end, int radiorange, int numsensors, int numdp, int matrix[][]){
		
		visited = new int[1000];
		smallest = new int[1000];
		cv=0;
		cs=Integer.MAX_VALUE;
		for(int a=0;a<vend.length;a++)
			vend[a]=-1;
		ce = 0;
		
		
		//int matrix[][] = new int[numsensors+numdp][numsensors+numdp];
		
		//Todas as distâncias infinitas
		for(int l=0; l<numsensors+numdp;l++){
			for(int c=0; c<numsensors+numdp;c++){
					if(matrix[l][c]==0)
						matrix[l][c] = Integer.MAX_VALUE;
			}	
		}
				
		//showMatrix2(matrix, numsensors, numdp);

		//int x = shortesPath(begin, end, numsensors, matrix);
		shortestPath(begin, end, numsensors, matrix);
		smallest[cs++] = end; 
		return smallest;
	}

	
	
	
	
	
	public static int shortestPath(int begin, int end, int numsensors, int m[][]){
		//System.out.print("\n("+begin+"-"+end+")");
		//for(int i=0; i<cv; i++)
			//System.out.print(" "+visited[i]);
					
		if(begin == end){
			if(cv<cs){
				//System.out.print("    AAAAAAAAAAA");
				smallest = visited.clone();
				cs = cv;	
				return 0;
			}
			else {
				//System.out.print("    BBBBBBBBBBBBBBBBBBB");
				return 0;
			}
		}

		for(int a=0; a<cv; a++){
			if(visited[a]==begin){
				return Integer.MAX_VALUE;
			}
		}
		
		visited[cv++] = begin;
		int menor = Integer.MAX_VALUE;
		for(int i=0; i<numsensors; i++){
			if(m[begin][i]==1){
				int x;
				if(vend[i]==Integer.MAX_VALUE)
					x = vend[i];
				else{
					x = shortestPath(i,end,numsensors,m);
					if(x==0){
						menor=0;
						break;
					}
				}
				if(x<menor){
					menor=x;
				}
			}
		}
		
		
		cv--;
		if (menor!=Integer.MAX_VALUE){
			//if(begin == 11)
				//System.out.println("");
			vend[begin]=menor+1;
			return menor+1;
		}
		else{
			//if(begin == 11)
				//System.out.println("");
			vend[begin]=menor;
			return Integer.MAX_VALUE;
		}
	}

	//Return the number of node in the path. It considers the initial drone position and the final node   
	public static int pathSize(){
		return cs;  
	}
	
	public static void showMatrix(int ret[][], int numsensors, int numdrones){
        for(int l=0; l<(numdrones+numsensors);l++){
        	if(l<numsensors) System.out.print(l+"\t");
        	else System.out.print((l-numsensors)+"\t");
        	for(int c=0; c<(numdrones+numsensors);c++){
        		if(ret[l][c]==Integer.MAX_VALUE)
        			System.out.print("   # ");
        		else if(ret[l][c]>999)
        			System.out.print(ret[l][c] +" ");
        		else if(ret[l][c]>99)
        			System.out.print(" " +ret[l][c] +" ");
        		else if(ret[l][c]>9)
        			System.out.print("  " +ret[l][c] +" ");
        		else 
        			System.out.print("   " +ret[l][c] +" ");
        	}
    		System.out.print("\n");
        }
 
        System.out.print("\n\t");
        for(int i=0; i<numdrones+numsensors;i++) {
        	if(i==Integer.MAX_VALUE)
        		System.out.print("   " +'#' +" ");
        	if(i>999)
    			System.out.print(1 +" ");
    		else if(i>99)
    			System.out.print(" " +i +" ");
    		else if(i>9)
    			System.out.print("  " +i +" ");
    		else 
    			System.out.print("   " +i +" ");
        }

	}


	public static void showMatrix2(int ret[][], int t){
        for(int l=0; l<(t);l++){
        	System.out.print("\t{");
        	for(int c=0; c<(t);c++){
        			if(c==(t-1))
        				System.out.print(+ret[l][c]);
        			else
        				System.out.print(+ret[l][c] +",");
        	}
        	if(l==(t-1)) 
        		System.out.print("}\n");
        	else
        		System.out.print("},\n");
        }
 
        System.out.print("\n\t");
        for(int i=0; i<t;i++) {
        	if(i==Integer.MAX_VALUE)
        		System.out.print("   " +'#' +" ");
        	if(i>999)
    			System.out.print(1 +" ");
    		else if(i>99)
    			System.out.print(" " +i +" ");
    		else if(i>9)
    			System.out.print("  " +i +" ");
    		else 
    			System.out.print("   " +i +" ");
        }

	}

	
	
    public static double distance(Node p1, Node p2){
    	return Math.sqrt((p1.x - p2.x)*(p1.x - p2.x) + (p1.y-p2.y)*(p1.y-p2.y));
    }
    
    //The main function show how to find the call the functions findShortestPath
	public static void main(String[] args) {
		//double s[] = { 91.0,15.0,123.0,107.0,3.0,168.0,99.0,161.0,53.0,172.0,171.0,149.0,89.0,85.0,14.0,97.0,6.0,171.0,195.0,123.0,176.0,89.0,52.0,73.0,32.0,189.0,78.0,153.0,181.0,103.0,56.0,94.0,151.0,59.0,196.0,90.0,3.0,46.0,68.0,113.0,54.0,183.0,199.0,100.0,132.0,98.0,27.0,23.0,81.0,109.0,199.0,107.0,66.0,0.0,121.0,135.0,162.0,172.0,16.0,11.0};
		//double dp[] = { 0.0,0.0,16.0,16.0,16.0,100.0,16.0,184.0,100.0,16.0,100.0,100.0,100.0,184.0,184.0,16.0,184.0,100.0,184.0,184.0};

/*		
		double s[] = { 140.0,118.0,188.0,10.0,1.0,100.0,82.0,132.0,44.0,117.0,124.0,126.0,67.0,71.0,51.0,174.0,40.0,195.0,105.0,63.0,191.0,191.0,31.0,133.0,33.0,197.0,116.0,111.0,138.0,144.0,159.0,191.0,74.0,21.0,95.0,79.0,8.0,157.0,124.0,34.0,72.0,69.0,171.0,114.0,25.0,67.0,165.0,24.0,111.0,120.0,27.0,5.0,190.0,181.0,22.0,63.0,174.0,148.0,111.0,129.0};
		double dp[] = { 0.0,0.0,16.0,16.0,16.0,100.0,16.0,184.0,100.0,16.0,100.0,100.0,100.0,184.0,184.0,16.0,184.0,100.0,184.0,184.0};
		
		Vector <Node>sensors = new Vector<Node>();
		Vector <Node>dronepositions = new Vector<Node>();
 
		int count=0;
		for(int i=0;i<s.length;i+=2){
			sensors.add(new Node(count++, s[i],s[i+1]));
		}
		count=0;
		for(int i=0;i<dp.length;i+=2){
			dronepositions.add(new Node(count++, dp[i],dp[i+1]));
		}

		int sa = 8;
		//for(int sa=0; sa<sensors.size(); sa++){
				int begin = sensors.size()+5;
				int end = sa;	
				int r[] = findShortestPath(sensors, dronepositions,begin, end,60);
				//r[cs++] = sa;      //The last position is not in the answer
*/
		int mat[][] = { 
				{2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,1,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647},
				{2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,1,2147483647,1,2147483647,1,1,2147483647,2147483647,2147483647},
				{1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647},
				{2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,1,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,1,2147483647,1,2147483647,1,1,2147483647,2147483647,2147483647},
				{1,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,1,1,2147483647,1,2147483647,2147483647,1,1,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647},
				{2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,1,1,1,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,1,1,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{1,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,1,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,1,2147483647,1,1,2147483647,2147483647,2147483647,1,1,1,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647},
				{1,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,1,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,1,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,1,1,2147483647,1,2147483647,1,1,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,1,2147483647,2147483647,1,1,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,1,1,1,1,1,2147483647,1,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,1,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,1,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{1,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647},
				{1,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647},
				{2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647},
				{2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,1,1,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647},
				{2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647},
				{1,2147483647,1,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,1,2147483647,2147483647},
				{2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,1,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647},
				{2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,1,1,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,1,2147483647,1,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,1,2147483647,2147483647},
				{2147483647,2147483647,1,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,1,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,1,1,2147483647,2147483647,1,2147483647,1,1,1,2147483647,1,2147483647,2147483647,2147483647,2147483647},
				{1,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647},
				{2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,1,1,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,1,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,1,1,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,1,1,1,2147483647,1,2147483647,1,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,1,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,2147483647},
				{2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647},
				{1,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,1,1,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647},
				{3,2147483647,1,2147483647,3,1,1,2147483647,2147483647,2147483647,1,2,1,2147483647,2,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,1,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2,2,1,2147483647,2147483647,2147483647,1,2147483647,1,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2147483647,2,2147483647,2147483647,2147483647,2147483647,2147483647},
				{2147483647,3,2147483647,1,2147483647,2147483647,2147483647,1,3,2,2147483647,2147483647,2147483647,3,2147483647,1,2147483647,2,1,2,1,1,2147483647,2147483647,2,4,4,1,2,3,2147483647,2147483647,2147483647,2,2,3,2147483647,3,2147483647,2,4,1,2,1,2,2,2147483647,1,1,3,1,2147483647}
				
				};

		//      teste(int begin, int end, int radiorange, int numsensors, int numdp, int matrix[][]){
				int r[] = teste(50,22,50,50,1,mat);
		
				System.out.print("\n" +cs + ":");
				if(cs!=Integer.MAX_VALUE)
					for(int i=0; i<cs; i++)
						System.out.print(" "+r[i]);
				
				
				//System.out.print("\n");
//				showMatrix(mat, 30, 2);
				
		//}
	}
	
	
	
}
