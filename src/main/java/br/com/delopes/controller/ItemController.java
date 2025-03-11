package br.com.delopes.controller;

import br.com.delopes.model.Item;
import br.com.delopes.repository.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public String listItems(Model model) {
        model.addAttribute("items", itemRepository.findAll());
        return "items";
    }

    @GetMapping("/new")
    public String newItemForm(Model model) {
        model.addAttribute("item", new Item());
        return "item-form";
    }

    @PostMapping
    public String saveItem(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "redirect:/items";
    }

    @GetMapping("/edit/{id}")
    public String editItem(@PathVariable String id, Model model) {
        model.addAttribute("item", itemRepository.findById(id).orElseThrow());
        return "item-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable String id) {
        itemRepository.deleteById(id);
        return "redirect:/items";
    }
}
