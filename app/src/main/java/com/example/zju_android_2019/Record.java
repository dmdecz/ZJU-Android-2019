package com.example.zju_android_2019;

import android.util.Log;

public class Record implements Comparable<Record> {
	private int mRank;
	private String mText;
	private int mHotValue;
	private int mState;
	private int mNew;
	private boolean mHot;

	public Record(int rank, String text, int hotValue) {
		mRank = rank;
		mText = text;
		mHotValue = hotValue;
		mState = 0;
		mNew = 0;
		mHot = false;
	}

	public int getRank() {
		return mRank;
	}

	public void setRank(int rank) {
		mRank = rank;
	}

	public String getText() {
		return mText;
	}

	public void setText(String text) {
		 mText = text;
	}

	public int getHotValue() {
		return mHotValue;
	}

	public void setHotValue(int hotValue) {
		if (hotValue < 0) {
			hotValue = 0;
		}
		mHot = false;
		Log.d("Record", "updating" + mText);
		Log.d("Record", "change " + (hotValue - mHotValue));
		if (hotValue < mHotValue) {
			mState = -1;
		} else if (hotValue > mHotValue) {
			mState = 1;
			if (hotValue - mHotValue >= 5) {
				mHot = true;
				Log.d("Record", "set hot " + mText);
			}
		} else {
			mState = 0;
		}
		mHotValue = hotValue;
	}

	public int getState() {
		return mState;
	}

	public boolean isNew() {
		return (mNew != 0);
	}

	public void setNew(int New) {
		mNew = New;
	}

	public boolean isHot() {
		return mHot;
	}

	public void updateOnce() {
		if (isNew()) {
			mNew--;
		}
	}

	@Override
	public int compareTo(Record o) {
		if (mHotValue > o.getHotValue() || mHotValue == o.getHotValue() && mState > o.getState()) {
			return 1;
		} else {
			return -1;
		}
	}
}
