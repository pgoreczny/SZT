package com.szt.bandCMS.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Subsite {
    @Id
    String name;

    @Transient
    private Long parentId;

    @Column
    String title;

    @Column
    String content;

    @Column
    int position;

    @Column
    String path;

    @Column
    boolean builtIn;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_id")
    Subsite parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Subsite> children;

    @Column
    Date edited;

    @ManyToOne
    @JoinColumn(name = "username")
    User editedBy;

    public Subsite() {
    }

    public Subsite(String name, String title, String content, int position, String path) {
        this.name = name;
        this.title = title;
        this.content = content;
        this.position = position;
        this.path = path;
        this.children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isBuiltIn() {
        return builtIn;
    }

    public void setChildren(List<Subsite> children) {
        this.children = children;
    }

    public Long getParentId() {
        return parentId;
    }

    public Subsite getParent() {
        return parent;
    }

    public List<Subsite> getChildren() {
        return children;
    }
}
