package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity
public class Complain implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "Complain_date")
    private LocalDate Date;
    @Column(name = "complain_time")
    private LocalTime Time;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "order_id")
    private Order order;

    private double refund;
    private boolean status;
    private String complain_text;
    private String answer_text;

    public Complain() {}

    public Complain(LocalDate date, LocalTime time, Order order, String text){
        this.Date = date;
        this.Time = time;
        this.order = order;
        this.complain_text = text;
    }

    public void setCustomer(Customer customer){this.customer = customer;}
    public void setRefund(double refund) {this.refund = refund;}
    public double getRefund() {return refund;}
    public void setStatus(boolean status) {this.status = status;}
    public boolean getStatus() {return status;}
    public String getComplain_text() {return complain_text;}
    public void setComplain_text(String complain_text) {this.complain_text = complain_text;}
    public String getAnswer_text() {return answer_text;}
    public void setAnswer_text(String answer_text) {this.answer_text = answer_text;}
    public Customer getCustomer() {return customer;}
    public Branch getBranch() {return branch;}
    public void setBranch(Branch branch) {this.branch = branch;}

    public Order getOrder() {return order;}
    public LocalDate getDate() {
        return Date;
    }
    public LocalTime getTime() {
        return Time;
    }
    public int getId(){return id;}


    public void setOrder(Order order) {
        if (this.order == order) return;
        Order old = this.order;
        this.order = order;
//        if (old != null) old.getComplains().remove(this);
//        if (order != null && !order.getComplains().contains(this)) {
//            order.getComplains().add(this);
//        }
    }


    public void setDate(LocalDate localDate) {
        System.out.println("mohammed");
        this.Date = localDate;  // ✅ נכון: מקצה את הפרמטר לשדה
    }

}
