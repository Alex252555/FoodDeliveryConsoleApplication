import org.junit.Test;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DatabaseConnectionTest {

    @Test
    public void testAddingRestaurantIntoDatabase(){

        Restaurant r = new Restaurant("Pizzeria","Han Asparuh 52",5,100);

        Server.d.addEstablishment(Server.c,r.getName(),r.getAddress(),r.getRating());
        Server.d.loadTables(Server.c);

        assertEquals(r.getName(), Server.registeredEstablishments.get(Server.registeredEstablishments.size()-1).getName());
        assertEquals(r.getAddress(), Server.registeredEstablishments.get(Server.registeredEstablishments.size()-1).getAddress());
        assertEquals(r.getRating(), Server.registeredEstablishments.get(Server.registeredEstablishments.size()-1).getRating());
    }

    @Test
    public void testDatabaseEntryBeingDeleted() throws IOException {

        Server.d.addEstablishment(Server.c,"Pizzeria","Han Asparuh 52",5);
        Server.d.addDish(Server.c,"Pizza","d","Pizzeria","Han Asparuh 52","Dish",12);
        Server.d.addDish(Server.c,"Fries","d2","Pizzeria","Han Asparuh 52","Appetizer",5);
        Server.d.loadTables(Server.c);

        if(Server.registeredEstablishments.size()>1){
            Dish secondToLastBeforeDeletion=Server.menu.get(Server.menu.size()-2);
            Dish last=Server.menu.get(Server.menu.size()-1);

            String secondToLastName=secondToLastBeforeDeletion.getName();
            String secondToLastResName=secondToLastBeforeDeletion.getRestaurant().getName();
            String secondToLastResAddress=secondToLastBeforeDeletion.getRestaurant().getAddress();

            Server.d.removeDish(Server.c,last.getName(),last.getRestaurant().getName(),last.getRestaurant().getAddress());
            Server.d.loadTables(Server.c);

            assertEquals(secondToLastName,Server.menu.get(Server.menu.size()-1).getName());
            assertEquals(secondToLastResName,Server.menu.get(Server.menu.size()-1).getRestaurant().getName());
            assertEquals(secondToLastResAddress,Server.menu.get(Server.menu.size()-1).getRestaurant().getAddress());
        }
        else{
            fail("Restaurants list size not big enough to execute test. Please register >1 components");
        }

    }

}
