package com.nigu.saurus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class HelpView extends View {

	private final Paint paint = new Paint();
	private static String COLOR = "#E3CA94";

	public HelpView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		paint.setColor(Color.parseColor(COLOR));
		int scaledSize = getResources().getDimensionPixelSize(R.dimen.helpFontSize);
		paint.setTextSize(scaledSize);
		paint.setTextAlign(Align.CENTER);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText("?", getWidth() / 2, (int) ((getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)), paint);
		
	}
	
	public void setTheme(String color) {
		COLOR = color;
		invalidate();
	}
	
}
