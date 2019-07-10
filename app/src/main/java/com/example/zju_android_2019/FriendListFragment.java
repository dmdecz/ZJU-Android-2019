package com.example.zju_android_2019;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.zju_android_2019.FriendListView.FriendListAdapter;
import com.example.zju_android_2019.FriendListView.FriendRecord;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FriendListFragment extends Fragment {

	private RecyclerView mFriendListView;
	private FriendListAdapter mAdapter;
	private List<FriendRecord> mFriendList = new ArrayList<>();

	private LottieAnimationView mAnimation;

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friendlist, container, false);

		mFriendListView = view.findViewById(R.id.rv_friend_list);
		mAnimation = view.findViewById(R.id.lav_load_friend_list);

		mAdapter = new FriendListAdapter();
		mFriendListView.setAdapter(mAdapter);
		mFriendListView.setLayoutManager(new LinearLayoutManager(getContext()));
		mFriendListView.setVisibility(View.INVISIBLE);

		for (int i = 0; i < 100; i++) {
			mFriendList.add(new FriendRecord(i + ""));
		}
		mAdapter.setFriendList(mFriendList);

		mAnimation.playAnimation();

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getView().postDelayed(new Runnable() {
			@Override
			public void run() {
				mFriendListView.setVisibility(View.VISIBLE);
				ObjectAnimator animationOut = ObjectAnimator.ofFloat(mAnimation, "alpha", 1f, 0f);
				animationOut.setDuration(2000);
				ObjectAnimator listIn = ObjectAnimator.ofFloat(mFriendListView, "alpha", 0f, 1f);
				listIn.setDuration(2000);
				AnimatorSet animatorSet = new AnimatorSet();
				animatorSet.playTogether(animationOut, listIn);
				animatorSet.start();

				animatorSet.addListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {

					}

					@Override
					public void onAnimationEnd(Animator animation) {
						mAnimation.cancelAnimation();
					}

					@Override
					public void onAnimationCancel(Animator animation) {

					}

					@Override
					public void onAnimationRepeat(Animator animation) {

					}
				});
			}
		}, 5000);
	}
}
