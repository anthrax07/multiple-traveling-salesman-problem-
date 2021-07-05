package util;
import graph.Node;

import java.util.Vector;

import main.Principal;


public class GeraMatriz {

	static int inf = Integer.MAX_VALUE;
	
	static int stp = 1;

	
	static int m[][]={ {0,inf,inf,stp,stp,inf,inf,inf},
		    		   {inf,0,inf,stp,inf,stp,inf,inf},
		    		   {inf,inf,0,inf,stp,inf,inf,inf},
		    		   {stp,stp,inf,0,inf,inf,inf,inf},
		    		   {stp,inf,stp,inf,0,inf,inf,inf},				
		    		   {inf,stp,inf,inf,inf,0,stp,inf},
		    		   {inf,inf,inf,inf,inf,stp,0,inf},
		    		   {inf,inf,inf,inf,inf,inf,inf,0},
           };

	static int a[];
	
	public int t(int n1, int n2){
		if(n1==n2) return 0;
		if(m[n1][n2]<Integer.MAX_VALUE) return m[n1][n2];
		if (a[n2]==1) return Integer.MAX_VALUE;
		a[n2] = 1;
		int menor = Integer.MAX_VALUE;
		for(int i=0;i<m.length;i++){
			if(m[n2][i]==stp){
				menor = Math.min(menor, t(n1,i));
			}
		}
		a[n2]=0;
		if (menor==inf) return menor;
		return menor+stp;
	}
	
	public int valor(int l, int c){
		a[c]=1;
		if(m[l][c] == stp) {
			a[c]=0;
			return stp;
		}
		int menor = Integer.MAX_VALUE;
		for(int i=0; i<m.length;i++){
			if(i==c) continue;
			if(m[c][i] == inf) continue;
			if(m[c][i] == 0) continue;
			
			if(m[c][i] == stp){
				if(a[i]==1) continue;
				menor = Math.min(menor, valor(l,i));
			}
		}
		a[c]=0;
		if (menor==inf) return menor;
		return menor+stp;
	}

	public void create(){
		a = new int[m.length];
		for(int i=0;i<m.length;i++){
				a[i] = 0;
		}
		for(int y=0;y<m.length;y++){

			for(int x=y; x<m.length; x++){
				for(int i=0;i<m.length;i++) a[i]=0;
				m[y][x] = t(y,x);
				m[x][y] = m[y][x];
				//System.out.print(m[y][x] + " ");
			}
		//System.out.print("\n");
		}
	}
	
	public static void main(String[] args) {
		(new GeraMatriz()).create();
		show(5,3);
		
//			a[0]=1;
		//System.out.println(valor(0,6));
		/*
			(new GeraMatriz()).generate();
			for(int l=0; l<m.length;l++){
				for(int c=0; c<m.length;c++){
					if(m[l][c]==inf)
						System.out.print("  #");
					else
						System.out.print("  "+m[l][c]);
				}
				System.out.print("\n");
			}
			*/
			/*
			GeraMatriz aa = new GeraMatriz();
			for(int y=0;y<m.length;y++){

				for(int x=0; x<m.length; x++){
					for(int i=0;i<m.length;i++) a[i]=0;
					m[y][x] = aa.t(y,x);
					m[x][y] = m[y][x];
					System.out.print(m[y][x] + " ");
				}
			System.out.print("\n");
			}
			*/
			
	}
		

		public  void generate(){
			for(int l=0; l<m.length;l++){
				for(int c=0; c<m.length;c++){
					if(l==c) continue;
						m[l][c] = valor(l,c); 
				}
			}
		}
		

		public void receive(double range, Vector <Node> sensors, Vector <Node> drones, int valueperhop){
			int side = sensors.size() + drones.size();
			m = new int [side][side];
			
			for(int l=0; l<sensors.size()+drones.size(); l++){
				for(int c=0; c<sensors.size()+drones.size(); c++){
					m[l][c] = Integer.MAX_VALUE;
				}
			}
			
			for(int l=0; l<sensors.size()+drones.size();l++){
				for(int c=0; c<sensors.size()+drones.size();c++){
					m[l][c] = Integer.MAX_VALUE;
				}
			}
			
			for(int l=0; l<drones.size() ;l++){
				Node d = drones.get(l);
				for(int c=0; c<sensors.size();c++){
					Node s = sensors.get(c);
					if(d.distance(s)<=range){
						m[l+sensors.size()][c]=1;
					}
				}
			}

			a = new int[m.length];
			for(int i=0;i<m.length;i++){
					a[i] = 0;
			}
		}

		public static void show(int numsensors,int numdrones){
	        for(int l=0; l<(numdrones+numsensors);l++){
	        	if(l<numsensors) System.out.print(l+"\t");
	        	else System.out.print((l-numsensors)+"\t");
	        	for(int c=0; c<(numdrones+numsensors);c++){
	        		if(m[l][c]==Integer.MAX_VALUE)
	        			System.out.print("   # ");
	        		else if(m[l][c]>999)
	        			System.out.print(m[l][c] +" ");
	        		else if(m[l][c]>99)
	        			System.out.print(" " +m[l][c] +" ");
	        		else if(m[l][c]>9)
	        			System.out.print("  " +m[l][c] +" ");
	        		else 
	        			System.out.print("   " +m[l][c] +" ");
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
}
