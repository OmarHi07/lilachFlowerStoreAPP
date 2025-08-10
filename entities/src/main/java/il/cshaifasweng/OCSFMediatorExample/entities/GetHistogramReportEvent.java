
package il.cshaifasweng.OCSFMediatorExample.entities;
import java.io.Serializable;
import java.util.Map;

public class GetHistogramReportEvent implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;


    private final String type; // למשל "complaints"
    private final Map<String, Long> data; // שם סניף -> כמות

    public GetHistogramReportEvent(String type, Map<String, Long> data) {
        System.out.println("11111");
        this.type = type;
        this.data = data;
    }

    public String getType() {
        System.out.println("222222");
        return type;
    }

    public Map<String, Long> getData() {
        System.out.println("333333");
        return data;
    }
}