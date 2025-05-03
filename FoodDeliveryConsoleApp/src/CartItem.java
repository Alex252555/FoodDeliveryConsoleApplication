public class CartItem {

    private Dish dish;

    private int amount;

    public CartItem(Dish dish, int amount){
        this.dish=dish;
        this.amount=amount;
    }

    public Dish getDish(){
        return dish;
    }

    public int getAmount(){
        return amount;
    }
}
