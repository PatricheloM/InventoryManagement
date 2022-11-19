package inventorymanagement.backend.repository;

import inventorymanagement.backend.model.Item;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    boolean itemEquals(Object o1, Object o2);
    void saveItem(Item item);
    int getItemId(Item item);
    boolean idExists(int id);
    boolean deleteItem(int id);
    Optional<Item> fetchItem(int id);
    List<Item> fetchItemByName(String name);
    List<Item> fetchItemByCompany(String company);
    List<Item> fetchItemByLocation(String location);
    List<Item> fetchItemByArrival(Date start, Date end);
    List<Item> fetchAllItems();
}
