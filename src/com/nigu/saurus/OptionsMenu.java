package com.nigu.saurus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class OptionsMenu extends DialogFragment {
	
	private int theme = 0;
	
    public interface OptionsDialogListener {
        public void onClick(OptionsMenu dialog, int theme);
    }
    
    public void setTheme(int themeChoice) {
    	theme = themeChoice;
    }
    
    OptionsDialogListener listener;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            listener = (OptionsDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement OptionsDialogListener");
        }
    }
//	@Override
//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//	    // Get the layout inflater
//	    LayoutInflater inflater = getActivity().getLayoutInflater();
//	    
//	    Spinner spinner = (Spinner) findViewById(R.id.themes_spinner);
//		// Create an ArrayAdapter using the string array and a default spinner layout
//		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//		        R.array.themes_array, android.R.layout.simple_spinner_item);
//		// Specify the layout to use when the list of choices appears
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		// Apply the adapter to the spinner
//		spinner.setAdapter(adapter);
//
//	    // Inflate and set the layout for the dialog
//	    // Pass null as the parent view because its going in the dialog layout
//	    builder.setTitle("Options")
//	    	   .setItems(R.array.reset_score, new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					
//				}
//			})
////			   .setMultiChoiceItems(R.array.sound, null, new DialogInterface.OnMultiChoiceClickListener() {
////				
////				@Override
////				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
////					// TODO Auto-generated method stub
////					
////				}
////			})
//	    	   .setView(inflater.inflate(R.layout.options_menu, null));
//	    return builder.create();
//	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle("Themes")
	           .setItems(R.array.themes_array, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   theme = which;
	            	   listener.onClick(OptionsMenu.this, theme);
	               }
	           });
//               .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
//                   public void onClick(DialogInterface dialog, int id) {
//                       // Send the positive button event back to the host activity
//                       listener.onDialogPositiveClick(OptionsMenu.this, theme);
//                   }
//               })
//               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                   public void onClick(DialogInterface dialog, int id) {
//                       // Send the negative button event back to the host activity
//                       listener.onDialogNegativeClick(OptionsMenu.this);
//                   }
//               });
	    return builder.create();
	}
}
