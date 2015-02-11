package gui;

import infrastructure.Computation;
import infrastructure.DynamicCell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import testdata.Generate;
import testdata.Geom;


public class SystemFrame extends JFrame {
	SystemPanel panel;
	//	JPanel statsPanel;
	JButton generateCellButton;
	JButton showHideLongestVectorButton;
//	JButton showHideLeastSquareButton;
	JButton showHideSectorsButton;
	JButton clearDataButton;

	public SystemFrame() {
		panel = new SystemPanel();
		panel.setPreferredSize(new Dimension(700, 700));
		

		generateCellButton = new JButton("Generate Cell");
		showHideLongestVectorButton = new JButton("Show/Hide Longest Vector");
//		showHideLeastSquareButton = new JButton("Show/Hide Least Square Vector");
		showHideSectorsButton = new JButton("Show/Hide Sectors");
		clearDataButton = new JButton("Clear Data");

		generateCellButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread generateCellThread = new Thread() {
					@Override
					public void run() {
						DynamicCell dc = Generate.dynamicCellWithDefaultMeasurements(
								new Point2D.Double(200.0, 200.0), 
								265.0, 
								10.0, 
								113.0, 
								30.0, 
								20);
						
						Line2D.Double longestVector = Geom.longestVector(dc.getMeasurements(), 10);
//						Line2D.Double leastSquareVector = Geom.linearRegressionVector(dc.getMeasurements(), 20, longestVector);
						DynamicCell heuristicDC1 = Geom.findSector(longestVector, dc, 20);
						DynamicCell heuristicDC2 = Geom.findSector(
								new Line2D.Double(longestVector.getP2(), longestVector.getP1()), dc, 20);
						
						Computation comp = new Computation();
						comp.setLongestVector(longestVector);
//						comp.setLeastSquareVector(leastSquareVector);
						comp.setHeuristicDynamicCell1(heuristicDC1);
						comp.setHeuristicDynamicCell2(heuristicDC2);
						
						updateGUI(dc, comp);
					}
				};
				generateCellThread.start();
			}
		});
		
		showHideLongestVectorButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.showHideLongestVectors();
				repaintSystem();
			}
		});
		
//		showHideLeastSquareButton.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				panel.showHideLeastSquareVectors();
//				repaintSystem();
//			}
//		});
		
		showHideSectorsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.showHideHeuristicDCs();
				repaintSystem();
			}
		});
		
		clearDataButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.clearData();
				panel.hideAll();
				repaintSystem();
				
			}
		});

		panel.add(generateCellButton);
		panel.add(showHideLongestVectorButton);
//		panel.add(showHideLeastSquareButton);
		panel.add(showHideSectorsButton);
		panel.add(clearDataButton);

		panel.setBackground(Color.white);
		add(panel);
		//		add(panel, BorderLayout.LINE_START);
		//		add(statsPanel, BorderLayout.LINE_END);
		//		add(button);
	}

	private void updateGUI(DynamicCell dc, Computation computation) {

//		panel.clearData();
		panel.addData(dc, computation);

		repaintSystem();
	}
	
	private void repaintSystem() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				repaint();
			}
		});
	}

	public void showUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Cell System");
		setSize(700, 700);
		setVisible(true);
	}
}
