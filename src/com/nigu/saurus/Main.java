package com.nigu.saurus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

//Handles all user interaction

public class Main extends FragmentActivity {
	
	private List<CircleView> circles = new ArrayList<CircleView>();
	private List<CircleView> activated = new ArrayList<CircleView>();
	private CircleView fakeCircle = null;
	private static final String HIGH_SCORE = "HighScore";
	private int highScore;
	private int rows = 4;
	private int cols = 4;
	private int BACKGROUND_INDEX = 0;
	private int CIRCLE_NORMAL_INDEX = 1;
	private int CIRCLE_ACTIVE_INDEX = 2;
	private int SCORE_INDEX = 3;
	private int MENU_INDEX = 4;
	private int MENU_PRESSED_INDEX = 5;
	private String theme;
	private boolean sound;
	private SharedPreferences.OnSharedPreferenceChangeListener listener;
	private SharedPreferences sharedPref;
	private SoundPool soundPool;
	private int popId;
	private int loseId;
	private boolean playing = false;
		
	private String[] classic = { "#F2E6D0", "#ABDEC9", "#E8747F", "#F29D85", "#E3CA94", "#887959" };
	private String[] rainforest = { "#EDE1C0", "#97BD8C", "#695F6B", "#738A7C", "#DE9A88", "#855C52" };
	private String[] vintage = { "#F2EEE9", "#B9BFBE", "#5E5656", "#797D79", "#B9BFBE", "#5E5656" };
//	private String[] honeydew = { "#D3EBE3", "#FFCB8C", "#A63731", "#6F8B86", "#6F8B86", "#435350" };
	private String[] cotton_candy = { "#FFF0DE", "#A5DADE", "#F7A1BE", "#62C4C7", "#F7A1BE", "#946172" };
//	private String[] fuschia = { "#F0F0BE", "#A88BBA", "#BA6F52", "#3D3C42", "#5E4082", "#5E4082" };
	private String[] wintergreen = { "#F5DCBC", "#6FB093", "#E89356", "#406E5F", "#AD845C", "#684F37" };
	
	private String[][] themeColors = { classic, rainforest, vintage, cotton_candy, wintergreen };
	
	private LinearLayout game;
	private PlayView pv;
	private ScoreView sv;
	private TableLayout tl;
	private MenuView mv;
	private HelpView hv;
	
	//
    // Handle animation thread
    //
    static class InnerHandler extends Handler {
    	private final WeakReference<Main> mFrag;
    	
    	InnerHandler(Main main) {
            mFrag = new WeakReference<Main>(main);
        }
    	
    	public void handleMessage(Message msg) {
    		Main m = mFrag.get();
    		if (msg.getData().containsKey("fake")) {
        		int fake = msg.getData().getInt("fake");
        		m.fakeCircle = m.circles.get(fake);
        		m.fakeCircle.changeToFake();
        		m.circles.remove(fake);
        		if (m.sound)
        			m.soundPool.play(m.popId, 1, 1, 1, 0, 1);
        	} else if (msg.getData().containsKey("index")) {
        		int index = msg.getData().getInt("index");
        		CircleView choice = m.circles.get(index);
        		choice.changeToActive();
        		if (m.sound)
        			m.soundPool.play(m.popId, 1, 1, 1, 0, 1);
        		
        		m.circles.remove(index); // has O(n)...change this?
            	m.activated.add(choice);
            	if (msg.getData().containsKey("stop")) {
            		m.circles.add(m.fakeCircle);
            		m.fakeCircle.fakeToNormal();
            		m.fakeCircle = null;
            	}
    		} else if (msg.getData().containsKey("game over")) {
    			if (m.fakeCircle != null) {
    				m.circles.add(m.fakeCircle);
    				m.fakeCircle.fakeToActive();
    			}
    			for (CircleView cv : m.circles) {
    				cv.changeToActive();
    				cv.setEnabled(false);
    			}
    			for (CircleView cv : m.activated) {
    				cv.setEnabled(false);
    				m.circles.add(cv);
    			}
    			m.activated.clear();
    			m.highScore = m.sv.getBest();
    			if (m.sound)
    				m.soundPool.play(m.loseId, 1, 1, 1, 0, 1);

    		} else if (msg.getData().containsKey("flash")) {
    			for (CircleView cv : m.circles) {
    				cv.changeToNormal();
    			}
    			m.pv.refresh();
    			m.hv.setEnabled(true);
    			m.mv.setEnabled(true);
    			m.pv.setEnabled(true);
    			m.playing = false;
    		}
    	}
    }
    
    InnerHandler gameHandler = new InnerHandler(this);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.game_layout);
        
        game = (LinearLayout) findViewById(R.id.Game);
        pv = (PlayView) findViewById(R.id.Play); 
        tl = (TableLayout) findViewById(R.id.CircleTable);
        sv = (ScoreView) findViewById(R.id.Score);
        mv = (MenuView) findViewById(R.id.Menu);
        hv = (HelpView) findViewById(R.id.Help);
        
