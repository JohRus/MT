package datasets;

import com.googlecode.jcsv.reader.CSVEntryParser;

public class MeasurementsEntryParser implements CSVEntryParser<MeasurementDto> {
	
	@Override
    public MeasurementDto parseEntry(String... data) {
    	return Parsers.stringArrayToMeasurementDto(data);
    }
}
