package com.example.zju_android_2019;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;


public class MainActivity extends AppCompatActivity {

	private View target;
	private View startColorPicker;
	private View endColorPicker;
	private Button durationSelector;
	private AnimatorSet animatorSet;
	private Button mNext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		target = findViewById(R.id.target);
		startColorPicker = findViewById(R.id.start_color_picker);
		endColorPicker = findViewById(R.id.end_color_picker);
		durationSelector = findViewById(R.id.duration_selector);

		mNext = findViewById(R.id.next);
		mNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, LottieActivity.class));
			}
		});

		startColorPicker.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorPicker picker = new ColorPicker(MainActivity.this);
				picker.setColor(getBackgroundColor(startColorPicker));
				picker.enableAutoClose();
				picker.setCallback(new ColorPickerCallback() {
					@Override
					public void onColorChosen(int color) {
						onStartColorChanged(color);
					}
				});
				picker.show();
			}
		});

		endColorPicker.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorPicker picker = new ColorPicker(MainActivity.this);
				picker.setColor(getBackgroundColor(endColorPicker));
				picker.enableAutoClose();
				picker.setCallback(new ColorPickerCallback() {
					@Override
					public void onColorChosen(int color) {
						onEndColorChanged(color);
					}
				});
				picker.show();
			}
		});

		durationSelector.setText(String.valueOf(1000));
		durationSelector.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new MaterialDialog.Builder(MainActivity.this)
						.inputType(InputType.TYPE_CLASS_NUMBER)
						.input(getString(R.string.duration_hint), durationSelector.getText(), new MaterialDialog.InputCallback() {
							@Override
							public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
								onDurationChanged(input.toString());
							}
						})
						.show();
			}
		});
		resetTargetAnimation();
	}

	private void onStartColorChanged(int color) {
		startColorPicker.setBackgroundColor(color);
		resetTargetAnimation();
	}

	private void onEndColorChanged(int color) {
		endColorPicker.setBackgroundColor(color);
		resetTargetAnimation();
	}

	private void onDurationChanged(String input) {
		boolean isValid = true;
		try {
			int duration = Integer.parseInt(input);
			if (duration < 100 || duration > 10000) {
				isValid = false;
			}
		} catch (Throwable e) {
			isValid = false;
		}
		if (isValid) {
			durationSelector.setText(input);
			resetTargetAnimation();
		} else {
			Toast.makeText(MainActivity.this, R.string.invalid_duration, Toast.LENGTH_LONG).show();
		}
	}

	private int getBackgroundColor(View view) {
		Drawable bg = view.getBackground();
		if (bg instanceof ColorDrawable) {
			return ((ColorDrawable) bg).getColor();
		}
		return Color.WHITE;
	}

	private void resetTargetAnimation() {
		if (animatorSet != null) {
			animatorSet.cancel();
		}

		// 在这里实现了一个 ObjectAnimator，对 target 控件的背景色进行修改
		// 可以思考下，这里为什么要使用 ofArgb，而不是 ofInt 呢？
		ObjectAnimator animator1 = ObjectAnimator.ofArgb(target,
				"backgroundColor",
				getBackgroundColor(startColorPicker),
				getBackgroundColor(endColorPicker));
		animator1.setDuration(Integer.parseInt(durationSelector.getText().toString()));
		animator1.setRepeatCount(ObjectAnimator.INFINITE);
		animator1.setRepeatMode(ObjectAnimator.REVERSE);

		ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(target,
				"scaleX", 1f, 2f);
		animatorScaleX.setDuration(Integer.parseInt(durationSelector.getText().toString()));
		animatorScaleX.setRepeatCount(ObjectAnimator.INFINITE);
		animatorScaleX.setRepeatMode(ObjectAnimator.REVERSE);

		ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(target,
				"scaleY", 1f, 2f);
		animatorScaleY.setDuration(Integer.parseInt(durationSelector.getText().toString()));
		animatorScaleY.setRepeatCount(ObjectAnimator.INFINITE);
		animatorScaleY.setRepeatMode(ObjectAnimator.REVERSE);

		ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(target,
				"alpha", 1f, 0.0f);
		animatorAlpha.setDuration(Integer.parseInt(durationSelector.getText().toString()));
		animatorAlpha.setRepeatCount(ObjectAnimator.INFINITE);
		animatorAlpha.setRepeatMode(ObjectAnimator.REVERSE);

		animatorSet = new AnimatorSet();
		animatorSet.playTogether(animator1, animatorScaleX, animatorScaleY, animatorAlpha);
		animatorSet.start();
	}
}
