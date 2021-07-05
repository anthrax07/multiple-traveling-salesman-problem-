package screens; 
import javax.swing.*;

import util.MoreConnected.Position;
import main.Principal;
import graph.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class pnlImage extends JPanel{
	int height;
	int width;
	int radiussensor;
	int radiusdrone;
	int radiorange;
	
	boolean showoption = false;  //False:shows the original graph, True:shows the spanningtree
	boolean showmoreconnected = false;
	boolean showRoute = false;
	boolean showimprovements = false;
	
	int spanningtree[][];        //Save the spanning tree
	
	//Retirar, tentar trabalhar somente com o graph
	Vector <Node>sensors;
	Vector <Node>droneposition;
	Vector<Node> moreconnected;   //contem a ordem dos mais conectados
	Vector<Node> improvements;
		
	int route[];
	int more; //n�mero de mais conectados

	
	Handler obj = new Handler();

	
	public pnlImage(int height, int width, int radiussensor, int radiusdrone,int radiorange){
		
		this.height=height;
		this.width=width;
		this.radiussensor=radiussensor;
		this.radiusdrone=radiusdrone;
		this.radiorange = radiorange;
		
		addMouseMotionListener(obj);
		addMouseListener(obj);
		setVisible(true);
	}
		

	public void showSpanningTree(){
		showoption = true;
		repaint();
	}
	
	public void showMoreConnected(){
		showmoreconnected = true;
		repaint();
	}
	
	public void showGraph(){
		showoption = false;
		repaint();
	}
	
	public void paint(Graphics g){
		g.clearRect(0, 0, height+50, width+50);
		if(showimprovements){
			drawImprovements(g);
		}
		else if(showoption){  //show spanning tree
			if(spanningtree!=null) drawSpanningTree(g);
		}                //show graph
		else{
			if(droneposition != null && sensors!=null)drawEdgesNodesDrones(g);
			if (sensors!=null) {
				drawEdgesNodesNodes(g);
				drawSensors(g);
			}
			if(droneposition != null) {
				drawDronePositions(g);
			}
		}
		if(showmoreconnected){
			drawMoreConnected(g);
		}
		if(showRoute){
			drawRoute(g);
		}
	}

	public void receiveRoute(int r[]){
		route = r;
		showRoute = true;
		repaint();
	}
	
	public void drawImprovements(Graphics g){
		for(int i=0; i<improvements.size();i++){
			int x1 = improvements.get(i).x;
			int y1 = improvements.get(i).y;
			g.fillOval(x1-(radiussensor/2), y1-(radiussensor/2), radiussensor, radiussensor);	
			g.drawString("" + improvements.get(i).getNodeId(), x1-(radiussensor/2), y1-(radiussensor/2));
		}
		
		int x = droneposition.get(route[0]).x;
		int y = droneposition.get(route[0]).y;
		g.drawLine(0,0,x,y);
		for(int i=0; i<route.length-1;i++){
			int x1 = droneposition.get(route[i]).x;
			int y1 = droneposition.get(route[i]).y;
			int x2 = droneposition.get(route[i+1]).x;
			int y2 = droneposition.get(route[i+1]).y;
			g.drawLine(x1,y1,x2,y2);			
		}
		x = droneposition.get(route[route.length-1]).x;
		y = droneposition.get(route[route.length-1]).y;
		g.drawLine(0,0,x,y);
		
		
	}
	
	
	public void receiveImprovements(Vector<Node>best, int st[][],int r[]){
		improvements=best;
		route = r;
		showoption = false;  //False:shows the original graph, True:shows the spanningtree
		showmoreconnected = false;
		showRoute = false;
		showimprovements = true;
		repaint();
	}
	
	public void drawRoute(Graphics g){
		g.setColor(Color.green);
		int x = droneposition.get(route[0]).x;
		int y = droneposition.get(route[0]).y;
		g.drawLine(0, 0, x, y);
		for(int i=0;i<route.length-1;i++){
			int x1 = droneposition.get(route[i]).x;
			int y1 = droneposition.get(route[i]).y;
			int x2 = droneposition.get(route[i+1]).x;
			int y2 = droneposition.get(route[i+1]).y;
			g.drawLine(x1, y1, x2, y2);	
		}
		x = droneposition.get(route[route.length-1]).x;
		y = droneposition.get(route[route.length-1]).y;
		g.drawLine(0, 0, x, y);		
		g.setColor(Color.BLACK);
	}
	
	public void drawSpanningTree(Graphics g){
		int numsensors = sensors.size();
		int numdrones = droneposition.size();
		for(int l=numsensors;l<spanningtree.length; l++){   //Line varies only drone positions in the matrix
			for(int c=0; c<spanningtree.length; c++){
				if(spanningtree[l][c]>0 && spanningtree[l][c]<Principal.INFINITY){
					if(c<numsensors){
						Node sensor = sensors.get(c);
						Node drone = droneposition.get(l-numsensors);
						g.drawLine((int)sensor.getX(),(int)sensor.getY(),(int)drone.getX(),(int)drone.getY());
					}
					/*else {
						g.setColor(Color.YELLOW);
						Node droneline = droneposition.get(l-numsensors);
						Node dronecolumn = droneposition.get(c-numsensors);
						g.drawLine((int)droneline.getX(),(int)droneline.getY(),(int)dronecolumn.getX(),(int)dronecolumn.getY());
						g.setColor(Color.BLACK);
					}*/
				}
			}
		}
		drawSensors(g);
		drawDronePositions(g);
	}
	
	
	//Recebe a spanningtree e desenha na tela o resultado
	public void receiveSpanningTree(int[][] m){
		spanningtree = m;	
		showSpanningTree();
		repaint();
	}

	
	public void receiveSensors(Vector<Node> sensors){
		this.sensors = sensors;
		repaint();
	}
	
	public void receiveDronePosition(Vector<Node> droneposition){
		this.droneposition = droneposition;
		repaint();
	}
	
	//Recebe a spanningtree e desenha na tela o resultado
	public void receiveMoreConnected(Vector<Node> v, int more){
		this.moreconnected = v;	
		this.more = more;
		//showSpanningTree();
		//repaint();
	}
	
	public void drawEdgesNodesDrones(Graphics g){
		g.setColor(Color.GREEN);
		for(int d=0;d<droneposition.size();d++){
			for(int n=0;n<sensors.size();n++){
				if(sensors.get(n).distance(droneposition.get(d))<=radiorange){
					g.drawLine(droneposition.get(d).x,droneposition.get(d).y,sensors.get(n).x,sensors.get(n).y);
				}
			}
		}
		g.setColor(Color.BLACK);
	}
	
	public void drawEdgesNodesNodes(Graphics g){
		for(int d=0;d<sensors.size();d++){
			for(int n=0;n<sensors.size();n++){
				if(sensors.get(n).distance(sensors.get(d))<=radiorange){
					g.drawLine(sensors.get(d).x,sensors.get(d).y,sensors.get(n).x,sensors.get(n).y);
				}
			}
		}
	}
	public void drawSensors(Graphics g){
		for(int i=0; i<sensors.size(); i++){
			g.drawString(""+sensors.get(i).getNodeId(), (int)sensors.get(i).getX()-(radiussensor/2), (int)sensors.get(i).getY()-(radiussensor/2));
			g.fillOval((int)sensors.get(i).getX()-(radiussensor/2), (int)sensors.get(i).getY()-(radiussensor/2), radiussensor, radiussensor);
		}
	}
	
	
	public void drawDronePositions(Graphics g){
		g.setColor(Color.RED);
		for(int i=0; i<droneposition.size(); i++){
			g.drawString(""+droneposition.get(i).getNodeId(),(int)droneposition.get(i).getX()-(radiusdrone/2), (int)droneposition.get(i).getY()-(radiusdrone/2));
			g.fillOval((int)droneposition.get(i).getX()-(radiusdrone/2), (int)droneposition.get(i).getY()-(radiusdrone/2), radiusdrone, radiusdrone); 
		}
		g.setColor(Color.BLACK);
	}
	
	public void drawMoreConnected(Graphics g){
		int q=4;
		g.setColor(Color.BLUE);
		for(int i=0; i<more; i++){
			g.fillOval((int)moreconnected.get(i).getX()-((radiusdrone+q)/2), (int)moreconnected.get(i).getY()-((radiusdrone+q)/2), radiusdrone+q, radiusdrone+q);
			//g.drawString(""+moreconnected.get(i).id+"   "+((int)moreconnected.get(i).getX()),10,(i*20)+20);
		}
		g.setColor(Color.BLACK);
	}
	
//	boolean escolhido = false;  //true=escolher nó, false=escolher nova posição
//	int no_escolhido = 0;
	public class Handler extends MouseMotionAdapter implements MouseListener{
		public void mouseClicked(MouseEvent e) {
/*			if(!escolhido){	
				double p = 5;
				//JOptionPane.showMessageDialog(null, ""+e.getX()+" "+e.getY());
				double eventX = e.getX();
				double eventY = e.getY();
				for(int i=0; i<sensors.size();i++){
					double x = sensors.get(i).getX();
					double y = sensors.get(i).getY();
					
					if(eventX<x+p && eventX>x-p){
						if(eventY<y+p && eventY>y-p){
							escolhido = true;
							no_escolhido = i;
							//JOptionPane.showMessageDialog(null, i + ": "+x+","+y);
						}
					}
				}
			}
			else{
				sensors.get(no_escolhido).setLocation(e.getX(), e.getY());
				repaint();
				escolhido = false;
			}
*/
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseMoved(MouseEvent e) {
				
		}
	}

}
