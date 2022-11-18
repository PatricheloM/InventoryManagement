package inventorymanagement.backend.service.impl;

import inventorymanagement.backend.dto.ItemDTO;
import inventorymanagement.backend.model.Item;
import inventorymanagement.backend.repository.ItemRepository;
import inventorymanagement.backend.service.ItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public boolean itemEquals(Object o1, Object o2) {
        return itemRepository.itemEquals(o1, o2);
    }

    @Override
    public void saveItem(ItemDTO item) {
        itemRepository.saveItem(modelMapper.map(item, Item.class));
    }

    @Override
    public int getItemId(ItemDTO item) {
        return itemRepository.getItemId(modelMapper.map(item, Item.class));
    }

    @Override
    public boolean idExists(int id) {
        return itemRepository.idExists(id);
    }

    @Override
    public boolean deleteItem(int id) {
        return itemRepository.deleteItem(id);
    }

    @Override
    public Optional<ItemDTO> fetchItem(int id) {
        Optional<Item> item = itemRepository.fetchItem(id);
        if (item.isPresent()) {
            return Optional.of(modelMapper.map(item, ItemDTO.class));
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public List<ItemDTO> fetchItemByName(String name) {
        return itemRepository.fetchItemByName(name).stream()
                .map(item -> modelMapper.map(item, ItemDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> fetchItemByCompany(String company) {
        return itemRepository.fetchItemByCompany(company).stream()
                .map(item -> modelMapper.map(item, ItemDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> fetchItemByLocation(String location) {
        return itemRepository.fetchItemByLocation(location).stream()
                .map(item -> modelMapper.map(item, ItemDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> fetchItemByArrival(Date start, Date end) {
        return itemRepository.fetchItemByArrival(start, end).stream()
                .map(item -> modelMapper.map(item, ItemDTO.class)).collect(Collectors.toList());
    }
}
