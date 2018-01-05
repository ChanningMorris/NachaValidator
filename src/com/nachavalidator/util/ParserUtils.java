package com.nachavalidator.util;

import java.util.ArrayList;

import com.nachavalidator.model.AddendaRecord;
import com.nachavalidator.model.Batch;
import com.nachavalidator.model.BatchRecord;
import com.nachavalidator.model.Nacha;
import com.nachavalidator.model.NinePadRecord;

public class ParserUtils {
	
	public static String parsePositions(String string, Integer firstPosition, Integer secondPosition){
		return string.substring(firstPosition - 1, secondPosition);
	}
	
	public static void printIncorrectFieldError(String objectName, String fieldName, String correctValue, String actualValue){
		System.out.println(objectName + " has incorrect " + fieldName + "! Is \"" + actualValue + "\", but should be \"" + correctValue + "\"!");
	}
	
	public static void printNonEmptyFieldError(String objectName, String fieldName, String actualValue){
		System.out.println(objectName + " has " + fieldName + " that is not empty! Is \"" + actualValue + "\"");
	}
	
	public static void printIncorrectFieldPrefixError(String objectName, String fieldName, String correctValue, String actualValue){
		System.out.println(objectName + " has incorrect " + fieldName + "! Is \"" + actualValue + "\", but should start with \"" + correctValue + "\"!");
	}
	
	//TODO: break out
	public static void printNacha(Nacha nacha) {
		System.out.println("Printing full NACHA:");
		if(nacha != null){
			if(nacha.getHeader() != null)
				System.out.println(nacha.getHeader().toString());
			if(nacha.getBatch() != null){
				printBatchList(nacha.getBatch());
			if(nacha.getControl() != null)
				System.out.println(nacha.getControl().toString());
			if(nacha.getPadRecord() != null)
				printPadRecords(nacha.getPadRecord());
			}
		}
	}

	private static void printPadRecords(ArrayList<NinePadRecord> padRecordList) {
		for(NinePadRecord padRecord : padRecordList)
			System.out.println(padRecord.getPadRecord());
	}

	private static void printBatchList(ArrayList<Batch> batchList) {
		for(Batch batch : batchList){
			if(batch.getHeader() != null)
				System.out.println(batch.getHeader().toString());
			if(batch.getDetailRecords() != null)
				printDetailRecords(batch.getDetailRecords());
			if(batch.getControl() != null)
				System.out.println(batch.getControl().toString());
		}
	}

	private static void printDetailRecords(ArrayList<BatchRecord> detailRecords) {
		for(BatchRecord batchRecord : detailRecords){
			if(batchRecord.getDetailRecord() != null)
				System.out.println(batchRecord.getDetailRecord().toString());
			if(batchRecord.getAddenda() != null)
				printAddendaRecords(batchRecord.getAddenda());
		}
	}

	private static void printAddendaRecords(ArrayList<AddendaRecord> addendaRecords) {
		for(AddendaRecord addenda : addendaRecords)
			System.out.println(addenda.toString());
	}

}
