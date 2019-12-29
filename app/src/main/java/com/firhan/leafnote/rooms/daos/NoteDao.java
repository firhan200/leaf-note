package com.firhan.leafnote.rooms.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.firhan.leafnote.rooms.entities.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :keyword || '%' OR body LIKE '%' || :keyword || '%' ORDER BY id DESC")
    List<Note> searchActiveNotes(String keyword);

    @Query("SELECT * FROM notes WHERE is_deleted=0 ORDER BY id DESC")
    List<Note> getAll();

    @Query("SELECT * FROM notes WHERE is_deleted=1 ORDER BY id DESC")
    List<Note> getAllTrash();

    @Query("SELECT * FROM notes WHERE id=:id")
    Note getNoteById(long id);

    @Insert
    long insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);
}
