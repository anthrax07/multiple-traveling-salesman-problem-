package reduction;

import java.util.Collections;
import graph.Node;

import java.util.Vector;

import graph.*;

import java.util.*;

import main.Principal;
public class Reduction {
	int numsensors, numdrones;
	int [][] adjacency;
	int [][] spanning_tree;

	//Parameters: number of sensors, number of drones positions, matrix with the distance (hops) all drones X all nodes and spanning tree
	public Reduction(int numsensors, int numdrones, int [][] adjacency, int [][] spanning_tree){
		this.numsensors = numsensors;
		this.numdrones = numdrones;
		this.adjacency = adjacency;
		this.spanning_tree = spanning_tree.clone();
    }
	//I do not use it
	//Remove nodes without change the cost
	public void perform(int con){
		//show();
		//System.out.println("\n\n\n");
		for(int l=numsensors;l<numsensors+numdrones;l++){
			int count = countDronePositionConnections(l);   //Return the number of connections of the drone position (l-numsensors)
			if(count==con){
				//System.out.println("\ndrone position:"+l);
				//System.out.print(" nodes connected:");
				for(int c=0; c<numsensors;c++){             //Analise all nodes connected to drone position l
				    if(spanning_tree[l][c]>0 && spanning_tree[l][c]<Principal.INFINITY){
				    	//System.out.print("  ("+c);
				    	int newdroneposition = bestCandidate(l, count, c);  //find a new connection to c
				    	//Changing the connection
				    	spanning_tree[l][c] = Principal.INFINITY;
				    	spanning_tree[newdroneposition][c] = adjacency[newdroneposition][c]; 
						//System.out.print(" mudar para:"+newdroneposition);
				    	//System.out.print(")");
				    }
				}
			}
			
		}

//		System.out.print("\n\n\n");
	}
	
	//Return the number of nodes connected to a drone position
	public int countDronePositionConnections(int droneposition){
		int s = 0;
		for(int c=0; c<numsensors;c++){
		    if(spanning_tree[droneposition][c]>0 && spanning_tree[droneposition][c]<Principal.INFINITY)
		    	s++;
		}
		return s;
	}
	
	//Find a new drone position to node, since it is connected to actualDrone
	public int bestCandidate(int actualDrone, int actualDroneConnections, int node){
		int linkweight = adjacency[actualDrone][node]; //Get the weight of the link drone X sensor
		int maxnumconnections = countDronePositionConnections(actualDrone);
		int maxconnected = actualDrone;
		for(int l=numsensors; l<numsensors+numdrones;l++){
			if(adjacency[l][node]<=linkweight && l!=actualDrone){
				int connections = countDronePositionConnections(l);
				//System.out.print("   drone:"+l+" peso:"+adjacency[l][node]+ " connections:"+connections);
				if (connections>maxnumconnections){
					maxnumconnections=connections;
					maxconnected = l;
				}
			}
		}
		//System.out.print(" escolher:"+maxconnected+" connections:"+maxnumconnections);
		return maxconnected;
	}



	//
	public int bestElimination() {
//		System.out.println("\n\nBest Elimination\n");
		int smallestimpact = Integer.MAX_VALUE;
		int dronesmallest = -1;
		for(int l=numsensors;l<numsensors+numdrones;l++){
			if (countDronePositionConnections(l)==0) continue; //Do not analyze drone position without connection
			int sum = 0;
			int sumnew = 0;
//			System.out.print(l+":");
			for(int c=0; c<numsensors;c++){
				if(spanning_tree[l][c]<Principal.INFINITY && spanning_tree[l][c]>0){
					int aux = costBestNewConnection(l, c);
					if(aux == Principal.INFINITY){  //the sensor node has no drone position to connect
						sumnew = Principal.INFINITY;
						sum = 0;
						break;
					}
					else
						sumnew += costBestNewConnection(l, c);
					sum += spanning_tree[l][c];
					//System.out.print(spanning_tree[l][c]+"[" + costBestNewConnection(l, c));
					//System.out.print("]+");
				}
			}
			int impact = sumnew-sum;
			if(impact < smallestimpact){
				smallestimpact = impact;
				dronesmallest = l;
			}
//			System.out.println("="+(sumnew-sum));
		}
//		System.out.println("\nEliminate:"+ dronesmallest+ " impact:"+smallestimpact);
		if(dronesmallest == Integer.MAX_VALUE)
			return -1;
		else
			return dronesmallest;
	}

	//return another(the best) drone position to connect the node according to the adjacency matrix
	public int bestNewConnection(int actualdrone,int node){
		int small = adjacency[0][node]; //first drone positon
		int smalldrone = 0;
		for(int l=numsensors;l<numsensors+numdrones;l++){
			if(l==actualdrone) continue;
			if(adjacency[l][node]<small && adjacency[l][node]>0 && countDronePositionConnections(l)>0){
				small = adjacency[l][node];
				smalldrone = l;
			}
		}
		//System.out.print("'" + small+"'");
		return smalldrone;		
	}

