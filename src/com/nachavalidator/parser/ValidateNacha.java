package com.nachavalidator.parser;

import static com.nachavalidator.parser.ParseNacha.parseNacha;
import static com.nachavalidator.util.ParserUtils.printIncorrectFieldError;
import static com.nachavalidator.util.ParserUtils.printNonEmptyFieldError;
import static com.nachavalidator.parser.CheckNachaBalances.checkNachaBalances;

import java.util.ArrayList;

import com.nachavalidator.model.AddendaRecord;
import com.nachavalidator.model.Batch;
import com.nachavalidator.model.BatchControlRecord;
import com.nachavalidator.model.BatchHeader;
import com.nachavalidator.model.BatchRecord;
import com.nachavalidator.model.EntryDetailRecord;
import com.nachavalidator.model.FileControlRecord;
import com.nachavalidator.model.FileHeader;
import com.nachavalidator.model.Nacha;
import com.nachavalidator.model.NinePadRecord;

public class ValidateNacha {

	public static void main(String[] args) {
		Nacha nacha = parseNacha(args[0]);
		validateNacha(nacha);
		checkNachaBalances(nacha);
	}

	static final String padNineRecord = "9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";

	public static void validateNacha(Nacha nacha) {
		if(nacha == null)
			System.out.println("No nacha found!");
		
		if(nacha.getHeader() != null)
			validateFileHeader(nacha.getHeader());
		else System.out.println("File header not found!");
		
		if(nacha.getBatch() != null)
			validateBatch(nacha.getBatch());
		else System.out.println("Batch records not found!");
		
		if(nacha.getControl() != null)
			validateFileControlRecord(nacha.getControl());
		else System.out.println("File control record (9 record) not found!");
		
		if(nacha.getPadRecord() != null)
			validatePadRecords(nacha.getPadRecord());
	}

	private static void validatePadRecords(ArrayList<NinePadRecord> padRecordList) {
		for(NinePadRecord padRecord : padRecordList)
			if(!padRecord.getPadRecord().equals(padNineRecord))
				System.out.println("Invalid padding record found!");
	}

	private static void validateBatch(ArrayList<Batch> batchList) {
		for(Batch batch : batchList){
			if(batch.getHeader() != null)
				validateBatchHeader(batch.getHeader());
			else System.out.print("Batch missing header record (5 record)!");
			
			if(batch.getDetailRecords() != null)
				validateBatchDetails(batch.getDetailRecords());
			else System.out.println("Batch without detail records present!");
			
			if(batch.getControl() != null)
				validateBatchControlRecord(batch.getControl());
			else System.out.println("Batch missing control record (8 record)!");
		}

	}

	private static void validateBatchDetails(ArrayList<BatchRecord> detailRecords) {
		for(BatchRecord batchRecord : detailRecords){
			if(batchRecord.getDetailRecord() != null)
				validateEntryDetailRecord(batchRecord.getDetailRecord());
			else System.out.println("Batch without 6 record present!");
			
			if(batchRecord.getDetailRecord().getAddendaRecordIndicator().equals("0")
					&& batchRecord.getAddenda() != null)
				System.out.println("Addenda record indicator is 0 when 7 records are present!");
			
			if(batchRecord.getDetailRecord().getAddendaRecordIndicator().equals("1")
					&& batchRecord.getAddenda() == null)
				System.out.println("Addenda record indicator is 1 when 7 records are absent!");

			if(batchRecord.getAddenda() != null)
				validateAddendaRecordList(batchRecord.getAddenda());
		}
	}

	private static void validateAddendaRecordList(ArrayList<AddendaRecord> addendaList) {
		for(AddendaRecord addendaRecord : addendaList)
			validateAddendaRecord(addendaRecord);
	}

	private static void validateFileHeader(FileHeader fileHeader){
		Boolean error = false;
		final String objectName = "File header";

		if(!fileHeader.getRecordTypeCode().equals("1")){
			printIncorrectFieldError(objectName, "record type code", "1", fileHeader.getRecordTypeCode());
			error = true;
		}

		if(!fileHeader.getRecordSize().equals("094")){
			printIncorrectFieldError(objectName, "record size", "094", fileHeader.getRecordSize());
			error = true;
		}

		if(!fileHeader.getBlockingFactor().equals("10")){
			printIncorrectFieldError(objectName, "blocking factor", "10", fileHeader.getBlockingFactor());
			error = true;
		}

		if(!fileHeader.getFormatCode().equals("1")){
			printIncorrectFieldError(objectName, "format code", "1", fileHeader.getFormatCode());
			error = true;
		}

		if(error) System.out.println(fileHeader.toString());

	}

