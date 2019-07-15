package com.example.zju_android_2019.db;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "notes")
public class Note {

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	private long id;

	@ColumnInfo(name = "date")
	private long time;
//	@Ignore
//	private Date date;

	@Embedded(prefix = "state")
	private NoteState state;

	@ColumnInfo(name = "content")
	private String content;

	@Embedded(prefix = "priority")
	private NotePriority priority;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Date getDate() {
		return new Date(time);
	}

	public void setDate(Date date) {
		this.time = date.getTime();
	}

	public NoteState getState() {
		return state;
	}

	public void setState(NoteState state) {
		this.state = state;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public NotePriority getPriority() {
		return priority;
	}

	public void setPriority(NotePriority priority) {
		this.priority = priority;
	}

}
