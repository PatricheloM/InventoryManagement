package inventorymanagement.backend.service;

import inventorymanagement.backend.dto.ItemDTO;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    boolean itemEquals(Object o1, Object o2);
    void saveItem(ItemDTO item);
    int getItemId(ItemDTO item);
    boolean idExists(int id);
    boolean deleteItem(int id);
    Optional<ItemDTO> fetchItem(int id);
    List<ItemDTO> fetchItemByName(String name);
    List<ItemDTO> fetchItemByCompany(String company);
    List<ItemDTO> fetchItemByLocation(String location);
    List<ItemDTO> fetchItemByArrival(Date start, Date end);
    List<ItemDTO> fetchAllItems();
}
