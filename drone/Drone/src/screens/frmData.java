package screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import main.*;

public class frmData extends JFrame{
	Font font = new Font("Arial",10,10);
	JLabel jlbNumPoints, jlbPosition;
	JScrollPane jspPoints;
	JList jltPoints;
	DefaultListModel listmodel;
	JButton jbtOk;
	
	Principal principal;
	Handler obj = new Handler();
	
	//Recebe o frame que exibe os dados coletados
	public frmData(Principal principal){
		this.principal = principal;
		setLayout(null);

		jlbPosition = new JLabel("Posi��oo: ");
		jlbPosition.setBounds(5, 5, 300, 30);
		add(jlbPosition);

		jlbNumPoints = new JLabel("N�mero de pontos = 0");
		jlbNumPoints.setBounds(5, 30, 300, 30);
		add(jlbNumPoints);
		
		jbtOk = new JButton("OK");
		jbtOk.setBounds(5,65,60,30);
		jbtOk.addActionListener(obj);
		add(jbtOk);
		
		listmodel = new DefaultListModel();
	
		jltPoints = new JList(listmodel);
		jltPoints.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		jltPoints.setLayoutOrientation(JList.VERTICAL);
		//jltPoints.setVisibleRowCount(-1);
		
		jspPoints = new JScrollPane(jltPoints, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //
		jspPoints.setBounds(250, 5, 130, 90);
		
		add(jspPoints);
		
		setBounds(600,10,400,130);
		setVisible(false);
		setAlwaysOnTop(true);
	}
	
	//Atualiza o número de pontos no label
	public void setNumPoints(int num){
		jlbNumPoints.setText("Número de pontos = "+num);
		
	}

	//Modifica a posição do mouse no label
	public void setPosition(String p){
		jlbPosition.setText("Posição: "+p);
	}

	//Insere um item no listbox
	public void setPoint(String p){
		listmodel.addElement(p);
	}
	
	public void limpaList(){
		listmodel.clear();
	}
	
	public class Handler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==jbtOk){
				jlbNumPoints.setText("Número de pontos = 0");
				//principal.gettingPointsOff();
				setVisible(false);
			}
		}
	}
}
