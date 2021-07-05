package tsp.temp;
import java.util.*;
/**
 * This class searchs the minimum round trip through a given adjacency matrix.
 * idea:
 * finding the min edge in the system and search the min triangle with the two point of the min edge.
 * now, find for all edges of the triangle new triangles ... and so on ...
 * do this, until all nodes are reached.
 * 
 * 
 * TODO
 * 	1. make the hole code more comfortable
 *  2. find a solution for the problem with a small amount of edges 
 *  (algo do not find the min, this is based on a problem with closing single arms ...)
 *  
 * @author Andre Pura
 *
 */
public class MyAlgo {
	// the adjacency matrix -> is the base for the algorithm
	private int[][] adjMat;
	private int minLength = 0;

	private int[][] currentTrip;
	//all solved triangles
	private PriorityQueue<Triangle> knownTriangles;
	//all triangles, which are in use
	private List<Triangle> usedTriangles = new ArrayList<Triangle>();
	// a list of all available nodes
	private List<Integer> nodes = new ArrayList<Integer>();
	
	
	/**
	 * Constructor needs an adjacency matrix
	 * @param adjMat
	 */
	public MyAlgo(int[][] adjMat)
	{
		// boring ... a normal constructor .... nothing interesting in it ! 
		MyComparator c = new MyComparator();
		this.adjMat = adjMat;
		currentTrip = new int[adjMat.length][adjMat.length];
		knownTriangles = new PriorityQueue<Triangle>(adjMat.length^3, c);
		for(int i=0; i<adjMat.length;i++) nodes.add(i);
	}
	
	/**
	 * starts the algorithm 
	 */
	public void start()
	{
		int[] startnodes = new int[2];
		
		//finds the startnodes -> startnodes are nodes, next to the min edge
		startnodes = minEdge();
		// removing the nodes from the set -> they are passed
		nodes.remove((Object)startnodes[0]);
		nodes.remove((Object)startnodes[1]);
		// find a triangle, which has min value (min scale)
		Triangle t = additionalPoint(startnodes[0],startnodes[1]);
		// add it to the triangles
		knownTriangles.add(t);
		
		
		//start the algo
		algorithm();
		// adds startnodes the the trip 
		currentTrip[startnodes[1]][startnodes[0]] = 1;
		currentTrip[startnodes[0]][startnodes[1]] = 1;	
		ausgabe(currentTrip);
		for(Triangle triade : usedTriangles)
		{
			System.out.println(triade.getValue());
			minLength=minLength+triade.getValue();
		}
		minLength = minLength + adjMat[startnodes[1]][startnodes[0]];
		System.out.println("\n"+minLength+" - vergleich trip = "+berechneLaenge(currentTrip, adjMat));
		
		// now optimizing the solution by searching better solutions for the arms ..
		optimizing();
		ausgabe(currentTrip);
		System.out.println("length: "+berechneLaenge(currentTrip, adjMat));
	}

