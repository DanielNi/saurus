package com.nigu.saurus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;

//Handles all user interaction

public class Main extends Activity {
	
	List<CircleView> circles = new ArrayList<CircleView>();
	List<CircleView> activated = new ArrayList<CircleView>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
//        
//        for (final CircleView cv : circles) {
//        	cv.setOnTouchListener(new OnTouchListener() {
//
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//				    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//				    	cv.changeToActive();
//				    	cv.increaseScore();
//				    	cv.invalidate();
//				    	sv.invalidate();
//				    	return true;
//				    } else if (event.getAction() == MotionEvent.ACTION_UP) {
//				    	cv.changeToNormal();
//				    	cv.invalidate();
//				    	return true;
//				    } else {
//				    	return false;
//				    }
//				}
//    			
//    		});
//        }
        
        setContentView(R.layout.game_layout);
        
        final MenuView mv = (MenuView) findViewById(R.id.Menu); 
        final TableLayout tl = (TableLayout) findViewById(R.id.CircleTable);
        final ScoreView sv = (ScoreView) findViewById(R.id.Score);
        
//        Handler handler = new Handler(Looper.getMainLooper()) {
//        	@Override
//            public void handleMessage(Message inputMessage) {
//                PhotoTask photoTask = (PhotoTask) inputMessage.obj;
//            }
//        };
        
        final Handler handler = new Handler() {
        	public void handleMessage(Message msg) {
        		if (msg.getData().containsKey("index")) {
            		int index = msg.getData().getInt("index");
            		CircleView choice = circles.get(index);
            		choice.changeToActive();
            		
            		circles.remove(index); // has O(n)...change this?
                	activated.add(choice);
        		} else if (msg.getData().containsKey("game over")) {
        			for (CircleView cv : activated) {
        				cv.changeToNormal();
        				circles.add(cv);
        			}
        			activated.clear();
        			for (CircleView cv : circles) {
        				cv.setEnabled(false);
        			}
        			sv.invalidate();
        			mv.refresh();
        			
        		} else if (msg.getData().containsKey("flash")) {
        			for (CircleView cv : circles) {
        				cv.toggle();
        				cv.reset();
        			}
        			mv.setEnabled(true);
        		}
        	}
        };
        
        for (int i = 0; i < 4; i++) {
        	TableRow row = (TableRow) tl.getChildAt(i);
        	for (int j = 0; j < 4; j++) {
        		final CircleView cv = (CircleView) row.getChildAt(j);
        		circles.add(cv);
        		cv.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (!cv.activated()) {
							return false;
						} else {
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								cv.increaseScore();
								cv.changeToNormal();
								cv.setBest();
								
						    	sv.invalidate();
						    	circles.add(cv);
						    	activated.remove(cv);
								return true;
							} else {
								return false;
							}

						}
					}
        			
        		});
        	}
        }
        
        mv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mv.pressed();
					mv.setEnabled(false);
					new Animate(handler, circles, activated).start();
					for (CircleView cv : circles) {
						cv.setEnabled(true);
					}

					return true;
				} else {
					return false;
				}
			}
        	
        });
        
//        GridView circles = (GridView) findViewById(R.id.CircleGrid);
//        circles.setAdapter(new CircleAdapter(this));
//        circles.setOnTouchListener(new OnTouchListener() {
//        	
//        	// Prevent scrolling
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_MOVE){
//                    return true;
//                }
//                return false;
//            }
//        });
    }
    
    
//    public void playGame() {
//    	ScoreView sv = (ScoreView) findViewById(R.id.Score);
//    	while (true) {
//    		if (activated.size() > 3) {
//    			if (sv.getScore() > sv.getBest()) {
//    				sv.setBest();
//    				
//    				//
//    				// end the game
//    				//
//    			}
//    			return;
//    		}
//    		
//    		highlightRandom();
////    		try {
////				Thread.sleep(500);
////			} catch (InterruptedException e) {
////				e.printStackTrace();
////			}
//    	}
//    }
//    
//    public void highlightRandom() {
//    	int size = circles.size();
//    	int rand = new Random().nextInt(size);
//    	CircleView choice = circles.get(rand);
//    	choice.changeToActive();
//    	
//    	circles.remove(rand); // has O(n)...change this?
//    	activated.add(choice);
//    }
	
}
