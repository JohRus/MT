package gui;

import infrastructure.Computation;
import infrastructure.DynamicCell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import logic.Controller;
import logic.Geom;



public class SystemFrame extends JFrame {
	SystemPanel visualizePanel;
	JPanel menuPanel;
	
	JButton generateCellButton;
	JButton showHideMeasurementsButton;
	JButton showHideLongestVectorButton;
	JButton showHideSectorsButton;
	JButton showHideChosenSectorButton;
	JButton clearDataButton;
	JButton computeAverageErrorButton;
	
	Controller controller;
	
	public SystemFrame() {
		visualizePanel = new SystemPanel();
		visualizePanel.setPreferredSize(new Dimension(700, 700));
		
		menuPanel = new JPanel();
		menuPanel.setPreferredSize(new Dimension(300, 700));	
		BoxLayout boxLayout = new BoxLayout(menuPanel, BoxLayout.Y_AXIS);
		menuPanel.setLayout(boxLayout);
		
		controller = new Controller();
		
		generateCellButton = new JButton("Generate Cell");
		showHideMeasurementsButton = new JButton("Show/Hide Measurements");
		showHideLongestVectorButton = new JButton("Show/Hide Longest Vector");
		showHideSectorsButton = new JButton("Show/Hide Sectors");
		showHideChosenSectorButton = new JButton("Show/Hide Chosen Sector");
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
						JTextField deadzoneRadius = new JTextField("20.0");
						JTextField n = new JTextField("30");
						JTextField d = new JTextField("1.0");
						
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
						inputPanel.add(new JLabel("Has Signal: "));
						inputPanel.add(hasSignal);
						inputPanel.add(new JLabel("Deadzone radius: "));
						inputPanel.add(deadzoneRadius);
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
									Double.parseDouble(deadzoneRadius.getText()));

							Computation comp = controller.generateComputation(
									dc, 
									Integer.parseInt(n.getText()), 
									java.lang.Double.parseDouble(d.getText()),
									Boolean.parseBoolean(hasSignal.getText()));

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
				visualizePanel.showHideMeasurements();
				repaintSystem();
			}
		});
		
		showHideLongestVectorButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				visualizePanel.showHideLongestVectors();
				repaintSystem();
			}
		});
		
		
		showHideSectorsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				visualizePanel.showHideHeuristicDCs();
				repaintSystem();
			}
		});

		showHideChosenSectorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				visualizePanel.showHideChosenHeuristicDC();
				repaintSystem();
			}
		});

		clearDataButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				visualizePanel.clearData();
				repaintSystem();
				
			}
		});
		
		computeAverageErrorButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField testSubjects = new JTextField("1000", 5);
				JTextField M = new JTextField("100");
				JTextField n = new JTextField("10,20,40,80");
				JTextField sectorAngle = new JTextField("120.0");
				JTextField d = new JTextField("1.0");
				JTextField maxDist = new JTextField("113.0", 5);
				JTextField minDist = new JTextField("0.0", 5);
				JTextField hasSignal = new JTextField("true");
				JTextField deadzoneRadius = new JTextField("20.0,30.0,40.0,50.0,60.0");
				JTextField writeToFileString = new JTextField("false");
				
				JPanel inputPanel = new JPanel();
				inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
				inputPanel.add(new JLabel("Sector angle: "));
				inputPanel.add(sectorAngle);
				inputPanel.add(new JLabel("Max distance: "));
				inputPanel.add(maxDist);
				inputPanel.add(new JLabel("Min distance: "));
				inputPanel.add(minDist);
				inputPanel.add(new JLabel("M: "));
				inputPanel.add(M);
				inputPanel.add(new JLabel("Has Signal"));
				inputPanel.add(hasSignal);
				inputPanel.add(new JLabel("Deadzone radius: "));
				inputPanel.add(deadzoneRadius);
				inputPanel.add(new JLabel("n: "));
				inputPanel.add(n);
				inputPanel.add(new JLabel("d: "));
				inputPanel.add(d);
				inputPanel.add(new JLabel("Test subjects: "));
				inputPanel.add(testSubjects);
				inputPanel.add(new JLabel("Write to file: "));
				inputPanel.add(writeToFileString);
				
				int result = JOptionPane.showConfirmDialog(
						null, 
						inputPanel, 
						"Enter values for the computation", 
						JOptionPane.OK_CANCEL_OPTION);
				
				if(result == JOptionPane.OK_OPTION) {
					
					controller.errors(
							sectorAngle.getText().split(","),
							java.lang.Double.parseDouble(maxDist.getText()), 
							java.lang.Double.parseDouble(minDist.getText()), 
							Integer.parseInt(M.getText()), 
							Boolean.parseBoolean(hasSignal.getText()), 
							deadzoneRadius.getText().split(","), 
							n.getText().split(","), 
							d.getText().split(","), 
							Integer.parseInt(testSubjects.getText()), 
							Boolean.parseBoolean(writeToFileString.getText()));
				}
				
			}
		});

		
		menuPanel.add(generateCellButton);
		menuPanel.add(showHideMeasurementsButton);
		menuPanel.add(showHideLongestVectorButton);
		menuPanel.add(showHideSectorsButton);
		menuPanel.add(showHideChosenSectorButton);
		menuPanel.add(clearDataButton);
		menuPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		menuPanel.add(computeAverageErrorButton);
		
//		panel.add(generateCellButton);
//		panel.add(showHideMeasurementsButton);
//		panel.add(showHideLongestVectorButton);
//		panel.add(showHideSectorsButton);
//		panel.add(clearDataButton);
//		panel.add(computeAverageErrorButton);

		visualizePanel.setBackground(Color.white);
		add(visualizePanel, BorderLayout.LINE_START);
		add(menuPanel, BorderLayout.LINE_END);
	}

	private void updateGUI(DynamicCell dc, Computation computation) {

		visualizePanel.addData(dc, computation);

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
