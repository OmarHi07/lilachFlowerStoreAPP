package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.time.LocalDate;

public class ReportRequest implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;


    private String reportType; // למשל "complain"
    private LocalDate fromDate;
    private LocalDate toDate;
    private int branchId; // -1 אם רוצים את כל הסניפים

    public ReportRequest(String reportType, LocalDate fromDate, LocalDate toDate, int branchId) {
        this.reportType = reportType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.branchId = branchId;
    }

    public String getReportType() {
        return reportType;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public int getBranchId() {
        return branchId;
    }
}
