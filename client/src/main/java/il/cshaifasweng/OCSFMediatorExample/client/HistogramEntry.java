package il.cshaifasweng.OCSFMediatorExample.client;

public class HistogramEntry {
    private final String branchName;
    private final Long value;

    public HistogramEntry(String branchName, Long value) {
        System.out.println("HistogramEntry");

        this.branchName = branchName;
        this.value = value;
    }

    public String getBranchName() {
        System.out.println("getBranchName");

        return branchName;
    }

    public Long getValue() {
        System.out.println("getValue");

        return value;
    }
}
