package com.example.demo1.model;

public class ReportedRecipe {

    private String name;
    private String authorName;
    private String reporterName;
    private String dateReporting;
    private String image;

    public ReportedRecipe(String name,String authorName,String reporterName,String dateReporting,String image) {
        this.name = name;
        this.authorName = authorName;
        this.reporterName = reporterName;
        this.dateReporting = dateReporting;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getDateReporting() {
        return dateReporting;
    }

    public void setDateReporting(String dateReporting) {
        this.dateReporting = dateReporting;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
