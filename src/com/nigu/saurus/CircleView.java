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
	private static String BACKGROUND = "#F2E6D0";
	private String color = NORMAL_COLOR;
	private boolean activated = false;
	private boolean fake = false;
	private boolean example = false;

	public CircleView(Context context) {
		super(context);
	}
	
	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onDraw(Canvas canvas) {	
		if (example) {
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.parseColor(BACKGROUND));
			canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 3, paint);
			paint.setColor(0x88000000);
			canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 3, paint);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.emptyCircle));
		}
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
	
//	public void toggle() {
//		if (activated) {
//			color = NORMAL_COLOR;
//			activated = false;
//		} else {
//			color = ACTIVE_COLOR;
//			activated = true;
//		}
//		invalidate();
//	}
	
	public void setTheme(String normal, String active, String background) {
		NORMAL_COLOR = normal;
		ACTIVE_COLOR = active;
		BACKGROUND = background;
		color = normal;
		invalidate();
	}
	
	public void changeToFake() {
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.emptyCircle));
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
	
	public void fakeToActive() {
		paint.setStyle(Paint.Style.FILL);
		color = ACTIVE_COLOR;
		fake = false;
		invalidate();
	}
	
	public boolean fake() {
		return fake;
	}
	
	public void changeToFakeExample() {
		color = ACTIVE_COLOR;
		example = true;
		invalidate();
	}
}
