package util;

import graph.Node;

import java.awt.Point;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import main.Principal;

public class MoreConnected {
	double matrix[][];
	int numdrones, numsensors;
	Vector <Position>v = new Vector<Position>();   //vetor que, ordenado, ter� a sequencia dos priores para os melhores n�s em rela��o � conex�o 
	
	public MoreConnected(double matrix[][], int numsensors, int numdrones){
		this.matrix = matrix;
		this.numdrones = numdrones;
		this.numsensors = numsensors;
	}

    //Create a weight for each drone position
	public void analizeDronePositions(int maxhops){
		//System.out.println("\n\nnumsensors:"+numsensors+"   numdrones:"+numdrones);
		for(int l=numsensors; l<numsensors+numdrones;l++){
			double soma = 0;
			for(int c=0; c<numsensors;c++){
				if(matrix[l][c]!=Principal.INFINITY && matrix[l][c]<=maxhops){
					soma+=maxhops/matrix[l][c];
				}
			}
			v.addElement(new Position(l-numsensors,soma));
			//System.out.println(l-numsensors + ":" + soma);
		}
	}
	
    //Create a weight for each drone position
	public void calculateWeights(){	
		for(int l=numsensors; l<numsensors+numdrones;l++){
			double farthest = farthestNode(l);
			if(farthest == 0) {
				v.addElement(new Position(l-numsensors,0));
				continue;
			}
			double generalsum = 0;
			while(farthest>0){
				double hopsum = 0;
				boolean nodealone = false;
				for(int c=0; c<numsensors;c++){
					if(oneConnection(c) && matrix[l][c]>0 && matrix[l][c]<Principal.INFINITY){           //If c is connected just to l, then the weight of l is the number of sensors
						generalsum = Principal.INFINITY;
						nodealone = true;
						break;
					}
				    if(matrix[l][c]==farthest)
				    	hopsum++;
				}
				if(nodealone){
					farthest = farthest - 1;
					break;
				}
				generalsum+=(hopsum/farthest);
				farthest = farthest - 1;
			}
			
			//Se a droneposition tiver um um sensor que s� se conecta a ela, colocar seu peso como infinito
			
			
			v.addElement(new Position(l-numsensors,generalsum));
			//System.out.print("\n"+(l-numsensors) + ":" + generalsum);
		}
	}
	
	
	public boolean oneConnection(int node){
		int sum = 0;
		int position = 0;
		for(int l=numsensors; l<numsensors+numdrones; l++){
			if(matrix[l][node]<Principal.INFINITY && matrix[l][node]>0){
				sum ++;
			}
		}
		if(sum==1)
			return true;
		else
			return false;
		
	}
	

	//Verify which is the farthest node from a drone position 
    public double farthestNode(int drone){
    	double maxhop = 0;
    	for(int c=0; c<numsensors; c++){
    		if(matrix[drone][c]>maxhop && matrix[drone][c]<Principal.INFINITY)
    			maxhop = matrix[drone][c]; 
    	}
    	return maxhop;
    }
	
	
	public void show(){
        for(int l=0; l<(numdrones+numsensors);l++){
        	if(l<numsensors) System.out.print(l+"\t");
        	else System.out.print((l-numsensors)+"\t");
        	for(int c=0; c<(numdrones+numsensors);c++){
        		if(matrix[l][c]==Principal.INFINITY)
        			System.out.print("   # ");
        		else if(matrix[l][c]>999)
        			System.out.print((int)matrix[l][c] +" ");
        		else if(matrix[l][c]>99)
        			System.out.print(" " +(int)matrix[l][c] +" ");
        		else if(matrix[l][c]>9)
        			System.out.print("  " +(int)matrix[l][c] +" ");
        		else 
        			System.out.print("   " +(int)matrix[l][c] +" ");
        	}
    		System.out.print("\n");
        }
 
        System.out.print("\n\t");
        for(int i=0; i<numdrones+numsensors;i++) {
        	if(i==Principal.INFINITY)
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

	public void sort(){
		//PositionComparator pc = new PositionComparator();
		//v.sort(pc);
		PositionComparator pc = new PositionComparator();
		Collections.sort(v, pc);
	}
	
	public void printV(){
		for(int i=0; i<v.size();i++)
			System.out.println(v.get(i).getNodeId()+":"+v.get(i).weight);
		System.out.println("\n\n");
	}
	//Type used to create the sorted vector.
	public class Position extends Node{
//		public int id;
		public double weight;
		public Position(int id, double weight){
			super(id,0,0);
//			this.id = id;
			this.weight = weight;
		}
	}
	
	//Inverted sort
	class PositionComparator implements Comparator<Position> {
		public int compare(Position one, Position two) {
			if(one.weight>two.weight) return -1;
			else if(one.weight<two.weight) return 1;
			return 0;
		}
	}

	public Vector<Node> manipulate(int m){
		Vector<Node> aux = (Vector<Node>) v.clone();
		Vector<Node> result = new Vector<Node>();
		int r = 2*Principal.radiorange;
		
		//Insert the first, it is the most connected node
		result.add(aux.get(0));
		aux.remove(0);
		
		while(result.size()<m){
			for(int i=0; i<aux.size(); i++){
				boolean far = true;
				for(int j=0; j<result.size(); j++){
					if(aux.get(i).distance(result.get(j)) < r){
						far = false;
						break;
					}
				}
				if(v.get(i).weight==Principal.INFINITY) far = true;
				if(far){
					result.add(aux.get(i));
					aux.remove(i);
					if(aux.size()==0 || result.size()>=m) return result;
				}
			}
			r = r - 10;
		}
		return result;
	}

	
	
	public void setPositions(Vector <Node>dronepositions) {
		for(int d=0; d<dronepositions.size(); d++){
			v.get(d).x =  dronepositions.get(v.get(d).getNodeId()).x;
			v.get(d).y =  dronepositions.get(v.get(d).getNodeId()).y;
		}
	}

	//Return the 'm' most connected nodes, does not matter the distance between then
	public Vector<Node> getMore(int m) {
		Vector<Node>more = new Vector<Node>();
		for(int i=0;i<m;i++){
			more.add(v.get(i));
		}
		return more;
		
	}

	public Vector<Node> manipulate_Grasp(int m, int randomGrasp){
		Vector<Node> aux = (Vector<Node>) v.clone();
		Vector<Node> result = new Vector<Node>();
		int r = 2*Principal.radiorange;
		
		
		int aleat=0;
		if(aux.size()<=randomGrasp) {
			aleat = rand(0,aux.size()-1);
		}
		else {
			aleat = rand(0,randomGrasp-1);
		}
		
		result.add(aux.get(aleat));
		aux.remove(aleat);
		
		System.out.println("-->"+aleat);
		//Anterior
		//Insert the first, it is the most connected node
		//result.add(aux.get(0));
		//aux.remove(0);
		
		while(result.size()<m){
			for(int i=0; i<aux.size(); i++){
				boolean far = true;
				for(int j=0; j<result.size(); j++){
					if(aux.get(i).distance(result.get(j)) < r){
						far = false;
						break;
					}
				}
				if(v.get(i).weight==Principal.INFINITY) far = true;
				if(far){
					result.add(aux.get(i));
					aux.remove(i);
					if(aux.size()==0 || result.size()>=m) return result;
				}
			}
			r = r - 10;
		}
		return result;
	}

	public int rand(int i, int f) {
		return i + (int) (Math.random() * (f - i + 1));
	}
	

}
