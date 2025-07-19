package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class AddFlower implements Serializable {
    private Flower flower;
    public AddFlower(Flower flower) {
        this.flower = flower;
    }
    public Flower getFlower() {
        return flower;
    }
    public void setFlower(Flower flower) {
        this.flower = flower;
    }
}
