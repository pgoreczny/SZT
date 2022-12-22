package com.szt.bandCMS.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class NewsItem {
    @Id
    long id;

    @Column
    Date date;

    @Column
    String title;

    @Column
    String content;

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public NewsItem(Date date, String title, String content) {
        this.date = date;
        this.title = title;
        this.content = content;
    }

    public NewsItem() {
    }
}
