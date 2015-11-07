package com.xiaokai.sectionview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2015/10/24.
 */
public class SectionView extends View implements View.OnTouchListener {

	private char[] mChars = new char[26];
	private int mHeight;
	private int mTextSize = 12;// 单位sp
	private int mTextColor = 0xff919191;// 默认字体颜色
	private int mSelectedColor = 0xffffffff;// 选中字体颜色
	private int mTextHeight;
	private int mBg = 0xff000000;// 背景
	private Paint mPaint;

	private int mDownY, mMoveY;
	private int mPosition = -1;
	private OnSelectChangeListener mOnSelectChageListener;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	public interface OnSelectChangeListener {
		void onHandleSelected(int section, String s);

		void onHandleComplete();
	}

	public SectionView(Context context) {
		this(context, null);
	}

	public SectionView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("ClickableViewAccessibility")
	public SectionView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setOnTouchListener(this);
		for (int i = 'A'; i <= 'Z'; i++) {
			mChars[i - 'A'] = (char) i;
		}
		mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				mTextSize, context.getResources().getDisplayMetrics());
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.SectionView);
		mTextSize = (int) ta.getDimension(R.styleable.SectionView_textsize,
				mTextSize);
		mTextColor = ta.getColor(R.styleable.SectionView_textcolor, mTextColor);
		mSelectedColor = ta.getColor(R.styleable.SectionView_selectedcolor,
				mSelectedColor);
		mBg = ta.getColor(R.styleable.SectionView_bg, mBg);
		ta.recycle();

		mPaint = new Paint();
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(3);
		mPaint.setTextSize(mTextSize);
		setBackgroundColor(mBg);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mHeight = getHeight();

		Paint.FontMetrics fm = mPaint.getFontMetrics();
		mTextHeight = (int) Math.ceil(fm.bottom - fm.top);

		int x = getWidth() / 2;
		int y = 0;
		for (int i = 0; i < mChars.length; i++) {
			y = mHeight / (2 * mChars.length) + mTextHeight / 4 + i
					* (mHeight / mChars.length);
			if (i != mPosition) {
				mPaint.setColor(mTextColor);
			} else {
				mPaint.setColor(mSelectedColor);
			}
			canvas.drawText("" + mChars[i], x, y, mPaint);
		}
	}

	public void setOnSelectChageListener(
			OnSelectChangeListener onSelectChageListener) {
		this.mOnSelectChageListener = onSelectChageListener;
	}

	private int getPositionByY(int y) {
		int pos = y / (mHeight / mChars.length);
		if (pos < 0) {
			return 0;
		}
		if (pos > mChars.length - 1) {
			return mChars.length - 1;
		}
		return pos;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int y = (int) event.getY();

		int pos = getPositionByY(y);
		int section = (int) mChars[pos];
		String s = "" + mChars[pos];

		if (mOnSelectChageListener != null) {
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				if (mPosition != pos) {
					mDownY = (int) event.getY();
					mOnSelectChageListener.onHandleSelected(section, s);
					mPosition = pos;
					invalidate();
				}
				break;

			case MotionEvent.ACTION_MOVE:
				mMoveY = (int) event.getY();
				if (Math.abs(mMoveY - mDownY) > 5) {
					if (mPosition != pos) {
						mPosition = pos;
						invalidate();
						mOnSelectChageListener.onHandleSelected(section, s);
					}
				}
				break;

			case MotionEvent.ACTION_UP:
				mOnSelectChageListener.onHandleComplete();
				mPosition = -1;
				invalidate();
				break;
			}
		}
		return true;
	}
}
