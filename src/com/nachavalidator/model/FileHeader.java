package com.nachavalidator.model;

import static com.nachavalidator.util.ParserUtils.parsePositions;
import lombok.Data;

@Data
public class FileHeader {

	String recordTypeCode;
	String priorityCode;
	String immediateDestination;
	String immediateOrigin;
	String fileDate;
	String fileTime;
	String fileIDModifier;
	String recordSize;
	String blockingFactor;
	String formatCode;
	String immediateDestinationName;
	String immediateOriginName;
	String referenceCode;
	
	public FileHeader(String fileHeaderString){
		this.recordTypeCode = parsePositions(fileHeaderString, 1, 1);
		this.priorityCode = parsePositions(fileHeaderString, 2, 3);
		this.immediateDestination = parsePositions(fileHeaderString, 4, 13);
		this.immediateOrigin = parsePositions(fileHeaderString, 14, 23);
		this.fileDate = parsePositions(fileHeaderString, 24, 29);
		this.fileTime = parsePositions(fileHeaderString, 30, 33);
		this.fileIDModifier = parsePositions(fileHeaderString, 34, 34);
		this.recordSize = parsePositions(fileHeaderString, 35, 37);
		this.blockingFactor = parsePositions(fileHeaderString, 38, 39);
		this.formatCode = parsePositions(fileHeaderString, 40, 40);
		this.immediateDestinationName = parsePositions(fileHeaderString, 41, 63);
		this.immediateOriginName = parsePositions(fileHeaderString, 64, 86);
		this.referenceCode = parsePositions(fileHeaderString, 87, 94);
	}
	
	@Override
	public String toString(){
		return  recordTypeCode + priorityCode + immediateDestination + immediateOrigin + fileDate + 
				fileTime + fileIDModifier + recordSize + blockingFactor + formatCode + immediateDestinationName 
				+ immediateOriginName + referenceCode;
	}
	
}
