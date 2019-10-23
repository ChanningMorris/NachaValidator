package com.nachavalidator.model;

import static com.nachavalidator.util.ParserUtils.parsePositions;

import lombok.Data;

@Data
public class BatchHeader {

    String recordTypeCode;
    String serviceClassCode;
    String companyName;
    String companyDiscretionaryData;
    String foreignExchangeIndicator;
    String foreignExchangeReferenceIndicator;
    String foreignExchangeReference;
    String isoDestinationCountryCode;
    String originatorID;
    String isoOriginCurrencyCode;
    String isoDestinationCurrencyCode;
    String companyID;
    String standardEntryClassCode;
    String companyEntryDescription;
    String companyDescriptiveDate;
    String effectiveEntryDate;
    String settlementDate;
    String originatorStatusCode;
    String originatingDFIIdentification;
    String batchNumber;

    public BatchHeader(String batchHeaderString, boolean iatFlag) {
        this.recordTypeCode = parsePositions(batchHeaderString, 1, 1);
        this.serviceClassCode = parsePositions(batchHeaderString, 2, 4);
        this.companyName = parsePositions(batchHeaderString, 5, 20);
        if (!iatFlag) {
            this.companyDiscretionaryData = parsePositions(batchHeaderString, 21, 40);
            this.companyID = parsePositions(batchHeaderString, 41, 50);
            this.companyDescriptiveDate = parsePositions(batchHeaderString, 64, 69);
        } else {
            this.foreignExchangeIndicator = parsePositions(batchHeaderString, 21, 22);
            this.foreignExchangeReferenceIndicator = parsePositions(batchHeaderString, 23, 23);
            this.foreignExchangeReference = parsePositions(batchHeaderString, 24, 38);
            this.isoDestinationCountryCode = parsePositions(batchHeaderString, 39, 40);
            this.originatorID = parsePositions(batchHeaderString, 41, 50);
            this.isoOriginCurrencyCode = parsePositions(batchHeaderString, 64, 66);
            this.isoDestinationCountryCode = parsePositions(batchHeaderString, 67, 69);
        }
        this.standardEntryClassCode = parsePositions(batchHeaderString, 51, 53);
        this.companyEntryDescription = parsePositions(batchHeaderString, 54, 63);
        this.effectiveEntryDate = parsePositions(batchHeaderString, 70, 75);
        this.settlementDate = parsePositions(batchHeaderString, 76, 78);
        this.originatorStatusCode = parsePositions(batchHeaderString, 79, 79);
        this.originatingDFIIdentification = parsePositions(batchHeaderString, 80, 87);
        this.batchNumber = parsePositions(batchHeaderString, 88, 94);
    }

    public String toString(boolean iatFlag) {
        if (iatFlag)
            return recordTypeCode + serviceClassCode + companyName + foreignExchangeIndicator +
                    foreignExchangeReferenceIndicator + foreignExchangeReference + isoDestinationCountryCode +
                    originatorID + standardEntryClassCode + companyEntryDescription + isoOriginCurrencyCode +
                    isoDestinationCurrencyCode + effectiveEntryDate + settlementDate + originatorStatusCode +
                    originatingDFIIdentification + batchNumber;
        else
            return recordTypeCode + serviceClassCode + companyName + companyDiscretionaryData + companyID +
                    standardEntryClassCode + companyEntryDescription + companyDescriptiveDate + effectiveEntryDate +
                    settlementDate + originatorStatusCode + originatingDFIIdentification + batchNumber;

    }

}
