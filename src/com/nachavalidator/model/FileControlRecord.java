package com.nachavalidator.model;

import static com.nachavalidator.util.ParserUtils.parsePositions;
import lombok.Data;

@Data
public class FileControlRecord {
	
	String recordTypeCode;
	String batchCount;
	String blockCount;
	String entryCount;
	String entryHash;
	String totalDebitAmount;
	String totalCreditAmount;
	String reserved;
	
	public FileControlRecord(String fileControlRecord){
		this.recordTypeCode = parsePositions(fileControlRecord, 1, 1);
		this.batchCount = parsePositions(fileControlRecord, 2, 7);
		this.blockCount = parsePositions(fileControlRecord, 8, 13);
		this.entryCount = parsePositions(fileControlRecord, 14, 21);
		this.entryHash = parsePositions(fileControlRecord, 22, 31);
		this.totalDebitAmount = parsePositions(fileControlRecord, 32, 43);
		this.totalCreditAmount = parsePositions(fileControlRecord, 44, 55);
		this.reserved = parsePositions(fileControlRecord, 56, 94);
	}

	@Override
	public String toString(){
		return recordTypeCode + batchCount + blockCount + entryCount + entryHash + 
				totalDebitAmount + totalCreditAmount + reserved;
	}
	
}
