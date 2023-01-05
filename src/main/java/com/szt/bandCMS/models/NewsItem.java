package com.szt.bandCMS.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
public class NewsItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date date;

    @Column
    String title;

    @Column
    String content;

    @Column
    Date edited;

    @ManyToOne
    @JoinColumn(name = "username")
    User editedBy;

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEdited(Date edited) {
        this.edited = edited;
    }

    public void setEditedBy(User editedBy) {
        this.editedBy = editedBy;
    }

    public NewsItem(Date date, String title, String content) {
        this.date = date;
        this.title = title;
        this.content = content;
    }

    public NewsItem() {
    }
}
