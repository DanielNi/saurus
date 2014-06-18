package com.nigu.saurus;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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

public class Main extends FragmentActivity implements OptionsMenu.OptionsDialogListener {
	
	private static List<CircleView> circles = new ArrayList<CircleView>();
	private static List<CircleView> activated = new ArrayList<CircleView>();
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
	private int theme;
	private FragmentManager manager = getSupportFragmentManager();
	
	private OptionsMenu optionsMenu = new OptionsMenu();
	private HelpMenu helpMenu = new HelpMenu();
	
	private String[] vintage = { "#F2E6D0", "#ABDEC9", "#E8747F", "#F29D85", "#E3CA94", "#887959" };
//	private String[] venus = { "#C5E3EB", "#A9D6CB", "#D6B2C2", "#8DA693", "#6B636B" };
	private String[] antique = { "#F2EEE9", "#B9BFBE", "#5E5656", "#797D79", "#B9BFBE", "#5E5656" };
	private String[] rainforest = { "#EDE1C0", "#97BD8C", "#695F6B", "#738A7C", "#DE9A88", "#855C52" };
	private String[] honeydew = { "#D3EBE3", "#FFCB8C", "#8BAEA7", "#A63731", "#F7744D", "#94462E" };
	
	private String[][] themeColors = { vintage, antique, rainforest, honeydew };
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.game_layout);
        
        LinearLayout game = (LinearLayout) findViewById(R.id.Game);
        final PlayView pv = (PlayView) findViewById(R.id.Play); 
        final TableLayout tl = (TableLayout) findViewById(R.id.CircleTable);
        final ScoreView sv = (ScoreView) findViewById(R.id.Score);
        MenuView mv = (MenuView) findViewById(R.id.Menu);
        HelpView hv = (HelpView) findViewById(R.id.Help);
        
//        String[][] themeColors = { getResources().getStringArray(R.array.vintage), getResources().getStringArray(R.array.venus) };
        
        sv.reset();

        // Retrieve the high score and theme from memory
        SharedPreferences settings = getSharedPreferences(HIGH_SCORE, 0);
        int best = settings.getInt("best", 0);
        highScore = best;
        sv.savedBest(best);
        theme = settings.getInt("theme", 0);
        setColors(theme, themeColors);
               
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
        
        //
        // Handle animation thread
        //
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
        			for (CircleView cv : circles) {
        				cv.changeToActive();
        				cv.setEnabled(false);
        			}
        			for (CircleView cv : activated) {
        				cv.setEnabled(false);
        				circles.add(cv);
        			}
        			activated.clear();
        			pv.refresh();
        			
        		} else if (msg.getData().containsKey("flash")) {
        			for (CircleView cv : circles) {
        				cv.toggle();
        			}
        			pv.setEnabled(true);
        		}
        	}
        };
        
    
        //
        // Start the game when refresh is touched
        //
        pv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					pv.pressed();
    				sv.reset();
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					pv.refresh();
					pv.setEnabled(false);
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
   
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	    	
    	SharedPreferences settings = getSharedPreferences(HIGH_SCORE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("best", highScore);
        editor.putInt("theme", theme);
        editor.commit();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        optionsMenu.show(manager, "options");
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }
        
    public void openOptions(View v) {
        optionsMenu.show(manager, "options");
    }
    
    public void setColors(int choice, String[][] themeColors) {  
        LinearLayout game = (LinearLayout) findViewById(R.id.Game);
        PlayView pv = (PlayView) findViewById(R.id.Play); 
        TableLayout tl = (TableLayout) findViewById(R.id.CircleTable);
        ScoreView sv = (ScoreView) findViewById(R.id.Score);
        MenuView mv = (MenuView) findViewById(R.id.Menu);
        HelpView hv = (HelpView) findViewById(R.id.Help);
        
        game.setBackgroundColor(Color.parseColor(themeColors[choice][BACKGROUND_INDEX]));
        sv.setTheme(themeColors[choice][SCORE_INDEX]);
        mv.setTheme(themeColors[choice][MENU_INDEX]);
        hv.setTheme(themeColors[choice][MENU_INDEX]);
        pv.setTheme(themeColors[choice][MENU_INDEX], themeColors[choice][MENU_PRESSED_INDEX]);
        
        for (int i = 0; i < rows; i++) {
        	TableRow row = (TableRow) tl.getChildAt(i);
        	for (int j = 0; j < cols; j++) {
        		CircleView cv = (CircleView) row.getChildAt(j);
        		cv.setTheme(themeColors[choice][CIRCLE_NORMAL_INDEX], themeColors[choice][CIRCLE_ACTIVE_INDEX]);
        	}
        }
    }
	
	@Override
	public void onClick(OptionsMenu dialog, int themeChoice) {
		theme = themeChoice;
		setColors(themeChoice, themeColors);
	}
	
	public void openHelp(View v) {
		helpMenu.show(manager, "help");
	}
}
