/*
 * Copyright © 2012 Iain Churcher
 *
 * Based on GLtron by Andreas Umbach (www.gltron.org)
 *
 * This file is part of GL TRON.
 *
 * GL TRON is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GL TRON is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GL TRON.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.glTron;

import com.glTron.Game.GLTronGame.HyprMXCallback;
import com.hyprmx.android.activities.HyprMXActivity;
import com.hyprmx.android.sdk.HyprMXHelper;
import com.hyprmx.android.sdk.api.data.Offer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class glTron extends HyprMXActivity implements HyprMXCallback {
    /** Called when the activity is first created. */
	private OpenGLView _View;
	
	private Boolean _FocusChangeFalseSeen = false;
	private Boolean _Resume = false;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	HyprMXHelper.getInstance(this, "-60", "1", "3");
    	
    	WindowManager w = getWindowManager();
	    Display d = w.getDefaultDisplay();
	    int width = d.getWidth();
	    int height = d.getHeight();
	   
	    super.onCreate(savedInstanceState);
	    
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
        _View = new OpenGLView(this, width, height);
        setContentView(_View);

    }
    
    
    @Override
    public void onPause() {
    	_View.onPause();
    	super.onPause();
    }
    
    @Override
    public void onResume() {
    	if(!_FocusChangeFalseSeen)
    	{
    		_View.onResume();
    	}
    	_Resume = true;
    	super.onResume();
    }
    
    @Override
    public void onWindowFocusChanged(boolean focus) {
    	if(focus)
    	{
    		if(_Resume)
    		{
    			_View.onResume();
    		}
    		
    		_Resume = false;
    		_FocusChangeFalseSeen = false;
    	}
    	else
    	{
    		_FocusChangeFalseSeen = true;
    	}
    }   
    
    //open menu when key pressed
     public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
        	this.startActivity(new Intent(this, Preferences.class));
        }
        return super.onKeyUp(keyCode, event);
    }


    boolean splashScreenDisplayed = false;
    public static boolean glTronRestarted = true;
    
	@Override
	public void onGameLost() {
		Log.v("intr","on game is has lost been");
		if (!splashScreenDisplayed && glTronRestarted) {
			HyprMXHelper.getInstance().displaySplashScreen(this, null);
			splashScreenDisplayed = true;
			glTronRestarted = false;
		}
	}


	@Override
	public void onNoContentAvailable() {
		Log.v("intr","no content available");
		splashScreenDisplayed = false;
	}

	@Override
	public void onOfferCancelled(Offer arg0) {
		Log.v("intr","offer canceled");
		// TODO Auto-generated method stub
		splashScreenDisplayed = false;
	}


	@Override
	public void onOfferCompleted(Offer arg0) {
		Log.v("intr","offer completed");
		splashScreenDisplayed = false;
	}


	@Override
	public void onUserOptedOut() {
		Log.v("intr","Opted out.");
		splashScreenDisplayed = false;
	}
}