package com.nigu.saurus;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PlayView extends View {
	
	private boolean pressed = false;
	private int COLOR = 0xE3CA94;
	private int COLOR_PRESSED = 0x887959;
	private int color = COLOR;
	
	public PlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		Resources res = getResources();
		Paint paint = new Paint();
		int width = getWidth();
		int height = getHeight();
		if (pressed) {
			color = COLOR_PRESSED;
//			Bitmap refresh = BitmapFactory.decodeResource(res, R.drawable.refresh_pressed);
//			canvas.drawBitmap(refresh, (width-refresh.getWidth())/2, (height-refresh.getHeight())/2, paint);
		} else {
			color = COLOR;
		}
		setPaintColor(color, paint);
		Bitmap refresh = BitmapFactory.decodeResource(res, R.drawable.refresh);
		canvas.drawBitmap(refresh, (width-refresh.getWidth())/2, (height-refresh.getHeight())/2, paint);
//			path.moveTo((width) / 2 - (width / 6), (height) / 2 - (height / 4));
//			path.lineTo((width) / 2 - (width / 6), (height) / 2 + (height / 4));
//			path.lineTo((width) / 2 + (width / 6), (height) / 2);
//			path.lineTo((width) / 2 - (width / 6), (height) / 2 - (height / 4));
//			
//			canvas.drawPath(path, paint);

	}
	
	public void pressed() {
		pressed = true;
		invalidate();
	}
	
	public void refresh() {
		pressed = false;
		invalidate();
	}
	
	private void setPaintColor(int color, Paint paint) {
		float r = (float) Color.red(color);
		float g = (float) Color.green(color);
		float b = (float) Color.blue(color);
		
		float[] colorTransform =
			{       
				0	 , 0    , 0    , 0, r,  // R color
				0    , 0	, 0    , 0, g,  // G color
				0    , 0    , 0	   , 0, b,  // B color
				0    , 0    , 0    , 1, 0
			};
		
		ColorMatrix colorMatrix = new ColorMatrix();
	    colorMatrix.setSaturation(0f); // Remove colour 
	    colorMatrix.set(colorTransform);
	    
	    ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
	    paint.setColorFilter(colorFilter);
	}
	
	public void setTheme(String colorNew, String colorPressed) {
		COLOR = Integer.decode(colorNew);
		COLOR_PRESSED = Integer.decode(colorPressed);
		color = COLOR;
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
