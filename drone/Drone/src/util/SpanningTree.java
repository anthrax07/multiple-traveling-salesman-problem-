package util;

import graph.Node;
import kruskal_2.Kruskal;
import java.util.Vector;
import main.Principal;

public class SpanningTree {
	private double matrix[][];
	private double range;
	int numsensors;
	int numdrones;
	Vector <Node>sensors = null;
	Vector <Node>drones = null;
	Vector <Node>analized = new Vector<Node>();
	
	private int value;
	
	//Parameter: radio range, number of sensor nodes, number of drones positions, Weight for hop 
	public SpanningTree(double range, Vector <Node> sensors, Vector <Node> drones, int valueperhop){
		int side = sensors.size() + drones.size();
		this.matrix = new double [side][side];
		this.range = range;
		this.numsensors = sensors.size();
		this.numdrones = drones.size();
		this.value = valueperhop;
		
		//First: put everything infinity
		for(int l=0; l<numsensors+numdrones; l++){
			for(int c=0; c<numsensors+numdrones; c++){
				matrix[l][c] = Principal.INFINITY;
			}
		}
		receiveSensorsDrones(sensors, drones);
	}

	
	public int[][] smallestEdges(double adjacency[][]){
		int st[][] = new int[numsensors+numdrones][numsensors+numdrones];
		for(int c=0;c<numsensors;c++){
			double small = Principal.INFINITY;
			int index = 0;
			for(int l=numsensors; l<numsensors+numdrones; l++){
				if(adjacency[l][c]>0 && adjacency[l][c]<Principal.INFINITY){
					if(adjacency[l][c]<small){
						small =adjacency[l][c];
						index = l;
					}
				}
				st[l][c]=Principal.INFINITY;
			}
			st[index][c] = (int)small;
		}
		//show(st);
		return st;
	}
	
	
	
	
	public void receiveSensorsDrones( Vector <Node>sensors, Vector <Node>drones){
		this.sensors = sensors;
		this.drones = drones;		
	}
	
	
	private Node wasAnalized(int id){
		for(int i=0; i<analized.size(); i++){
			if(analized.get(i).getNodeId()==id)
				return analized.get(i);
		}
		return null;
	}
	
	//Retorna um vector com os vizinhos de um n�. Analisa somente o vector de sensores
	private Vector<Node> neighborNodes(Node n){
		Vector <Node>neighbors = new Vector<Node>();
		for(int i=0; i<sensors.size();i++){
			if(n.distance(sensors.get(i))<=range )
				neighbors.add(sensors.get(i));
		}
		return neighbors;
	}

	//Calcular� os hops de cada n�. Resultado estar� no vector analized
	private void hopDistance(Node n, int hop){
		Vector<Node>neighborNodes = neighborNodes(n);

		//Verify if each neighbor was analized or not
		for(int i=0; i<neighborNodes.size();i++){
			Node neigh = neighborNodes.get(i);
			if(wasAnalized(neigh.getNodeId())==null ){  //Se o vizinho n�o est� na lista dos analizados, insere 
				neigh.hop = hop+value;
				analized.add(neigh);
				hopDistance(neigh, hop+value);
			}
			else {  //Se j� foi analisado, verifica se a dist�ncia � menor
				if(wasAnalized(neigh.getNodeId()).hop > hop+value){
					wasAnalized(neigh.getNodeId()).hop = hop+value;
					hopDistance(neigh, hop+value);
				}
			}
		}
	}
	
	private int getAnalizedHop(int sensorid){
		for(int i=0; i<analized.size();i++){
			if(sensorid == analized.get(i).getNodeId()){
				return analized.get(i).hop;
			}
		}
		return Principal.INFINITY;
	}
	
	//Fill all positions in the matrix with the result int the analized vector
	private void fillMatrix(int d){
			for(int c=0; c<numsensors; c++){
				//if (matrix[d][c] > getAnalizedHop(c))
				matrix[d][c] = getAnalizedHop(c);
			}
	}
	
	
	//Create links drone x drone
	private void createDroneLinks(double value){
		//Create links between drones and drones
		for(int l=numsensors; l<numsensors+numdrones; l++){
			for(int c=numsensors; c<l; c++){
				if(l!=c)
					matrix[l][c] = value;   //********  Valor colocado para os links entre drones ********//
			}
		}
	}
	
