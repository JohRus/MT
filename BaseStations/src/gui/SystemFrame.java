package gui;

import infrastructure.Computation;
import infrastructure.DynamicCell;

import java.awt.Color;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import testdata.Generate;
import testdata.Geom;


public class SystemFrame extends JFrame {
	SystemPanel panel;
	JButton button;

	public SystemFrame() {
		panel = new SystemPanel();
		button = new JButton("Generate Dynamic Cell");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread generateCellThread = new Thread() {
					@Override
					public void run() {
						DynamicCell dc = Generate.dynamicCellWithDefaultMeasurements(
								new Point2D.Double(200.0, 200.0), 
								190.0, 
								30.0, 
								113.0, 
								30.0, 
								20);
						
						
//						Line2D.Double heuristicVector1 = Geom.linearRegressionVector(dc.getMeasurements(), 10);
						Line2D.Double heuristicVector2 = Geom.longestVector(dc.getMeasurements(), 10);
//						List<Line2D.Double> heuristicVectorList = new ArrayList<Line2D.Double>();
//						heuristicVectorList.add(heuristicVector1);
//						heuristicVectorList.add(heuristicVector2);
						
						// Dobbellagring av dette punktet når hele den heuristiske DynamicCell'en blir lagret
						Point2D.Double heuristicCTPos = Geom.distanceFromSurroundingPointsToVector(heuristicVector2, dc.getMeasurements(), 10);
//						List<Point2D.Double> heuristicCTList = new ArrayList<Point2D.Double>();
//						heuristicCTList.add(heuristicCTPos);
						
						Line2D.Double heuristicVectorWithCorrectEndPoints = Geom.adjustEndpoints(heuristicVector2, heuristicCTPos);
						
						DynamicCell heuristicDC = Geom.calculateSector(heuristicVectorWithCorrectEndPoints, dc.getSectorAngle());
//						List<DynamicCell> heuristicDCList = new ArrayList<DynamicCell>();
//						heuristicDCList.add(heuristicDC);
						
						Computation computation = new Computation(
								heuristicVectorWithCorrectEndPoints, 
								heuristicCTPos, 
								heuristicDC);
						
						List<Computation> computationList = new ArrayList<Computation>();
						computationList.add(computation);
						
						updateGUI(dc, computationList);
					}
				};
				generateCellThread.start();

				
			}
		});
		panel.add(button);
		panel.setBackground(Color.white);
		add(panel);
//		add(button);
	}
	
	private void updateGUI(DynamicCell dc, List<Computation> computationList) {
		
		panel.clearData();
		panel.addData(dc, computationList);
//		panel.clearCells();
//		panel.addDynamicCell(dc);
//		
//		panel.clearHeuristicVectors();
//		panel.addHeuristicVectors(dc, heuristicVectorList);
//		
//		panel.clearHeuristicCellTowers();
//		panel.addHeuristicCellTowers(dc, heuristicCTList);
//		
//		panel.clearHeuristicDynamicCells();
//		panel.addHeuristicDynamicCells(dc, heuristicDCList);
		
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
