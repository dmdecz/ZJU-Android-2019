package com.example.zju_android_2019;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Locale;

public class ClockView extends View {

//	@SuppressLint("HandlerLeak")
//	private Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			invalidate();
//		}
//	};
//	private UpdateThread mUpdateThread;

	private int mMode;
	public static final int CLOCK_MODE_NUMBER = 2;
	public static final int CLOCK_ANALOG_MODE = 0;
	public static final int CLOCK_DIGIT_MODE = 1;
	public static final int CLOCK_DEFAULT_MODE = CLOCK_ANALOG_MODE;

	// Colors
	private static final int FULL_ALPHA = 255;
	private static final int DEFAULT_HOUR_DEGREE_ALPHA = 140;

	// Analog Consts
	private static final int FULL_ANGLE = 360;
	private static final int HOUR_ANGLE = 30;
	private static final int MINUTE_ANGLE = 6;
	private static final int SECOND_ANGLE = 6;
	private static final int STEP_ANGLE = 6;
	private static final float DEFAULT_DEGREE_STROKE_OUTER_RADIUS_RATIO = 0.98f;
	private static final float DEFAULT_DEGREE_STROKE_INNER_RADIUS_RATIO = 0.90f;
	private static final float DEFAULT_DEGREE_STROKE_WIDTH_RATIO = 0.01f;
	private static final float DEFAULT_ANALOG_TEXT_SIZE_RATIO = 0.16f;
	private static final float DEFAULT_ANALOG_NUMBER_CENTER_RADIUS_RATIO = 0.75f;
	private static final float DEFAULT_HOUR_NEEDLE_STROKE_RADIUS_RATIO = 0.30f;
	private static final float DEFAULT_HOUR_NEEDLE_STROKE_WIDTH_RATIO = 0.015f;
	private static final float DEFAULT_MIN_NEEDLE_STROKE_RADIUS_RATIO = 0.50f;
	private static final float DEFAULT_MIN_NEEDLE_STROKE_WIDTH_RATIO = 0.015f;
	private static final float DEFAULT_SEC_NEEDLE_STROKE_RADIUS_RATIO = 0.60f;
	private static final float DEFAULT_SEC_NEEDLE_STROKE_WIDTH_RATIO = 0.01f;
	private static final float DEFAULT_ANALOG_CENTER_STROKE_WIDTH_RATIO = 0.015f;
	private static final float DEFAULT_ANALOG_CENTER_RADIUS_RATIO = 0.05f;
	private static final float DEFAULT_ANALOG_CENTER_RADIUS_INNER_RATIO = 0.03f;

	// Digit Consts
	private static final int AM = 0;
	private static final float DEFAULT_DIGIT_TEXT_SIZE_RATIO = 0.20f;


	private int mWidth, mCenterX, mCenterY, mRadius;
	// Analog Clock Mode
	private int mAnalogNumberColor;
	private float mAnalogTextSizeRatio;
	private float mAnalogNumberCenterRadiusRatio;
	private int mDegreeColor;
	private int mHourDegreeAlpha;
	private int mDegreeAlpha;
	private float mDegreeStrokeOuterRadiusRatio;
	private float mDegreeStrokeInnerRadiusRatio;
	private float mDegreeStrokeWidthRatio;
	private int mHourNeedleColor;
	private float mHourNeedleStrokeWidthRatio;
	private float mHourNeedleStrokeRadiusRatio;
	private int mMinNeedleColor;
	private float mMinNeedleStrokeWidthRatio;
	private float mMinNeedleStrokeRadiusRatio;
	private int mSecNeedleColor;
	private float mSecNeedleStrokeWidthRatio;
	private float mSecNeedleStrokeRadiusRatio;
	private float mAnalogCenterRadiusRatio;
	private int mAnalogCenterColor;
	private float mAnalogCenterStrokeWidthRatio;
	private float mAnalogCenterInnerRadiusRatio;
	private int mAnalogCenterInnerColor;

