package com.nigu.saurus;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;

//Handles all user interaction

public class Main extends Activity {
	
	private static List<CircleView> circles = new ArrayList<CircleView>();
	private static List<CircleView> activated = new ArrayList<CircleView>();
	private static final String HIGH_SCORE = "HighScore";
	private int highScore;
	
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
        
        final PlayView pv = (PlayView) findViewById(R.id.Menu); 
        final TableLayout tl = (TableLayout) findViewById(R.id.CircleTable);
        final ScoreView sv = (ScoreView) findViewById(R.id.Score);

        SharedPreferences settings = getSharedPreferences(HIGH_SCORE, 0);
        int best = settings.getInt("best", 0);
        sv.savedBest(best);
        
        final Handler handler = new Handler() {
        	public void handleMessage(Message msg) {
        		if (msg.getData().containsKey("index")) {
            		int index = msg.getData().getInt("index");
            		CircleView choice = circles.get(index);
            		choice.changeToActive();
            		
            		circles.remove(index); // has O(n)...change this?
                	activated.add(choice);
        		} else if (msg.getData().containsKey("game over")) {
        			highScore = sv.getBest();
        			for (CircleView cv : activated) {
        				cv.changeToNormal();
        				circles.add(cv);
        			}
        			activated.clear();
        			for (CircleView cv : circles) {
        				cv.setEnabled(false);
        			}
        			pv.refresh();
        			
        		} else if (msg.getData().containsKey("flash1")) {
        			for (CircleView cv : circles) {
        				cv.toggle();
        			}
        		} else if (msg.getData().containsKey("flash2")) {
        			for (CircleView cv : circles) {
        				cv.toggle();
        				sv.reset();
        			}
        			pv.setEnabled(true);
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
								sv.increaseScore();
								cv.changeToNormal();
								sv.setBest();
								
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
        
        pv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					pv.pressed();
					new Animate(handler, circles, activated).start();
					for (CircleView cv : circles) {
						cv.setEnabled(true);
					}

					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					pv.refresh();
					pv.setEnabled(false);
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
    
    @Override
    protected void onPause() {
    	super.onPause();
    	
    	SharedPreferences settings = getSharedPreferences(HIGH_SCORE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("best", highScore);
        editor.commit();
    }
}
