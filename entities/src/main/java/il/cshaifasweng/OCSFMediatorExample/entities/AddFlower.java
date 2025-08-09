package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class AddFlower implements Serializable {
    private Flower flower;
    boolean isTrue ;

    public AddFlower(Flower flower , boolean isTrue) {
        this.flower = flower;
        this.isTrue = isTrue;
    }
    public boolean isTrue() {
        return isTrue;
    }
    public void setTrue(boolean isTrue) {
        this.isTrue = isTrue;
    }
    public Flower getFlower() {
        return flower;
    }
    public void setFlower(Flower flower) {
        this.flower = flower;
    }
}
