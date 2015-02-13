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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import testdata.Controller;
import testdata.Generate;
import testdata.Geom;


public class SystemFrame extends JFrame {
	SystemPanel panel;
	JPanel statsPanel;
	
	JButton generateCellButton;
	JButton showHideMeasurementsButton;
	JButton showHideLongestVectorButton;
	JButton showHideSectorsButton;
	JButton clearDataButton;
	JButton computeAverageErrorButton;
	
	Controller controller;
	
	public SystemFrame() {
		panel = new SystemPanel();
		panel.setPreferredSize(new Dimension(700, 700));
		
		statsPanel = new JPanel();
		statsPanel.setPreferredSize(new Dimension(300, 700));
		
		controller = new Controller();
		
		generateCellButton = new JButton("Generate Cell");
		showHideMeasurementsButton = new JButton("Show/Hide Measurements");
		showHideLongestVectorButton = new JButton("Show/Hide Longest Vector");
		showHideSectorsButton = new JButton("Show/Hide Sectors");
		clearDataButton = new JButton("Clear Data");
		computeAverageErrorButton = new JButton("Compute Average Error");

		generateCellButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread generateCellThread = new Thread() {
					@Override
					public void run() {
						DynamicCell dc = controller.generateDynamicCell(
								new Point2D.Double(200.0, 200.0), 
								150.0, 
								60.0, 
								113.0, 
								0.0, 
								100);
						
						Computation comp = controller.generateComputation(dc, 20);
						
						double dist1 = dc.getCellTowerCoordinates().distance(comp.getHeuristicDynamicCell1().getCellTowerCoordinates());
						double dist2 = dc.getCellTowerCoordinates().distance(comp.getHeuristicDynamicCell2().getCellTowerCoordinates());
//						if(dist1 <= dist2)
							System.out.printf("%.2f\n", dist1);
//						else
							System.out.printf("%.2f\n", dist2);

						
						
						updateGUI(dc, comp);
					}
				};
				generateCellThread.start();
			}
		});
		
		showHideMeasurementsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.showHideMeasurements();
				repaintSystem();
			}
		});
		
		showHideLongestVectorButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.showHideLongestVectors();
				repaintSystem();
			}
		});
		
		
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
		
		computeAverageErrorButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField testSubjectsString = new JTextField("1000", 5);
				JTextField MString = new JTextField("1000",9);
				JTextField nString = new JTextField(30);
				JTextField sectorAngleString = new JTextField("10,45,90,120",12);
				JTextField maxDistString = new JTextField("113.0", 5);
				JTextField minDistString = new JTextField("0.0", 5);
				JTextField writeToFileString = new JTextField("false", 5);
				
				JPanel inputPanel = new JPanel();
				inputPanel.add(new JLabel("Test subjects: "));
				inputPanel.add(testSubjectsString);
				inputPanel.add(new JLabel("Sector angle: "));
				inputPanel.add(sectorAngleString);
				inputPanel.add(new JLabel("M: "));
				inputPanel.add(MString);
				inputPanel.add(new JLabel("n: "));
				inputPanel.add(nString);
				inputPanel.add(new JLabel("Max distance: "));
				inputPanel.add(maxDistString);
				inputPanel.add(new JLabel("Min distance: "));
				inputPanel.add(minDistString);
				inputPanel.add(new JLabel("Write to file: "));
				inputPanel.add(writeToFileString);
				
				int result = JOptionPane.showConfirmDialog(
						null, 
						inputPanel, 
						"Enter values for the computation", 
						JOptionPane.OK_CANCEL_OPTION);
				
				if(result == JOptionPane.OK_OPTION) {
//					double error = controller.averageError(
//							Integer.parseInt(testSubjectsString.getText()), 
//							Integer.parseInt(MString.getText()), 
//							Integer.parseInt(nString.getText()), 
//							java.lang.Double.parseDouble(sectorAngleString.getText()), 
//							java.lang.Double.parseDouble(maxDistString.getText()), 
//							java.lang.Double.parseDouble(minDistString.getText()), 
//							Boolean.parseBoolean(writeToFileString.getText()));
					
					controller.averageErrors(
							Integer.parseInt(testSubjectsString.getText()), 
							Integer.parseInt(MString.getText()), 
							nString.getText().split(","), 
							sectorAngleString.getText().split(","), 
							java.lang.Double.parseDouble(maxDistString.getText()), 
							java.lang.Double.parseDouble(minDistString.getText()), 
							Boolean.parseBoolean(writeToFileString.getText()));
					
					
//					System.out.printf("Error: %.2f\n",error);
					
					
				}
				
				
			}
		});

		panel.add(generateCellButton);
		panel.add(showHideMeasurementsButton);
		panel.add(showHideLongestVectorButton);
		panel.add(showHideSectorsButton);
		panel.add(clearDataButton);
		panel.add(computeAverageErrorButton);

		panel.setBackground(Color.white);
//		add(panel);
		add(panel, BorderLayout.LINE_START);
		add(statsPanel, BorderLayout.LINE_END);
	}

	private void updateGUI(DynamicCell dc, Computation computation) {

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
		setSize(1000, 700);
		setVisible(true);
	}
}
