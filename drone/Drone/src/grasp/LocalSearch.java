package grasp;

import graph.Node;

import java.util.Vector;

import javax.swing.JOptionPane;

import main.Principal;
import util.*;

public class LocalSearch {
	//Change each drone position for its neighbor and return the best
	public static Reference search(Vector<Node>sensors, Vector<Node> dronepositions, Vector<Node> bestanswer, int spanning_tree[][], double bestcost, double dronespeed, double datatime, int dist){
		Vector<Node> best = null;           //Best cost
		Reference bestresult = new Reference();
		
		//if(bestanswer.get(0).getNodeId()==5 && bestanswer.get(0).getNodeId()==8 && bestanswer.get(0).getNodeId()==9 && bestcost == 358)
		//	System.out.print("\nAqui\n");
		
		
		int answersize = bestanswer.size();
		for(int i=0; i<answersize;i++){
			Node lc = bestanswer.get(i);                                    
			for(int j=0; j<dronepositions.size();j++){
				Node n = dronepositions.get(j);
				if(lc.distance(n)<=dist && lc.getNodeId()!=n.getNodeId()){   
					//System.out.print("\nMudar "+ lc.getNodeId()+ "  para "+ n.getNodeId() + "    --   ");
	
					Vector<Node> newanswer = new Vector<Node>();
					for(int a=0; a<answersize;a++){
						if(a==i){
							newanswer.add(n);
						}
						else{
							newanswer.add(bestanswer.get(a));
						}
					}
					
					for(int r=0; r<newanswer.size()-1;r++){
						for(int u=r+1; u<newanswer.size();u++){
							if(newanswer.get(r).getNodeId() == newanswer.get(u).getNodeId()){
								//System.out.print("aqui");
								newanswer.remove(u);
								u=r+1;
							}
						}
					}
					
					
					//for(int a=0; a<answersize;a++)
						//System.out.print(newanswer.get(a).getNodeId()+"  ");
					
					Reference result = new Reference();
					double custo = Cost.totalCost(sensors, dronepositions,newanswer,dronespeed , datatime,result);
					
					//System.out.print("mudou: "+ custo + " "+ result.tripdistance + " " + result.numhops + " " + bestanswer.size());
					
					
					if(custo < bestcost){
						bestcost = custo;
						best = (Vector<Node>) newanswer.clone();
						bestresult.numhops = result.numhops;
						bestresult.tripdistance = result.tripdistance;
						bestresult.bestanswer = (Vector<Node>) newanswer.clone();
						bestresult.cost = custo;
						bestresult.tour = result.tour.clone();
					}	
				}

			}
		}
		//if(best!=null){
		//	System.out.print("\nNovo custo: "+ bestcost + " "+ bestresult.tripdistance + " " + bestresult.numhops + " " + bestanswer.size());
		//}
		if(bestresult.cost == 0) //Local Search couldn't find a better solutions 
			bestresult = null;
		verify(bestresult);
		return bestresult;
	}

	//Verify if there is repetition in the tour and remove it.
	//It can happen when the best localsearch would be remove a node, but it was not created
	private static void verify(Reference bestresult){
		if(bestresult == null) return;
	/*	for(int i=0; i<bestresult.tour.length-1; i++){
			if(bestresult.tour[i]==bestresult.tour[i+1]){
				System.out.print("\n**********    ");
				for(int j=0; j<bestresult.tour.length; j++)
					System.out.print(" "+bestresult.tour[j]);
			}
		}
		*/
		for(int i=0; i<bestresult.tour.length-1; i++){
			if(bestresult.tour[i]==bestresult.tour[i+1]){
				bestresult.numhops--;
				bestresult.bestanswer.remove(i);
				int aux[] = new int[bestresult.tour.length-1];
				int b=0;
				for(int a=0; a<aux.length;a++){
					if(a==i) b++;
					aux[a]=bestresult.tour[a+b];
				}
				bestresult.tour = aux;
				break;
			}
		}
	}
}
