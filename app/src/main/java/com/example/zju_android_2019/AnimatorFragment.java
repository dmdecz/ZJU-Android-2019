package com.example.zju_android_2019;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

public class AnimatorFragment extends Fragment {

	private View target;
	private View startColorPicker;
	private View endColorPicker;
	private Button durationSelector;
	private AnimatorSet animatorSet;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_animator, container, false);

		target = view.findViewById(R.id.target);
		startColorPicker = view.findViewById(R.id.start_color_picker);
		endColorPicker = view.findViewById(R.id.end_color_picker);
		durationSelector = view.findViewById(R.id.duration_selector);

		startColorPicker.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorPicker picker = new ColorPicker(getActivity());
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
				ColorPicker picker = new ColorPicker(getActivity());
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
				new MaterialDialog.Builder(getContext())
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
		return view;
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
			Toast.makeText(getContext(), R.string.invalid_duration, Toast.LENGTH_LONG).show();
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
				"alpha", 1f, 0.25f);
		animatorAlpha.setDuration(Integer.parseInt(durationSelector.getText().toString()));
		animatorAlpha.setRepeatCount(ObjectAnimator.INFINITE);
		animatorAlpha.setRepeatMode(ObjectAnimator.REVERSE);

		animatorSet = new AnimatorSet();
		animatorSet.playTogether(animator1, animatorScaleX, animatorScaleY, animatorAlpha);
		animatorSet.start();
	}
}
