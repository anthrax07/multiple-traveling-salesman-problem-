package graph;
import java.util.HashSet;
import java.awt.Point;

public class Node extends Point{
    private int nodeId;
    private boolean visited;
    private HashSet<Edge> edges;
    public int hop;  //Usefull to create the Drone X Nodes links

    public Node(int nodeId, double x, double y) {
        this.nodeId = nodeId;
        this.visited = false;
        edges = new HashSet<Edge>();
        super.x = (int)x;
        super.y=(int)y;
        hop = Integer.MAX_VALUE;
    }

    public Node(int nodeId) {
        this.nodeId = nodeId;
        this.visited = false;
        edges = new HashSet<Edge>();
        x=0;
        y=0;
    }
    
    public int getNodeId() {
        return nodeId;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public HashSet<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }
    
    public double distance(Point p){
    	return Math.sqrt((x - p.x)*(x - p.x) + (y-p.y)*(y-p.y));
    }
}