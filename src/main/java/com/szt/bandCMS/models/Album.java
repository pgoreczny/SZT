package com.szt.bandCMS.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @Column
    String photoPath;

    @Column
    String title;

    @Column
    String shortDesc;

    @Column
    String longDesc;

    @Column
    String code;

    @Column
    Date edited;

    @ManyToOne
    @JoinColumn(name = "username")
    User editedBy;

    public long getId() {
        return id;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getCode() {
        return code;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setEdited(Date edited) {
        this.edited = edited;
    }

    public void setEditedBy(User editedBy) {
        this.editedBy = editedBy;
    }

    public Album() {
    }

    public Album(String photoPath, String title, String shortDesc, String code) {
        this.photoPath = photoPath;
        this.title = title;
        this.shortDesc = shortDesc;
        this.code = code;
    }

    public Album(String photoPath, String title, String shortDesc, String longDesc, String code) {
        this.photoPath = photoPath;
        this.title = title;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.code = code;
    }
}
