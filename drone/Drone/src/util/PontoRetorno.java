package util;

public class PontoRetorno  implements Comparable{
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public double getDist() {
		return dist;
	}

	public void setDist(double dist) {
		this.dist = dist;
	}
	public double getEsc1() {
		return esc1;
	}

	public void setEsc1(double esc1) {
		this.esc1 = esc1;
	}

	public double getEsc2() {
		return esc2;
	}

	public void setEsc2(double esc2) {
		this.esc2 = esc2;
	}
	
	public double getID() {
		return id;
	}

	public void setID(double id) {
		this.id = id;
	}

	private double x,y, dist,esc1,esc2,id;

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		PontoRetorno aux = (PontoRetorno)arg0;
		if(aux.getDist()>dist)
			return -1;
		if(aux.getDist()==dist)
			return 0;
		if(aux.getDist()<dist)
		return 1;
		return 0;
	}
	

}
