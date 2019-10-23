package com.nachavalidator.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

public class ParseNacha {

	public static Nacha parseNacha(String fileName) {
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			Nacha nacha = new Nacha();
			nacha.lineCount = 0;
			nacha.batch = new ArrayList<Batch>();
			nacha.padRecord = new ArrayList<NinePadRecord>();
			nacha.invalidLengthFlag = false;

			Batch batch = null;

			ArrayList<BatchRecord> detailList = null;
			BatchRecord detailRecord = null;

			for(String line; (line = br.readLine()) != null; ){

				if(line.length() == 94)
					nacha.lineCount += 1;
				else
					nacha.invalidLengthFlag = true;

				if(line.startsWith("1"))
					nacha.header = new FileHeader(line);

				if(line.startsWith("5")){
					batch = new Batch(); //new object for current batch
					String temp = line.substring(50,53);
					batch.iatFlag = line.substring(50, 53).equals("IAT");
					batch.header = new BatchHeader(line, batch.iatFlag); //parses 5 record
					detailList = new ArrayList<>(); //list for 6 and 7 records
				}

				if(line.startsWith("6")){
					//add current 6/7 record object if it exists...
					if(detailRecord != null)
						detailList.add(detailRecord);

					//then create object for current set of 6 and 7 records, and parse the 6
					detailRecord = new BatchRecord();
					detailRecord.detailRecord = new EntryDetailRecord(line, batch.iatFlag);
				}

				if(line.startsWith("7"))
					//parse 7 record, add it to existing detailRecord
					detailRecord.addenda = parseAddendaRecord(detailRecord, line);

				if(line.startsWith("8")){
					//add detailRecord to detailList, null out objects for new batch
					detailList.add(detailRecord);
					batch.detailRecords = detailList;
					batch.control = new BatchControlRecord(line);
					nacha.batch.add(batch);
					detailRecord = null;
					detailList = null;
					batch = null;
				}

				if(line.startsWith("9")){
					if(line.endsWith("9"))
						nacha.padRecord.add(new NinePadRecord(line));
					else if(!line.endsWith("9"))
						nacha.control = new FileControlRecord(line);
					else System.out.println("Invalid 9 record found!");
				}

			}

			br.close();
			return nacha;

		} catch (IOException e) {
			System.out.println("Issue reading file!");
			e.printStackTrace();
			return null;
		}

	}

	private static ArrayList<AddendaRecord> parseAddendaRecord(BatchRecord currentBatch, String addendaRecordString){
		ArrayList<AddendaRecord> addendaList = currentBatch.addenda;
		if(addendaList == null) addendaList = new ArrayList<AddendaRecord>();
		addendaList.add(new AddendaRecord(addendaRecordString));
		return addendaList;
	}

}
