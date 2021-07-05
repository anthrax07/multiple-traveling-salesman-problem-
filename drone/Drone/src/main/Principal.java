package main;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import onehop.OneHop;
import bruteforce.BruteForce;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

//import jdk.nashorn.internal.scripts.JO;
import reduction.Reduction;
import screens.*;
import util.Path;
import util.Reference;
import util.ShortestPath;
import util.SpanningTree;
import util.Cost;
import util.GeraMatriz;
import util.MoreConnected;
import graph.*;
import grasp.*;
import tsp.*;

public class Principal extends JFrame {
	//Constant
	public static int INFINITY = Integer.MAX_VALUE;//2147483647; //Biggest integer value in Java
	private static int height = 200;
	private static int width = 200;
	private int radiussensor = 8; //Radius of the circle representing the sensor node
	private int radiusdrone = 8; //Radius of the circle representing the sensor node
	
	double precision=0;          //Distance between two drone positions in the grid --> informed by the user
	
	static private double speed = 2; //Drone's speed m/s
//	public static double datatime = 0.0256;   //default values
	public static double hoppacket = 0.0001;  //time to transmit a packet over one hop
	
	public static int seed = 30;  //utilizado para fazer os experimentos
	public static int pktsize = 28; //Number of bytes in each packet
	
	public static int radiorange = 60;
	
	static Vector <Node>sensors = new Vector<Node>();
	static Vector <Node>dronepositions = new Vector<Node>();
	Vector<Node> bestanswer = new Vector<Node>();                     //Useful to save the best answer found
	
	public static double adjacency[][]; //Adjacency matrix with the links drone positions - nodes. The weights are based in number hops.
	
	static public pnlImage image;    //Panel with the draw
	JScrollPane jspImage;     //Scrool to this panel
	
	Handler events = new Handler();
	
	JButton jbtGraph, jbtSpanningTree, jbtMoreConnected,  jbtExperimento1, jbtExperimento2, jbtExperimento3, jbtgambi; //, jbtExperimento3, jbtExperimento4, jbtExperimento5, jbtCreateSensors,jbtGenerate500x500, jbtCallNS;

	JLabel jlbPath;
	static JTextField jtfPath ;
	
	public Principal(){
		getContentPane().setLayout(null);

		createMenu();

		//Cria o painel com a parte gráfica
		image = new pnlImage(height,width,radiussensor,radiusdrone,radiorange);
		image.setPreferredSize(new Dimension(width, height));
		image.receiveSensors(sensors);
		
		//Criando o scroll
		jspImage = new JScrollPane(image);
		jspImage.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jspImage.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//jspImage.
		jspImage.setBounds(0, 0, width+18, height+18);
		
		add(jspImage);

		setResizable(true);
		//setBounds(0,0,1200,800);

		jbtGraph = new JButton("Draw Graph");
		jbtGraph.addActionListener(events);
		jbtGraph.setBounds(width+50, 30, 170, 30);
		add(jbtGraph);
		
		jbtSpanningTree = new JButton("Draw Spanning Tree");
		jbtSpanningTree.addActionListener(events);
		jbtSpanningTree.setBounds(width+50, 80, 170, 30);
		add(jbtSpanningTree);
		
		jbtMoreConnected = new JButton("More Connected");
		jbtMoreConnected.addActionListener(events);
		jbtMoreConnected.setBounds(width+50, 130, 170, 30);
		add(jbtMoreConnected);
		
		
		//Experimentos automatizados
		jbtExperimento1 = new JButton("Experiment: 400x400, 100 nodes");
		jbtExperimento1.setBounds(width+50, 230, 370, 30);
		jbtExperimento1.addActionListener(events);
		add(jbtExperimento1);
	/*	
		jbtExperimento2 = new JButton("Experiment: varying nodes 50 to 250");
		jbtExperimento2.setBounds(width+50, 280, 370, 30);
		jbtExperimento2.addActionListener(events);
		add(jbtExperimento2);

		jbtExperimento3 = new JButton("Experiment: varying data rate");
		jbtExperimento3.setBounds(width+50, 330, 370, 30);
		jbtExperimento3.addActionListener(events);
		add(jbtExperimento3);

		jbtgambi = new JButton("Gambi");
		jbtgambi.setBounds(width+50, 380, 370, 30);
		jbtgambi.addActionListener(events);
		add(jbtgambi);
*/
		
//		jbtExperimento4 = new JButton("Experiment 4-varying speed(400x400)");
//		jbtExperimento4.setBounds(width+50, 380, 370, 30);
//		jbtExperimento4.addActionListener(events);
//		add(jbtExperimento4);
		
//		jbtExperimento5 = new JButton("Experiment 5-varying data(400x400)");
//		jbtExperimento5.setBounds(width+50, 430, 370, 30);
//		jbtExperimento5.addActionListener(events);
//		add(jbtExperimento5);
		
//		jbtCreateSensors = new JButton("Create files with sensors");
//		jbtCreateSensors.setBounds(width+50, 480, 370, 30);
//		jbtCreateSensors.addActionListener(events);
//		add(jbtCreateSensors);
		
//		jbtGenerate500x500 = new JButton("Create sensors 400x400");
//		jbtGenerate500x500.setBounds(width+50, 530, 370, 30);
//		jbtGenerate500x500.addActionListener(events);
//		add(jbtGenerate500x500);

//		jbtCallNS = new JButton("Call NS");
//		jbtCallNS.setBounds(width+330,10,270,30);
//		jbtCallNS.addActionListener(events);
//		add(jbtCallNS);
		
		
		jlbPath = new JLabel("Path to ns script");
		jlbPath.setBounds(width+50, 580, 370, 30);
		add(jlbPath);	
		
		jtfPath = new JTextField("/home/rone/scripts-ns/experimentos_drone/exp_query/");
		jtfPath.setBounds(width+50, 620, 450, 30);
		add(jtfPath);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		generateDronePosition(84);/////DELETE THIS LINE
	}
	
