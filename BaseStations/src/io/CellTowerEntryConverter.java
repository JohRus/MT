package io;

import com.googlecode.jcsv.writer.CSVEntryConverter;

import datasets.CellTowerDto;

public class CellTowerEntryConverter implements CSVEntryConverter<CellTowerDto> {

	@Override
	public String[] convertEntry(CellTowerDto cellTower) {
		return Parsers.cellTowerDtoToStringArray(cellTower);	
	}
}