	// Digit Clock Mode
	private int mDigitNumberColor;
	private float mDigitTextSizeRatio;

	public ClockView(Context context) {
		super(context);
		init();
	}

	public ClockView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		mMode = CLOCK_DEFAULT_MODE;

		mAnalogNumberColor = Color.WHITE;
		mAnalogTextSizeRatio = DEFAULT_ANALOG_TEXT_SIZE_RATIO;
		mAnalogNumberCenterRadiusRatio = DEFAULT_ANALOG_NUMBER_CENTER_RADIUS_RATIO;
		mDegreeColor = Color.WHITE;
		mHourDegreeAlpha = DEFAULT_HOUR_DEGREE_ALPHA;
		mDegreeAlpha = FULL_ALPHA;
		mDegreeStrokeOuterRadiusRatio = DEFAULT_DEGREE_STROKE_OUTER_RADIUS_RATIO;
		mDegreeStrokeInnerRadiusRatio = DEFAULT_DEGREE_STROKE_INNER_RADIUS_RATIO;
		mDegreeStrokeWidthRatio = DEFAULT_DEGREE_STROKE_WIDTH_RATIO;
		mHourNeedleColor = Color.WHITE;
		mMinNeedleColor = Color.WHITE;
		mSecNeedleColor = Color.LTGRAY;
		mHourNeedleStrokeWidthRatio = DEFAULT_HOUR_NEEDLE_STROKE_WIDTH_RATIO;
		mHourNeedleStrokeRadiusRatio = DEFAULT_HOUR_NEEDLE_STROKE_RADIUS_RATIO;
		mMinNeedleStrokeWidthRatio = DEFAULT_MIN_NEEDLE_STROKE_WIDTH_RATIO;
		mMinNeedleStrokeRadiusRatio = DEFAULT_MIN_NEEDLE_STROKE_RADIUS_RATIO;
		mSecNeedleStrokeWidthRatio = DEFAULT_SEC_NEEDLE_STROKE_WIDTH_RATIO;
		mSecNeedleStrokeRadiusRatio = DEFAULT_SEC_NEEDLE_STROKE_RADIUS_RATIO;
		mAnalogCenterRadiusRatio = DEFAULT_ANALOG_CENTER_RADIUS_RATIO;
		mAnalogCenterStrokeWidthRatio = DEFAULT_ANALOG_CENTER_STROKE_WIDTH_RATIO;
		mAnalogCenterColor = Color.WHITE;
		mAnalogCenterInnerRadiusRatio = DEFAULT_ANALOG_CENTER_RADIUS_INNER_RATIO;
		mAnalogCenterInnerColor = Color.LTGRAY;

		mDigitNumberColor = Color.WHITE;
		mDigitTextSizeRatio = DEFAULT_DIGIT_TEXT_SIZE_RATIO;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
		int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

		int size = Math.min(widthWithoutPadding, heightWithoutPadding);
		setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		super.onDraw(canvas);

		mWidth = Math.min(getWidth(), getHeight());

		mCenterX = getWidth() / 2;
		mCenterY = getHeight() / 2;
		mRadius = mWidth / 2;

