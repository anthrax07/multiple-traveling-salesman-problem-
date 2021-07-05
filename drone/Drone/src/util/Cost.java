package util;

import graph.Node;

import java.util.Vector;

import tsp.TSP_old;
import main.Principal;

public class Cost {

	//Parameters: sensor nodes, possibles drones positions, set of PPP to analyze, drone speed, necessary time to drone transmit all data 
	public static double totalCost(Vector<Node>sensors, Vector<Node>dronepositions,Vector<Node>combination, double dronespeed, double timetransmission, Reference result ){
		
		SpanningTree m = new SpanningTree(Principal.radiorange, sensors, combination,1);
		int spanning_tree[][] = m.createSpanningTree();

		if(m.disconnected(spanning_tree)){
			result.tripdistance = Principal.INFINITY;
			result.numhops = Principal.INFINITY;
			return  Principal.INFINITY;
		}
			
		int aux[][] = TSP_old.preparData(combination, combination.size());
		TSP_old route = new TSP_old(aux, combination.size()+1);
	
		//int aux[][] = TSP.preparData(combination, combination.size());
		//TSP route = new TSP(aux, combination.size()+1);

		route.eval();                           //Calculate the best path
		
		int tour[] = route.getResult();            //Get the best path
		
		result.tour = new int[tour.length];
		//System.out.print("\n");
		//for (int i = 0; i < tour.length; i++)
			//System.out.print(combination.get(tour[i]-1).getNodeId() + "-");
			//System.out.print(tour[i] + "-");
		for (int i = 0; i < tour.length; i++){
			result.tour[i] = combination.get(tour[i]-1).getNodeId();
			//System.out.print(result.tour[i] + "-");
		}
		//combination.clear();
		//combination = seq;
		//System.out.print("\n");
		//for(int i=0; i<seq.size();i++)
			//System.out.print("\n"+seq.get(i).getNodeId()+":"+seq.get(i).getX()+","+seq.get(i).getY());
		
		//route.display();
		
		
		//Travel cost
		double travelcost = route.getFinalCost() / dronespeed;  //Get the cost of the best path
		result.tripdistance = route.getFinalCost();
		result.numhops = Cost.calculateCost(spanning_tree, sensors.size(), dronepositions.size());  

		//****Cost for the SAC paper: only transmissions
		double transmissioncost = result.numhops*timetransmission;
		double totalcost = travelcost+transmissioncost;
		result.cost = totalcost;
		//****
		
		
		//****New cost calculation
//		int adjacency[][] = createAdjacency(sensors);
////		//double transmissioncost = result.numhops*timetransmission;
//		double transmissioncost = tcollecting(spanning_tree,sensors.size(),combination.size(),timetransmission);
//		double disseminationcost = calcDisseminationCost(spanning_tree,sensors.size(),Principal.pktsize,timetransmission);
////		double recovercost = 	recoverTime(adjacency, spanning_tree, sensors.size(), dronepositions.size(), Principal.datatime, 32);					
//		double totalcost = travelcost + disseminationcost+transmissioncost;// + recovercost;
		//****

		//System.out.print("\ntravel:"+travelcost+ " tx:"+transmissioncust+"  ");
		return totalcost;
	}
	
/*	
	public static double tcollecting(int st[][],int numsensors, int numdp,double timeonepacket){
		int timesum = 0;
		int numpkt = (int)(datatime / timeonepacket);
		for(int c=0; c<numsensors; c++){
			for(int l=0;l<numsensors+numdp;l++){
				if(st[l][c]>0 && st[l][c]<Principal.INFINITY){
					//((*i).n * timeonepacket * PACKET_NUMBER) + ((1+querysize)*(*i).n * timeonepacket)
					timesum += st[l][c] * st[l][c] * timeonepacket * numpkt;
				}
			}
		}
		return timesum;
	}
	
*/	
	
	
	//Calcula o custo a moda antiga, sem considerar falhas
	public static double calculateCost(int spanning_tree[][],int numsensors, int numdrones){
		//int numsensors = sensors.size();
		//int numdrones = droneposition.size();
		double soma = 0;
		for(int l=numsensors;l<spanning_tree.length; l++){   //Line varies only drone positions in the matrix
			for(int c=0; c<numsensors; c++){
				if(spanning_tree[l][c]>0 && spanning_tree[l][c]<Principal.INFINITY){
					soma+=spanning_tree[l][c];
					//break;
				}
			}
		}
		return soma ;
	}
	
