package com.okcoin.ui;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import com.okcoin.*;

public class FrmMain extends JFrame {
	
    private JTextArea LogArea = new JTextArea();
    private JScrollPane scroll = new JScrollPane();
    private JPanel btc = new JPanel();
    private JPanel ltc = new JPanel();

	public FrmMain(){
		//getContentPane().setLayout(new BorderLayout()); 
		//LogArea.setLineWrap(true);
		this.add(LogArea);
		this.setSize(1366, 768);
		this.setVisible(true);
		this.add(scroll);
	}
	public void LoadMsg(String msg){
		LogArea.append(msg);
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
	}
	
}
