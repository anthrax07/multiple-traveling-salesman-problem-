package tsp.temp;
public class Triangle {
	private int a,b,c,value;
	
	Triangle(int a, int b, int c)
	{
		this.a=a;
		this.b=b;
		this.c=c;
	}
	Triangle(int a, int b, int c, int value)
	{
		this.a=a;
		this.b=b;
		this.c=c;
		this.value=value;
	}
	
	void setValue(int v){ value=v; }
	
	int getValue(){ return value;}
	int getA(){ return a;}
	int getB(){ return b;}
	int getC(){ return c;}
	
}
