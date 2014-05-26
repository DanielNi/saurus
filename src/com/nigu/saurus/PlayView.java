package com.nigu.saurus;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class PlayView extends View {
	
	private final Paint paint = new Paint();
	private Path path = new Path();
	private boolean pressed = false;

	public PlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		Resources res = getResources();
		int width = getWidth();
		int height = getHeight();
		if (pressed) {
			Bitmap refresh = BitmapFactory.decodeResource(res, R.drawable.refresh_pressed);
			canvas.drawBitmap(refresh, (width-refresh.getWidth())/2, (height-refresh.getHeight())/2, paint);
		} else {
			Bitmap refresh = BitmapFactory.decodeResource(res, R.drawable.refresh);
			canvas.drawBitmap(refresh, (width-refresh.getWidth())/2, (height-refresh.getHeight())/2, paint);
//			path.moveTo((width) / 2 - (width / 6), (height) / 2 - (height / 4));
//			path.lineTo((width) / 2 - (width / 6), (height) / 2 + (height / 4));
//			path.lineTo((width) / 2 + (width / 6), (height) / 2);
//			path.lineTo((width) / 2 - (width / 6), (height) / 2 - (height / 4));
//			
//			canvas.drawPath(path, paint);
		}

	}
	
	public void pressed() {
		pressed = true;
		invalidate();
	}
	
	public void refresh() {
		pressed = false;
		invalidate();
	}
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//	    	color = ACTIVE_COLOR;
//	    	increaseScore();
//	    	invalidate();
//	    	ViewGroup v = (ViewGroup) this.getParent().getParent().getParent();
//	    	ScoreView sv = (ScoreView) v.findViewById(R.id.Score);
//	    	sv.invalidate();
//	    	return true;
//	    } else if (event.getAction() == MotionEvent.ACTION_UP) {
//	    	color = NORMAL_COLOR;
//	    	invalidate();
//	    	return true;
//	    } else {
//	    	return false;
//	    }
//	}

//	private Paint paint;
//	private Path path;
//	
//	public MenuView() {
//		paint = new Paint();
//		paint.setColor(Color.parseColor("#E3CA94"));
//		path = new Path();
//	}
//	
//	public void draw(Canvas canvas, int lBound, int rBound, int tBound, int bBound) {
////		canvas.drawCircle((lBound + rBound) / 2, (tBound + bBound) / 2, 100, paint);
//		path.moveTo((lBound + rBound) / 2 - 50, (tBound + bBound) / 2 - 50);
//		path.lineTo((lBound + rBound) / 2 - 50, (tBound + bBound) / 2 + 50);
//		path.lineTo((lBound + rBound) / 2 + 50, (tBound + bBound) / 2);
//		path.lineTo((lBound + rBound) / 2 - 50, (tBound + bBound) / 2 - 50);
//		
//		canvas.drawPath(path, paint);
//		
//	}
}
