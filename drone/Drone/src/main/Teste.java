package main;

public class Teste {

	public static void generate(int max, int subset[]){
		String s ="";
		for(int i=0;i<subset.length;i++) s= s + " " + subset[i];
		System.out.println(s);
		
		subset[subset.length-1]++;

		int vmax[] = new int[subset.length];
		for(int i=0; i<subset.length;i++)
			vmax[i] = max - subset.length + i+1;
		
		int m = subset.length-1;
		while(m>0){
			if(subset[m]>vmax[m]){
				subset[m-1]++;
				if(subset[m-1]<=vmax[m-1]){
					for(int i=m;i<subset.length;i++)
						subset[i]=subset[i-1]+1;
				}
			} 
			m--;
		}
		
		int count=0;
		for(int i=subset.length-1;i>=0;i--){
			if(subset[i]>=vmax[i]) count++;
		}
		if(count == subset.length) return;
		generate(max, subset);
	}

	
	
	
	public static int[] next(int max, int subset[]){
		subset[subset.length-1]++;

		int vmax[] = new int[subset.length];
		for(int i=0; i<subset.length;i++)
			vmax[i] = max - subset.length + i+1;
		
		int m = subset.length-1;
		while(m>0){
			if(subset[m]>vmax[m]){
				subset[m-1]++;
				if(subset[m-1]<=vmax[m-1]){
					for(int i=m;i<subset.length;i++)
						subset[i]=subset[i-1]+1;
				}
			} 
			m--;
		}
		
		int count=0;
		for(int i=subset.length-1;i>=0;i--){
			if(subset[i]>vmax[i]) count++;
		}
		if(count == subset.length) 
			return null;
		return subset;

	}
	
	
	
	
	
	
	public static void main(String[] args) {
		int t=4;
		int subset[] = new int[t];
		for(int i=0; i<t; i++)
			subset[i] = i;
		subset[1] = 1;
		subset[2] = 2;
		subset[3] = 3;
	//	subset[4] = 4;
//		generate(10,subset);
		
		
		
		while(true){
			String s ="";
			for(int i=0;i<subset.length;i++) s= s + " " + subset[i];
			System.out.println(s);
			subset = next(10,subset);
			if(subset==null) break;
		}

		
	}
}
