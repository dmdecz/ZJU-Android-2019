package com.example.zju_android_2019;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

	private View mRootView;
	private ClockView mClockView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mRootView = findViewById(R.id.root);
		mClockView = findViewById(R.id.clock);

		mRootView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mClockView.setNextMode();
			}
		});
	}
}
