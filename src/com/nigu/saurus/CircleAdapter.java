package com.nigu.saurus;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CircleAdapter extends BaseAdapter {
	
	private Context context;
	
	public CircleAdapter(Context c) {
		this.context = c;
	}

	@Override
	public int getCount() {
		return 16;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CircleView circleView;
		if (convertView == null) {
			circleView = new CircleView(context);
			
		} else {
			circleView = (CircleView) convertView;
		}
		
		return circleView;
	}

	
}
