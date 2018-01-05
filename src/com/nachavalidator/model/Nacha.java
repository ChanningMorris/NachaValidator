package com.nachavalidator.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Nacha {
	
	public Integer lineCount;
	public FileHeader header;
	public ArrayList<Batch> batch;
	public FileControlRecord control;
	public ArrayList<NinePadRecord> padRecord;

}
