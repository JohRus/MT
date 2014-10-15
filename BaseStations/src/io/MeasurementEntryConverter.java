package io;

import com.googlecode.jcsv.writer.CSVEntryConverter;

import datasets.MeasurementDto;

public class MeasurementEntryConverter implements CSVEntryConverter<MeasurementDto> {

	@Override
	public String[] convertEntry(MeasurementDto measurement) {
		return Parsers.measurementDtoToStringArray(measurement);
	}
}
