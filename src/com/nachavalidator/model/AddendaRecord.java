package com.nachavalidator.model;

import static com.nachavalidator.util.ParserUtils.parsePositions;
import lombok.Data;

@Data
public class AddendaRecord {
	
	String recordTypeCode;
	String addendaTypeCode;
	String paymentRelatedInformation;
	String addendaSequenceNumber;
	String entryDetailSequenceNumber;
	
	public AddendaRecord (String addendaString){
		this.recordTypeCode = parsePositions(addendaString, 1, 1);
		this.addendaTypeCode = parsePositions(addendaString, 2, 3);
		this.paymentRelatedInformation = parsePositions(addendaString, 4, 83);
		this.addendaSequenceNumber = parsePositions(addendaString, 84, 87);
		this.entryDetailSequenceNumber = parsePositions(addendaString, 88, 94);
	}

	@Override
	public String toString(){
		return recordTypeCode + addendaTypeCode + paymentRelatedInformation + addendaSequenceNumber + entryDetailSequenceNumber;
	}
}
