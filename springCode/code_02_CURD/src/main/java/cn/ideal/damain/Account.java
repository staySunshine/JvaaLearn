package cn.ideal.damain;
import java.io.Serializable;

public class Account implements Serializable {
    private  Integer id;
    private String name;
    private Float balance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return  "id " + id + "name " + name + "balance " + balance;
    }
}
