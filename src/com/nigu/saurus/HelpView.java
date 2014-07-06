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
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class HelpView extends View {

	private final Paint paint = new Paint();
	private static int COLOR = 0xE3CA94;

	public HelpView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
//		paint.setColor(Color.parseColor(COLOR));
//		int scaledSize = getResources().getDimensionPixelSize(R.dimen.helpFontSize);
//		paint.setTextSize(scaledSize);
//		paint.setTextAlign(Align.CENTER);
//		paint.setTypeface(Typeface.DEFAULT_BOLD);
//		canvas.drawText("?", getWidth() / 2, (int) ((getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)), paint);
		Resources res = getResources();
		int width = getWidth();
		int height = getHeight();
		setPaintColor(COLOR, paint);
		Bitmap menu = BitmapFactory.decodeResource(res, R.drawable.help);
		canvas.drawBitmap(menu, (width-menu.getWidth())/2, (height-menu.getHeight())/2, paint);
		
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
	
	public void setTheme(String color) {
		COLOR = Integer.decode(color);
		invalidate();
	}
}
