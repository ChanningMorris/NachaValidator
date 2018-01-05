package com.nachavalidator.model;

import static com.nachavalidator.util.ParserUtils.parsePositions;
import lombok.Data;

@Data
public class BatchControlRecord {
	
	String recordTypeCode;
	String serviceClassCode;
	String entryAddendaCount;
	String entryHash;
	String totalDebitAmount;
	String totalCreditAmount;
	String companyID;
	String messageAuthenticationCode;
	String reserved;
	String originatingDFIIdentification;
	String batchNumber;
	
	public BatchControlRecord(String batchControlString){
		this.recordTypeCode = parsePositions(batchControlString, 1, 1);
		this.serviceClassCode = parsePositions(batchControlString, 2, 4);
		this.entryAddendaCount = parsePositions(batchControlString, 5, 10);
		this.entryHash = parsePositions(batchControlString, 11, 20);
		this.totalDebitAmount = parsePositions(batchControlString, 21, 32);
		this.totalCreditAmount = parsePositions(batchControlString, 33, 44);
		this.companyID = parsePositions(batchControlString, 45, 54);
		this.messageAuthenticationCode = parsePositions(batchControlString, 55, 73);
		this.reserved = parsePositions(batchControlString, 74, 79);
		this.originatingDFIIdentification = parsePositions(batchControlString, 80, 87);
		this.batchNumber = parsePositions(batchControlString, 88, 94);
	}
	
	@Override
	public String toString(){
		return recordTypeCode + serviceClassCode + entryAddendaCount + entryHash + totalDebitAmount + totalCreditAmount + 
				companyID + messageAuthenticationCode + reserved + originatingDFIIdentification + batchNumber;
	}

}
