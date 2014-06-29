package com.nigu.saurus;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class GameOver extends Thread {
	
	private Handler handler;
	private int YOU_LOSE;
	
	public GameOver(Handler handler) {
		this.handler = handler;
		YOU_LOSE = 1000;
	}
	
	@Override
	public void run() {
		gameOver();
		try {
			sleep(YOU_LOSE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		flash("flash");
		return;
	}
	
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
