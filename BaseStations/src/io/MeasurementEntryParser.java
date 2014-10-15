package io;

import com.googlecode.jcsv.reader.CSVEntryParser;

import datasets.MeasurementDto;

public class MeasurementEntryParser implements CSVEntryParser<MeasurementDto> {
	
	@Override
    public MeasurementDto parseEntry(String... data) {
    	return Parsers.stringArrayToMeasurementDto(data);
    }
}
