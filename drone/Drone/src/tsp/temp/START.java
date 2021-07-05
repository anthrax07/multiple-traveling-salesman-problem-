package tsp.temp;
public class START {
	public static void main(String args[])
	{
		// cost matrix -> not available ways are marked with the integer maximum value
        int[][] costMatrix =    {    {Integer.MAX_VALUE,2,Integer.MAX_VALUE,Integer.MAX_VALUE,3,1},
                {2,Integer.MAX_VALUE,2,Integer.MAX_VALUE,1,2},
                {Integer.MAX_VALUE,2,Integer.MAX_VALUE,1,Integer.MAX_VALUE,4},
                {Integer.MAX_VALUE,Integer.MAX_VALUE,1,Integer.MAX_VALUE,2,Integer.MAX_VALUE},
                {3,1,Integer.MAX_VALUE,2,Integer.MAX_VALUE,10},
                {1,2,4,Integer.MAX_VALUE,10,Integer.MAX_VALUE}
            };
        
     /*   
		int x[][] = {{1,2,3,4,5,6,7,8,9,0,1,2,3,4},
				 {1,2,3,4,5,6,7,8,9,0,1,2,3,4},
				 {1,2,3,4,5,6,7,8,9,0,1,2,3,4},
				 {1,2,3,4,5,6,7,8,9,0,1,2,3,4},
				 {1,2,3,4,5,6,7,8,9,0,1,2,3,4},
				 {1,2,3,4,5,6,7,8,9,0,1,2,3,4},
				 {1,2,3,4,5,6,7,8,9,0,1,2,3,4},
				 {1,2,3,4,5,6,7,8,9,0,1,2,3,4},
				 {1,2,3,4,5,6,7,8,9,0,1,2,3,4},
				 {1,2,3,4,5,6,7,8,9,0,1,2,3,4},
				 {1,2,3,4,5,6,7,8,9,0,1,2,3,4},
				 {1,2,3,4,5,6,7,8,9,0,1,2,3,4},
				 {1,2,3,4,5,6,7,8,9,0,1,2,3,4},
				 {1,2,3,4,5,6,7,8,9,0,1,2,3,4}};
        */
        
        
        int x[][] = {{1,2,3,4,5},
				 {1,2,3,4,5},
				 {1,2,3,4,5},
				 {1,2,3,4,5},
				 {1,2,3,4,5}};
        // making a new algorithm object
		MyAlgo m = new MyAlgo(x);
		// and start it ! 
		m.start();
	}
}