	public void show(){
		int r[][] = new int[matrix.length][matrix[0].length];
		for(int l=0; l<matrix.length; l++){
			for(int c=0; c<matrix[l].length;c++){
				r[l][c] = (int)matrix[l][c];
			}
		}
		show(r);
	}
	
	
	public void show(int ret[][]){
        for(int l=numsensors; l<(numdrones+numsensors);l++){
        	if(l<numsensors) System.out.print(l+"\t");
        	else System.out.print((l-numsensors)+"\t");
        	for(int c=0; c<(numdrones+numsensors);c++){
        		if(ret[l][c]==Principal.INFINITY)
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

	public static void showMatrix(int ret[][], int numsensors, int numdrones){
        for(int l=0; l<(numdrones+numsensors);l++){
        	if(l<numsensors) System.out.print(l+"\t");
        	else System.out.print((l-numsensors)+"\t");
        	for(int c=0; c<(numdrones+numsensors);c++){
        		if(ret[l][c]==Principal.INFINITY)
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

	
	
	//Return the matrix with the weights based on the number of hop from each node to each drone position
	public double[][] getMatrixWeight(){
		createMatrizHops();
		return matrix;
	}
	
	//Create links between sensors and drones
	public void createMatrizHops(){
		for(int d = 0; d<numdrones;d++){
			analized.clear();
			hopDistance(drones.get(d), 0);
			fillMatrix(numsensors+d);
		}
	}
	
	public int[][] createSpanningTree(){
		//Create links between sensors and drones
		createMatrizHops();
		
		//Create edge between each two drones (parameter is the weight of the edge)
		createDroneLinks(1);
		
		for(int l=0; l<numsensors+numdrones; l++){
			for(int c=0; c<numsensors+numdrones; c++){
				matrix[l][c] = matrix[c][l];                        //igualando superior com inferior
			}
		}
		
		int print_matrix[][] = new int[numdrones+numsensors][numdrones+numsensors];
		
		for(int l=0; l<numsensors+numdrones; l++){
			for(int c=0; c<numsensors+numdrones; c++){
				print_matrix[l][c] = (int)matrix[l][c];
			}
		}
		//show(print_matrix);
		//System.out.println("\n\n\n*****************************************");
		//numLinksNode(print_matrix,numdrones+numsensors);
		//System.out.println("\n\n\n*****************************************");
		
		Kruskal k = new Kruskal();
	    k.readInGraphData(print_matrix,numsensors+numdrones);
	    k.performKruskal();
	    k.printFinalEdges();

	    //System.out.println("\n\nVamo v� agora\n\n");
	    //show(print_matrix);
	    //System.out.println("\nAcabou\n\n");
	    
	    
        return print_matrix;
		
	}
	private int line(int[][]m, int l){
		int count = 0;
		for(int c=0; c<numsensors+numdrones; c++)
			if(m[l][c]>=value && m[l][c]<Principal.INFINITY) count++;
		return count;
	}
	
	private void numLinksNode(int[][]m, int side){
		int max = 0;
		for(int l=numsensors; l<numsensors+numdrones; l++){
			int count = 0;
			for(int c=0; c<numsensors; c++)
				if(m[l][c]>value && m[l][c]<Principal.INFINITY) count++;
			System.out.print(l+":" +count+" ");
			if(count > max)
				max = count;
		}
		for(int l=0; l<numsensors+numdrones; l++){
			for(int c=0; c<numsensors+numdrones; c++){
				if(m[l][c]>=value && m[l][c]<Principal.INFINITY){
					System.out.print("\nl:"+l+"   c:"+c+"   atual:"+m[l][c]+"   mudando:"+(m[l][c]+max - line(m,l)));
					m[l][c] += max - line(m,l);
				}
			}
		}
		System.out.print("\nmax:" +max	);
	}
	
	
	//Verify is the network is disconnected or not
	public boolean disconnected(int m[][]){
		for(int c=0; c<numsensors;c++){
			int s = 0;
			for(int l=numsensors; l<numdrones+numsensors; l++){
				if(m[l][c]>0 && m[l][c]<Principal.INFINITY)
					s += m[l][c];
			}
			if(s==0) return true;
		}
		return false;
	}
	

	//Verify is the network is disconnected or not
	public static boolean netDisconnected(double m[][], int numnodes, int numdrones){
		for(int l=numnodes; l<numnodes+numdrones; l++){
			double s = 0;
			for(int c=0; c<numnodes;c++){
				if(m[l][c]>0 && m[l][c]<Principal.INFINITY)
					s += m[l][c];
			}
			if(s==0) return true;
		}
		return false;
	}
	
	public int[][] oneHop(){
		//	int st[][] = new int[sensors.size()+drones.size()][sensors.size()+drones.size()];
		
		int st[][] = createSpanningTree();
		//nodexnode
		for(int l=0; l<sensors.size();  l++){
			for(int c=0; c<sensors.size();  c++){
				if(c!=l){
					Node n1 = sensors.get(l);
					Node n2 = sensors.get(c);
					if(n1.distance(n2)<=range ){
						st[l][c] = 1;
						st[c][l] = 1;
					}
					
				}
			}	
		}
		
		//Drone position pdp
		for(int l=0; l<drones.size();  l++){
			for(int c=0; c<sensors.size();  c++){
				Node d = drones.get(l);
				Node s = sensors.get(c);
				if(d.distance(s)<=range){
					boolean filled = false;
					for(int i=sensors.size(); i<sensors.size()+drones.size();i++){
						if(st[i][c]>0){
							filled = true;
							break;
						}
					}
					
					if(!filled)
						st[l+sensors.size()][c] = 1;
				}
			}
		}
		return st;
	}
}
