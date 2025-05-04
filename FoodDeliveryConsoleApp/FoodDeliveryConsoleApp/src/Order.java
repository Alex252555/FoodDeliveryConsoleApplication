import java.util.ArrayList;

public class Order {
    private String username;
    private double orderDeliveryReward;
    private String orderStatus;

    public String getUsername(){
        return username;
    }
    public double getOrderDeliveryReward(){
        return orderDeliveryReward;
    }
    public String getOrderStatus(){
        return orderStatus;
    }
    public Order(String username, double orderDeliveryReward, String orderStatus){
        this.username=username;
        this.orderDeliveryReward=orderDeliveryReward;
        this.orderStatus=orderStatus;
    }

}