	private void algorithm()
	{
		//do it, until all nodes are reached
		while(!nodes.isEmpty())
		{
			//get the best triangle (with the min scale)
			Triangle p = knownTriangles.poll();
			//move the triangle to the used  (required for the out print and min length)
			usedTriangles.add(p);

			// mark the new point==node as passed
			nodes.remove((Object)p.getC());
			//any nodes left?
			if(nodes.isEmpty())
			{
				if(p.getA()!=p.getC() && p.getB()!=p.getC())
				{
					currentTrip[p.getA()][p.getC()] = 1;
					currentTrip[p.getC()][p.getA()] = 1;
					currentTrip[p.getC()][p.getB()] = 1;
					currentTrip[p.getB()][p.getC()] = 1;
				}
				else if (p.getA()==p.getC())
				{
					currentTrip[p.getC()][p.getB()] = 1;
					currentTrip[p.getB()][p.getC()] = 1;
				}
				else if (p.getB()==p.getC())
				{
					currentTrip[p.getA()][p.getC()] = 1;
					currentTrip[p.getC()][p.getA()] = 1;
				}
				break;
			}
			//are the triangles already in the set of triangles ?
			if(p.getA()!=p.getC() && p.getB()!=p.getC())
			{
				boolean flag1 = false;
				boolean flag2 = false;
				Triangle triAdd1 = additionalPoint(p.getA(),p.getC());
				Triangle triAdd2 = additionalPoint(p.getB(),p.getC());
				for(Triangle triAddCompare : knownTriangles)
				{
					// compare both triangles with the actual one from the triangleset
					if(triAdd1.getC()==triAddCompare.getC() && triAdd1.getB()==triAddCompare.getB() && triAdd1.getA()==triAddCompare.getA())
					{
						flag1 = true;
						break;
					}
					else if(triAdd2.getC()==triAddCompare.getC() && triAdd2.getB()==triAddCompare.getB() && triAdd2.getA()==triAddCompare.getA())
					{
						flag2 = true;						
						break;
					}
				}
				//add the triangles or triangle to triangleset
				if(triAdd1.getC() == triAdd2.getC() && triAdd1.getB() == triAdd2.getB() && triAdd1.getA() == triAdd2.getA()) flag1=true;
				if(flag1 && flag2) System.out.println("no triangle added");
				else if(flag1 && !flag2) knownTriangles.add(triAdd2);
				else if(!flag1 && flag2) knownTriangles.add(triAdd1);
				else
				{
					knownTriangles.add(triAdd1);
					knownTriangles.add(triAdd2);
				}
				
				//set the edges as used for the min trip
				currentTrip[p.getA()][p.getB()] = 0;
				currentTrip[p.getB()][p.getA()] = 0;
				currentTrip[p.getA()][p.getC()] = 1;
				currentTrip[p.getC()][p.getA()] = 1;
				currentTrip[p.getC()][p.getB()] = 1;
				currentTrip[p.getB()][p.getC()] = 1;

			}
			// is it a arm ? (this means, that 2 points in the triangle equals)
			else if (p.getA()==p.getC())
			{
				boolean flag = false;
				Triangle triAdd = additionalPoint(p.getB(),p.getC());
				// any other triangle in the set with the same points ? (to avoid duplicates)
				for(Triangle triAddCompare : knownTriangles)
				{
					if(triAdd.getC()==triAddCompare.getC() && triAdd.getB()==triAddCompare.getB() && triAdd.getA()==triAddCompare.getA())
					{
						flag = true;
						break;
					}
				}
				//add triangle to the set
				if(!flag) knownTriangles.add(triAdd);
				// mark edges as used
				currentTrip[p.getC()][p.getB()] = 1;
				currentTrip[p.getB()][p.getC()] = 1;
			}
			// the same as above
			else if (p.getB()==p.getC())
			{
				boolean flag = false;
				Triangle triAdd = additionalPoint(p.getA(),p.getC());
				for(Triangle triAddCompare : knownTriangles)
				{
					if(triAdd.getC()==triAddCompare.getC() && triAdd.getB()==triAddCompare.getB() && triAdd.getA()==triAddCompare.getA())
					{
						flag = true;
						break;
					}
				}
				if(!flag) knownTriangles.add(triAdd);
				currentTrip[p.getA()][p.getC()] = 1;
				currentTrip[p.getC()][p.getA()] = 1;
			}
			for(int i : nodes)
			{
				System.out.println("node: "+i);
			}
			System.out.println("\n ______________\n");
		}
	}
	// searches for the arms to optimize
	private void optimizing()
	{
		for(int i = 0; i < currentTrip.length; i++)
		{
			// arm is a flag for am identified arm (=true)
			boolean arm = false;
			for(int j = 0; j < currentTrip.length; j++)
			{
				if(currentTrip[i][j]==1 && !arm) arm = true;
				else if(currentTrip[i][j]==1 && arm) 
				{
					// no arm found -> no need for further searches
					arm = false;
					break;
				}
			}
			// start optimizing the arm
			if(arm) optimizeArm(i);
		}
	}
	// optimizes the arm given by int value (=end node)
	private void optimizeArm(int currentArm)
	{
		int armLength =0;
		int connectedNodes=0;
		int currentNode = currentArm;
		int x = currentArm;
		int lastnode=currentArm;
		// search until 3 nodes were found
		while(connectedNodes!=3)
		{
			// default = no node connected to the current one
			connectedNodes=0;
			for(int i = 0; i<currentTrip.length; i++)
			{
				// counts the connected nodes. the last node is not counted
				if(currentTrip[currentNode][i]==1 && i!=lastnode) 
				{
					connectedNodes=connectedNodes+1;
					// set x for the next node number to go back on the arm
					x = i;
				}
			}
			if(connectedNodes!=3) 
			{
				// the node is not the origin of the arm -> adding the length to the armlength
				armLength = armLength + adjMat[currentNode][x];
				// setting actual node as last node for the next step
				lastnode=currentNode;
				currentNode = x;
			}
		}
		System.out.println("Arm"+currentArm+" length "+armLength);
		// searching for a better edge between the end of the arm and another node in the net
		// the edge must be shorter then two times the arm length (which means the trip over the arm itself)
		for(int i = 0; i<currentTrip.length;i++)
		{
			if(adjMat[currentArm][i] < armLength*2 && adjMat[currentArm][i]!=1)
			{
				// node found -> adding the edge to the trip
				currentTrip[currentArm][i] = 1;
				currentTrip[i][currentArm] = 1;
			}
		}
		
	}	
	
