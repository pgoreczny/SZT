package com.szt.bandCMS.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Event {
    @Id
    long id;

    @Column
    Date startDate;

    @Column
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