	int more=0;
	public class Handler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==jbtExperimento1){
				double dronespeed = speed;
				//xvalue é o valor informado no JOptionPane, tempo necessário para um nó enviar todos os seus dados para o drone
				//xvalue = 1 equivale a um nó sensor com 20 kbits na memória. No gráfico, o que é plotado é o 20
				double xvalue = 1;             
				generateDronePosition(84);
				while(true){   
					System.out.println("\nTempo de transmissão (dado da caixa de diálogo)==> "+xvalue);
					//Varia de acordo com o número de repetições.
					for(int i=1;i<=35;i++){
						//String filename = "/home/rone/"+i+"sensores.txt";
						String filename = "/home/rone/30sensores.txt";
	//					System.out.println("filename:"+filename);
						readSensores(filename);
						performIncremental(xvalue, dronespeed, "",xvalue);
	//					performDecremental(xvalue, dronespeed, "",xvalue);
					}
					//xvalue *= 2;
					//if(xvalue > 256) break;
					xvalue ++;
					if(xvalue>6) break;
				}	
			}
			/*
			if(e.getSource()==jbtExperimento2){
				double dronespeed = speed;
				double xvalue = 1;             //Start point in X
				generateDronePosition(84);
				while(true){   
					double datatime = 0.01 * xvalue;    //Tempo que um nó leva para enviar seus dados por um hop
					//To control the axis X
					System.out.println("\n==> "+xvalue);
					//Repetition per point
					for(int nnodes=50; nnodes<=50;nnodes+=50){
						//if(nnodes==100) nnodes = 150; //100 já tenho
						for(int i=1;i<=35;i++){					
							seed = i; //somente para salvar o nome do arquivo de resultado
						
							String path = jtfPath.getText();
							String filename = path + "sensors/"+nnodes+"_"+ i +".txt";
							
							readSensores(filename);
							//performIncremental(xvalue, dronespeed, "");
							performDecremental(xvalue, dronespeed, "",datatime);
							//JOptionPane.showMessageDialog(null, "Avalia");
							//System.out.println(filename);
						}
					}
					xvalue *= 2;
					if(xvalue > 256) break;
				}
								
			}

			if(e.getSource()==jbtExperimento3){
				//double datasize = datatime;
				//double dronespeed = speed;
				double xvalue = 1;             //Start point in X
				int nnodes=100;
				generateDronePosition(84);
				while(true){   
					System.out.println("\n==> "+xvalue);
					for(hoppacket=0.01; hoppacket>=0.0001; hoppacket/=10 ){
						for(int i=1;i<=35;i++){

							double datatime = hoppacket*xvalue;
							seed = i; //somente para salvar o nome do arquivo de resultado
							
							String path = jtfPath.getText();
							String filename = path + "sensors/"+nnodes+"_"+ i +".txt";
							
							readSensores(filename);
							//performIncremental(xvalue, dronespeed, "");
							String link = "";
							if(hoppacket == 0.01)
								link="30";	
							else if(hoppacket == 0.001)
								link="300";
							else if(hoppacket == 0.0001)
								link="3000";
							String fileoutput = xvalue+"_"+link+"_"+i;
							performDecremental(datatime, speed, fileoutput,datatime); //Arruma essa burrice aqui!!!!!!!
							performIncremental(datatime, speed, fileoutput, datatime); //Arruma essa burrice aqui!!!!!!!
							//JOptionPane.showMessageDialog(null, "Avalia");
							System.out.println(fileoutput);
						}
					}
					xvalue *= 2;
					if(xvalue > 256) break;
				}
								
			}
		*/	
			
			
			
			
			
			if(e.getSource() == jmiDecremental){
				double datatime = Double.parseDouble(JOptionPane.showInputDialog("Time spent by one node to send their data over one hop:"));
				performDecremental(datatime,speed,"",datatime);
				image.repaint();
			}
			if(e.getSource()==jmiBruteForce){
				double datatime = Double.parseDouble(JOptionPane.showInputDialog("Time spent by one node to send their data over one hop:"));
				performBruteForce(datatime,speed,"");
				image.repaint();
			}
			if(e.getSource()==jmiGenerateSensors){
				generateSensors();
				image.receiveSensors(sensors);
				image.repaint();

			}
			if(e.getSource()==jmiCreateDronesPosition){
				generateDronePosition();
			}
			if(e.getSource()==jmiOneHop){
				double datatime = Double.parseDouble(JOptionPane.showInputDialog("Time spent by one node to send their data over one hop:"));
				performOneHop(datatime, speed);				
			}
			if(e.getSource()==jmiCenter){
				double datatime = Double.parseDouble(JOptionPane.showInputDialog("Time spent by one node to send their data over one hop:"));
				performCenter(datatime, speed, true,null,datatime);  //true = center of the monitored area
			}
			if(e.getSource()==jmi00){
				double datatime = Double.parseDouble(JOptionPane.showInputDialog("Time spent by one node to send their data over one hop:"));
				performCenter(datatime, speed, false,null,datatime);  //false = position 0,0
			}
			if(e.getSource()==jmiReadSensors){
				readSensores("");
			}
			if(e.getSource()==jmiReadDrones){
				readDrones();
			}
			if(e.getSource() == jmiChangeBest){
				//performLocalSearch(bestanswer);
			}
			if(e.getSource()==jmiIncremental){
				double datatime = Double.parseDouble(JOptionPane.showInputDialog("Time spent by one node to send their data over one hop:"));
				performIncremental(datatime,speed,"",datatime);
			}
			if(e.getSource() ==jbtGraph){
				image.showGraph();
			}
			if(e.getSource() == jbtSpanningTree){
				image.showSpanningTree();
			}
			if(e.getSource() == jbtMoreConnected){
				image.receiveMoreConnected(bestanswer,more);   
				image.showMoreConnected();
			}
			if(e.getSource()==jmiSaveSensors){
				savePoints(sensors);
			}
			if(e.getSource()==jmiReadDrones){
				savePoints(dronepositions);
			}
			if(e.getSource()==jbtgambi){
				

				//for(int pktnumber=1; pktnumber<=256; pktnumber*=2){
					for(int ss=1; ss<=35; ss++){
						for(int numnodes=150; numnodes<=200; numnodes+=50){
							String path = "/home/rone/scripts-ns/experimentos_drone/exp_query/sensors/"+numnodes+"_" +ss+".txt";
							System.out.println(path);
							readSensores(path);
							savePoints(sensors,path);
						}
					}
				//}

			}
		}
	}

	
	public static void main(String[] args) {
		new Principal();  
	}
	
	JMenuBar jmbMenuBar;
	JMenu jmnFile, jmnAlgorithms, jmnSensors, jmnDronePosition, jmnImprovements, jmnView, jmnAbout, jmnDefault;
	JMenuItem jmiExit, jmiReadSensors, jmiSaveSensors, jmiReadDrones; 
	JMenuItem jmiGenerateSensors, jmiOneHop, jmiIncremental, jmiDecremental, jmiBruteForce, jmiCenter, jmi00;
	JMenuItem jmiCreateDronesPosition;
	JMenuItem jmiChangeBest;
	JMenuItem jmiDefault;

	public void createMenu(){
		//MenuBar
		jmbMenuBar = new JMenuBar();
		
		//Menu arquivo
		jmnFile = new JMenu("File");
		jmnFile.setMnemonic('f');
		
		jmiReadSensors = new JMenuItem("Read Sensors");
		jmiReadSensors.setMnemonic('r');
		jmiReadSensors.addActionListener(events);
		
		jmiSaveSensors = new JMenuItem("Save Sensors");
		jmiSaveSensors.setMnemonic('s');
		jmiSaveSensors.addActionListener(events);
		
		jmiReadDrones = new JMenuItem("Read Drone Position");
		jmiReadDrones.setMnemonic('r');
		jmiReadDrones.addActionListener(events);
		
		jmiExit = new JMenuItem("Exit");
		jmiExit.setMnemonic('e');
		jmiExit.addActionListener(events);
		
		jmnFile.add(jmiReadSensors);
		jmnFile.add(jmiSaveSensors);
		jmnFile.add(jmiReadDrones);
		jmnFile.add(jmiExit);
		
		//menu Sensors
		jmnSensors = new JMenu("Sensors");
		jmnSensors.setMnemonic('s');
		jmnSensors.addActionListener(events);
		
		jmiGenerateSensors = new JMenuItem("Generate Sensor Deployment");
		jmiGenerateSensors.setMnemonic('c');
		jmiGenerateSensors.addActionListener(events);
		
		jmnSensors.add(jmiGenerateSensors);
		
		//Menu Drone Positions
		jmnDronePosition = new JMenu("Drone");
		jmnDronePosition.setMnemonic('d');
		
		jmiCreateDronesPosition = new JMenuItem("Create drone positions");
		jmiCreateDronesPosition.setMnemonic('p');
		jmiCreateDronesPosition.addActionListener(events);
		jmnDronePosition.add(jmiCreateDronesPosition);
		
		//MenuItems Algoritmos
		jmnAlgorithms = new JMenu("Algorithms");
		jmnAlgorithms.setMnemonic('a');
		
		jmiOneHop = new JMenuItem("One Hop");
		jmiOneHop.setMnemonic('c');
		jmiOneHop.addActionListener(events);
		
		jmiIncremental = new JMenuItem("Incremental");
		jmiIncremental.setMnemonic('x');
		jmiIncremental.addActionListener(events);
		

		jmiDecremental = new JMenuItem("Decremental");
		jmiDecremental.addActionListener(events);
		
		jmiBruteForce = new JMenuItem("Brute Force");
		jmiBruteForce.addActionListener(events);

		jmiCenter = new JMenuItem("Node in the center");
		jmiCenter.addActionListener(events);
		
		jmi00 = new JMenuItem("Node position 0,0");
		jmi00.addActionListener(events);
		
		jmnAlgorithms.add(jmiOneHop);
		jmnAlgorithms.add(jmiIncremental);
		jmnAlgorithms.add(jmiDecremental);
		jmnAlgorithms.add(jmiBruteForce);
		jmnAlgorithms.add(jmiCenter);
		jmnAlgorithms.add(jmi00);
		
		//MenuItem Improvements
		jmnImprovements = new JMenu("Improvements");
		jmnImprovements.setMnemonic('i');
		
		jmiChangeBest = new JMenuItem("Local Search.");
		jmiChangeBest.setMnemonic('c');
		jmiChangeBest.addActionListener(events);
		
		jmnImprovements.add(jmiChangeBest);
		
		
		//Menu Default
		jmnDefault = new JMenu("Default");
		jmnDefault.setMnemonic('d');
		jmnDefault.addActionListener(events);
		
		jmiDefault = new JMenuItem("Default");
		jmiDefault.setMnemonic('d');
		jmiDefault.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		jmiDefault.addActionListener(events);
		jmnDefault.add(jmiDefault);

		jmnAbout = new JMenu("About");
		jmnAbout.setMnemonic('a');
		jmnAbout.addActionListener(events);
		
		//Insere menus
		jmbMenuBar.add(jmnFile);
		jmbMenuBar.add(jmnSensors);
		jmbMenuBar.add(jmnDronePosition);
		jmbMenuBar.add(jmnAlgorithms);
		jmbMenuBar.add(jmnImprovements);
		jmbMenuBar.add(jmnAbout);
		jmbMenuBar.add(jmnDefault);
		
		
		//Inseri a bara de menu no formulário
		setJMenuBar(jmbMenuBar);	
	}
	
	//Create the adjacency matrix of drone positions and nodes, with weights based on number of hops.
	public void createAdjacencyMatrix(){
		if(sensors.size()>0 && dronepositions.size()>0){
			Cursor cursor = Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR );  
			this.setCursor( cursor );  
			SpanningTree m = new SpanningTree(radiorange, sensors, dronepositions,1);
			adjacency = m.getMatrixWeight();  //get the matrix with connections drone-sensor. The weight is based in hops
			cursor = Cursor.getDefaultCursor();
			this.setCursor(cursor);
		}
	}
	
	
	public static void executeComand(String s){
		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(s);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JOptionPane.showMessageDialog(null, output.toString());
		
	}
	
	
	
	//Create integer adjacency matrix. The matrix adjacency is double
	public int[][] intAdjacencyMatrix(){
		int [][] adj = new int[adjacency.length][adjacency.length];
		for(int l=0; l<adjacency.length; l++){
			for(int c=0; c<adjacency.length; c++){
				adj[l][c] = (int) adjacency[l][c];
			}
		}
		return adj;
	}
	
	public void generateSensors(){
		generateSensors(-1);
	}
	public void generateSensors(int q){
		int quant;
		if(q==-1)
			quant = Integer.parseInt(JOptionPane.showInputDialog("Informe o n�mero de sensores"));
		else
			quant = q;
		sensors.clear();
		int largura = width;
		int altura = height;
		for(int i=0;i<quant;i++){
			int x = (int)(Math.random() * largura);
			int y = (int)(Math.random() * altura);
			sensors.add(new Node(i,x,y));
		}
		createAdjacencyMatrix();
	}
	
	public void generateDronePosition(){
		generateDronePosition(-1);
	}
	
	public void generateDronePosition(double q){
		dronepositions.clear();
		if(q==-1)
			precision = Integer.parseInt(JOptionPane.showInputDialog("Precision"));
		else
			precision = q;

		double begin = (width - (precision * (((int)width/(int)precision))))/2;
		//double begin = precision / 2;
		
		
		dronepositions.add(new Node(0,0,0));  //a posi��o 0,0 est� sempre entre os pdps
		int count=1;
		for(double c = begin; c<width; c+=precision){
			for(double l=begin; l<height; l+=precision){
				dronepositions.add(new Node(count,c,l));
				count++;
			}
		}
		image.receiveDronePosition(dronepositions);
		createAdjacencyMatrix();
	}
	
	public void readSensores(String arq){
	    String file="",ent="";
		if(arq.equals("")){
			JFileChooser chooser = new JFileChooser();
		    int returnval = chooser.showOpenDialog(null);
	
		    if(returnval!=JFileChooser.CANCEL_OPTION)
		        file = chooser.getSelectedFile().getAbsolutePath();
		    else
		    	return;
		}
		else{
			file = arq;
		}
			
		
		sensors.clear();
		try{
			FileReader filereader = new FileReader(file);
			BufferedReader read = new BufferedReader(filereader);
			ent = read.readLine(); //Get just the first line
			read.close();
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null,"File not found!");
		}
		String x = "", y = "";
		if (ent.charAt(0)=='#'){
			//Retira o #
			ent = ent.substring(1, ent.length());
		}
		//System.out.println(ent+"\n\n");
		
		int count = 0;
		while(ent.length() > 0){
			int virg = ent.indexOf(",");
			x = ent.substring(0,virg);
			ent = ent.substring(virg+1, ent.length());
			virg = ent.indexOf(",");
			if(virg >= 0 ){
				y = ent.substring(0,virg);
				ent = ent.substring(virg+1, ent.length());
				sensors.add(new Node(count++,Double.parseDouble(x),Double.parseDouble(y)));
				//System.out.println(x+","+y);
				//System.out.println(x+","+y+"   ");
			}
			else {
				sensors.add(new Node(count++,Double.parseDouble(x),Double.parseDouble(ent)));
				//System.out.println(x+","+ent);
				break;
			}
			
		}
		image.receiveSensors(sensors);
		createAdjacencyMatrix();

		//show(adjacency);

	}

	
	///////////////////////
	public void show(double ret[][]){
        for(int l=sensors.size(); l<(dronepositions.size()+sensors.size());l++){
        	if(l<sensors.size()) System.out.print(l+"\t");
        	else System.out.print((l-sensors.size())+"\t");
        	for(int c=0; c<(dronepositions.size()+sensors.size());c++){
        		if(ret[l][c]==Principal.INFINITY)
        			System.out.print("   # ");
        		else if(ret[l][c]>999)
        			System.out.print((int)ret[l][c] +" ");
        		else if(ret[l][c]>99)
        			System.out.print(" " +(int)ret[l][c] +" ");
        		else if(ret[l][c]>9)
        			System.out.print("  " +(int)ret[l][c] +" ");
        		else 
        			System.out.print("   " +(int)ret[l][c] +" ");
        	}
    		System.out.print("\n");
        }
 
        System.out.print("\n\t");
        for(int i=0; i<dronepositions.size()+sensors.size();i++) {
        	if(i==Principal.INFINITY)
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
////////
	
	
	
	
	
	
	
	
	
	
	public void readDrones(){
		//String file="/home/rone/sensores_1000_3.txt", ent="";
	    String file="",ent="";
		JFileChooser chooser = new JFileChooser();
	    int returnval = chooser.showOpenDialog(null);

	    if(returnval!=JFileChooser.CANCEL_OPTION)
	        file = chooser.getSelectedFile().getAbsolutePath();
	    else
	    	return;

		dronepositions.clear();
		try{
			FileReader filereader = new FileReader(file);
			BufferedReader read = new BufferedReader(filereader);
			ent = read.readLine(); //Get just the first line
			read.close();
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null,"File not found!");
		}
		String x = "", y = "";
		if (ent.charAt(0)=='#'){
			//Retira o #
			ent = ent.substring(1, ent.length());
		}
		//System.out.println(ent+"\n\n");
		
		int count = 0;
		while(ent.length() > 0){
			int virg = ent.indexOf(",");
			x = ent.substring(0,virg);
			ent = ent.substring(virg+1, ent.length());
			virg = ent.indexOf(",");
			if(virg >= 0 ){
				y = ent.substring(0,virg);
				ent = ent.substring(virg+1, ent.length());
				dronepositions.add(new Node(count++,Double.parseDouble(x),Double.parseDouble(y)));
				//System.out.println(x+","+y);
				//System.out.println(x+","+y+"   ");
			}
			else {
				dronepositions.add(new Node(count++,Double.parseDouble(x),Double.parseDouble(ent)));
				//System.out.println(x+","+ent);
				break;
			}
			
		}
		image.receiveDronePosition(dronepositions);
		createAdjacencyMatrix();
		

	}

	public void savePoints(Vector<Node> v){
		savePoints(v,null);
	}
	
	
	public void savePoints(Vector<Node> v, String filename){
		JFileChooser chooser = null;
		int ret = JFileChooser.APPROVE_OPTION;
		File file = null;
		
		if(filename == null){                             //Show dialog box to get the file name
			chooser = new JFileChooser();
			ret = chooser.showSaveDialog(null);
			if (ret != JFileChooser.CANCEL_OPTION)
				file = new File(chooser.getSelectedFile().getAbsolutePath());
		}
		else{                                           //Use the parameter file name
			file = new File(filename);
		}
		
		if (ret != JFileChooser.CANCEL_OPTION){
			//File file = new File(chooser.getSelectedFile().getAbsolutePath());
			try	{
				//if(file.createNewFile()){
					FileWriter writer = new FileWriter(file,false);
					writer.write("#");
					for(int i=0; i< v.size(); i++){
						int x = ((Point)v.elementAt(i)).x;
						int y = ((Point)v.elementAt(i)).y;
						writer.write(x+",");
						writer.write(y+",");
						//System.out.print(x+",");
						//System.out.print(y+",");
					}
					writer.write("\n$node_(0) set X_ 0");
					writer.write("\n$node_(0) set Y_ 0");
					writer.write("\n$node_(0) set Z_ 0");
					writer.write("\n$ns_ at 0.000000000000 \"$flood_(0) position 0 0 0\"");
					
					
					for(int i=0; i< v.size(); i++){
						int x = ((Point)v.elementAt(i)).x;
						int y = ((Point)v.elementAt(i)).y;

						writer.write("\n$node_(" + (i+1) + ") set X_ " + x);
						writer.write("\n$node_(" + (i+1) + ") set Y_ " + y);
						writer.write("\n$node_(" + (i+1) + ") set Z_ 0");
						writer.write("\n$ns_ at 0.000000000000 \"$flood_(" + (i+1) + ") position " + x + " " + y + " 0\"\n");
						
						//System.out.print(x+",");
						//System.out.print(y+",");
					}
					writer.close();
			}
			catch(IOException ioe){
				System.out.println("Erro ao criar arquivo de pontos");
			}
		}
	}	
	
	
	//Cristiano
	//receive the amount of time a node spend do send its data over one hop
	public void performDecremental(double datasize, double dronespeed, String fileoutput,double datatime){
		int numsensors = sensors.size();
		int numdrones = dronepositions.size();

		SpanningTree m = new SpanningTree(radiorange, sensors, dronepositions,1);
		int spanning_tree[][] = m.createSpanningTree();
//		image.receiveSpanningTree(spanning_tree);	

		Reduction r = new Reduction(numsensors,numdrones,intAdjacencyMatrix(),spanning_tree);
		
		double bestcost = Double.MAX_VALUE;
		double custo = 0;
		Reference bestresult = null;
		Vector<Node> bestnodes = null;

	//	image.repaint();
	//	JOptionPane.showMessageDialog(null, "Antes");
		for(int i=1; i<dronepositions.size(); i++){
			int remove = r.bestElimination();
			if(remove == -1)break;
			r.eliminate(remove);
			
			//Custo
			Vector<Node> rest = r.getPositions(dronepositions);
			if(rest.size()<11){     //12 por causa do cacheiro viajante
				Reference result = new Reference();
				custo = Cost.totalCost(sensors, dronepositions,rest, dronespeed, datasize,result);
				if(custo < bestcost){
					bestcost=custo;
					bestresult = result;
					bestnodes = (Vector<Node>)rest.clone();
				}
				//String s="";
				//for(int u=0;u<rest.size();u++)
					//s=s+" "+rest.get(u).getNodeId();
				//System.out.print("\ncusto:" + custo + "   -->"+s);
			
				if(custo != Principal.INFINITY){
					Reference lc =  LocalSearch.search(sensors, dronepositions, rest, spanning_tree, custo, dronespeed, datasize,(int)precision);
				    //System.out.print("\nLocal Search tenta: " + lc.cost + " "+ lc.tripdistance + " " + lc.numhops + " " + lc.bestanswer.size() + " -- ");
				    //s="";
				    //for(int u=0;u<lc.bestanswer.size();u++)
					//		s=s+" "+lc.bestanswer.get(u).getNodeId();
				    //System.out.print(s);
					
				    if(lc!=null && lc.cost<bestcost && custo!=Principal.INFINITY){
						bestcost = lc.cost;
						bestnodes = (Vector<Node>)lc.bestanswer.clone();
						bestresult.bestanswer = (Vector<Node>)lc.bestanswer.clone();
						bestresult.cost = lc.cost;
						bestresult.numhops = lc.numhops;
						bestresult.tripdistance = lc.tripdistance;
						bestresult.tour = lc.tour.clone();
					}
	
				}
			}
//			image.repaint();
//			JOptionPane.showMessageDialog(null, i); 
		}
		bestanswer = bestnodes; 
		
		bestnodes.clear();
		for(int i=0; i<bestresult.tour.length; i++)
			bestnodes.add(dronepositions.get(bestresult.tour[i]));

		
		
		m = new SpanningTree(radiorange, sensors, bestnodes,1);
		spanning_tree = m.createSpanningTree();
		image.receiveDronePosition(bestnodes);
		image.receiveSpanningTree(spanning_tree);
		
		
		showResult("Decremental", bestcost, bestresult, bestnodes,fileoutput,datatime);
		
	}

	
	//Cristiano
	public void performIncremental(double datasize, double dronespeed, String fileoutput,double datatime){
		
		//t.matrix is a matrix. Number of colunms = number of lines = (number of sensor) + (number of drone positions) 
		//t.matrix has the distance in hops between each par sensor X drone position
		//The distance between two sensor nodes or between two drone position is infinite
		MoreConnected t = new MoreConnected(adjacency, sensors.size(), dronepositions.size());
		//t.show();
		t.calculateWeights();    //Create a weight for each drone position
		t.setPositions(dronepositions);  //Set the position for each node in the sorted vector
		t.sort();                    //Sort acording to the weights
		
		double bestcost=Principal.INFINITY;
		Vector<Node> bestnodes=null;
		Reference bestresult=null;

		Vector<Node>more = null;
		int spanning_tree[][] = null;
		
		double lastcost = Principal.INFINITY;

		//Each loop iteraction increases more with one drone position
		for(int n=1; n<11; n++){
			//more has only a subset of drone positions, that increases each loop
			more = t.manipulate(n);
			
			SpanningTree m = new SpanningTree(radiorange, sensors, more,1);
			spanning_tree = m.createSpanningTree();    //spanning_tree has the smallest distance in hops between the drone positions in more and the sensor nodes
			
			//SpanningTree.showMatrix(spanning_tree, spanning_tree.length, 0);

			
			Reference result = new Reference();  //used to receive by reference data about the answer
			double custo = Cost.totalCost(sensors, dronepositions,more, dronespeed, datasize,result); //custo receive the total cust for the drone positions in more
			if(custo<bestcost && custo!=Principal.INFINITY){
				bestcost = custo;
				bestnodes = (Vector<Node>)more.clone();
				bestresult = result;
			}
			//String s="";
			//for(int u=0;u<more.size();u++)
			//	s=s+" "+more.get(u).getNodeId();
			//System.out.print("\nPartial:      " + custo + " "+ result.tripdistance + " " + result.numhops + " " + n + " -->"+s);

		//	custo:
			if(custo != Principal.INFINITY){
				Reference lc =  LocalSearch.search(sensors, dronepositions, more, spanning_tree, custo, dronespeed, datasize,(int)precision);
				if(lc != null){
				    //System.out.print("\nLocal Search tenta: " + lc.cost + " "+ lc.tripdistance + " " + lc.numhops + " " + lc.bestanswer.size() + " -- ");
				    //	for(int u=0;u<lc.bestanswer.size();u++)
					//		s=s+" "+lc.bestanswer.get(u).getNodeId();
					//	System.out.print(s);
					
					if(lc.cost<bestcost && lc.cost!=Principal.INFINITY){
						bestcost = lc.cost;
						bestnodes = (Vector<Node>)lc.bestanswer.clone();
						bestresult = lc;
					}
				}
			}

		

			//Finish faster
			if(lastcost<custo)
				break;
			else	
				lastcost = custo;
			
		}
		//                                     melhor custo                             tempo da viagem                                                         hops                    
		String s = "\nIncremental \nTempo total:"+bestcost + "\nTempo de viagem:"+ (bestresult.tripdistance/speed) + "\nTempo de transmissão:" + (bestresult.numhops * datatime)+ "\nTamanho do caminho: " + bestnodes.size() + "\n" ;
		//System.out.print(s);
		
		for(int i=0; i<bestnodes.size();i++){
			s+=" " + bestnodes.get(i).getNodeId();
			//System.out.print(" " + bestnodes.get(i).getNodeId());
		}
		
		String caminho="\nCaminho: ";
		bestnodes.clear();
		for(int i=0; i<bestresult.tour.length; i++){
			bestnodes.add(dronepositions.get(bestresult.tour[i]));
			caminho += bestresult.tour[i]+ " ";
		}
		//System.out.println(caminho);

		
		//Show de queries
		showResult("Incremental", bestcost, bestresult, bestnodes,fileoutput,datatime);
		
		SpanningTree m = new SpanningTree(radiorange, sensors, bestnodes,1);
		spanning_tree = m.createSpanningTree();
		image.receiveDronePosition(bestnodes);
		image.receiveSpanningTree(spanning_tree);
		//JOptionPane.showMessageDialog(null, n);

	}
	
	public void performBruteForce(double datasize, double dronespeed, String fileoutput){
		BruteForce bf = new BruteForce(dronespeed, datasize, sensors, dronepositions);
		bf.perform(5);
	}
		
	//Calculate de spanning tree and its cost
	public boolean performOneHop(double datasize, double dronespeed){
		OneHop oh = new OneHop(datasize,dronespeed,adjacency, sensors, dronepositions);
		//return oh.perform();
		oh.performExact(datasize,dronespeed);
		return true;
	}
	
	//Consider only one drone position in the center of the monitored area
	public void performCenter(double datasize, double dronespeed, boolean center, String fileoutput,double datatime){
		Vector<Node> oneDronePosition = new Vector<Node>();
		String s="";
		if(center){  //One PDP in the center of the monitored area
			oneDronePosition.add(new Node(0,width/2,height/2));
			s="Center";
		}
		else{       //One PDP in the position 0,0
			oneDronePosition.add(new Node(0,0,0));
			s="Position00";
		}
		SpanningTree m = new SpanningTree(Principal.radiorange, sensors, oneDronePosition,1);
		int spanning_tree[][] = m.createSpanningTree();

		Reference result = new Reference();
		double custo = Cost.totalCost(sensors, dronepositions,oneDronePosition, dronespeed, datasize,result);

		if(custo!=Principal.INFINITY){
			//s += " "+custo + " "+ result.tripdistance + " " + result.numhops + " " + oneDronePosition.size() + " -- ";
			//System.out.println(s);
			if(center){
				//Prepar the result
				//oneDronePosition.clear();
				//oneDronePosition.add(new Node(0,0,0));
				//oneDronePosition.add(new Node(0,width/2,height/2));
				result.tour[0] = 0; 
				showResult(s, custo, result,oneDronePosition,fileoutput,datatime);
			}
			else
				showResult(s, custo, result,oneDronePosition,fileoutput,datatime);
		}
		else{
			System.out.print("\n"+s + " 0 0 0 1");
		}
		image.receiveDronePosition(oneDronePosition);
		image.receiveSpanningTree(spanning_tree);
	}

	public boolean performLocalSearch(Vector<Node>bestanswer, double lastcost,double dronespeed, double datasize){
		System.out.println("local search");
		//image.receiveDronePosition(bestanswer);
		SpanningTree m = new SpanningTree(radiorange, sensors, bestanswer,1);
		int spanning_tree[][] = m.createSpanningTree();
		
		Reference lc =  LocalSearch.search(sensors, dronepositions, bestanswer, spanning_tree, lastcost, dronespeed, datasize,(int)precision);
		if(lc!=null){
			double newcusto = Cost.totalCost(sensors, dronepositions, lc.bestanswer, dronespeed, datasize, new Reference());
			System.out.print("\nLocal Search: "+ newcusto+ " "+ lc.tripdistance + " " + lc.numhops + " " + lc.bestanswer.size()+ "  -- ");
			for(int a=0; a<lc.bestanswer.size();a++)
				System.out.print(lc.bestanswer.get(a).getNodeId()+" ");
			return true;
		}
		return false;
		
		//image.receiveDronePosition(bestanswer);
		//image.receiveSpanningTree(spanning_tree);
		//JOptionPane.showMessageDialog(null, "antigo:"+lastcost+ "  novo:"+newcusto);
	}
	
	//Save in arq the string that is in data
	public static void saveText(String data, String arq, boolean keepfile){
		try{
			FileWriter file = new FileWriter(arq,keepfile);
			PrintWriter writer = new PrintWriter(file);
			
			writer.printf(data);
			file.close();
		}
		catch(IOException ioex){
			
		}
	}

	public static void showResult(String method, double bestcost, Reference bestresult, Vector<Node> bestnodes, String fileoutput,double datatime){

		System.out.print("\nResultado: ");
		//Result data
		//String s = "\n"+method+": "+bestcost + " "+ bestresult.tripdistance + " " + bestresult.numhops + " " + bestnodes.size();
		String s = method +"\n\tTempo total:"+bestcost + "\n\tTempo de viagem:"+ (bestresult.tripdistance/speed) + "\n\tTempo de transmissão:" + (bestresult.numhops * datatime)+ "\n\tTamanho do caminho: " + bestnodes.size() + "\n" ;
		
		//Nodes in the drone tour
		for(int i=0; i<bestresult.tour.length; i++)
			s += " "+bestresult.tour[i];
		
		//Print the result data
		System.out.print("\t" + s);

		System.out.println("\n\nParâmetros:\n\tLargura de regiao (metros): " + height );
		System.out.println("\tAltura de regiao (metros): " + height );
		System.out.println("\tVelocidade do drone (metros/segundo): " + speed );
		System.out.println("\tNúmero de possíveis paradas do drone:" + dronepositions.size() + "(sempre considera o ponto 0,0)");
		System.out.println("\tNúmero de sensores:" + sensors.size() );
		System.out.println("\tTempo de transmissão(segundos):" + datatime + " (tempo que um nó leva para transmitir todos os seus dados em um hop)" );
		
		
		
		/*
		
		//exemplo de nome: Incremental_100.txt ==> <metodo>_<num pacotes de dados por no>
		String arq = jtfPath.getText()+"resultsfromjava/" + method + "_" + (int)(datatime/0.01) +".txt";
		saveText(s, arq,true);
		
		//Connections drone x nodes
		SpanningTree m = new SpanningTree(radiorange, sensors, bestnodes,1);
		int spanning_tree[][] = m.oneHop();
		
		Vector<Integer> regions = new Vector<Integer>();
//		System.out.print("\n");
		//Calculates the area covered by each drone positon
		for(int i=0; i<bestresult.tour.length; i++){
			//int dp = bestresult.tour[i];
			int l = i + sensors.size(); 
			//for(int l=sensors.size();l<dronepositions.size()+sensors.size();l++){
				double sx = Double.MAX_VALUE, sy = Double.MAX_VALUE;
				double lx = 0, ly = 0;
				for(int c=0; c<sensors.size(); c++){
					if(spanning_tree[l][c]>0 && spanning_tree[l][c]<INFINITY){
						double x = sensors.get(c).getX();
						double y = sensors.get(c).getY();
						
						if(sx > x) sx = x;
						if(sy > y) sy = y;
						if(lx < x) lx = x;
						if(ly < y) ly = y;
					}
				}
				regions.add((int)sx);
				regions.add((int)sy);
				regions.add((int)lx);
				regions.add((int)ly);	
//				System.out.print(bestresult.tour[i] +":"+((int)sx)+","+((int)sy)+ " " + ((int)lx)+","+((int)ly)+ " ");
		}

		System.out.print("\n");
		Vector<Integer>schedule = new Vector<Integer>();   //Save node/parent
		Vector<Integer>pathsize = new Vector<Integer>();   //Save the longest path size of each drone position 
		Vector<Integer>counter =  new Vector<Integer>();   //Save the number of nodes of each drone position
		Vector<Double>distances = new Vector<Double>();    //Distance between the drone position i-1 and i
		for(int l=0; l<bestresult.tour.length; l++){
			//System.out.print("\n" + bestresult.tour[l] + ": ");//dp has the drone position id + number of nodes
			//Calculate the distance
			if(l==0){
				double x;
				double y;   
				if(method.equals("Center")){
					x = width/2;
					y = width/2;   
				}
				else {
					x = dronepositions.get(bestresult.tour[0]).x;
					y = dronepositions.get(bestresult.tour[0]).y;
				}
				double firsttrip = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
				distances.add(firsttrip);
			}
			else {
				Node now = dronepositions.get(bestresult.tour[l]);
				Node before = dronepositions.get(bestresult.tour[l-1]);
				distances.add(now.distance(before));
			}
			int maxhop = -1;
			int node = 0;
			int cont = 0;
			for(int c=0; c<sensors.size(); c++){
				if(spanning_tree[l+sensors.size()][c]>0 && spanning_tree[l+sensors.size()][c]<INFINITY){
//					int r[] = Path.teste(l+sensors.size(), c, radiorange,sensors.size(), bestresult.tour.length,spanning_tree);
//					int size = Path.pathSize();

					
					ShortestPath sp = new ShortestPath();
					int r[] = sp.getShortesPath(spanning_tree,l+sensors.size(), c,sensors.size());
					int size = r.length;
					
					
//					boolean iguais = true;
//					for(int i=0; i<size2;i++){
//						if(r[i]!=r2[i] || size != size2)
//							System.out.println("Diferentes");
//					}
					
					//SpanningTree.showMatrix(spanning_tree, sensors.size(), dronepositions.size());
					
					//Save the max hop for each drone position
					if(size>maxhop){
						maxhop = size;
						node = c;
					}
					
					//In the ns, node ids received +1
					schedule.add(c+1);
					//In the ns, 0 is the drone
					if(r[size-2]>=sensors.size()){
						schedule.add(0);
					}
					else {
						//Since 0 is the drone, each node id receiv +1
						schedule.add(r[size-2]+1);
					}
					cont++;
					//System.out.print(" " + c + "-" + r[size-2]);
				}
			}
			//pathsize.add(l);
			counter.add(cont);
			pathsize.add(maxhop-1);   //Reduce one since we count hop, not number of nodes in the path
			//System.out.print("(maxhop:"+(maxhop-1)+"  -  node:"+ node + ")");
		}
*/


/* exibe os comandos para simulação no ns		
		System.out.print("\n-----------------------------------------------------\n");
		oneQueryPerNode(bestresult, spanning_tree,method,fileoutput, true,datatime);  //true = wait

		
		
		System.out.print("\n-----------------------------------------------------\n");
		printParentDefined(bestresult, spanning_tree,method,fileoutput,true,datatime); //true = wait
	
	
		
		System.out.print("\n-----------------------------------------------------\n");
		queryRegionNS(bestresult.tour, regions, spanning_tree,fileoutput,method);

		*/
		
//////////////////////////////////////////////////////////////////////////////////		
		
		
		
		
		
/*
		System.out.print("\n-----------------------------------------------------\n");
		floodingNS(bestresult.tour,pathsize,counter, distances);
		

		//System.out.print("\n-----------------------------------------------------\n");
		//printDronePositions(bestresult.tour,regions,counter, distances);
		
		
*/
//		image.receiveDronePosition(bestnodes);
//		image.receiveSpanningTree(spanning_tree);
		//image.repaint();

	}

	
	
	
    public static void oneQueryPerNode(Reference bestresult, int spanning_tree[][],String method, String fileoutput, boolean predefinedtime,double datatime){
    	double time = 0;
    	int countquery = 1;
		String s = "";
    	
		Vector<Integer>pathsize = new Vector<Integer>();   //Save the longest path size of each drone position 
		Vector<Integer>counter =  new Vector<Integer>();   //Save the number of nodes of each drone position
		Vector<Double>distances = new Vector<Double>();    //Distance between the drone position i-1 and i
		for(int l=0; l<bestresult.tour.length; l++){
			Vector<String>vectorroutes = new Vector<String>();       //Save the routes of all nodes to drone. Obs: the first position is the num of hops in the route
			Vector<Integer>routehops = new Vector<Integer>();        //Save the number of hops in the route of each node
			Vector<Integer>schedule = new Vector<Integer>();   //Save node/parent
			//System.out.print("\n" + bestresult.tour[l] + ": ");//dp has the drone position id + number of nodes
			//Calculate the distance
			if(l==0){
				double x,y;
				if(method.equals("Center")){
					x=width/2;
					y=width/2;
				}
				else {
					x = dronepositions.get(bestresult.tour[0]).x;
					y = dronepositions.get(bestresult.tour[0]).y;
				}
				double firsttrip = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
				distances.add(firsttrip);
			}
			else {
				Node now = dronepositions.get(bestresult.tour[l]);
				Node before = dronepositions.get(bestresult.tour[l-1]);
				distances.add(now.distance(before));
			}
			int maxhop = -1;
			int node = 0;
			int cont = 0;
			//int iddp = bestresult.tour[l];   //Id of the drone position
			for(int c=0; c<sensors.size(); c++){
				if(spanning_tree[l+sensors.size()][c]>0 && spanning_tree[l+sensors.size()][c]<INFINITY){
					//int r[] = Path.teste(l+sensors.size(), c, radiorange,sensors.size(), bestresult.tour.length,spanning_tree);
					//int size = Path.pathSize();
			 		
					ShortestPath sp = new ShortestPath();
					int r[] = sp.getShortesPath(spanning_tree,l+sensors.size(), c,sensors.size());
					int size = r.length;
					
					
					String route = "";

					for(int i=0; i<size; i++){
						if(r[i]>=sensors.size())
							route+= " 0";
						else
							route += " " + (r[i]+1);  //"+1" because in ns node ids starts in 1, 0 is the drone						
					}
					vectorroutes.add(route);
					routehops.add(size);

					//Save the max hop for each drone position
					if(size>maxhop){
						maxhop = size;
						node = c;
					}
					
					//In the ns, node ids received +1
					schedule.add(c+1);
					//In the ns, 0 is the drone
					if(r[size-2]>=sensors.size()){
						schedule.add(0);
					}
					else {
						//Since 0 is the drone, each node id receiv +1
						schedule.add(r[size-2]+1);
					}
					cont++;
					//System.out.print(" " + c + "-" + r[size-2]);
					//System.out.print("\n" + route);
				}
			}
			//pathsize.add(l);
			counter.add(cont);
			pathsize.add(maxhop-1);   //Reduce one since we count hop, not number of nodes in the path
			
			//double ctime = counter.get(l)*datatime;   //Collecting time
			double ttime = distances.get(l)/speed;    //Trip time
			
			//if x or y equal to 0, the setdest will return error
			double dx = dronepositions.get(bestresult.tour[l]).x;
			if(dx==0) dx = 0.001;
			double dy = dronepositions.get(bestresult.tour[l]).y;
			if(dy==0) dy = 0.001;
			if(method.equals("Center")){
				dx=width/2;
				dy=width/2;
			}
			
			String aux, aux2;
			if(predefinedtime){
				aux = "0";
				aux2 = "start-query-unique-wait move ";
			}
			else{
				aux = time + "";
				aux2 = "move ";
			}
			s += "$ns_ at "+ aux  +" \"$flood_(0) " + aux2  + dx + " " + dy + " "+ speed+"\"";
			time += ttime;			
			
			double pointtime = 0;
			
			
			for(int y=0;y<vectorroutes.size();y++){
				
				String stime,typequery;
				if(predefinedtime){
					stime = "0";
					typequery = "start-query-unique-wait";
				}
				else{
					stime = (pointtime+time)+ "";
					typequery = "start-query-unique";
				}
				
				s+="\n$ns_ at "+ stime+ " \"$flood_(0) " + typequery + " " + (countquery++) + " " + routehops.get(y) +vectorroutes.get(y)+"\"";
				int hop = routehops.get(y)-1;
				double ti = (hop * hoppacket)+(datatime*hop*3);
				pointtime += ti;

				//s+="   " + ti;// + " hop:"+hop+" ad:"+adjacency[l][0];
			}
			s+="\n";
			
			time += pointtime;
			
		}
		if(predefinedtime)
			s += "$ns_ at 0.001 \"$flood_(0) start-query-unique-wait start\"";
		System.out.print("\n"+s);
		

		String arq = jtfPath.getText()+"queries/vary_link/onepernode/"+method+"_" + fileoutput + ".txt";

		//System.out.println("\n"+arq);
		saveText(s, arq,false);
    }	
	
	
	
    public static void printParentDefined(Reference bestresult, int spanning_tree[][], String method, String fileoutput, boolean typequery,double datatime){
    	double time = 0;
    	String s = "";

		Vector<Integer>pathsize = new Vector<Integer>();   //Save the longest path size of each drone position 
		Vector<Integer>counter =  new Vector<Integer>();   //Save the number of nodes of each drone position
		Vector<Double>distances = new Vector<Double>();    //Distance between the drone position i-1 and i
		for(int l=0; l<bestresult.tour.length; l++){
			Vector<Integer>schedule = new Vector<Integer>();   //Save node/parent
			//System.out.print("\n" + bestresult.tour[l] + ": ");//dp has the drone position id + number of nodes
			//Calculate the distance
			if(l==0){
				double x;
				double y;   
				if(method.equals("Center")){
					x = width/2;
					y = width/2;   
				}
				else {
					x = dronepositions.get(bestresult.tour[0]).x;
					y = dronepositions.get(bestresult.tour[0]).y;
				}
				double firsttrip = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
				distances.add(firsttrip);
			}
			else {
				Node now = dronepositions.get(bestresult.tour[l]);
				Node before = dronepositions.get(bestresult.tour[l-1]);
				distances.add(now.distance(before));
			}
			int maxhop = -1;
			int node = 0;
			int cont = 0;
			int numnodes = 0;
			for(int c=0; c<sensors.size(); c++){
				if(spanning_tree[l+sensors.size()][c]>0 && spanning_tree[l+sensors.size()][c]<INFINITY){
					//int r[] = Path.teste(l+sensors.size(), c, radiorange,sensors.size(), bestresult.tour.length,spanning_tree);
					//int size = Path.pathSize();
					
					ShortestPath sp = new ShortestPath();
					int r[] = sp.getShortesPath(spanning_tree,l+sensors.size(), c,sensors.size());
					int size = r.length;
					
					
					//Save the max hop for each drone position
					if(size>maxhop){
						maxhop = size;
						node = c;
					}
					
					//In the ns, node ids received +1
					schedule.add(c+1);
					//In the ns, 0 is the drone
					if(r[size-2]>=sensors.size()){
						schedule.add(0);
					}
					else {
						//Since 0 is the drone, each node id receiv +1
						schedule.add(r[size-2]+1);
					}
					cont += spanning_tree[l+sensors.size()][c];
					numnodes++;
					//System.out.print(" " + c + "-" + r[size-2]);
				}
			}
			//pathsize.add(l);
			counter.add(cont);
			pathsize.add(maxhop-1);   //Reduce one since we count hop, not number of nodes in the path
			
			double ctime = counter.get(l)*datatime;   //Collecting time
			double ttime = distances.get(l)/speed;    //Trip time
			
			
			//if x or y equal to 0, the setdest will return error
			double dx = dronepositions.get(bestresult.tour[l]).x;
			if(dx==0) dx = 0.001;
			double dy = dronepositions.get(bestresult.tour[l]).y;
			if(dy==0) dy = 0.001;
			if(method.equals("Center")){
				dx=width/2;
				dy=width/2;
			}
			
			String stime, aux="",auxm="";
			if(typequery){
				stime = "0";
				auxm = "start-query-parent-wait move";
			}
			else{
				stime = time + "";
				auxm = "move";
			}
			s += "\n$ns_ at "+ stime  +" \"$flood_(0) " + auxm + " " + dx + " " + dy + " "+ speed+"\" \n"; 
			time += ttime;
			if(typequery){
				stime = "0";
				aux = "start-query-parent-wait";
			}
			else{
				stime = time + "";
				aux = "start-query-parent";
			}
			s += "$ns_ at "+ stime+ " \"$flood_(0) " + aux +" "+(l+1) + " " + (schedule.size()/2);
			double dissemination = counter.get(l)*hoppacket*(Math.ceil((numnodes*2)/(double)pktsize));
			time += ctime+dissemination;
			for(int i=0; i<schedule.size();i++){
				s += " " + schedule.get(i);
			}
			s += "\"";
		}
		if(typequery)
			s += "\n$ns_ at 0.001 \"$flood_(0) start-query-parent-wait start\"";
		System.out.print("\n"+s);
		
		
//		String arq = jtfPath.getText()+"queries/" + method+ "_"+ fileoutput + ".txt";
		String arq = jtfPath.getText()+"queries/vary_link/parent/" +method+"_" + fileoutput + ".txt";

		//System.out.println("\n"+arq);

		saveText(s, arq,false);
    }
	
	/*
	public static void printDronePositions(int tour[], Vector<Integer>regions, Vector<Integer>counter, Vector<Double>distances){
		//Processing send-drone-positions
		System.out.print("\n");
		String s = "$ns_ at 0.01 \"$flood_(0) send-drone-positions 1 ";
		s += tour.length;
		for(int i=0; i<tour.length; i++)
			s += " " + dronepositions.get(tour[i]).x + " " + dronepositions.get(tour[i]).y;
		s += "\"\n";
		
		System.out.print("\n");
		//Calculates the distance between the position 0,0 and a the firt drone position
		//double x = dronepositions.get(tour[0]).getX();
		//double y = dronepositions.get(tour[0]).getY();
		//double firsttrip = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))/speed;
		
		
		//Each drone movement
		double time = 1.0;
		for(int i=0; i<tour.length; i++){
			double ctime = counter.get(i)*datatime;   //Collecting time
			double ttime = distances.get(i)/speed;    //Trip time
			s += "$ns_ at "+time+" \"$node_(0) setdest " + dronepositions.get(tour[i]).x + " " + dronepositions.get(tour[i]).y + " "+ speed+"\" \n"; 
			time += ttime;
			//if(i==0) time+=firsttrip;
			s += "$ns_ at "+time+" \"$flood_(0) query-drone-positions "+ (i+1) +"\"\n\n";
			time += ctime;
		}
		System.out.print(s);
	}
	
	public static void floodingNS(int tour[], Vector<Integer>pathsize,Vector<Integer>counter, Vector<Double>distances){
		//Processing send-drone-positions
		System.out.print("\n");
		String s = "";

		double time = 0.01;
		for(int i=0; i<tour.length; i++){
			double ctime = counter.get(i)*datatime;   //Collecting time
			double ttime = distances.get(i)/speed;    //Trip time

			s += "$ns_ at "+time+" \"$node_(0) setdest " + dronepositions.get(tour[i]).x + " " + dronepositions.get(tour[i]).y + " "+ speed+"\" \n"; 
			time += ttime;
			s += "$ns_ at "+time+" \"$flood_(0) start-query-processing " + (i+1) + " " +  pathsize.get(i)   +"\" \n\n";
			time += ctime;
		}
		System.out.print(s);
	}	
	*/
	public static void queryRegionNS(int tour[], Vector<Integer>regions, int spanning_tree[][],String fileoutput, String method){
		int numnodes[]= new int[tour.length];
		int p = 0;
		for(int l=sensors.size(); l<sensors.size()+tour.length; l++){
			int count=0;
			for(int c=0; c<sensors.size();c++){
				if(spanning_tree[l][c]>0 && spanning_tree[l][c]<INFINITY){
					count++;  
				}
			}
			numnodes[p++] = count;
		}
		
		
		System.out.print("\n");
		String s = "";
		
		for(int i=0; i<tour.length; i++){
			s += "$ns_ at 0.0 \"$flood_(0) start-query-region move " + dronepositions.get(tour[i]).x + " " + dronepositions.get(tour[i]).y + " "+ speed+"\" \n"; 
			s += "$ns_ at 0.0 \"$flood_(0) start-query-region "+ numnodes[i] + " 1";
			s += " " + regions.get(i*4);
			s += " " + regions.get(i*4+1);
			s += " " + regions.get(i*4+2);
			s += " " + regions.get(i*4+3);
			Vector<Integer> v = new Vector<Integer>();   //save the node for each hovering possition
			String aux = "";
			int l = sensors.size()+i;
				for(int c=0; c<sensors.size();c++){
					if(spanning_tree[l][c]>0 && spanning_tree[l][c]<INFINITY){
						aux += " "+(c+1);   // I put "+1" because in ns the node id starts in 1
					}
				}
				//"\n$ns_ at 0.0 \"$flood_(0) start-query-region nodes "
				s += "    " + aux;
			
			s += "\" \n\n";
			
		}
		s += "\n$ns_ at 0.0001 \"$flood_(0) start-query-region start\"";
		System.out.print(s);

		//String arq = jtfPath.getText()+"queries/" + method+ "_" + fileoutput +"_" + seed + ".txt";
		String arq = jtfPath.getText()+"queries/vary_link/region/" +method+"_" + fileoutput + ".txt";

		saveText(s, arq,false);
	}
	
}
