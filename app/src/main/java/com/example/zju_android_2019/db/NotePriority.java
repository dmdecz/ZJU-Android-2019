package com.example.zju_android_2019.db;

import android.graphics.Color;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

public class NotePriority {

	public static final NotePriority High = new NotePriority(2, Color.RED);
	public static final NotePriority Medium = new NotePriority(1, Color.argb(127, 255, 0, 0));
	public static final NotePriority Low = new NotePriority(0, Color.WHITE);

	private static final NotePriority[] values = new NotePriority[] {Low, Medium, High};

	@ColumnInfo(name = "")
	private int mPriorityNumber;

	@Ignore
	private int mColor;

	private NotePriority(int priorityNumber, int color) {
		mPriorityNumber = priorityNumber;
		mColor = color;
	}

	public NotePriority(int priorityNumber) {
		NotePriority p = NotePriority.from(priorityNumber);
		this.mPriorityNumber = p.mPriorityNumber;
		this.mColor = p.mColor;
	}

	public static NotePriority from(int priorityNumber) {
		for (NotePriority p : NotePriority.values) {
			if (p.mPriorityNumber == priorityNumber) {
				return p;
			}
		}
		return Low;
	}

	public int getPriorityNumber() {
		return mPriorityNumber;
	}

	public int getColor() {
		return mColor;
	}

	public void setColor(int color) {
		mColor = color;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj instanceof NotePriority) {
			return (mPriorityNumber == ((NotePriority) obj).mPriorityNumber) &&
					(mColor == ((NotePriority) obj).mColor);
		}
		return false;
	}
}
