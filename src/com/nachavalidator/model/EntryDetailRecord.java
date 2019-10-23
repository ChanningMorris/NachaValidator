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
    String numberOfAddendaRecords;
    String iatFiller1;
    String iatFiller2;
    String iatAccountNumber;

    public EntryDetailRecord(String detailRecord, boolean iatFlag) {
        this.recordTypeCode = parsePositions(detailRecord, 1, 1);
        this.transactionCode = parsePositions(detailRecord, 2, 3);
        this.receivingDFIIdentification = parsePositions(detailRecord, 4, 11);
        this.checkDigit = parsePositions(detailRecord, 12, 12);

        if (iatFlag) {
            this.numberOfAddendaRecords = parsePositions(detailRecord, 13, 16);
            this.iatFiller1 = parsePositions(detailRecord, 17, 29);
            this.iatAccountNumber = parsePositions(detailRecord, 40, 74);
            this.iatFiller2 = parsePositions(detailRecord, 75, 76);
        } else {
            this.dfiAccountNumber = parsePositions(detailRecord, 13, 29);
            this.individualIdentificationNumber = parsePositions(detailRecord, 40, 54);
            this.individualName = parsePositions(detailRecord, 55, 76);
        }

        this.amount = parsePositions(detailRecord, 30, 39);
        this.discretionaryData = parsePositions(detailRecord, 77, 78);
        this.addendaRecordIndicator = parsePositions(detailRecord, 79, 79);
        this.traceNumber = parsePositions(detailRecord, 80, 94);
    }

    public String toString(boolean iatFlag) {
        if (iatFlag)
            return recordTypeCode + transactionCode + receivingDFIIdentification + checkDigit + numberOfAddendaRecords +
                    iatFiller1 + amount + iatAccountNumber + iatFiller2 + discretionaryData + addendaRecordIndicator
                    + traceNumber;
        else
            return recordTypeCode + transactionCode + receivingDFIIdentification + checkDigit + dfiAccountNumber +
                    amount + individualIdentificationNumber + individualName + discretionaryData +
                    addendaRecordIndicator + traceNumber;
    }

}