//      String[][] themeColors = { getResources().getStringArray(R.array.vintage), getResources().getStringArray(R.array.venus) };
        
        sv.reset();
        
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        
        // Retrieve user settings
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sound = sharedPref.getBoolean(SettingsActivity.SOUND, true);
        theme = sharedPref.getString(SettingsActivity.THEME, "Classic");
        setColors(themeToInt(theme), themeColors);

        // Retrieve the high score from memory
        SharedPreferences settings = getSharedPreferences(HIGH_SCORE, 0);
        int best = settings.getInt("best", 0);
        highScore = best;
        sv.savedBest(best);
        boolean firstTime = settings.getBoolean("firstTime", true);
        if (firstTime) {
        	startActivity(new Intent(this, HelpActivity.class));
        	settings.edit().putBoolean("firstTime", false).commit();
        }
        
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        		if (key.equals(SettingsActivity.SOUND)) {
        			sound = prefs.getBoolean(key, false);
        		} else if (key.equals(SettingsActivity.THEME)) {
        			theme = prefs.getString(key, "Classic");
        			setColors(themeToInt(theme), themeColors);
        		} else if (key.equals(SettingsActivity.CLEAR)) {
        			boolean clear = prefs.getBoolean(key, false);
        			if (clear) {
            			sv.clearBest();
            			sv.reset();
            			highScore = 0;
        			}
        		} 
//        		else if (key.equals(SettingsActivity.RATE)) {
//        			Uri uri = Uri.parse("http://www.google.com");
//        			startActivity(new Intent(Intent.ACTION_VIEW, uri));
//        		}
        	}
        };
        	
        sharedPref.registerOnSharedPreferenceChangeListener(listener);
        
        // Sound effects
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        popId = soundPool.load(this, R.raw.pop, 1);
        loseId = soundPool.load(this, R.raw.you_lose, 1);
               
        //
        // Add touch events for the circles, increasing the score in the process
        //
        for (int i = 0; i < rows; i++) {
        	TableRow row = (TableRow) tl.getChildAt(i);
        	for (int j = 0; j < cols; j++) {
        		final CircleView cv = (CircleView) row.getChildAt(j);
        		circles.add(cv);
        		cv.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (cv.fake()) {
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								Animate.end();
								new GameOver(gameHandler).start();
								return true;
							} else {
								return false;
							}
						} else if (!cv.activated()) {
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
            
        
        //
        // Start the game when play is touched
        //
        pv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					pv.pressed();
    				sv.reset();
					pv.setEnabled(false);
					mv.setEnabled(false);
					hv.setEnabled(false);
					playing = true;
					new Animate(gameHandler, circles, activated).start();
					for (CircleView cv : circles) {
						cv.setEnabled(true);
					}
					return true;
				} else {
					return false;
				}
			}
        	
        });
   
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	    	
    	SharedPreferences settings = getSharedPreferences(HIGH_SCORE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("best", highScore);
        editor.commit();
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	if (playing)
    		return false;
    	startActivity(new Intent(this, SettingsActivity.class));
        return super.onPrepareOptionsMenu(menu);
    }
        
    public void openOptions(View v) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
    
    public void setColors(int choice, String[][] themeColors) {
        game.setBackgroundColor(Color.parseColor(themeColors[choice][BACKGROUND_INDEX]));
        sv.setTheme(themeColors[choice][SCORE_INDEX]);
        mv.setTheme(themeColors[choice][MENU_INDEX]);
        hv.setTheme(themeColors[choice][MENU_INDEX]);
        pv.setTheme(themeColors[choice][MENU_INDEX], themeColors[choice][MENU_PRESSED_INDEX]);
        
        for (int i = 0; i < rows; i++) {
        	TableRow row = (TableRow) tl.getChildAt(i);
        	for (int j = 0; j < cols; j++) {
        		CircleView cv = (CircleView) row.getChildAt(j);
        		cv.setTheme(themeColors[choice][CIRCLE_NORMAL_INDEX], themeColors[choice][CIRCLE_ACTIVE_INDEX], themeColors[choice][BACKGROUND_INDEX]);
        	}
        }
    }
	
	public void openHelp(View v) {
		startActivity(new Intent(this, HelpActivity.class));
	}
	
	private int themeToInt(String theme) {
		if (theme.equals("Classic")) {
			return 0;
		} else if (theme.equals("Rainforest")) {
			return 1;
		} else if (theme.equals("Vintage")) {
			return 2;
		} else if (theme.equals("Cotton Candy")) {
			return 3;
		} else {
			return 4;
		}
	}
}
