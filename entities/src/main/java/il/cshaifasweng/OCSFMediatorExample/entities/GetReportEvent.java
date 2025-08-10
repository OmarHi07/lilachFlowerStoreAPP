package il.cshaifasweng.OCSFMediatorExample.entities;
import il.cshaifasweng.OCSFMediatorExample.entities.GetReportEvent;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class GetReportEvent implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    @Serial
    private  String reportType;
    private  List<?> reportData;

    public GetReportEvent(String reportType, List<?> reportData) {
        this.reportType = reportType;
        this.reportData = reportData;
    }
    public GetReportEvent(){}
    public void setReportType(String reportType1) {
        this.reportType = reportType1;
    }
    public void setReportData(List<?> reportData1) {
        this.reportData = reportData1;
    }
    public String getReportType() {
        return reportType;
    }

    public List<?> getReportData() {
        return reportData;
    }
}

