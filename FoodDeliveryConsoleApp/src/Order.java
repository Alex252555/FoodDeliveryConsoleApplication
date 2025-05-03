import java.util.ArrayList;

public class Order {
    private String username;
    private String address;
    private double orderDeliveryReward;
    private String orderStatus;

    public String getUsername(){
        return username;
    }
    public String getAddress(){
        return address;
    }
    public double getOrderDeliveryReward(){
        return orderDeliveryReward;
    }
    public String getOrderStatus(){
        return orderStatus;
    }
    public Order(String username, String address, double orderDeliveryReward, String orderStatus){
        this.username=username;
        this.address=address;
        this.orderDeliveryReward=orderDeliveryReward;
        this.orderStatus=orderStatus;
    }

}
