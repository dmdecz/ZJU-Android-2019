package com.example.zju_android_2019;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

	private static final int PAGE_COUNT = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ViewPager pager = findViewById(R.id.view_pager);
		TabLayout tabLayout = findViewById(R.id.tab_layout);
		pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int i) {
				switch (i) {
					case 0:
						return new AnimatorFragment();
					case 1:
						return new LottieFragment();
					case 2:
						return new FriendListFragment();
					default:
						return new AnimatorFragment();
				}
			}

			@Override
			public int getCount() {
				return PAGE_COUNT;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				switch (position) {
					case 0:
						return "Animator";
					case 1:
						return "Lottie";
					case 2:
						return "Friends";
					default:
						return "Animator";
				}
			}
		});
		tabLayout.setupWithViewPager(pager);
	}
}
