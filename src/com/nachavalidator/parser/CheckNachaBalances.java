package com.nachavalidator.parser;

import java.math.BigInteger;

import com.nachavalidator.model.Batch;
import com.nachavalidator.model.BatchControlRecord;
import com.nachavalidator.model.BatchRecord;
import com.nachavalidator.model.EntryDetailRecord;
import com.nachavalidator.model.FileControlRecord;
import com.nachavalidator.model.Nacha;
import static com.nachavalidator.util.ParserUtils.printIncorrectFieldError;

public class CheckNachaBalances {

	public static void checkNachaBalances(Nacha nacha) {
		checkBatchControlRecords(nacha);
		checkFileControlRecord(nacha);
	}

	private static void checkBatchControlRecords(Nacha nacha) {
		if(nacha.getBatch() == null){
			System.out.println("No batch records to check balances on!");
			return;
		}

		for(Batch batch : nacha.getBatch()){
			String entryCount = countEntryRecords(batch);
			String entryHash = calculateEntryHash(batch);
			String totalDebitAmount = calculateAmount(batch, "debit");
			String totalCreditAmount = calculateAmount(batch, "credit");

			final String objectName = "Batch control record";
			Boolean error = false;
			BatchControlRecord controlRecord = batch.getControl();

			if(!controlRecord.getEntryAddendaCount().equals(entryCount)){
				printIncorrectFieldError(objectName, "entry count", entryCount, controlRecord.getEntryAddendaCount());
				error = true;
			}
			if(!controlRecord.getEntryHash().equals(entryHash)){
				printIncorrectFieldError(objectName, "entry hash", entryHash, controlRecord.getEntryHash());
				error = true;
			}
			if(!controlRecord.getTotalDebitAmount().equals(totalDebitAmount)){
				printIncorrectFieldError(objectName, "debit amount", totalDebitAmount, controlRecord.getTotalDebitAmount());
				error = true;
			}
			if(!controlRecord.getTotalCreditAmount().equals(totalCreditAmount)){
				printIncorrectFieldError(objectName, "credit amount", totalCreditAmount, controlRecord.getTotalCreditAmount());
				error = true;
			}

			if(error) System.out.println(controlRecord.toString());

		}

	}

	private static String calculateAmount(Batch batch, String type) {
		BigInteger totalAmountInt = new BigInteger("0");
		if(batch.getDetailRecords() != null)
			for(BatchRecord batchRecord : batch.getDetailRecords())
				if(batchRecord.getDetailRecord() != null){
					EntryDetailRecord detailRecord = batchRecord.getDetailRecord();
					if(getTransactionType(detailRecord.getTransactionCode()) == "credit" && type ==	"credit")
						totalAmountInt = totalAmountInt.add(new BigInteger(detailRecord.getAmount()));
					if(getTransactionType(detailRecord.getTransactionCode()) == "debit" && type ==	"debit")
						totalAmountInt = totalAmountInt.add(new BigInteger(detailRecord.getAmount()));
				}

		String totalAmount = totalAmountInt.toString();
		while(totalAmount.length() < 12)
			totalAmount = "0" + totalAmount;
		return totalAmount;
	}

	private static String getTransactionType(String transactionCode) {
		switch(transactionCode){
		case "22": 
		case "32": 
		case "23": 
		case "33":	return "credit";

		case "27": 
		case "37": 
		case "28": 
		case "38":	return "debit";
		}
		System.out.println("Invalid transaction type code found! " + transactionCode);
		return "Error";
	}

	private static String calculateEntryHash(Batch batch) {
		BigInteger entryHashInt = new BigInteger("0");
		if(batch.getDetailRecords() != null)
			for(BatchRecord batchRecord : batch.getDetailRecords())
				if(batchRecord.getDetailRecord() != null)
					entryHashInt = entryHashInt.add(new BigInteger(batchRecord.getDetailRecord().getReceivingDFIIdentification()));

		String entryHash = entryHashInt.toString();
		while(entryHash.length() < 10)
			entryHash = "0" + entryHash;
		if(entryHash.length() > 10)
			entryHash = entryHash.substring(entryHash.length() - 10, entryHash.length());
		return entryHash;
	}

	private static String countEntryRecords(Batch batch) {
		Integer entryCountInt = 0;
		if(batch.getDetailRecords() != null)
			for(BatchRecord batchRecord : batch.getDetailRecords()){
				if(batchRecord.getDetailRecord() != null)
					entryCountInt += 1;
				if(batchRecord.getAddenda() != null)
					entryCountInt += batchRecord.getAddenda().size();
			}

		String entryCount = Integer.toString(entryCountInt);
		while(entryCount.length() < 6)
			entryCount = "0" + entryCount;
		return entryCount;
	}

