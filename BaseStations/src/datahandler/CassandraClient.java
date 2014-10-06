package datahandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import datasets.MeasurementDto;

@Deprecated
public class CassandraClient {
	
	private Cluster cluster;
	private Session session;
	private String connectionPoint = "localhost";
	
	private PreparedStatement preparedStatementMeasurements;

	public void connect() {
		cluster = Cluster.builder().addContactPoint(connectionPoint).build();
		session = cluster.connect();
		Metadata metadata = cluster.getMetadata();
		System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
		for(Host host : metadata.getAllHosts()) {
			System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(), host.getRack());
		}
		session.execute("use celltowers;");
	}
	
	public void close() {
		cluster.close();
	}
	
	public void prepareToInsertMeasurements() {
		String preparedStatementMeasurementInsert = "INSERT INTO measurements "
				+ "(mcc, net, area, cell, lon, lat, signal, measured, created, rating, speed, direction, "
				+ "radio, ta, rnc, cid, psc, tac, pci, sid, nid, bid, frommeasurements) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		preparedStatementMeasurements = session.prepare(preparedStatementMeasurementInsert);				
	}
	
	public void insertMeasurement(MeasurementDto measurement) {
//		UUID id = UUID.randomUUID();
		session.execute(new BoundStatement(preparedStatementMeasurements).bind(
				measurement.getMcc(),
				measurement.getNet(),
				measurement.getArea(),
				measurement.getCell(),
				measurement.getLon(),
				measurement.getLat(),
				measurement.getSignal(),
				measurement.getMeasured(),
				measurement.getCreated(),
				measurement.getRating(),
				measurement.getSpeed(),
				measurement.getDirection(),
				measurement.getRadio(),
				measurement.getTa(),
				measurement.getRnc(),
				measurement.getCid(),
				measurement.getPsc(),
				measurement.getTac(),
				measurement.getPci(),
				measurement.getSid(),
				measurement.getNid(),
				measurement.getBid()));
//				measurement.getFromMeasurementsFile()));
	}
	
	public List<MeasurementDto> queryForMeasurements(int mcc, int net, int area, long cell) {
		List<MeasurementDto> measurements = new ArrayList<MeasurementDto>();
		String query = "SELECT * "
				+ "FROM measurements "
				+ "WHERE mcc="+mcc+" "
						+ "AND net="+net+" "
								+ "AND area="+area+" "
										+ "AND cell="+cell+" "
												+ "ALLOW FILTERING;";
		ResultSet resultSet = session.execute(query);
		List<Row> rows = resultSet.all();
		for(Row row : rows) {
			MeasurementDto measurement = new MeasurementDto();
//			measurement.setId(row.getUUID("id"));
			measurement.setMcc(row.getInt("mcc"));
			measurement.setNet(row.getInt("net"));
			measurement.setArea(row.getInt("area"));
			measurement.setCell(row.getLong("cell"));
			measurement.setLon(row.getDouble("lon"));
			measurement.setLat(row.getDouble("lat"));
			measurement.setSignal(row.getInt("signal"));
			measurements.add(measurement);
		}
		return measurements;
	}
}
