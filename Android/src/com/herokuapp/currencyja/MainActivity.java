package com.herokuapp.currencyja;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class MainActivity extends SherlockFragmentActivity implements TabListener {

	private ActionBar actionBar;
	private ViewPager viewPager;
	private ConnectivityManager mConnectivityManager;
	private String[] tabNames = { "USD", "GBP", "CAD", "EUR" };
	private SharedPreferences chosenTheme;
	private static final String THEME = "THEME";
	private int selectedTheme;
	private int[] listOfThemes = {R.style.AppSherlockTheme,R.style.AppDarkTheme,R.style.AppLightTheme};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mConnectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean notConnection = true;
		if(mConnectivityManager!=null)
        {
            NetworkInfo[] info = mConnectivityManager.getAllNetworkInfo();
            if (info != null){
            	for (int i = 0; i < info.length; i++){
                	if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                		notConnection = false;
                        break;
                    }
                }
            }
        }
	    chosenTheme = getSharedPreferences("currenncyThemes", MODE_PRIVATE);
		selectedTheme = chosenTheme.getInt(THEME, 0);
		switch(selectedTheme){
			case R.style.AppDarkTheme:
				setTheme(R.style.AppDarkTheme);
				break;
			case R.style.AppLightTheme:
				setTheme(R.style.AppLightTheme);
				break;
			case R.style.AppSherlockTheme:
				setTheme(R.style.AppSherlockTheme);
				break;
			default:
				selectedTheme = R.style.AppSherlockTheme;
				setTheme(R.style.AppSherlockTheme);
				break;
		}
		setContentView(R.layout.activity_main);
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setOffscreenPageLimit(4);
		if(notConnection){
			new AlertDialog.Builder(this)
		    .setTitle("Internet Connectivity")
		    .setMessage("No internet connection could be found.")
		    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
		    	public void onClick(DialogInterface dialog, int which) {}
            })
		     .show();
			return;
		}
		viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				actionBar.setSelectedNavigationItem(position);
			}
			
		});
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for (String tab_name : tabNames) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }
	}

	@Override
	public void onTabSelected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onTabReselected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
			switch(item.getItemId()){
				case R.id.action_change_screen:
					changeTheme();
					return true;
			}
			return false;
	}*/
	
	public void changeTheme(){
		SharedPreferences.Editor preferencesEditor = chosenTheme.edit();
		//Log.d("theme","changing theme "+selectedTheme);
		for(int i = 0; i < listOfThemes.length; i++){
			if(listOfThemes[i] == selectedTheme && i+1 == listOfThemes.length){
				selectedTheme = listOfThemes[0];
				break;
			}
			if(listOfThemes[i] == selectedTheme){
				selectedTheme = listOfThemes[i+1];
				break;
			}
        }
		preferencesEditor.putInt(THEME,selectedTheme);
		preferencesEditor.commit();
		Bundle temp_bundle = new Bundle();
		onSaveInstanceState(temp_bundle);
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("bundle", temp_bundle);
		startActivity(intent);
		finish();
	}
}
