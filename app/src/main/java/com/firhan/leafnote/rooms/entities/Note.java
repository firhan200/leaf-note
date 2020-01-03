package com.firhan.leafnote.rooms.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "body")
    private String body;

    @ColumnInfo(name = "is_deleted")
    private Boolean isDeleted;

    @Ignore
    private Boolean selected;

    public Note(String title, String body) {
        this.title = title;
        this.body = body;

        //default selected
        this.selected = false;
        this.isDeleted = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        Note note = (Note) o;
        return id == note.id &&
                title.equals(note.title) &&
                body.equals(note.body) &&
                isDeleted.equals(note.isDeleted) &&
                selected.equals(note.selected);
    }
}
