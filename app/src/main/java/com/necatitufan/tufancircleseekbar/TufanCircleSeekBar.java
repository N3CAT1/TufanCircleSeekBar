/*
 *  Copyright 2023 Necati TUFAN
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.necatitufan.tufancircleseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class TufanCircleSeekBar extends View
{
//	  <attr name="wheel_stroke_width" format="integer" />
//    <attr name="pointer_radius" format="integer" />
//    <attr name="pointer_halo_radius" format="integer" />
//    <attr name="max" format="integer"></attr>
//    <attr name="init_progress" format="integer"></attr>
//    <attr name="show_text" format="boolean"></attr>
//    <attr name="angle_start" format="integer"></attr>
//    <attr name="angle_end" format="integer"></attr>
//    <attr name="text_size" format="integer"></attr>
//    <attr name="wheel_active_color" format="string"></attr>
//    <attr name="wheel_unactive_color" format="string"></attr>
//    <attr name="wheel_second_active_color" format="string"></attr>
//    <attr name="pointer_color" format="string"></attr>
//    <attr name="pointer_halo_color" format="string"></attr>
//    <attr name="text_color" format="string"></attr>
	
	private int mWheelStrokeWidth;
	private int mPointerRadius;
	private int mPointerHaloRadius;
	private int mMax;
	private int mInitProgress;
	private boolean mShowText;
	private int mAngleStart;
	private int mAngleEnd;
	private int mTextSize;
	private Paint mWheelActivePaint;
	private Paint mWheelUnactivePaint;
	private Paint mWheelSecondActivePaint;
	private Paint mPointerPaint;
	private Paint mPointerHaloPaint;
	private Paint mTextPaint;
	private float mTranslationOffset;
	private RectF mWheelRectangle = new RectF();
	private Rect mBounds = new Rect();
	private float mWheelRadius;
	private float mAngle;
	private float mSweepAngle = 0;
	private float mSecondSweepAngle = 0;
	private int mProgress;
	private int mSecondProgress = 0;
	private boolean mUserIsMovingPointer = false;
	private OnProgressChangeListener mOnProgressChangeListener;
	private OnSecondProgressChangeListener mOnSecondProgressChangeListener;
	private OnStartTrackingTouchListener mOnStartTrackingTouchListener;
	private OnStopTrackingTouchListener mOnStopTrackingTouchListener;
	
	private static final String STATE_PARENT = "parent";
	private static final String STATE_ANGLE = "angle";
	private static final String STATE_PROGRESS = "progress";
	private static final String STATE_SECOND_PROGRESS = "second_progress";
    
	public TufanCircleSeekBar(Context context)
	{
		super(context);
		init(null, 0);
	}
	public TufanCircleSeekBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(attrs, 0);
	}

	public TufanCircleSeekBar(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}
	
	private void init(AttributeSet attrs, int defStyle)
	{
		final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TufanCircleSeekBar, defStyle, 0);
		DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
		
		mWheelStrokeWidth = a.getInteger(R.styleable.TufanCircleSeekBar_wheel_stroke_width, 8);
		mWheelStrokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mWheelStrokeWidth, displayMetrics);
		
		mPointerRadius = a.getInt(R.styleable.TufanCircleSeekBar_pointer_radius, 8);
		mPointerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPointerRadius, displayMetrics);
		
		mPointerHaloRadius = a.getInt(R.styleable.TufanCircleSeekBar_pointer_halo_radius, 12);
		mPointerHaloRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPointerHaloRadius, displayMetrics);
		
		mMax = a.getInt(R.styleable.TufanCircleSeekBar_max, 100);
		mInitProgress = a.getInteger(R.styleable.TufanCircleSeekBar_init_progress, 0);
		mShowText = a.getBoolean(R.styleable.TufanCircleSeekBar_show_text, true);
		
		mAngleStart = a.getInt(R.styleable.TufanCircleSeekBar_angle_start, 0);
		mAngleStart = mAngleStart - 90;
		
		mAngleEnd = a.getInt(R.styleable.TufanCircleSeekBar_angle_end, 360);
		mAngleEnd = mAngleEnd - 90;
		
		mTextSize = a.getInt(R.styleable.TufanCircleSeekBar_text_size, 20);
		mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, displayMetrics);
		
		String colorCode = a.getString(R.styleable.TufanCircleSeekBar_wheel_active_color);
		
		mWheelActivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mWheelActivePaint.setStyle(Paint.Style.STROKE);
		mWheelActivePaint.setStrokeWidth(mWheelStrokeWidth);
		mWheelActivePaint.setColor(getColorFromColorString(colorCode, Color.CYAN));
		
		colorCode = a.getString(R.styleable.TufanCircleSeekBar_wheel_unactive_color);
		
		mWheelUnactivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mWheelUnactivePaint.setStyle(Paint.Style.STROKE);
		mWheelUnactivePaint.setStrokeWidth(mWheelStrokeWidth);
		mWheelUnactivePaint.setColor(getColorFromColorString(colorCode, Color.WHITE));
		
		colorCode = a.getString(R.styleable.TufanCircleSeekBar_wheel_second_active_color);
		
		mWheelSecondActivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mWheelSecondActivePaint.setStyle(Paint.Style.STROKE);
		mWheelSecondActivePaint.setStrokeWidth(mWheelStrokeWidth);
		mWheelSecondActivePaint.setColor(getColorFromColorString(colorCode, Color.MAGENTA));
		
		colorCode = a.getString(R.styleable.TufanCircleSeekBar_pointer_color);
		
		mPointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPointerPaint.setStrokeWidth(mPointerRadius);
		mPointerPaint.setColor(getColorFromColorString(colorCode, Color.BLUE));
		
		colorCode = a.getString(R.styleable.TufanCircleSeekBar_pointer_halo_color);
		
		mPointerHaloPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPointerHaloPaint.setStrokeWidth(mPointerRadius);
		mPointerHaloPaint.setColor(getColorFromColorString(colorCode, Color.DKGRAY));
		
		colorCode = a.getString(R.styleable.TufanCircleSeekBar_text_color);
		
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
		mTextPaint.setStyle(Style.FILL_AND_STROKE);
		mTextPaint.setTextAlign(Align.LEFT);
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setColor(getColorFromColorString(colorCode, Color.RED));
		
		a.recycle();
		
		if(mInitProgress >= mMax)
			mSweepAngle = mAngleEnd - mAngleStart;
		else
			mSweepAngle = ((mAngleEnd - mAngleStart) / mMax) * mInitProgress;
		
		mProgress = mInitProgress;
		
		mAngle = mAngleStart + mSweepAngle;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.translate(mTranslationOffset, mTranslationOffset);
		
		canvas.drawArc(mWheelRectangle, mAngleStart, (mAngleEnd - mAngleStart), false, mWheelUnactivePaint);
		
		canvas.drawArc(mWheelRectangle, mAngleStart, mSecondSweepAngle, false, mWheelSecondActivePaint);
		
		canvas.drawArc(mWheelRectangle, mAngleStart, mSweepAngle, false, mWheelActivePaint);
		
		float[] pointerPosition = calculatePointerPosition(mAngle);
		
		canvas.drawCircle(pointerPosition[0], pointerPosition[1], mPointerHaloRadius, mPointerHaloPaint);
		
		canvas.drawCircle(pointerPosition[0], pointerPosition[1], mPointerRadius, mPointerPaint);
		
		if(mShowText)
		{
			String textProgress = Integer.toString(mProgress);
			mTextPaint.getTextBounds(textProgress, 0, textProgress.length(), mBounds);
			canvas.drawText(
					textProgress, 
					(mWheelRectangle.centerX()) - (mTextPaint.measureText(textProgress) / 2),
					mWheelRectangle.centerY() + mBounds.height() / 2,
					mTextPaint);
		}
		
		super.onDraw(canvas);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		int min = Math.min(width, height);
		setMeasuredDimension(min, min);

		mTranslationOffset = min * 0.5f;
		
		mWheelRadius = mTranslationOffset - mPointerHaloRadius - mWheelStrokeWidth;

		mWheelRectangle.set(-mWheelRadius, -mWheelRadius, mWheelRadius, mWheelRadius);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		getParent().requestDisallowInterceptTouchEvent(true);

		// Convert coordinates to our internal coordinate system
		float x = event.getX() - mTranslationOffset;
		float y = event.getY() - mTranslationOffset;
		
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				// Check whether the user pressed on the pointer.
				float[] pointerPosition = calculatePointerPosition(mAngle);
				if (x >= (pointerPosition[0] - mPointerHaloRadius)
						&& x <= (pointerPosition[0] + mPointerHaloRadius)
						&& y >= (pointerPosition[1] - mPointerHaloRadius)
						&& y <= (pointerPosition[1] + mPointerHaloRadius))
				{
					mUserIsMovingPointer = true;
					
					invalidate();
					
					if(mOnStartTrackingTouchListener != null)
						mOnStartTrackingTouchListener.onStartTrackingTouch(this);
				}
				// If user did not press pointer or center, report event not handled
				else
				{
					getParent().requestDisallowInterceptTouchEvent(false);
					return false;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (mUserIsMovingPointer)
				{
					mAngle = (float) java.lang.Math.atan2(y, x);
					mAngle = (float) Math.toDegrees(mAngle);
					
					
					if(mAngle < 0 && x < 0)
						mAngle = 360f + mAngle;
					
					if(mAngle < mAngleStart)
						mAngle = mAngleStart;
					
					if(mAngle > mAngleEnd)
						mAngle = mAngleEnd;
					
					mSweepAngle = mAngle - mAngleStart;
					
					mProgress = (int) (mSweepAngle / (mAngleEnd - mAngleStart) * mMax);
					
					invalidate();
					
					if (mOnProgressChangeListener != null)
						mOnProgressChangeListener.onProgressChanged(this, mProgress, true);
				}
				// If user did not press pointer or center, report event not handled
				else
				{
					getParent().requestDisallowInterceptTouchEvent(false);
					return false;
				}
				break;
			case MotionEvent.ACTION_UP:
				mUserIsMovingPointer = false;
				
				invalidate();
				
				if(mOnStopTrackingTouchListener != null)
					mOnStopTrackingTouchListener.onStopTrackingTouch(this);
				break;
		}

		return true;
	}
	
	@Override
	protected Parcelable onSaveInstanceState()
	{
		Parcelable superState = super.onSaveInstanceState();

		Bundle state = new Bundle();
		state.putParcelable(STATE_PARENT, superState);
		state.putFloat(STATE_ANGLE, mAngle);
		state.putInt(STATE_PROGRESS, mProgress);
		state.putInt(STATE_SECOND_PROGRESS, mSecondProgress);

		return state;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state)
	{
		Bundle savedState = (Bundle) state;

		Parcelable superState = savedState.getParcelable(STATE_PARENT);
		super.onRestoreInstanceState(superState);

		mAngle = savedState.getFloat(STATE_ANGLE);
		mProgress = savedState.getInt(STATE_PROGRESS);
		mSecondProgress = savedState.getInt(STATE_PROGRESS);
		mSweepAngle = ((mAngleEnd - mAngleStart) / (float) mMax) * mProgress;
		mSecondSweepAngle = ((mAngleEnd - mAngleStart) / 100f) * mSecondProgress;
	}
	
	/**
	 * Calculate the pointer's coordinates on the color wheel using the supplied
	 * angle.
	 * 
	 * @param angle
	 *            The position of the pointer expressed as angle (in rad).
	 * 
	 * @return The coordinates of the pointer's center in our internal
	 *         coordinate system.
	 */
	private float[] calculatePointerPosition(float angle)
	{
		float radian = (float) Math.toRadians(angle);
		
		float x = (float) (mWheelRadius * Math.cos(radian));
		float y = (float) (mWheelRadius * Math.sin(radian));

		return new float[] { x, y };
	}
	
	private int getColorFromColorString(String colorString, int alternativeColor)
	{
		if(colorString == null)
			return alternativeColor;
		
		int color = alternativeColor;
		
		try
		{
			color = Color.parseColor(colorString);
		}
		catch(IllegalArgumentException e)
		{
			color = alternativeColor;
		}
		
		return color;
	}
	
	public int getProgress()
	{
		return mProgress;
	}
	
	public int getSecondProgress()
	{
		return mSecondProgress;
	}
	
	public void setProgress(int progress)
	{
		if(progress <= 0)
			progress = 0;
		else if(progress >= mMax)
			progress = mMax;
		
		mProgress = progress;
		
		mSweepAngle = ((mAngleEnd - mAngleStart) / (float) mMax) * progress;
		
		mAngle = mAngleStart + mSweepAngle;
		
		invalidate();
		
		if (mOnProgressChangeListener != null)
			mOnProgressChangeListener.onProgressChanged(this, mProgress, true);
	}
	
	public void setSecondProgress(int progress)
	{
		if(progress <= 0)
			progress = 0;
		else if(progress >= 100)
			progress = 100;
		
		mSecondProgress = progress;
		
		mSecondSweepAngle = ((mAngleEnd - mAngleStart) / 100f) * progress;
		
		invalidate();
		
		if(mOnSecondProgressChangeListener != null)
			mOnSecondProgressChangeListener.onSecondProgressChanged(this, mSecondProgress, true);
	}
	
	public void setOnProgressChangeListener(OnProgressChangeListener listener)
	{
		mOnProgressChangeListener = listener;
	}
	
	public void setOnSecondProgressChangeListener(OnSecondProgressChangeListener listener)
	{
		mOnSecondProgressChangeListener = listener;
	}
	
	public void setOnStartTrackingTouchListener(OnStartTrackingTouchListener listener)
	{
		mOnStartTrackingTouchListener = listener;
	}
	
	public void setOnStopTrackingTouchListener(OnStopTrackingTouchListener listener)
	{
		mOnStopTrackingTouchListener = listener;
	}

	public interface OnProgressChangeListener
	{
		public abstract void onProgressChanged(TufanCircleSeekBar seekBar, int progress, boolean fromUser);
	}
	
	public interface OnSecondProgressChangeListener
	{
		public abstract void onSecondProgressChanged(TufanCircleSeekBar seekBar, int progress, boolean fromUser);
	}
	
	public interface OnStartTrackingTouchListener
	{
		public abstract void onStartTrackingTouch(TufanCircleSeekBar seekbar);
	}
	
	public interface OnStopTrackingTouchListener
	{
		public abstract void onStopTrackingTouch(TufanCircleSeekBar seekbar);
	}
}
