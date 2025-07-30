package il.cshaifasweng.OCSFMediatorExample.entities;
import il.cshaifasweng.OCSFMediatorExample.entities.GetReportEvent;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class GetReportEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String reportType;
    private final List<?> reportData;

    public GetReportEvent(String reportType, List<?> reportData) {
        this.reportType = reportType;
        this.reportData = reportData;
    }

    public String getReportType() {
        return reportType;
    }

    public List<?> getReportData() {
        return reportData;
    }
}

