package infrastructure;

import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.Point2D;
import java.util.List;

import testdata.Geom;

public class OneSector extends DefaultCell implements Sector {
	
	private Line2D.Double[] vectors;
	
	public OneSector(Point2D.Double cellTowerCoordinates, Point2D.Double firstVectorPoint, List<Measurement> measurements) {
		super(cellTowerCoordinates, measurements);
		this.vectors = new Line2D.Double[2];
		this.vectors[0] = new Line2D.Double(cellTowerCoordinates, firstVectorPoint);
		this.vectors[1] = Geom.rotateVector(this.vectors[0], 120.0);
//		System.out.println(this.vectors[0].getP1().toString()+"|"+this.vectors[0].getP2());
//		System.out.println(this.vectors[1].getP1().toString()+"|"+this.vectors[1].getP2());
	}

	@Override
	public Double[] getVectors() {
		return this.vectors;
	}

	@Override
	public void setVectors(Double[] vectors) {
		this.vectors = vectors;
	}

	@Override
	public double[] vectorAngles() {
		double[] angles = new double[this.vectors.length];
		for(int i = 0; i < this.vectors.length; i++) {
			angles[i] = Geom.angle(this.vectors[i]);
		}
		return angles;
	}

	@Override
	public boolean measurementIsWithin(Point2D.Double measCoords) {
		Line2D.Double vectorToMeas = new Line2D.Double(super.getCellTowerCoordinates(), measCoords);
//		System.out.println(Math.toDegrees(Geom.angle(vectors[0], vectorToMeas)));
		if(Geom.angle(vectors[0], vectorToMeas) <= Geom.angle(vectors[0], vectors[1])) {
			return true;
		}
		else
			return false;
	}
	
	@Override
	public String toString() {
		double[] vectorAngles = vectorAngles();
		String s = String.format("Cell Tower Coordinates: [%.1f,%.1f] - Vector Angles: (%.1f,%.1f)\n\tMeasurements: %d", 
				super.getCellTowerCoordinates().getX(), super.getCellTowerCoordinates().getY(),
				Math.toDegrees(vectorAngles[0]), Math.toDegrees(vectorAngles[1]), super.getMeasurements().size());
		for(Measurement measurement : super.getMeasurements()) {
			s += "\n\t"+measurement.toString();
			if(!measurementIsWithin(measurement.getCoordinates())) {
				s += " - Not inside sector";
			}
		}
		s += "\n";
		return s;
	}

}
