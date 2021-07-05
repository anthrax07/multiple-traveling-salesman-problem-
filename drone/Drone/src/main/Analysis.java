package main;
import java.io.*;

import util.Ref;
public class Analysis {


	public static void intervalo_confianca(double val[], int t, Ref retorno){

	    //Calculando a média
	    double soma=0;
	    for (int i=0; i<t; i++)
	        soma = soma + val[i];
	    retorno.media = soma / t;
	    //System.out.println("média:" + retorno.media);
	    //printf("\nmédia: %f", media);

	    //Calculando a Variancia
	    double variancia=0.0;
	    for (int i=0; i<t; i++){
	        variancia = variancia + ((val[i] - retorno.media)*(val[i] - retorno.media));
	    }
	    variancia = variancia / t;
	    retorno.variancia = variancia;
	    //System.out.println("variacia:" + retorno.variancia);

	    //Calculando x
	    double x;
	    x = Math.sqrt(variancia)/Math.sqrt(10);
	    //printf("\nx: %f", x);

	    //Calculando o limite inferior
	    retorno.inferior = retorno.media - 1.96 * x;
	    //System.out.println("inferior:" + retorno.inferior);
	    //printf("\ninferior: %f", inferior);

	    //Calculando o limite superior
	    retorno.superior = retorno.media + 1.96 * x;
	    //System.out.println("superior:" + retorno.superior);
	    //printf("\nsuperior: %f", superior);
	}

	
	public static String piece(String text, String delim, int index){
		if(text.length()<delim.length()) return "";
		int cont = 0;
		int t = delim.length();
		int lasti = 0;
		for(int i=0; i<text.length()-(t-1); i++){
			if(text.substring(i, i+t).equals(delim)){
				cont++;
				if(cont == index){
					return text.substring(lasti,i);
				}
				lasti = i+delim.length();
			}
		}
		if(cont+1 == index){
			return text.substring(lasti, text.length());
		}
		return "";
	}
	
	public static void main(String args[]){
		//piece("Rone  Ilídio  da  silva","  ", 4);
		String path = "C:\\Users\\rone\\Documents\\My Dropbox\\PosDoutorado\\Pesquisa\\drone_wsn\\Experimentos\\graph\\graph3\\";
		//Define which data will be analyzed: 2-overall time(s), 3-trip distance(m), 4-number of hops 
		int colw = 4;
		double param = 1;
		
		double center[] = new double[35];
		double position[] = new double[35];
		double spanning[] = new double[35];
		double incremental[] = new double[35];
		double decremental[] = new double[35];
		double bruteforce[] = new double[35];
		int c=0, pos=0, s=0, i=0, d=0, b=0;
		
		int contx = 0;
	
		try{
			FileReader file = new FileReader("C:\\Users\\rone\\Documents\\My Dropbox\\PosDoutorado\\Pesquisa\\drone_wsn\\Experimentos\\30_nos_84 de distance entre pdp_aumenta datasize 1-6_200x200_60_range.txt");
			//FileReader file = new FileReader("C:\\Users\\rone\\Documents\\My Dropbox\\PosDoutorado\\Pesquisa\\drone_wsn\\Experimentos\\30_nos_84 de distance entre pdp_aumenta dronespeed 0.5-3_200x200_60_range.txt");
			//FileReader file = new FileReader("C:\\Users\\rone\\Documents\\My Dropbox\\PosDoutorado\\Pesquisa\\drone_wsn\\Experimentos\\100-250_nos_84 de distance entre pdp_400x400_60_range.txt");
			//FileReader file = new FileReader("C:\\Users\\rone\\Documents\\My Dropbox\\PosDoutorado\\Pesquisa\\drone_wsn\\Experimentos\\150_nos_0.5-3.0_drone speed_84 de distance entre pdp_400x400_60_range.txt");
			//FileReader file = new FileReader("C:\\Users\\rone\\Documents\\My Dropbox\\PosDoutorado\\Pesquisa\\drone_wsn\\Experimentos\\150_nos_84 de distance_aumenta datasize 1-6_400x400_60_range.txt");
			
			BufferedReader read = new BufferedReader(file);
			String line = read.readLine();
			while(line != null){
				String p = piece(line, " ", 1);
				//System.out.println(p);
				
				if(p.equals("==>")) {
					contx++;
					param = contx;
					
					if(contx!=1) {
						//Delete this line if necessary
						//System.out.println("\n---------->"+(param));
						Ref retorno = new Ref();
						String saida = "";
						
						intervalo_confianca(position,pos,retorno);
						saida = (contx-1) +" "+ retorno.media + " " + (retorno.superior-retorno.media)+"\n";
						saveText(saida, path+"position.txt");

						intervalo_confianca(center,c,retorno);
						saida = (contx-1) +" "+ retorno.media + " " + (retorno.superior-retorno.media)+"\n";
						saveText(saida, path+"center.txt");

						intervalo_confianca(spanning,s,retorno);
						saida = (contx-1) +" "+ retorno.media + " " + (retorno.superior-retorno.media)+"\n";
						saveText(saida, path+"spanning.txt");
						
						intervalo_confianca(decremental,d,retorno);
						saida = (contx-1) +" "+ retorno.media + " " + (retorno.superior-retorno.media)+"\n";
						saveText(saida, path+"decremental.txt");
						
						intervalo_confianca(incremental,i,retorno);
						saida = (contx-1) +" "+ retorno.media + " " + (retorno.superior-retorno.media)+"\n";
						saveText(saida, path+"incremental.txt");
						
						intervalo_confianca(bruteforce,b,retorno);
						saida = (contx-1) +" "+ retorno.media + " " + (retorno.superior-retorno.media)+"\n";
						saveText(saida, path+"bruteforce.txt");
						
						
						c=0;
						pos=0;
						s=0;
						i=0;
						d=0;
						b=0;
					}
				}
				
				
			    if(p.equals("Position00")){
			            String part;
			            part = piece(line," ",colw);
			            double t = Double.parseDouble(part);
			            if(t!=0){
			            	position[pos++] = t*param;
			            }
			    }

			    if(p.equals("Center")){
		            String part;
		            part = piece(line," ",colw);
		            double t = Double.parseDouble(part);
		            if(t!=0){
		            	center[c++] = t*param;
		            }
			    }

			    if(p.equals("OneHop")){
		            String part;
		            part = piece(line," ",colw);
		            double t = Double.parseDouble(part);
		            //if(t==2.147483647E9)t=0;
		            spanning[s++] = t*param;
			    }
				
			    if(p.equals("Incremental")){
		            String part;
		            part = piece(line," ",colw);
		            double t = Double.parseDouble(part);
		            //if(t==2.147483647E9)t=0;
		            incremental[i++] = t*param;
			    }
				
			    if(p.equals("Decremental")){
		            String part;
		            part = piece(line," ",colw);
		            double t = Double.parseDouble(part);
		           // if(t==2.147483647E9)t=0;
		            decremental[d++] = t*param;
			    }
				
			    if(p.equals("BruteForce")){
		            String part;
		            part = piece(line," ",colw);
		            double t = Double.parseDouble(part);
		            //if(t==2.147483647E9)t=0;
		            bruteforce[b++] = t*param;
			    }
				line = read.readLine();
			}			
		}
		catch(IOException ex){
			System.out.println("Exceção");
		}
	}
	
	//Save in arq the string that is in data
	public static void saveText(String data, String arq){
//		System.out.print(data);
		try{
			FileWriter file = new FileWriter(arq,true);
			PrintWriter writer = new PrintWriter(file);
			
			writer.printf(data);
			file.close();
		}
		catch(IOException ioex){
			
		}

	}
}
