package datasets;

import com.googlecode.jcsv.writer.CSVEntryConverter;

public class MeasurementEntryConverter implements CSVEntryConverter<MeasurementDto> {

	@Override
	public String[] convertEntry(MeasurementDto measurement) {
		return Parsers.measurementDtoToStringArray(measurement);
	}
}
