package com.example.zju_android_2019.ui;

import com.example.zju_android_2019.db.Note;

public interface NoteOperator {

	public void updateNote(Note note);
	public void deleteNote(Note note);
}
