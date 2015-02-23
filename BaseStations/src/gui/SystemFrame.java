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

import javax.swing.BoxLayout;
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
import testdata.Stopwatch;


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
//		JPanel buttonPane = new JPanel();
//		statsPanel.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
		
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
//				Thread generateCellThread = new Thread() {
//					@Override
//					public void run() {
//						Stopwatch sw = new Stopwatch();
						
						JTextField ctX = new JTextField("200.0");
						JTextField ctY = new JTextField("200.0");
						JTextField vectorAngle = new JTextField("30.0");
						JTextField sectorAngle = new JTextField("120.0");
						JTextField maxDist = new JTextField("113.0");
						JTextField minDist = new JTextField("0.0");
						JTextField measurements = new JTextField("100");
						JTextField hasSignal = new JTextField("true");
						JTextField someMeasurementsAreDefect = new JTextField("true");
						JTextField n = new JTextField("30");
						JTextField d = new JTextField("10.0");
						
						JPanel inputPanel = new JPanel();
						inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
						inputPanel.add(new JLabel("Cell Tower x: "));
						inputPanel.add(ctX);
						inputPanel.add(new JLabel("Cell Tower y: "));
						inputPanel.add(ctY);
						inputPanel.add(new JLabel("Vector Angle: "));
						inputPanel.add(vectorAngle);
						inputPanel.add(new JLabel("Sector Angle: "));
						inputPanel.add(sectorAngle);
						inputPanel.add(new JLabel("Max distance: "));
						inputPanel.add(maxDist);
						inputPanel.add(new JLabel("Min distance: "));
						inputPanel.add(minDist);
						inputPanel.add(new JLabel("Measurements: "));
						inputPanel.add(measurements);
						inputPanel.add(new JLabel("Has Signal"));
						inputPanel.add(hasSignal);
						inputPanel.add(new JLabel("Some measurements are defect"));
						inputPanel.add(someMeasurementsAreDefect);
						inputPanel.add(new JLabel("n: "));
						inputPanel.add(n);
						inputPanel.add(new JLabel("d: "));
						inputPanel.add(d);
						
						int result = JOptionPane.showConfirmDialog(
								null, 
								inputPanel, 
								"Enter values for the computation", 
								JOptionPane.OK_CANCEL_OPTION);
						
						if(result == JOptionPane.OK_OPTION) {
							
							DynamicCell dc = controller.generateDynamicCell(
									new Point2D.Double(java.lang.Double.parseDouble(ctX.getText()), java.lang.Double.parseDouble(ctY.getText())), 
									java.lang.Double.parseDouble(vectorAngle.getText()), 
									java.lang.Double.parseDouble(sectorAngle.getText()), 
									java.lang.Double.parseDouble(maxDist.getText()), 
									java.lang.Double.parseDouble(minDist.getText()), 
									Integer.parseInt(measurements.getText()),
									Boolean.parseBoolean(hasSignal.getText()),
									Boolean.parseBoolean(someMeasurementsAreDefect.getText()));

							Computation comp = controller.generateComputation(
									dc, 
									Integer.parseInt(n.getText()), 
									java.lang.Double.parseDouble(d.getText()));





							updateGUI(dc, comp);
						}
//					}
//				};
//				generateCellThread.start();
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
				JTextField MString = new JTextField("100");
				JTextField nString = new JTextField("10,20,40,80", 23);
				JTextField sectorAngleString = new JTextField("120.0");
				JTextField dString = new JTextField("1.0,2.0,4.0,8.0,16.0,32.0,64.0");
				JTextField maxDistString = new JTextField("113.0", 5);
				JTextField minDistString = new JTextField("0.0", 5);
				JTextField hasSignal = new JTextField("true");
				JTextField someMeasurementsAreDefect = new JTextField("true");
				JTextField writeToFileString = new JTextField("false");
				
				JPanel inputPanel = new JPanel();
				inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
				inputPanel.add(new JLabel("Test subjects: "));
				inputPanel.add(testSubjectsString);
				inputPanel.add(new JLabel("Sector angle: "));
				inputPanel.add(sectorAngleString);
				inputPanel.add(new JLabel("M: "));
				inputPanel.add(MString);
				inputPanel.add(new JLabel("n: "));
				inputPanel.add(nString);
				inputPanel.add(new JLabel("d: "));
				inputPanel.add(dString);
				inputPanel.add(new JLabel("Max distance: "));
				inputPanel.add(maxDistString);
				inputPanel.add(new JLabel("Min distance: "));
				inputPanel.add(minDistString);
				inputPanel.add(new JLabel("Has Signal"));
				inputPanel.add(hasSignal);
				inputPanel.add(new JLabel("Some measurements are defect"));
				inputPanel.add(someMeasurementsAreDefect);
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
							dString.getText().split(","),
							java.lang.Double.parseDouble(maxDistString.getText()), 
							java.lang.Double.parseDouble(minDistString.getText()), 
							Boolean.parseBoolean(hasSignal.getText()),
							Boolean.parseBoolean(someMeasurementsAreDefect.getText()),
							Boolean.parseBoolean(writeToFileString.getText()));
					
					
//					System.out.printf("Error: %.2f\n",error);
					
					
				}
				
				
			}
		});

//		buttonPane.add(generateCellButton);
		
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
		setSize(1000, 725);
		setVisible(true);
	}
}
