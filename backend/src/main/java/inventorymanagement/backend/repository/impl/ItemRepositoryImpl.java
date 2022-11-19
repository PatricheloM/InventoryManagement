package inventorymanagement.backend.repository.impl;

import inventorymanagement.backend.model.Item;
import inventorymanagement.backend.repository.ItemRepository;
import inventorymanagement.backend.repository.RedisRepository;
import inventorymanagement.backend.util.InventoryManagementStringTools;
import inventorymanagement.backend.util.date.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    @Autowired
    RedisRepository redisRepository;

    @Override
    public boolean itemEquals(Object o1, Object o2) {
        if (o1 == o2) return true;
        if (o1 == null || o2 == null || o1.getClass() != o2.getClass()) return false;
        Item item1 = (Item) o1;
        Item item2 = (Item) o2;
        return item1.getWeight() == item2.getWeight() && Objects.equals(item1.getName(), item2.getName()) && Objects.equals(item1.getCompany(), item2.getCompany()) && Objects.equals(item1.getArrival(), item2.getArrival()) && Objects.equals(item1.getLocation(), item2.getLocation());
    }

    @Override
    public void saveItem(Item item) {
        int id = redisRepository.incrBy(InventoryManagementStringTools.getItemIdKey(), 1);
        redisRepository.sadd(InventoryManagementStringTools.getItemSetKey(), String.valueOf(id));
        redisRepository.hmset("ITEM_" + id, Map.of(
                "name", item.getName(),
                "weight", String.valueOf(item.getWeight()),
                "company", item.getCompany(),
                "arrival", DateConverter.stringFromDate(item.getArrival()),
                "location", item.getLocation()
        ));
    }

    @Override
    public int getItemId(Item item) {
        List<Integer> items = redisRepository.smembers(InventoryManagementStringTools.getItemSetKey())
                .stream().map(Integer::parseInt).collect(Collectors.toList());
        for (Integer i : items) {
            if (itemEquals(fetchItem(i).get(), item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean idExists(int id) {
        return redisRepository.sismember(InventoryManagementStringTools.getItemSetKey(), String.valueOf(id));
    }

    @Override
    public boolean deleteItem(int id) {
        if (idExists(id)) {
            redisRepository.srem(InventoryManagementStringTools.getItemSetKey(), String.valueOf(id));
            redisRepository.del("ITEM_" + id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<Item> fetchItem(int id) {
        if (idExists(id)) {
            Map<String, String> fetch = redisRepository.hgetall("ITEM_" + id);
            Item item = new Item();
            item.setName(fetch.get("name"));
            item.setWeight(Integer.parseInt(fetch.get("weight")));
            item.setCompany(fetch.get("company"));
            item.setArrival(DateConverter.dateFromString(fetch.get("arrival")));
            item.setLocation(fetch.get("location"));
            return Optional.of(item);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Item> fetchItemByName(String name) {
        return redisRepository.smembers(InventoryManagementStringTools.getItemSetKey())
                .stream().map(id -> fetchItem(Integer.parseInt(id)).get())
                .filter(itemName -> itemName.getName().equals(name)).collect(Collectors.toList());
    }

    @Override
    public List<Item> fetchItemByCompany(String company) {
        return redisRepository.smembers(InventoryManagementStringTools.getItemSetKey())
                .stream().map(id -> fetchItem(Integer.parseInt(id)).get())
                .filter(companyName -> companyName.getCompany().equals(company)).collect(Collectors.toList());
    }

    @Override
    public List<Item> fetchItemByLocation(String location) {
        return redisRepository.smembers(InventoryManagementStringTools.getItemSetKey())
                .stream().map(id -> fetchItem(Integer.parseInt(id)).get())
                .filter(locationName -> locationName.getLocation().equals(location)).collect(Collectors.toList());
    }

    @Override
    public List<Item> fetchItemByArrival(Date start, Date end) {
        return redisRepository.smembers(InventoryManagementStringTools.getItemSetKey())
                .stream().map(id -> fetchItem(Integer.parseInt(id)).get())
                .filter(date -> date.getArrival().before(end) && date.getArrival().after(start)).collect(Collectors.toList());
    }

    @Override
    public List<Item> fetchAllItems() {
        return redisRepository.smembers(InventoryManagementStringTools.getItemSetKey())
                .stream().map(id -> fetchItem(Integer.parseInt(id)).get()).collect(Collectors.toList());
    }
}
