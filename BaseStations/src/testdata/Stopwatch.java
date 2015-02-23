package testdata;

import java.awt.geom.Point2D;

import infrastructure.Computation;
import infrastructure.DynamicCell;

public class Stopwatch {
	
	private long startTime;
	private long stopTime;
	private double time;
	
	private double sumOfTimes;
	private int timers;
	
	public Stopwatch() {
		sumOfTimes = 0.0;
		timers = 0;
	}
	
	public void start() {
		startTime = System.currentTimeMillis();
//		System.out.println(startTime);
	}
	
	public void stop() {
		stopTime = System.currentTimeMillis();
		time = (stopTime-startTime)/1000.0;
		sumOfTimes += time;
		timers++;
	}
	
	public double time() {
		return time;
	}
	
	public double averageTime() {
		return sumOfTimes/timers;
	}
	
//	public static void main(String[] args) {
//		Stopwatch swGen = new Stopwatch();
//		Stopwatch swComp = new Stopwatch();
//		for(int i = 1; i <= 5; i++) {
//			
//			for(int j = 0; j < 10; j++) {
//				for(int k = 1; k <= 10; k++) {
//					swGen.start();
//					DynamicCell dc = Generate.dynamicCellWithDefaultMeasurements(new Point2D.Double(0.0, 0.0), 0.0, 30.0, 113.0, 0.0, 1000);
//					swGen.stop();
//					swComp.start();
//					Computation comp = Generate.computation(dc, 500);
//					swComp.stop();
//				}
//			}
//			
//			
//		}
//		double avTimeGen = swGen.averageTime();
//		double avTimeComp = swComp.averageTime();
//		System.out.printf("AverageTime Gen: %.5f\n", avTimeGen);
//		System.out.printf("AverageTime Comp: %.5f\n", avTimeComp);
//		
//	}
}
