package com.nigu.saurus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CircleView extends View {
	
	private final Paint paint = new Paint();
	private static String NORMAL_COLOR = "#ABDEC9";
	private static String ACTIVE_COLOR = "#E8747F";
	private String color = NORMAL_COLOR;
	private boolean activated = false;
	private boolean fake = false;

	public CircleView(Context context) {
		super(context);
	}
	
	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onDraw(Canvas canvas) {	
		paint.setColor(Color.parseColor(color));
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 3, paint);
	}
	
	public void changeToActive() {
		activated = true;
    	color = ACTIVE_COLOR;
    	invalidate();
	}
	
	public void changeToNormal() {
		activated = false;
		color = NORMAL_COLOR;
		invalidate();
	}
	
	public boolean activated() {
		return activated;
	}
	
	public void toggle() {
		if (activated) {
			color = NORMAL_COLOR;
			activated = false;
		} else {
			color = ACTIVE_COLOR;
			activated = true;
		}
		invalidate();
	}
	
	public void setTheme(String normal, String active) {
		NORMAL_COLOR = normal;
		ACTIVE_COLOR = active;
		color = normal;
		invalidate();
	}
	
	public void changeToFake() {
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(10);
		color = ACTIVE_COLOR;
		fake = true;
		invalidate();
	}

	public void fakeToNormal() {
		paint.setStyle(Paint.Style.FILL);
		color = NORMAL_COLOR;
		fake = false;
		invalidate();
	}
	
	public boolean fake() {
		return fake;
	}
}
