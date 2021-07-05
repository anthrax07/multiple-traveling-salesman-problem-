package bruteforce;

import graph.Node;

import java.util.Vector;

import main.Principal;
import util.Cost;
import util.Reference;
import util.SpanningTree;

public class BruteForce {
	double dronespeed;        //drone�s speed
	double timetransmit;    //overall time to node send all data to drone
	int size;
	
	Vector<Node>sensors;
	Vector<Node>dronepositions;
	
	double bestTotalTime = Double.MAX_VALUE;
	
	//Paramters: drone speed, time to transmit all data, nodes and drone positions
	public BruteForce(double dronespeed, double timetransmit, Vector<Node>sensors, Vector<Node>dronepositions){
		this.dronespeed = dronespeed;
		this.timetransmit = timetransmit;
		
		size = dronepositions.size();
		this.sensors = sensors;
		this.dronepositions = dronepositions;
	}
	
	
	public static int[] next(int max, int subset[]){
		subset[subset.length-1]++;

		int vmax[] = new int[subset.length];
		for(int i=0; i<subset.length;i++)
			vmax[i] = max - subset.length + i+1;
		
		int m = subset.length-1;
		while(m>0){
			if(subset[m]>vmax[m]){
				subset[m-1]++;
				if(subset[m-1]<=vmax[m-1]){
					for(int i=m;i<subset.length;i++)
						subset[i]=subset[i-1]+1;
				}
			} 
			m--;
		}
		
		int count=0;
		for(int i=subset.length-1;i>=0;i--){
			if(subset[i]>vmax[i]) count++;
		}
		if(count == subset.length) 
			return null;
		return subset;

	}

	//Receive the size of the biggest set of drone positions
	public void perform(int numset){
		double bestcost=Double.MAX_VALUE;
		Vector<Node> bestnodes=null;
		Reference bestresult=null;
		for(int numno=1; numno<=numset;numno++){
			int subset[] = new int[numno];
			for(int i=0; i<numno; i++)      //Fill the vector with the first set: 1, 2, ..., n-1
			    subset[i]=i;
			while(true){
				//Analyze
				Vector<Node>combination = new Vector<Node>();
				
				for(int a=0;a<numno;a++)
					combination.add(dronepositions.get(subset[a]));
				
				Reference result = new Reference();
				double custo = Cost.totalCost(sensors, dronepositions, combination, dronespeed, timetransmit, result);

				if(custo<bestcost){
					bestcost = custo;
					//bestnodes = (Vector<Node>)combination.clone();
					bestresult = result;
					bestnodes = new Vector<Node>();
					for(int i=0; i<bestresult.tour.length; i++)
						bestnodes.add(dronepositions.get(bestresult.tour[i]));
					
				}
				
				
				//System.out.print("\nTotal:"+ (custo) +" -->");
				//for(int i=0; i<numno; i++)
				//	System.out.print(subset[i] + "  ");

				//Finde the next set to be analyzed
				subset = next(dronepositions.size()-1,subset);
				if(subset==null) break;
				
			}
			
			
			SpanningTree m = new SpanningTree(Principal.radiorange, sensors, bestnodes,1);
			int spanning_tree[][] = m.createSpanningTree();
			Principal.image.receiveDronePosition(bestnodes);
			Principal.image.receiveSpanningTree(spanning_tree);
			
		}

		//      melhor custo         tempo da viagem                   hops                    
		//System.out.print("\nBruteForce "+bestcost + " "+ bestresult.tripdistance + " " + bestresult.numhops + " " + bestnodes.size() + " --");
		
		//for(int i=0; i<bestnodes.size(); i++){
		//	System.out.print("  "+bestnodes.get(i).getNodeId());
		//}
		
		Principal.showResult("BruteForce", bestcost, bestresult, bestnodes,"",timetransmit);
	}

}
