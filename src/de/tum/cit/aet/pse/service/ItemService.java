package de.tum.cit.aet.pse.service;

import de.tum.cit.aet.pse.entity.Gluhwein;
import de.tum.cit.aet.pse.entity.Item;
import de.tum.cit.aet.pse.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    public List<Gluhwein> fetchGluhweinItems() {
        return itemRepository.findAllGluhweinItems();
    }
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public Item updateItem(Long id, Item updatedItem) {
        return itemRepository.findById(id).map(item -> {
            item.setName(updatedItem.getName());
            item.setBasePrice(updatedItem.getBasePrice());
            item.setStorable(updatedItem.isStorable());
            return itemRepository.save(item);
        }).orElseThrow(() -> new RuntimeException("Item not found!"));
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
