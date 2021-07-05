package util;
import main.Principal;
import graph.*;

public class AnalyzedNode extends Node{
	private int hop;
	public AnalyzedNode(int nodeId) {
		super(nodeId);
		hop = Principal.INFINITY;
	}
}
