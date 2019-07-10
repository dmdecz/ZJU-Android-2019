package com.example.zju_android_2019.FriendListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zju_android_2019.R;

public class FriendListViewHolder extends RecyclerView.ViewHolder {

	private TextView mTextView;

	public FriendListViewHolder(@NonNull View itemView) {
		super(itemView);
		mTextView = itemView.findViewById(R.id.friend_name);
	}

	public static FriendListViewHolder create(Context context, ViewGroup vg) {
		View v = LayoutInflater.from(context).inflate(R.layout.layout_friend_record, vg, false);
		return new FriendListViewHolder(v);
	}

	public void bind(FriendRecord friend) {
		mTextView.setText(friend.getName());
	}
}
