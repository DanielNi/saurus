package com.nigu.saurus;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class MenuView extends View {

	private final Paint paint = new Paint();
	private Path path = new Path();
	private boolean pressed = false;

	public MenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		paint.setColor(Color.parseColor("#E3CA94"));
		int width = getWidth();
		int height = getHeight();
		if (pressed) {
			
		} else {
			Resources res = getResources();
			Bitmap menu = BitmapFactory.decodeResource(res, R.drawable.menu);
			canvas.drawBitmap(menu, (width-menu.getWidth())/2, (height-menu.getHeight())/2, paint);
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
	
}
