package util;

import graph.Node;

import java.util.Vector;

//Used to implement pass by reference
public class Reference{
	public double tripdistance;
	public double numhops;
	public Vector<Node>bestanswer = null;
	public double cost;
	public int tour[];
}
