package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class AddSale implements Serializable {
    private static final long serialVersionUID = 1L;

    private int NumBranch;
    private int NumSale;
    public AddSale(int NumBranch, int NumSale){
        this.NumBranch = NumBranch;
        this.NumSale = NumSale;
    }
    public AddSale(){}
    public int getNumBranch() {return NumBranch;}
    public void setNumBranch(int NumBranch) {this.NumBranch = NumBranch;}
    public int getNumSale() {return NumSale;}
    public void setNumSale(int NumSale) {this.NumSale = NumSale;}
}