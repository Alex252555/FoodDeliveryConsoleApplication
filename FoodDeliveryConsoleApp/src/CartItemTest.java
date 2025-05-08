import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CartItemTest {

    @Test
    public void testCartItemCreation() throws IOException {

        Socket s=new Socket("127.0.0.1", 1211);
        Profile p=new Profile("User1","Password1","Customer");

        VisualInterface vi=new VisualInterface(s);

        vi.currentProfile=p;

        String dishName = "Pizza";
        String restaurantName = "gosho's pizza";
        double price = 12.5;
        int amount = 3;

        CartItem item = new CartItem(dishName, restaurantName, price, amount);

        vi.orderDish(item);

        assertEquals(restaurantName, vi.currentProfile.getCart().get(0).getRestaurantName());
        assertEquals(price, vi.currentProfile.getCart().get(0).getPrice(), 0.001);
        assertEquals(amount, vi.currentProfile.getCart().get(0).getAmount());
    }

    @Test
    public void testNegativeAmount() {
        CartItem item = new CartItem("Fries", "Georgi's grillhouse", 8.99, -1);
        assertEquals(-1, item.getAmount());
    }
}