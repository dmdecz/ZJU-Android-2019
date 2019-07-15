package com.example.zju_android_2019.db;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;

public class NoteState {

	public static final NoteState TODO = new NoteState(0);
	public static final NoteState DONE = new NoteState(1);

	@ColumnInfo(name = "")
	private int mStateNumber;

	public NoteState(int stateNumber) {
		mStateNumber = stateNumber;
	}

	public static NoteState from(boolean isDone) {
		if (isDone)
			return DONE;
		else
			return TODO;
	}

	public int getStateNumber() {
		return mStateNumber;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj instanceof NoteState) {
			return mStateNumber == ((NoteState) obj).mStateNumber;
		}
		return false;
	}
}
