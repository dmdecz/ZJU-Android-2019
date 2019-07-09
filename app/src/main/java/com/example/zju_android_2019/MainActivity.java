package com.example.zju_android_2019;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private RecyclerView mListView;
	private ListAdapter mAdapter;
	private TextView mRefreshView;
	private List<Record> mRecordList = new ArrayList<>();

	private Handler handler = new Handler();
	private Runnable randomUpdateHotValue = new Runnable() {
		@Override
		public void run() {
			mAdapter.randomlyUpdateHotValue();
			mRefreshView = findViewById(R.id.tv_refresh);
			Calendar cal = Calendar.getInstance();
			int hour;
			if (cal.get(Calendar.AM_PM) == Calendar.AM) {
				hour = cal.get(Calendar.HOUR);
			} else {
				hour = cal.get(Calendar.HOUR) + 12;
			}
			mRefreshView.setText("更新于：" + String.format("%02d:%02d:%02d", hour, cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)));
			handler.postDelayed(this, 3000);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recycler_view);

		mListView = findViewById(R.id.rv_list);
		mAdapter = new ListAdapter();
		mListView.setAdapter(mAdapter);
		mListView.addItemDecoration(new ListDecoration());
		mListView.setLayoutManager(new LinearLayoutManager(this));

		for (int i = 0; i < 100; i++) {
			mRecordList.add(new Record(i+1, i+"", 100 - i));
		}
		mAdapter.setRecordList(mRecordList);

		handler.post(randomUpdateHotValue);
	}
}
