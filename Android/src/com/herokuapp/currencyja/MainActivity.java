package com.herokuapp.currencyja;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;
import com.herokuapp.classes.InternetConnection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

/**
 * 
 * @author Deano
 * the main class that runs the application extends sherlock fragment so as to add support for tab 
 * swipping in older phones
 *
 */
public class MainActivity extends SherlockFragmentActivity implements TabListener {

	private ActionBar actionBar;
	private ViewPager viewPager;
	private String[] tabNames = { "USD", "GBP", "CAD", "EUR" };
	private SharedPreferences chosenTheme;
	private static final String THEME = "THEME";
	private int selectedTheme;
	private int[] listOfThemes = {R.style.AppSherlockTheme,R.style.AppDarkTheme,R.style.AppLightTheme};
	
	/**
	 * called the time the app is created each time or when orientation is changed
	 * 
	 * @param savedInstanceState all saved informations that is related to the game
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!InternetConnection.isConnected(MainActivity.this) ){
			new AlertDialog.Builder(this)
		    .setTitle("Internet Connectivity")
		    .setMessage("No internet connection could be found. Please connect to the internet and try again.")
		    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
		    	public void onClick(DialogInterface dialog, int which) {}
            })
		     .show();
			finish();
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
		viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
			
		});
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for (String tab_name : tabNames) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }
	}

	/**
	 * function that is called when tab is selected, used to set the view to the selected tab
	 * 
	 * @param tab the tab that was clicked
	 * @param ft the fragment transaction/operations.
	 */
	@Override
	public void onTabSelected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
	}
	
	/**
	 * function called when you leave a tab
	 * 
	 * @param tab the tab that was left
	 * @param ft the fragment transaction/operations.
	 */
	@Override
	public void onTabUnselected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * this function is called when you are on a tab and select back the same tab
	 * 
	 * @param tab the tab that was clicked
	 * @param ft the fragment transaction/operations.
	 */
	@Override
	public void onTabReselected(Tab tab,android.support.v4.app.FragmentTransaction ft) {}
	
	/**
	 * function that creates the menu options
	 * 
	 * @param menu a menu object
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * function that changes theme to next available theme -->currently commented out due to errors
	 */
	public void changeTheme(){
		SharedPreferences.Editor preferencesEditor = chosenTheme.edit();
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
