package tsp.temp;import java.util.Comparator;


public class MyComparator implements Comparator<Triangle> {

	@Override
	public int compare(Triangle t1, Triangle t2) {
		
		if(t1.getValue()<t2.getValue()) return -1;
		else if(t1.getValue()>t2.getValue()) return 1;
		else return 0;
	}

}
