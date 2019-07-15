package com.example.zju_android_2019.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

	@Query("select * from notes order by priority desc")
	public List<Note> getAllNotes();

	@Delete
	public int deleteNote(Note note);

	@Insert
	public long insertNote(Note note);

	@Update
	public int updateNote(Note note);


}
