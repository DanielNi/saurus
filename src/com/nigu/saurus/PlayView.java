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
	private static int COLOR = 0xE3CA94;
	private static int COLOR_PRESSED = 0x887959;
	private int color = COLOR;
	private final Paint paint = new Paint();
	private Resources res = getResources();
	private Bitmap refresh = BitmapFactory.decodeResource(res, R.drawable.play);
	
	public PlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();
		if (pressed) {
			color = COLOR_PRESSED;
		} else {
			color = COLOR;
		}
		setPaintColor(color, paint);
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
}
