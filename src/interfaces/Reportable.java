package interfaces;

/**
 * Interface for entities that can generate reports
 * Demonstrates Interface OOP concept
 */
public interface Reportable {
    /**
     * Generate a summary report
     * @return Report as string
     */
    String generateReport();
    
    /**
     * Get report title
     * @return Title of the report
     */
    String getReportTitle();
    
    /**
     * Get report data in formatted form
     * @return Formatted report data
     */
    String getFormattedData();
}
