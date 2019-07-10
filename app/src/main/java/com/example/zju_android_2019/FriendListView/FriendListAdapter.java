package com.example.zju_android_2019.FriendListView;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListViewHolder> {

	private List<FriendRecord> mFriendList = new ArrayList<>();

	@NonNull
	@Override
	public FriendListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return FriendListViewHolder.create(parent.getContext(), parent);
	}

	@Override
	public void onBindViewHolder(@NonNull FriendListViewHolder holder, int position) {
		holder.bind(mFriendList.get(position));
	}

	@Override
	public int getItemCount() {
		return mFriendList.size();
	}

	public void setFriendList(List<FriendRecord> friendList) {
		if (friendList == null || friendList.isEmpty()) {
			return;
		}
		mFriendList = friendList;
		notifyDataSetChanged();
	}
}
