package com.szt.bandCMS.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Item {
    @Id
    long id;

    @Column
    String page;

    @Column
    String key;

    @Column
    String text;

    public String getPage() {
        return page;
    }

    public String getKey() {
        return key;
    }

    public String getText() {
        return text;
    }

    public Item() {
    }

    public Item(String page, String key, String text) {
        this.page = page;
        this.key = key;
        this.text = text;
    }
}
