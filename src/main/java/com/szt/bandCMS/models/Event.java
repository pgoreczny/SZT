package com.szt.bandCMS.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date startDate;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date endDate;

    @Column
    String place;

    @Column
    String title;

    @Column
    String photoUrl;

    @Column
    String content;

    @Column
    String status;

    @Column
    Date edited;

    @ManyToOne
    @JoinColumn(name = "username")
    User editedBy;

    public long getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getPlace() {
        return place;
    }

    public String getTitle() {
        return title;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getContent() {
        return content;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEdited(Date edited) {
        this.edited = edited;
    }

    public void setEditedBy(User editedBy) {
        this.editedBy = editedBy;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Event() {
    }

    public Event(Date startDate, Date endDate, String place, String title, String photoUrl, String content) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.place = place;
        this.title = title;
        this.photoUrl = photoUrl;
        this.content = content;
    }
}
