package onehop;

import graph.Node;

import java.util.Vector;

import javax.swing.JOptionPane;

import bruteforce.BruteForce;
import main.Principal;
import util.*;

public class OneHop {
	double datasize;
	double dronespeed;
	double adjacency[][];
	Vector<Node> sensors;
	Vector<Node> dronepositions;
	
	int numsensors;
	int numdronepositions;
	
	public OneHop(double datasize, double dronespeed, double adjacency[][], Vector<Node> sensors, Vector<Node> dronepositions){
		this.datasize = datasize;
		this.dronespeed = dronespeed;
		this.adjacency = adjacency;
		this.sensors = sensors;
		this.dronepositions = dronepositions;
		
		numsensors = sensors.size();
		numdronepositions = dronepositions.size();
		
	}
	
	
	public boolean isIn(int num, int set[]){
		for(int i=0;i<set.length; i++){
			if(num == set[i]) return true;
		}
		return false;
	}
	
	//Analyzes if all node are connected
	public boolean verifySet(int onehop[][], int set[], int t){
		int aux[][] = new int[numsensors+numdronepositions][numsensors+numdronepositions];
		//SpanningTree.showMatrix(onehop, numsensors,numdronepositions);
		//aux receive only the pdps in set 
		for(int l=numsensors; l<numsensors+numdronepositions; l++){
			for(int c=0; c<numsensors;c++){
				if(isIn(l-numsensors,set)){
					aux[l][c] = onehop[l][c];
				}
				else
					aux[l][c] = Principal.INFINITY;
			}
		}

		//SpanningTree.showMatrix(aux, numsensors,numdronepositions);
		
		//analyze if all node are connected
		for(int c=0; c<numsensors;c++){
			boolean link = false;
			for(int l=numsensors; l<numsensors+numdronepositions; l++){
				if(aux[l][c] == 1){
					link = true;
					break;
				}
			}
			if(link == false) return false;
		}
		
		return true;
	}
	
	
	public void performExact(double timetransmit, double dronespeed){
		int onehop[][] = new int[numsensors+numdronepositions][numsensors+numdronepositions];
		//It fills the matriz (only the information about the drone position) with data from the adjacency matrix
		for(int l=numsensors; l<numsensors+numdronepositions; l++){
			for(int c=0; c<numsensors;c++){
				if(adjacency[l][c] == 1)
					onehop[l][c] = (int)adjacency[l][c];
				else
					onehop[l][c] = Principal.INFINITY;
			}
		}

		double bestcost=Double.MAX_VALUE;
		Vector<Node> bestnodes=null;
		Reference bestresult=null;
		
		int numset = numdronepositions;
		for(int numno=1; numno<=numset;numno++){
			int subset[] = new int[numno];
			for(int i=0; i<numno; i++)      //Fill the vector with the first set: 1, 2, ..., n-1
			    subset[i]=i;
			while(true){
				//System.out.print("\n");
				//for(int j=0; j<numno; j++)
				//	System.out.print(subset[j] + "  ");
				
				if(verifySet(onehop,subset,numno)){
					//System.out.print("    ok");
					//Analyze
					Vector<Node>combination = new Vector<Node>();
					
					for(int a=0;a<numno;a++)
						combination.add(dronepositions.get(subset[a]));
					
					
//					if(removeDronePositionAlone(combination,sensors,Principal.radiorange)){
	//					System.out.println("\n\nRemover\n\n");
//					}
					
					
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
				}
				
				
				subset = BruteForce.next(dronepositions.size()-1,subset);
				if(subset==null) break;
			}
		}

		//System.out.print("\nOneHop "+bestcost + " "+ bestresult.tripdistance + " " + bestresult.numhops + " " + bestnodes.size() + " --");
		
		//for(int i=0; i<bestnodes.size(); i++){
		//	System.out.print("  "+bestnodes.get(i).getNodeId());
		//}
		
		//Removes the drone position without nodes connected to it. 
		int remover = removeDronePositionAlone(bestnodes,sensors,Principal.radiorange); 
		if(remover!=-1){
			Reference result = new Reference();
			double custo = Cost.totalCost(sensors, dronepositions, bestnodes, dronespeed, timetransmit, result);
			bestcost = custo;
			//bestnodes = (Vector<Node>)bestnodes.clone();
			bestresult = result;
		}
		
		
		SpanningTree m = new SpanningTree(Principal.radiorange, sensors, bestnodes,1);
		int spanning_tree[][] = m.createSpanningTree();
		Principal.image.receiveDronePosition(bestnodes);
		Principal.image.receiveSpanningTree(spanning_tree);
		
		
		Principal.showResult("OneHop", bestcost, bestresult, bestnodes,"",timetransmit);

	}
		
	
	//Look for droneposition alone
	public static int removeDronePositionAlone(Vector<Node>bestanswer, Vector<Node>sensors, double radiorange){
		SpanningTree m = new SpanningTree(radiorange, sensors, bestanswer,1);
		int spanning_tree[][] = m.createSpanningTree();

		
		for(int l=sensors.size(); l<bestanswer.size()+sensors.size(); l++){
			int remove = l;
			for(int c=0; c<sensors.size(); c++){
				if(spanning_tree[l][c]!=0){
					remove = -1;
					break;
				}
			}
			if(remove!=-1){
				//System.out.print("\n***Remover:"+remove+" ***");
				bestanswer.remove(remove-sensors.size());
				return remove;
			}
		}
		return -1;
	}
	
	public boolean isIn(Vector<Node>v, int id){
		for(int i=0; i<v.size();i++){
			if(v.get(i).getNodeId()==id){
				return true;
			}
		}	
		return false;
	}
	
	
	//try to reduce the spanning tree (number of drones positions)
	public void reduction(int st[][]){
		for(int l=sensors.size(); l<sensors.size()+dronepositions.size(); l++){
			for(int c=0; c<sensors.size(); c++){
				if(st[l][c] < Principal.INFINITY && st[l][c] > 0){
					if(countConnectedNodes(l,st)==1) {                 //If the drone position has only one node connected 
						int subs = findSubstitute(c, l, st, st[l][c]);
						if(subs != -1){
							st[subs][c] = st[l][c];
							st[l][c] = Principal.INFINITY;
						}
					}
				}
			}
		}
	}
	
	//Look for another drone position. O sensor "s" will leave "dp" to another drone position. Return the id of the drone position
	public int findSubstitute(int s, int dp, int st[][], int distance){
		for(int l=sensors.size(); l<sensors.size()+dronepositions.size(); l++){
				if(adjacency[l][s] < Principal.INFINITY && adjacency[l][s] > 0){ //l has nodes connected  (0<adjacency[l][s]<infinity)
					if(l!=dp){                                                   //l is not dp
						if( adjacency[l][s]<=distance){                          // distance between (s and dp) is equal to (s and l)
							if(countConnectedNodes(l, st) > countConnectedNodes(dp, st)){  //l has more nodes connected
								return l;
							}
						}
					}
				}
		}
		return -1;
	}

	public int countConnectedNodes(int dp, int st[][]){
		int count=0;
		for(int c=0; c<sensors.size(); c++){
			if(st[dp][c] < Principal.INFINITY && st[dp][c] > 0){
				count++;
			}
		}
		return count;
	}
	
}
