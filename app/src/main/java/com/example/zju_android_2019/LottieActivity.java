package com.example.zju_android_2019;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class LottieActivity extends AppCompatActivity {

	private LottieAnimationView mAnimationView;
	private SeekBar mSeekBar;
	private CheckBox mCheckBox;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottie);

		mAnimationView = findViewById(R.id.animation_view);
		mSeekBar = findViewById(R.id.seekbar);
		mCheckBox = findViewById(R.id.loop_checkbox);

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
	}
}
