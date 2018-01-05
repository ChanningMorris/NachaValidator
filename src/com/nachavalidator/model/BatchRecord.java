package com.nachavalidator.model;

import java.util.ArrayList;
import lombok.Data;

@Data
public class BatchRecord {
	
	public EntryDetailRecord detailRecord;
	public ArrayList<AddendaRecord> addenda;

}
