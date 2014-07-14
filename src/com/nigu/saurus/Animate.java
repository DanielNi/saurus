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
	
	private int randFake;
	private Random rand;
	
	public Animate(Handler handler, List<CircleView> circles, List<CircleView> activated) {
		this.handler = handler;
		this.activated = activated;
		this.circles = circles;
		keepGoing = true;
		
		SPEED = 250;
		GET_READY = 500;
		
		numCircles = 0;
		
		rand = new Random();
		randFake = rand.nextInt(35) + 50;
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
    	int randCircle = rand.nextInt(size - 1); // don't want to highlight the last circle pressed immediately afterwards
    	Message msg = handler.obtainMessage();
    	Bundle b = new Bundle();
    	if (numCircles > 50 && numCircles == randFake) {
    		b.putInt("fake", randCircle);
        	fake = numCircles;
    	} else if (numCircles > 50 && fake + 10 == numCircles) {
    		b.putInt("stop", (byte) 1);
    		b.putInt("index", randCircle);
    		randFake = rand.nextInt(35) + numCircles;
    	} else {
        	b.putInt("index", randCircle);
    	}
    	msg.setData(b);
    	msg.sendToTarget();
    	numCircles++;
    }
	
	public void calculateSpeed() {
		if (numCircles < 50) {
			SPEED = 275;
		} else if (numCircles < 100) {
			SPEED = 265;
		} else if (numCircles < 150) {
			SPEED = 255;
		} else if (numCircles < 200) {
			SPEED = 245;
		} else if (numCircles < 250) {
			SPEED = 235;
		} else if (numCircles < 300) {
			SPEED = 230;
		} else if (numCircles < 350) {
			SPEED = 225;
		} else if (numCircles < 400) {
			SPEED = 220;
		} else if (numCircles < 450) {
			SPEED = 215;
		} else if (numCircles < 500) {
			SPEED = 210;
		} else if (numCircles < 550) {
			SPEED = 208;
		} else if (numCircles < 600) {
			SPEED = 206;
		} else if (numCircles < 650) {
			SPEED = 204;
		} else if (numCircles < 700) {
			SPEED = 202;
		} else {
			SPEED = 200;
		}
	}
	
	public static void end() {
		keepGoing = false;
	}
}