	private static void checkFileControlRecord(Nacha nacha) {
		final String objectName = "File control record";
		String blockCount = calculateBlockCount(nacha);
		String entryCount = countEntries(nacha);
		String batchCount = countBatch(nacha);
		String nachaHash = calculateNachaHash(nacha);
		String debitAmount = calculateAmountTotal(nacha, "debit");
		String creditAmount = calculateAmountTotal(nacha, "credit");

		FileControlRecord nineRecord = nacha.getControl();
		if(!nineRecord.getBlockCount().equals(blockCount))
			printIncorrectFieldError(objectName, "block count", blockCount, nineRecord.getBlockCount());
		if(!nineRecord.getEntryCount().equals(entryCount))
			printIncorrectFieldError(objectName, "entry count", entryCount, nineRecord.getEntryCount());
		if(!nineRecord.getBatchCount().equals(batchCount))
			printIncorrectFieldError(objectName, "batch count", batchCount, nineRecord.getBatchCount());
		if(!nineRecord.getEntryHash().equals(nachaHash))
			printIncorrectFieldError(objectName, "entry hash", nachaHash, nineRecord.getEntryHash());
		if(!nineRecord.getTotalDebitAmount().equals(debitAmount))
			printIncorrectFieldError(objectName, "total debit amount", debitAmount, nineRecord.getTotalDebitAmount());
		if(!nineRecord.getTotalCreditAmount().equals(creditAmount))
			printIncorrectFieldError(objectName, "total credit amount", creditAmount, nineRecord.getTotalCreditAmount());
	}

	private static String calculateAmountTotal(Nacha nacha, String type) {
		BigInteger amountInt = new BigInteger("0");
		if(nacha.getBatch() != null)
			for(Batch batch : nacha.getBatch()){
				if(type.equals("debit"))
					amountInt = amountInt.add(new BigInteger(batch.getControl().getTotalDebitAmount()));
				if(type.equals("credit"))
					amountInt = amountInt.add(new BigInteger(batch.getControl().getTotalCreditAmount()));
			}

		String amount = amountInt.toString();
		while(amount.length() < 12)
			amount = "0" + amount;
		return amount;
	}

	private static String calculateNachaHash(Nacha nacha) {
		BigInteger entryHashValue = new BigInteger("0");

		if(nacha.getBatch() != null)
			for(Batch batch : nacha.getBatch())
				if(batch.getDetailRecords() != null)
					for(BatchRecord batchRecord : batch.getDetailRecords())
						entryHashValue = entryHashValue.add(new BigInteger(batchRecord.getDetailRecord().getReceivingDFIIdentification()));

		String entryHash = entryHashValue.toString();
		while(entryHash.length() < 10)
			entryHash = "0" + entryHash;
		if(entryHash.length() > 10)
			entryHash = entryHash.substring(entryHash.length()-10, entryHash.length());

		return entryHash;
	}

	private static String countBatch(Nacha nacha) {
		Integer fiveRecordCount = 0;
		Integer eightRecordCount = 0;

		if(nacha.getBatch() != null)
			for(Batch batch : nacha.getBatch()){
				if(batch.getHeader() != null)
					fiveRecordCount += 1;
				if(batch.getControl() != null)
					eightRecordCount += 1;
			}

		if(fiveRecordCount != eightRecordCount)
			System.out.println("Number of five records does not match number of eight records!");

		String batchCount = Integer.toString(eightRecordCount);
		while(batchCount.length() < 6)
			batchCount = "0" + batchCount;

		return batchCount;
	}

	private static String countEntries(Nacha nacha) {
		Integer entryCount = 0;
		if(nacha.getBatch() != null)
			for(Batch batch : nacha.getBatch())
				if(batch.getDetailRecords() != null)
					for(BatchRecord batchRecord : batch.getDetailRecords()){
						if(batchRecord.getDetailRecord() != null)
							entryCount += 1;
						if(batchRecord.getAddenda() != null)
							entryCount += batchRecord.getAddenda().size();
					}

		String entryCountString = Integer.toString(entryCount);

		while(entryCountString.length() < 8)
			entryCountString = "0" + entryCountString;

		return entryCountString;
	}

	private static String calculateBlockCount(Nacha nacha) {
		String blockCount = "";

		if(nacha.getLineCount() % 10 == 0) 
			blockCount = Integer.toString(nacha.getLineCount()/10);
		else 
			blockCount = Integer.toString(nacha.getLineCount()/10 + 1);

		while(blockCount.length() < 6)
			blockCount = "0" + blockCount;

		return blockCount;
	}

}
