package com.nigu.saurus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;

public class ScoreView extends View {
	
	private final Paint paint = new Paint();

	public ScoreView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		int x = getWidth();
		int y = getHeight();
		int scaledSize = getResources().getDimensionPixelSize(R.dimen.fontSize);
		paint.setColor(Color.parseColor("#F29D85"));
		paint.setTextSize(scaledSize);
		paint.setTextAlign(Align.CENTER);
		
		String score = Integer.toString(CircleView.getScore());
		String best = Integer.toString(CircleView.getBest());
		
//		String current = Integer.toString(score);
//		String top = Integer.toString(best);
		
		canvas.drawText("score", x/4, y/3, paint);
		canvas.drawText(score, x/4, 2*y/3, paint);
		canvas.drawText("best", 3*x/4, y/3, paint);
		canvas.drawText(best, 3*x/4, 2*y/3, paint);
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
		int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
		
		setMeasuredDimension(parentWidth, parentHeight);
	}

//	private Paint paint;
//	private DisplayMetrics metrics;
//	private int score;
//	
//	public ScoreView() {
//		paint = new Paint();
//		paint.setColor(Color.parseColor("#F29D85"));
//		paint.setTextSize(90);
//		paint.setTextAlign(Align.CENTER);
//		metrics = new DisplayMetrics();
//	}
//	
//	public void draw(Canvas canvas, int lBound, int rBound, int tBound, int bBound) {
////		float y = metrics.ydpi * 1.5;
////	    float x = (float) metrics.xdpi * 1.5;
////	    canvas.drawText("score", x, y, paint);
//		canvas.drawText("score", (lBound+rBound)/4, (tBound+bBound)/3, paint);
//		canvas.drawText("0", (lBound+rBound)/4, 2*(tBound+bBound)/3, paint);
//		canvas.drawText("top", 3*(lBound+rBound)/4, (tBound+bBound)/3, paint);
//		canvas.drawText("0", 3*(lBound+rBound)/4, 2*(tBound+bBound)/3, paint);
////		canvas.drawLine(lBound, bBound, rBound, bBound, paint);
//	}

}
