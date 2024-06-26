package supertracker.storage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import supertracker.command.NewCommand;
import supertracker.item.Inventory;
import supertracker.item.Item;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemStorageTest {
    private static final String INVALID_EX_DATE = "01-01-99999";
    private static final DateTimeFormatter INVALID_EX_DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyyy");
    private static final LocalDate UNDEFINED_DATE = LocalDate.parse(INVALID_EX_DATE, INVALID_EX_DATE_FORMAT);
    private static final LocalDate CURR_DATE = LocalDate.now();

    @BeforeAll
    public static void setUp() throws IOException {
        Inventory.clear();
        Item[] newItems = {
            new Item("orange", 10, 2.00, CURR_DATE),
            new Item("b", -10, 2.00, CURR_DATE),
            new Item("a", 10, -2.00, CURR_DATE),
            new Item("6969", 50, 15.9, UNDEFINED_DATE),
            new Item("a1@   lol  qwe^^%qw)e", 9431, 21.57, UNDEFINED_DATE),
            new Item("1_+$%$_)00", 9999999, 20.90, CURR_DATE),
            new Item("cheese,,,p/,,,,", 9, 7.00, UNDEFINED_DATE),
            new Item("cheese*&_yoyo*&_ ,,,q/-120,,", 9, 0, CURR_DATE),
            new Item("p ,,, l", 40, 0, UNDEFINED_DATE)
        };

        for (Item newItem : newItems) {
            Inventory.put(newItem.getName(), newItem);
        }
        ItemStorage.saveData();
        Inventory.clear();
    }

    @Test
    void loadData_validData_correctlyRead() throws IOException {
        ItemStorage.loadData();

        Item[] items = {
            new Item("orange", 10, 2.00, CURR_DATE),
            new Item("6969", 50, 15.9, UNDEFINED_DATE),
            new Item("a1@   lol  qwe^^%qw)e", 9431, 21.57, UNDEFINED_DATE),
            new Item("1_+$%$_)00", 9999999, 20.90, CURR_DATE),
            new Item("cheese*&_p/*&_,", 9, 7.00, UNDEFINED_DATE),
            new Item("cheese*&_yoyo*&_ *&_q/-120,,", 9, 0, CURR_DATE),
            new Item("p*&_l", 40, 0, UNDEFINED_DATE)
        };

        for (Item item : items) {
            Item loadedItem = Inventory.get(item.getName());
            assertNotNull(loadedItem);
            assertEquals(item.getQuantity(), loadedItem.getQuantity());
            assertEquals(item.getPrice(), loadedItem.getPrice());
            assertEquals(item.getExpiryDate(), loadedItem.getExpiryDate());
        }
    }

    @AfterAll
    public static void reset() throws IOException {
        Inventory.clear();

        NewCommand[] newCommands = {
            new NewCommand("Apple", 10, 2.00, UNDEFINED_DATE),
            new NewCommand("Banana", 20, 3.00, CURR_DATE),
            new NewCommand("Cake", 30, 4.00, UNDEFINED_DATE)
        };

        for (NewCommand newCommand : newCommands) {
            newCommand.execute();
        }

        ItemStorage.saveData();
    }
}
