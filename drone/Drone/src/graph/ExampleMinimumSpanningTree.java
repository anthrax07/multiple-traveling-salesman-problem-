package graph;
public class ExampleMinimumSpanningTree {

    public static void main(String[] args) {
        int[][] m = new int[10][];

        for (int i = 0; i < m.length; i++) {
            m[i] = new int[i + 1];
        }

        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < i; j++) {
                m[i][j] = (int) (Math.random() * 100);
            }
        }

        System.out.println("Graph is >>");
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < i; j++) {
                System.out.print(m[i][j] + ",");
            }
            System.out.println();
        }
        System.out.println();
        Graph graph = new Graph(m);
        System.out.println("The Total Cost is : " + graph.performPrim(0));
    }

}