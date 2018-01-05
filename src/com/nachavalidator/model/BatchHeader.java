package com.nachavalidator.model;

import static com.nachavalidator.util.ParserUtils.parsePositions;
import lombok.Data;

@Data
public class BatchHeader {
	
	String recordTypeCode;
	String serviceClassCode;
	String companyName;
	String companyDiscretionaryData;
	String companyID;
	String standardEntryClassCode;
	String companyEntryDescription;
	String companyDescriptiveDate;
	String effectiveEntryDate;
	String settlementDate;
	String originatorStatusCode;
	String originatingDFIIdentification;
	String batchNumber;
	
	public BatchHeader(String batchHeaderString){
		this.recordTypeCode = parsePositions(batchHeaderString, 1, 1);
		this.serviceClassCode = parsePositions(batchHeaderString, 2, 4);
		this.companyName = parsePositions(batchHeaderString, 5, 20);
		this.companyDiscretionaryData = parsePositions(batchHeaderString, 21, 40);
		this.companyID = parsePositions(batchHeaderString, 41, 50);
		this.standardEntryClassCode = parsePositions(batchHeaderString, 51, 53);
		this.companyEntryDescription = parsePositions(batchHeaderString, 54, 63);
		this.companyDescriptiveDate = parsePositions(batchHeaderString, 64, 69);
		this.effectiveEntryDate = parsePositions(batchHeaderString, 70, 75);
		this.settlementDate = parsePositions(batchHeaderString, 76, 78);
		this.originatorStatusCode = parsePositions(batchHeaderString, 79, 79);
		this.originatingDFIIdentification = parsePositions(batchHeaderString, 80, 87);
		this.batchNumber = parsePositions(batchHeaderString, 88, 94);
	}
	
	@Override
	public String toString(){
		return recordTypeCode + serviceClassCode + companyName + companyDiscretionaryData + companyID + 
				standardEntryClassCode + companyEntryDescription + companyDescriptiveDate + effectiveEntryDate + 
				settlementDate + originatorStatusCode + originatingDFIIdentification + batchNumber;
	}

}