	private static void validateAddendaRecord(AddendaRecord addendaRecord){
		Boolean error = false;
		final String objectName = "Addenda record";

		if(!addendaRecord.getRecordTypeCode().equals("7")){
			printIncorrectFieldError(objectName, "record type code", "7", addendaRecord.getRecordTypeCode());
			error = true;
		}

		if(!addendaRecord.getAddendaTypeCode().equals("05")){
			printIncorrectFieldError(objectName, "addenda type code", "05", addendaRecord.getAddendaTypeCode());
			error = true;
		}

		if(error) System.out.println(addendaRecord.toString());
	}

	private static void validateBatchControlRecord(BatchControlRecord batchControlRecord){
		Boolean error = false;
		final String objectName = "Batch control record";

		if(!batchControlRecord.getRecordTypeCode().equals("8")){
			printIncorrectFieldError(objectName, "record type code", "8", batchControlRecord.getRecordTypeCode());
			error = true;
		}

		if(!batchControlRecord.getMessageAuthenticationCode().trim().equals("")){
			printNonEmptyFieldError(objectName, "message authentication code", batchControlRecord.getMessageAuthenticationCode());
			error = true;
		}

		if(!batchControlRecord.getReserved().trim().equals("")){
			printNonEmptyFieldError(objectName, "reserved space", batchControlRecord.getReserved());
			error = true;
		}

		if(error) System.out.println(batchControlRecord.toString());

	}

	private static void validateBatchHeader(BatchHeader batchHeader){
		Boolean error = false;
		final String objectName = "Batch header";

		if(!batchHeader.getRecordTypeCode().equals("5")){
			printIncorrectFieldError(objectName, "record type code", "5", batchHeader.getRecordTypeCode());
			error = true;
		}

		if(!(batchHeader.getServiceClassCode().equals("200") 
				|| batchHeader.getServiceClassCode().equals("220") 
				|| batchHeader.getServiceClassCode().equals("225"))){
			printIncorrectFieldError(objectName, "service class code", "200, 220, or 225", batchHeader.getRecordTypeCode());
			error = true;
		}

		if(error) System.out.println(batchHeader.toString());
	}

	private static void validateEntryDetailRecord(EntryDetailRecord entryDetailRecord){
		Boolean error = false;
		final String objectName	= "Entry detail record";

		if(!entryDetailRecord.getRecordTypeCode().equals("6")){
			printIncorrectFieldError(objectName, "record type code", "6", entryDetailRecord.getRecordTypeCode());
			error = true;
		}

		if(!(entryDetailRecord.getTransactionCode().equals("22")
				|| entryDetailRecord.getTransactionCode().equals("27")
				|| entryDetailRecord.getTransactionCode().equals("32")
				|| entryDetailRecord.getTransactionCode().equals("37")
				|| entryDetailRecord.getTransactionCode().equals("23")
				|| entryDetailRecord.getTransactionCode().equals("28")
				|| entryDetailRecord.getTransactionCode().equals("33")
				|| entryDetailRecord.getTransactionCode().equals("38"))){
			printIncorrectFieldError(objectName, "transaction code", "22, 23, 27, 28, 32, 33, 37, or 38", entryDetailRecord.getRecordTypeCode());
			error = true;
		}

		if((entryDetailRecord.getTransactionCode().equals("23")
				|| entryDetailRecord.getTransactionCode().equals("28")
				|| entryDetailRecord.getTransactionCode().equals("33")
				|| entryDetailRecord.getTransactionCode().equals("38"))){
			if(Integer.parseInt(entryDetailRecord.getAmount()) != 0){
				System.out.println(objectName + " has nonzero amount for a zero dollar transaction code! Is \"" + entryDetailRecord.getAmount() + "\"!");
				error = true;
			}
		}

		if(error) System.out.println(entryDetailRecord.toString());
	}

	private static void validateFileControlRecord(FileControlRecord fileControlRecord){
		if(fileControlRecord.toString().equals(padNineRecord)){ 
			return;
		}

		Boolean error = false;
		final String objectName = "File control record";

		if(!fileControlRecord.getRecordTypeCode().equals("9")){
			printIncorrectFieldError(objectName, "record type code", "9", fileControlRecord.getRecordTypeCode());
			error = true;
		}

		if(!fileControlRecord.getReserved().trim().equals("")){
			printNonEmptyFieldError(objectName, "reserved space", fileControlRecord.getReserved());
			error = true;
		}

		if(error) System.out.println(fileControlRecord.toString());

	}

}
