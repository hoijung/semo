package com.example.printinfo.dao;

public class AttachmentDTO {
	private String fileName;
	private byte[] fileData;

	public AttachmentDTO(String fileName, byte[] fileData) {
		this.fileName = fileName;
		this.fileData = fileData;
	}

	public String getFileName() {
		return fileName;
	}

	public byte[] getFileData() {
		return fileData;
	}
}
