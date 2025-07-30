package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.time.LocalDate;

public class HistogramReportRequest implements Serializable {
    private final LocalDate fromDate;
    private final LocalDate toDate;

    public HistogramReportRequest(LocalDate fromDate, LocalDate toDate) {
        System.out.println("ff");
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public LocalDate getFromDate() {
        System.out.println("gg");
        return fromDate;
    }

    public LocalDate getToDate() {
        System.out.println("hh");
        return toDate;
    }
}