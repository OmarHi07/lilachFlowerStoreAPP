package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;

public class DeletFlower {
    public Flower flower;
    DeletFlower(Flower flower) {
        this.flower = flower;
    }
    DeletFlower(){}
    public void setFlower(Flower flower) {
        this.flower = flower;
    }
    public Flower getFlower() {
        return flower;
    }

}
