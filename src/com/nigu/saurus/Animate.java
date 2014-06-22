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
	
	private int EASY;
	private int NORMAL;
	private int HARD;
	private int GET_READY;
	private int YOU_LOSE;
	
	public Animate(Handler handler, List<CircleView> circles, List<CircleView> activated) {
		this.handler = handler;
		this.activated = activated;
		this.circles = circles;
		
		EASY = 300;
		NORMAL = 200;
		HARD = 150;
		GET_READY = 500;
		YOU_LOSE = 1000;
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
    	while (true) {
    		if (activated.size() > 2) {
    			gameOver();
    			try {
    				sleep(YOU_LOSE);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    			flash("flash");
    			return;
    		}
    		
    		highlightRandom();
    		
    		try {
				sleep(NORMAL);
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
    	b.putInt("index", rand);
    	msg.setData(b);
    	//msg.arg1 = rand;
    	msg.sendToTarget();

    }
    
//    public void checkIfPlaying() {
//    	Message msg = handler.obtainMessage();
//    	Bundle b = new Bundle();
//    	b.putInt("index", rand);
//    	msg.setData(b);
//    	handler.handleMessage(msg);
//    }
    
    public void gameOver() {
    	Message msg = handler.obtainMessage();
    	Bundle b = new Bundle();
    	b.putByte("game over", (byte) 1);
    	msg.setData(b);
    	msg.sendToTarget();
    }
    
	public void flash(String message) {
		Message msg = handler.obtainMessage();
		Bundle b = new Bundle();
		b.putByte(message, (byte) 1);
		msg.setData(b);
		msg.sendToTarget();
	}
}
