package com.getfeetwithus.dto;

import org.springframework.web.multipart.MultipartFile;

public class DietUploadDto {
    private String dietId;
    private String dietTitle;
    private String notes;
    private MultipartFile file;

    public DietUploadDto() {}

    public String getDietId() { return dietId; }
    public void setDietId(String dietId) { this.dietId = dietId; }
    public String getDietTitle() { return dietTitle; }
    public void setDietTitle(String dietTitle) { this.dietTitle = dietTitle; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public MultipartFile getFile() { return file; }
    public void setFile(MultipartFile file) { this.file = file; }
}
