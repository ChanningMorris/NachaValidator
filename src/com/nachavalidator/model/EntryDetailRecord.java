package com.nachavalidator.model;

import static com.nachavalidator.util.ParserUtils.parsePositions;
import lombok.Data;

@Data
public class EntryDetailRecord {

	String recordTypeCode;
	String transactionCode;
	String receivingDFIIdentification;
	String checkDigit;
	String dfiAccountNumber;
	String amount;
	String individualIdentificationNumber;
	String individualName;
	String discretionaryData;
	String addendaRecordIndicator;
	String traceNumber;

	public EntryDetailRecord(String detailRecord){
		this.recordTypeCode = parsePositions(detailRecord, 1, 1);
		this.transactionCode = parsePositions(detailRecord, 2, 3);
		this.receivingDFIIdentification = parsePositions(detailRecord, 4, 11);
		this.checkDigit = parsePositions(detailRecord, 12, 12);
		this.dfiAccountNumber = parsePositions(detailRecord, 13, 29);
		this.amount = parsePositions(detailRecord, 30, 39);
		this.individualIdentificationNumber = parsePositions(detailRecord, 40, 54);
		this.individualName	= parsePositions(detailRecord, 55, 76);
		this.discretionaryData = parsePositions(detailRecord, 77, 78);
		this.addendaRecordIndicator = parsePositions(detailRecord, 79, 79);
		this.traceNumber = parsePositions(detailRecord, 80, 94);
	}

	@Override
	public String toString(){
		return recordTypeCode + transactionCode + receivingDFIIdentification + checkDigit + dfiAccountNumber + 
				amount + individualIdentificationNumber + individualName + discretionaryData + 
				addendaRecordIndicator + traceNumber;
	}

}
