package com.example.zju_android_2019;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

public class LottieFragment extends Fragment {

	private LottieAnimationView mAnimationView;
	private SeekBar mSeekBar;
	private CheckBox mCheckBox;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_lottie, container, false);

		mAnimationView = view.findViewById(R.id.animation_view);
		mSeekBar = view.findViewById(R.id.seekbar);
		mCheckBox = view.findViewById(R.id.loop_checkbox);

		mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mSeekBar.setEnabled(false);
					mAnimationView.playAnimation();
				} else {
					mSeekBar.setEnabled(true);
					mAnimationView.pauseAnimation();
				}
			}
		});

		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mAnimationView.setProgress((float) progress/mSeekBar.getMax());
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		return view;
	}
}
