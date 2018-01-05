package com.nachavalidator.model;

import lombok.Data;

@Data
public class NinePadRecord {
	
	String padRecord;
	
	public NinePadRecord(String record){
		this.padRecord = record;
	}
	
	@Override
	public String toString(){
		return padRecord;
	}

}
