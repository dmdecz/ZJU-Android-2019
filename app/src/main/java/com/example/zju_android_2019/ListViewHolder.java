package com.example.zju_android_2019;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListViewHolder extends RecyclerView.ViewHolder {

	private TextView mRankView;
	private TextView mTitleView;
	private TextView mHotValueView;

	private ListViewHolder(@NonNull View itemView) {
		super(itemView);
		mRankView = itemView.findViewById(R.id.rank);
		mTitleView = itemView.findViewById(R.id.title);
		mHotValueView = itemView.findViewById(R.id.hot_value);
	}

	public static ListViewHolder create(Context context, ViewGroup vg) {
		View v = LayoutInflater.from(context).inflate(R.layout.layout_record, vg, false);
		return new ListViewHolder(v);
	}

	@SuppressLint("SetTextI18n")
	public void bind(Record record) {
		if (record == null) {
			return;
		}
		if (record.getRank() <= 3) {
			mRankView.setTextColor(mRankView.getResources().getColor(R.color.no_top));;
		} else {
			mRankView.setTextColor(mRankView.getResources().getColor(R.color.no_common));
		}
		mRankView.setText(record.getRank() + ".");
		mTitleView.setText(record.getText() + "");
		mHotValueView.setText(record.getHotValue() + "");
	}

}
