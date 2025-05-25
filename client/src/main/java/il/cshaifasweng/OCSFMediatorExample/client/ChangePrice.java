package il.cshaifasweng.OCSFMediatorExample.client;

public class ChangePrice {
    private int id;
    private double price;
    public ChangePrice(int id, double price){
        this.id = id;
        this.price = price;
    }
    public int getId(){
       return this.id;
    }
    public double getPrice(){
        return this.price;
    }
    public void setPrice(int price){
        this.price = price;
    }
    public void setId(int id){
        this.id = id;
    }
}