		drawClock(canvas);
		postInvalidateDelayed(16);
	}

	public int getMode() {
		return mMode;
	}

	public void setMode(int mode) {
		if (mode >= CLOCK_MODE_NUMBER || mode < 0) {
			mode = CLOCK_DEFAULT_MODE;
		}
		mMode = mode;
		invalidate();
	}

	public void setNextMode() {
		mMode ++;
		if (mMode == CLOCK_MODE_NUMBER) {
			mMode = 0;
		}
		invalidate();
	}

	private void drawClock(Canvas canvas) {
		switch (mMode) {
			case CLOCK_ANALOG_MODE:
				drawAnalogClock(canvas);
				break;
			case CLOCK_DIGIT_MODE:
				drawDigitClock(canvas);
				break;
		}
	}

	private void drawAnalogClock(Canvas canvas) {
		drawAnalogDegree(canvas);
		drawAnalogNumber(canvas);
		drawAnalogNeedles(canvas);
		drawAnalogCenter(canvas);
	}

	private void drawAnalogDegree(Canvas canvas) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(mWidth * mDegreeStrokeWidthRatio);
		paint.setColor(mDegreeColor);

		float rPadded = mRadius * mDegreeStrokeOuterRadiusRatio;
		float rEnd = mRadius * mDegreeStrokeInnerRadiusRatio;

		for (int i = 0; i < FULL_ANGLE; i += STEP_ANGLE) {
			if ((i % HOUR_ANGLE) != 0)
				paint.setAlpha(mHourDegreeAlpha);
			else {
				paint.setAlpha(mDegreeAlpha);
			}

			float startX = (float) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
			float startY = (float) (mCenterY - rPadded * Math.sin(Math.toRadians(i)));

			float stopX = (float) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
			float stopY = (float) (mCenterY - rEnd * Math.sin(Math.toRadians(i)));

			canvas.drawLine(startX, startY, stopX, stopY, paint);
		}
	}

	private void drawAnalogNumber(Canvas canvas) {
		Paint textPaint = new Paint();
		textPaint.setTextSize(mRadius * mAnalogTextSizeRatio);
		textPaint.setColor(mAnalogNumberColor);
		textPaint.setTextAlign(Paint.Align.CENTER);

		Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
		float top = fontMetrics.top;
		float bottom = fontMetrics.bottom;
		float differenceBetweenCenterAndBaseLine = (bottom - top) / 2 - bottom;
		float analogNumberCenterRadius = mRadius * mAnalogNumberCenterRadiusRatio;

		for (int i = 0; i < FULL_ANGLE; i += HOUR_ANGLE) {
			int hourNumber = 15 - i / HOUR_ANGLE;
			if (hourNumber > 12) {
				hourNumber -= 12;
			}
			float textCenterX = (float) (mCenterX + analogNumberCenterRadius * Math.cos(Math.toRadians(i)));
			float textCenterY = (float) (mCenterY - analogNumberCenterRadius * Math.sin(Math.toRadians(i)));
			float textBaseLineY = textCenterY + differenceBetweenCenterAndBaseLine;
			canvas.drawText(hourNumber + "", textCenterX, textBaseLineY, textPaint);
		}
	}

	private void drawAnalogNeedles(Canvas canvas) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeCap(Paint.Cap.ROUND);

		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int millisecond = calendar.get(Calendar.MILLISECOND);

		float rPadded = mRadius * mAnalogCenterRadiusRatio;
		float rEnd;
		float startX, startY, stopX, stopY;

		paint.setStrokeWidth(mWidth * mSecNeedleStrokeWidthRatio);
		paint.setColor(mSecNeedleColor);
		float overSecond = millisecond / 1000f;
		float secondNeedleAngle = (second + overSecond) * SECOND_ANGLE;
		rEnd = mRadius * mSecNeedleStrokeRadiusRatio;
		startX = (float) (mCenterX + rPadded * Math.cos(Math.toRadians(90 - secondNeedleAngle)));
		startY = (float) (mCenterY - rPadded * Math.sin(Math.toRadians(90 - secondNeedleAngle)));
		stopX = (float) (mCenterX + rEnd * Math.cos(Math.toRadians(90 - secondNeedleAngle)));
		stopY = (float) (mCenterY - rEnd * Math.sin(Math.toRadians(90 - secondNeedleAngle)));
		canvas.drawLine(startX, startY, stopX, stopY, paint);

		paint.setStrokeWidth(mWidth * mMinNeedleStrokeWidthRatio);
		paint.setColor(mMinNeedleColor);
		float overMinute = (second + overSecond) / 60f;
		float minuteNeedleAngle = (minute + overMinute) * MINUTE_ANGLE;
		rEnd = mRadius * mMinNeedleStrokeRadiusRatio;
		startX = (float) (mCenterX + rPadded * Math.cos(Math.toRadians(90 - minuteNeedleAngle)));
		startY = (float) (mCenterY - rPadded * Math.sin(Math.toRadians(90 - minuteNeedleAngle)));
		stopX = (float) (mCenterX + rEnd * Math.cos(Math.toRadians(90 - minuteNeedleAngle)));
		stopY = (float) (mCenterY - rEnd * Math.sin(Math.toRadians(90 - minuteNeedleAngle)));
		canvas.drawLine(startX, startY, stopX, stopY, paint);

		paint.setStrokeWidth(mWidth * mHourNeedleStrokeWidthRatio);
		paint.setColor(mHourNeedleColor);
		float overHour = (minute + overMinute) / 60f;
		float hourNeedleAngle = (hour + overHour) * HOUR_ANGLE;
		rEnd = mRadius * mHourNeedleStrokeRadiusRatio;
		startX = (float) (mCenterX + rPadded * Math.cos(Math.toRadians(90 - hourNeedleAngle)));
		startY = (float) (mCenterY - rPadded * Math.sin(Math.toRadians(90 - hourNeedleAngle)));
		stopX = (float) (mCenterX + rEnd * Math.cos(Math.toRadians(90 - hourNeedleAngle)));
		stopY = (float) (mCenterY - rEnd * Math.sin(Math.toRadians(90 - hourNeedleAngle)));
		canvas.drawLine(startX, startY, stopX, stopY, paint);
	}

	private void drawAnalogCenter(Canvas canvas) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStrokeCap(Paint.Cap.ROUND);

		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(mWidth * mHourNeedleStrokeWidthRatio);
		paint.setColor(mAnalogCenterInnerColor);
		canvas.drawCircle(mCenterX, mCenterY, mRadius * mAnalogCenterInnerRadiusRatio, paint);

		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(mWidth * mAnalogCenterStrokeWidthRatio);
		paint.setColor(mAnalogCenterColor);
		canvas.drawCircle(mCenterX, mCenterY, mRadius * mAnalogCenterRadiusRatio, paint);
	}

	private void drawDigitClock(Canvas canvas) {
		TextPaint textPaint = new TextPaint();
		textPaint.setTextSize(mWidth * mDigitTextSizeRatio);
		textPaint.setColor(mDigitNumberColor);
		textPaint.setColor(mDigitNumberColor);
		textPaint.setAntiAlias(true);

		Calendar calendar = Calendar.getInstance();

		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int amPm = calendar.get(Calendar.AM_PM);

		String time = String.format("%s:%s:%s%s",
				String.format(Locale.getDefault(), "%02d", hour),
				String.format(Locale.getDefault(), "%02d", minute),
				String.format(Locale.getDefault(), "%02d", second),
				amPm == AM ? "AM" : "PM");

		SpannableStringBuilder spannableString = new SpannableStringBuilder(time);
		spannableString.setSpan(new RelativeSizeSpan(0.3f), spannableString.toString().length() - 2, spannableString.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // se superscript percent

		StaticLayout layout = new StaticLayout(spannableString, textPaint, canvas.getWidth(), Layout.Alignment.ALIGN_CENTER, 1, 1, true);
		canvas.translate(mCenterX - layout.getWidth() / 2f, mCenterY - layout.getHeight() / 2f);
		layout.draw(canvas);
	}

//	public class UpdateThread extends Thread {
//		@Override
//		public void run() {
//			super.run();
//			while (!isInterrupted()) {
//				mHandler.sendEmptyMessage(0);
//				try {
//					Thread.sleep(25);
//				} catch (InterruptedException e) {
//				}
//			}
//		}
//	}

//	@Override
//	protected void onAttachedToWindow() {
//		super.onAttachedToWindow();
//		mUpdateThread = new UpdateThread();
//		mUpdateThread.start();
//	}
//
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getHandler().removeCallbacksAndMessages(null);
	}
}
