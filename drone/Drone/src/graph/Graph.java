package graph;
import java.util.HashSet;

public class Graph {
    int[][] adjMatrix;
    Edge[] edges;
    Node[] nodes;

    public int[][] getAdjMatrix(){
    	return adjMatrix;
    }
    
    public Graph(int[][] adjMatrix) {
        this.adjMatrix = adjMatrix;
        this.createGraph();
    }

    public void createGraph() {
        int length = (this.adjMatrix.length) * (this.adjMatrix.length - 1) / 2;
        edges = new Edge[length];
        length = this.adjMatrix.length;
        nodes = new Node[length];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node(100 + i);
        }
        int counter = 0;
        for (int i = 0; i < this.adjMatrix.length; i++) {
            for (int j = 0; j < i; j++) {
                edges[counter] = new Edge(nodes[i], nodes[j], adjMatrix[i][j]);
                nodes[i].addEdge(edges[counter]);
                nodes[j].addEdge(edges[counter]);
                counter++;
            }
        }
    }

    public int performPrim(int startPoint) {
        HashSet<Edge> mstEdge = new HashSet<Edge>();
        nodes[startPoint].setVisited(true);
        do {
            HashSet<Node> visitedNodes = this.getVisitedNodes();
            Object[] vnodes = visitedNodes.toArray();
            HashSet<Edge> edgeList=new HashSet<Edge>();
            for(int i=0;i<vnodes.length;i++) {
                Object []egs=((Node)vnodes[i]).getEdges().toArray();
                for(int j=0;j<egs.length;j++) {
                    if(!((Edge)egs[j]).isUsed()) {
                        edgeList.add((Edge)egs[j]);
                    }
                }
            }
            try {
                Edge temp=Edge.getMinEdge(edgeList);
                temp.setUsed();
                mstEdge.add(temp);
            } catch (Exception e) {
            }
        } while (isGraphVisited());
        Object []finalEdges=mstEdge.toArray();
        int totalCost=0;
        for(int i=0;i<finalEdges.length;i++)
        {
            totalCost+=((Edge)finalEdges[i]).getWeight();
            System.out.println(((Edge)finalEdges[i]).node1.x + "," + ((Edge)finalEdges[i]).node1.y);
        }
        
        
        
        return totalCost;
    }

    public HashSet<Node> getVisitedNodes() {
        HashSet<Node> visitedNodes = new HashSet<Node>();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].isVisited()) {
                visitedNodes.add(nodes[i]);
            }
        }
        return visitedNodes;
    }

    public boolean isGraphVisited() {
        for (int i = 0; i < nodes.length; i++) {
            if (!nodes[i].isVisited()) {
                return true;
            }
        }
        return false;
    }

}