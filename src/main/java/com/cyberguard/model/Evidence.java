package com.cyberguard.model;

import java.sql.Timestamp;

public class Evidence {
    private int evidenceId;
    private int incidentId;
    private String fileName;   // sanitised original name (display only)
    private String fileType;   // MIME type decided by the server, never the browser
    private String filePath;   // random stored name on disk
    private Timestamp uploadedAt;

    public Evidence() { }
    public Evidence(String fileName, String fileType, String filePath) {
        this.fileName = fileName; this.fileType = fileType; this.filePath = filePath;
    }
    public int getEvidenceId() { return evidenceId; }
    public void setEvidenceId(int v) { evidenceId = v; }
    public int getIncidentId() { return incidentId; }
    public void setIncidentId(int v) { incidentId = v; }
    public String getFileName() { return fileName; }
    public void setFileName(String v) { fileName = v; }
    public String getFileType() { return fileType; }
    public void setFileType(String v) { fileType = v; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String v) { filePath = v; }
    public Timestamp getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Timestamp v) { uploadedAt = v; }
}
