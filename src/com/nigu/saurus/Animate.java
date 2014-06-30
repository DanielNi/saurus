package com.nigu.saurus;

import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class Animate extends Thread {
	
	private List<CircleView> activated;
	private List<CircleView> circles;
	private Handler handler;
	private static boolean keepGoing;
	
	private int SPEED;
	private int GET_READY;
	
	private int numCircles;
	private int fake;
	
	public Animate(Handler handler, List<CircleView> circles, List<CircleView> activated) {
		this.handler = handler;
		this.activated = activated;
		this.circles = circles;
		keepGoing = true;
		
		SPEED = 250;
		GET_READY = 500;
		
		numCircles = 0;
	}
	
	@Override
	public void run() {
		 // Moves the current Thread into the background
        //android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		try {
			sleep(GET_READY);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    	while (keepGoing) {
    		if (activated.size() > 2) {
    			new GameOver(handler).start();
    			return;
    		}
    		
    		calculateSpeed();
    		highlightRandom();
    		
    		try {
				sleep(SPEED);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
	}
	
    public void highlightRandom() {
    	int size = circles.size();
    	int rand = new Random().nextInt(size - 1); // don't want to highlight the last circle pressed immediately afterwards
    	Message msg = handler.obtainMessage();
    	Bundle b = new Bundle();
    	if (numCircles > 50 && numCircles % 30 == 0) {
    		b.putInt("fake", rand);
        	fake = numCircles;
    	} else if (numCircles > 50 && fake + 10 == numCircles) {
    		b.putInt("stop", (byte) 1);
    	} else {
        	b.putInt("index", rand);
    	}
    	msg.setData(b);
    	msg.sendToTarget();
    	numCircles++;
    }
	
	public void calculateSpeed() {
		if (numCircles < 100) {
			SPEED = 250;
		} else if (numCircles < 200) {
			SPEED = 235;
		} else if (numCircles < 300) {
			SPEED = 210;
		} else if (numCircles < 400) {
			SPEED = 195;
		} else if (numCircles < 500) {
			SPEED = 180;
		}
	}
	
	public static void end() {
		keepGoing = false;
	}
}
