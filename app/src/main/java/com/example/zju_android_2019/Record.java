package com.example.zju_android_2019;

public class Record implements Comparable<Record> {
	private int mRank;
	private String mText;
	private int mHotValue;
	private int mState;

	public Record(int rank, String text, int hotValue) {
		mRank = rank;
		mText = text;
		mHotValue = hotValue;
		mState = 0;
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

	public int getHotValue() {
		return mHotValue;
	}

	public void setHotValue(int hotValue) {
		if (hotValue < 0) {
			hotValue = 0;
		}
		if (hotValue < mHotValue) {
			mState = -1;
		} else if (hotValue > mHotValue) {
			mState = 1;
		}
		mHotValue = hotValue;
	}

	public int getState() {
		return mState;
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
