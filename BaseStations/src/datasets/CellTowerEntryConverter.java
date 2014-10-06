package datasets;

import com.googlecode.jcsv.writer.CSVEntryConverter;

public class CellTowerEntryConverter implements CSVEntryConverter<CellTowerDto> {

	@Override
	public String[] convertEntry(CellTowerDto cellTower) {
		return Parsers.cellTowerDtoToStringArray(cellTower);	
	}
}
