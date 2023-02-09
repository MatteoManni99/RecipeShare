package it.unipi.dii.aide.lsmsd.recipeshare.model;

public class ReportedRecipe extends RecipeReduced{
    private String reporterName;
    private String dateReporting;

    public ReportedRecipe(String name,String authorName,String reporterName,String dateReporting,String image) {
        super(name,authorName,image);
        this.reporterName = reporterName;
        this.dateReporting = dateReporting;
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


}
