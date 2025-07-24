package il.cshaifasweng.OCSFMediatorExample.client;

import java.util.List;

public class GetReportEvent {
    private final String reportType;
    private final List<?> reportData;

    public GetReportEvent(String reportType, List<?> reportData) {
        System.out.println("GetReportEvent");
        this.reportType = reportType;
        this.reportData = reportData;
    }

    public String getReportType() {
        System.out.println("getReportType");
        return reportType;
    }

    public List<?> getReportData() {
        System.out.println("getReportData");
        return reportData;
    }
}
