package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.time.LocalDate;

public class HistogramReportRequest implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;

    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final String type;
    private final int branchId;

    public HistogramReportRequest(String type, LocalDate fromDate, LocalDate toDate, int branchId) {
        this.type = type;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.branchId = branchId;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public String getType() {
        return type;
    }

    public int getBranchId() {
        return branchId;
    }
}