	//Create the adjacency matrix with the nodes connections
	public static int[][] createAdjacency(Vector<Node>sensors){
		int adjacency[][] = new int[sensors.size()][sensors.size()];
		for(int l=0;l<sensors.size(); l++){   //Line varies only drone positions in the matrix
			for(int c=0; c<sensors.size(); c++){
				Node n1 = sensors.get(l);
				Node n2 = sensors.get(c);
				if(n1.distance(n2)<=Principal.radiorange){
					adjacency[l][c] = 1;
				}
				else adjacency[l][c] = 0;
			}
		}
		return adjacency;
	}
	//Calculate the time to disseminate the query
	public static double calcDisseminationCost(int spanning_tree[][], int numsensors, int packetsize, double querytranstime){
		double totaltime = 0;
		//For each hovering possition, calculate the query size
		for(int l=numsensors;l<spanning_tree.length; l++){   //Line varies only drone positions in the matrix
			int somahops = 0;
			int querysize = 0;
			int numnodes = 0;
			for(int c=0; c<numsensors; c++){
				if(spanning_tree[l][c]>0 && spanning_tree[l][c]<Principal.INFINITY){
					somahops+=spanning_tree[l][c]; //sum the distance of each node for each position
					numnodes++;
				}
			}
			querysize = (int)Math.ceil((double)(2*numnodes)/packetsize);  //2* --> node,parent
			totaltime += (querysize * querytranstime * somahops); 
		}
		return totaltime;
	}
	
	//Calculate the Trec (time to recover) 
	//datatime=time to transmit all data from a node, wavg=averagecontention window 
	public static double recoverTime(int adjacency[][], int spanning_tree[][], int numsensors, int numdronepositions, double datatime, int wavg){
		double trec = 0;
		for(int l=numsensors; l<spanning_tree.length; l++){
			double probAvgNodes = probAvg(adjacency,spanning_tree,l,numsensors,numdronepositions,wavg);
			double Treci = tCollectingi(l,spanning_tree,datatime,numsensors,numdronepositions);
			trec += (Treci * probAvgNodes);
		}
		return trec;
	}
	
	
	//Tcollecting int the hovering position i
	public static double tCollectingi(int droneposition, int spanning_tree[][], double datatime, int numsensors, int numdronepositions){		
		int somahops = 0;
		for(int c=0; c<numsensors; c++){
			if(spanning_tree[droneposition][c]>0 && spanning_tree[droneposition][c]<Principal.INFINITY){
				somahops+=spanning_tree[droneposition][c]; //sum the distance of each node for each position
			}
		}
		return somahops*datatime;
	}
	
	
	//avg fail probability of a drone position (omega de i)
	public static double probAvg(int adjacency[][], int spanning_tree[][], int droneposition, int numsensors, int numdronepositions,double wavg){
		int numnodes = 0;
		double sum = 0;
		for(int c=0; c<numsensors; c++){
			if(spanning_tree[droneposition][c]>0 && spanning_tree[droneposition][c]<Principal.INFINITY){
				sum += nodeFailProbability(c,adjacency,numsensors,wavg);
				numnodes++;
			}
		}
		return sum/numnodes;
	}
	
	
	//Fail probability of a node (pj)
	public static double nodeFailProbability(int node, int adjacency[][], int numsensors, double wavg){
		int n = countNeighbors(adjacency, node, numsensors);
		return 1 - Math.pow(1-1/wavg,n-1);
	}

	//return the number o neighbors of a node (n)
	public static int countNeighbors(int adjacency[][],int node, int numsensors){
		int count = 0;
		for(int c=0; c<numsensors; c++){
			if(c!=node){
				if(adjacency[node][c]>0 && adjacency[node][c]<Principal.INFINITY){
					count++;
				}
			}
		}		
		return count;
	}
	
	
}