	//return the cost of another(the best) drone position to connect the node according to the adjacency matrix
	public int costBestNewConnection(int drone,int node){
		int small = adjacency[0][node]; //first drone positon
		int smalldrone = 0;
		for(int l=numsensors;l<numsensors+numdrones;l++){
			if(l==drone) continue;
			if(adjacency[l][node]<=small && adjacency[l][node]>0 && countDronePositionConnections(l)>0 ){
				small = adjacency[l][node];
				smalldrone = l;
			}
		}
		//System.out.print("'" + small+"'");
		return small;		
	}	
	
	public void eliminate(int droneposition){
		//System.out.println("\nDroneposition:"+droneposition + "  ");
		for(int c=0; c<numsensors; c++){
			if(spanning_tree[droneposition][c] > 0 && spanning_tree[droneposition][c]<Principal.INFINITY){
				int newdrone = bestNewConnection(droneposition,c);
				//System.out.print("  node:"+ c + " to:" + newdrone);
				
				spanning_tree[droneposition][c] = 0;
				spanning_tree[newdrone][c] = adjacency[newdrone][c];
				
			}
		}
	}
	
	public Vector<Node> getPositions(Vector<Node> dronepositions){
		Vector<Node> rest = new Vector<Node>();
		for(int l=numsensors; l<(numdrones+numsensors);l++){
			for(int c=0; c<(numdrones+numsensors);c++){
				if(spanning_tree[l][c]>0 && spanning_tree[l][c]<Principal.INFINITY){
					rest.add(dronepositions.get(l-numsensors));
					break;
				}
			}
		}
		return rest;
	}
	
	
	public void show(){
		for(int l=0; l<(numdrones+numsensors);l++){
			if(l<numsensors) continue;
        	if(l<numsensors) System.out.print(l+"\t");
        	else System.out.print((l-numsensors)+"\t");
        	for(int c=0; c<(numdrones+numsensors);c++){
				if(c>numsensors) continue;

        		if(spanning_tree[l][c]==Integer.MAX_VALUE)
        			System.out.print("   # ");
        		else if(spanning_tree[l][c]>999)
        			System.out.print(spanning_tree[l][c] +" ");
        		else if(spanning_tree[l][c]>99)
        			System.out.print(" " +spanning_tree[l][c] +" ");
        		else if(spanning_tree[l][c]>9)
        			System.out.print("  " +spanning_tree[l][c] +" ");
        		else 
        			System.out.print("   " +spanning_tree[l][c] +" ");
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

	
	//
	public int bestEliminationGrasp(int randomGrasp) {
//		System.out.println("\n\nBest Elimination\n");
		int smallestimpact = Integer.MAX_VALUE;
		int dronesmallest = -1;
		int contCandidate = 0;
		ArrayList <Candidate>candidates = new ArrayList<Candidate>();
		for(int l=numsensors;l<numsensors+numdrones;l++){
			if (countDronePositionConnections(l)==0) continue; //Do not analyze drone position without connection
			int sum = 0;
			int sumnew = 0;
//			System.out.print(l+":");
			for(int c=0; c<numsensors;c++){
				if(spanning_tree[l][c]<Principal.INFINITY && spanning_tree[l][c]>0){
					int aux = costBestNewConnection(l, c);
					if(aux == Principal.INFINITY){  //the sensor node has no drone position to connect
						sumnew = Principal.INFINITY;
						sum = 0;
						break;
					}
					else
						sumnew += costBestNewConnection(l, c);
					sum += spanning_tree[l][c];
					//System.out.print(spanning_tree[l][c]+"[" + costBestNewConnection(l, c));
					//System.out.print("]+");
				}
			}
			int impact = sumnew-sum;
			if(impact < smallestimpact){
				smallestimpact = impact;
				dronesmallest = l;
			}
			candidates.add(new Candidate(l,impact));
//			System.out.println("="+(sumnew-sum));
		}
		
		
//		System.out.println("\nEliminate:"+ dronesmallest+ " impact:"+smallestimpact);
		if(dronesmallest == Integer.MAX_VALUE || dronesmallest == -1)
			return -1;
		else {
			
			int aleat=0;
			if(candidates.size()<=randomGrasp) {
				aleat = rand(0,candidates.size()-1);
			}
			else {
				aleat = rand(0,randomGrasp-1);
			}
			Collections.sort(candidates, new CandidateComparator());
//			System.out.println("\nEntre os "+ randomGrasp +" melhores: "+candidates.get(aleat).id+" ("+aleat+")");
			
//			try {
				return candidates.get(aleat).id;
//			}
//			catch(Exception ex) {
//				System.err.println("Achou==> aleat:"+aleat+"  dronesmallest:"+dronesmallest+"   size:"+candidates.size());
//				return dronesmallest;
//			}
			//return dronesmallest;
		}
	}
	
	
	
	class Candidate{
		private int id,value;
		public Candidate(int id, int value) {
			setId(id);
			setValue(value);
		}
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}
	class CandidateComparator implements Comparator{
		@Override
		public int compare(Object obj1, Object obj2) {
			Candidate c1 = (Candidate)obj1;
			Candidate c2 = (Candidate)obj2;
			if(c1.getValue()<c2.getValue()) return -1;
			else if(c2.getValue()<c1.getValue()) return 1;
			else return 0;
		}
		
	}
	public int rand(int i, int f) {
		return i + (int) (Math.random() * (f - i + 1));
	}
}