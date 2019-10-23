package com.nachavalidator.model;

import java.util.ArrayList;
import lombok.Data;

@Data
public class Batch {
	
	public BatchHeader header;
	public ArrayList<BatchRecord> detailRecords;
	public BatchControlRecord control;
	public boolean iatFlag;

}