	/**
	 * Find the min edge value in a graph
	 * @return int[] {edge' node 1, edge's node 2, value} (=from to)
	 */
	private int[] minEdge()
	{
		int[] minPos = new int[3];
		int min = Integer.MAX_VALUE;
		for(int i = 0; i<adjMat.length; i++)
			for(int j = 0; j< adjMat.length; j++)
			{
				if(adjMat[i][j]<min){
					min = adjMat[i][j];
					minPos[0] = i;
					minPos[1] = j;
				}
			}
		minPos[2] = min;
		return minPos;
	}
	
	/**
	 * Finds the min triangle between a,b and a third connected point to a and b
	 * @param a
	 * @param b
	 * @return MinValue, Node
	 */
	private Triangle additionalPoint(int a, int b)
	{
		int[] min={Integer.MAX_VALUE, a};
		int flag = 0;
		for(int i = 0; i < adjMat.length; i++)
		{
			// is the node available ?
			if(nodes.contains(i))
			{
				// are there not available ways ? (=MAX INT VALUES)
				if(adjMat[a][i]!=Integer.MAX_VALUE && adjMat[i][b]!=Integer.MAX_VALUE )
					// is the new way shorter than the old one ?
					if(min[0]>adjMat[a][i]+adjMat[b][i])
					{		
						min[1] = i;
						min[0] = adjMat[a][i]+adjMat[i][b];
						flag = 1;
					}
				if (adjMat[a][i]!=Integer.MAX_VALUE)
					// is it shorter to go from a point to another and back, instead of using a third one ?
					if(min[0]>adjMat[a][i]*2)
					{
						min[1] = i;
						min[0] = adjMat[a][i]*2;						
						flag = 2;
					}
				if(adjMat[b][i]!=Integer.MAX_VALUE)
					// is it shorter to go from a point to another and back, instead of using a third one ?
					if(min[0]>adjMat[b][i]*2)
					{
						min[1] = i;
						min[0] = adjMat[b][i]*2;
						flag = 3;
					}
			}
		}
		// is it a arm or a normal triangle ?!
		if(flag == 2) b=min[1];
		else if(flag==3)a=min[1];
		// return the new triangle ....
		return new Triangle(a,b,min[1],min[0]);
	}
	
	/**
	 * schreibt ein array in die konsole
	 * date: june 2006
	 * @param a
	 */
	void ausgabe(int[][] a)
	{
		for(int i=0; i<a.length; i++)
		{
			for(int j=0;j<a.length;j++)
			{
				if(a[i][j]!=0) 
				{
					System.out.println("MATRIX "+i+" - "+j+" = "+a[i][j]);
				}
			}
		}
	}
	/**
	 * gibt den trip durch eine matrix zurueck (trip definiert durch ein in[][] array a)
	 * date: june 2006
	 * date: UPDATE: falls es arme gibt, werden diese beruecksichtigt fuer den trip mit ihrer wahren laenge
	 * @param a = matrix mit im set oder net
	 * @param c = kostenmatrix
	 * @return weglaenge
	 */
	int berechneLaenge(int[][] a, int[][] c)
	{
		int laenge = 0;
		for(int i = 0; i <a.length; i++)
		{
			int indicator = 0;
			for(int j = 0; j < a.length; j++)
			{
				if(a[i][j]==1 && j>i) laenge = laenge + c[i][j];
				if(a[i][j]==1) indicator = indicator +1;
			}
			if(indicator==1)
			{
				int armLength =0;
				int connectedNodes=0;
				int currentNode = i;
				int x = i;
				int lastnode=i;
				// search until 3 nodes were found
				while(connectedNodes!=3)
				{
					connectedNodes=0;
					for(int ii = 0; ii<currentTrip.length; ii++)
					{
						if(currentTrip[currentNode][ii]==1 && ii!=lastnode) 
						{
							connectedNodes=connectedNodes+1;
							x = ii;
						}
					}
					if(connectedNodes!=3) 
					{
						armLength = armLength + adjMat[currentNode][x];
						lastnode=currentNode;
						currentNode = x;
					}
				}
				laenge = laenge + armLength;
			}
		}
			
		return laenge;
	}
}
