package br.com.delopes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "items")
public class Item {

    @Id
    private String id;
    private String name;

    public Item() {
        this.id = UUID.randomUUID().toString();
    }

    public Item(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}