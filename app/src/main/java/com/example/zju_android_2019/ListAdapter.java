package com.example.zju_android_2019;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

	private List<Record> mRecordList = new ArrayList<>();

	@NonNull
	@Override
	public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return ListViewHolder.create(parent.getContext(), parent);
	}

	@Override
	public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
		holder.bind(mRecordList.get(position));
	}

	@Override
	public int getItemCount() {
		return mRecordList.size();
	}

	public void setRecordList(List<Record> recordList) {
		if (recordList == null || recordList.isEmpty()) {
			return;
		}
		mRecordList = recordList;
		notifyDataSetChanged();
	}

	public void randomlyUpdateHotValue() {
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < mRecordList.size(); i++) {
			Record tempRecord = mRecordList.get(i);
			int flag = random.nextInt(3) - 1;
			int change = random.nextInt(10) + 1;
			tempRecord.setHotValue(tempRecord.getHotValue() + flag * change);
		}
		Collections.sort(mRecordList, Collections.reverseOrder());
		for (int i = 0; i < mRecordList.size(); i++) {
			mRecordList.get(i).setRank(i + 1);
		}
		notifyDataSetChanged();
	}
}
