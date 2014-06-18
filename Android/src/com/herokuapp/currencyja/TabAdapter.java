package com.herokuapp.currencyja;

import com.herokuapp.fragments.CADFragment;
import com.herokuapp.fragments.EURFragment;
import com.herokuapp.fragments.GBPFragment;
import com.herokuapp.fragments.USDFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * @author Deano
 * TabAdapter to deal with switching of tabs extends FragmentPagerAdapter to aid with switching
 *
 */
public class TabAdapter extends FragmentPagerAdapter {
	private USDFragment usdFrag;
	private GBPFragment gbpFrag;
	private CADFragment cadFrag;
	private EURFragment eurFrag;
	
	/**
	 * constructor from the extended class, also instantiates each fragment
	 * 
	 * @param fm
	 */
	public TabAdapter(FragmentManager fm) {
		super(fm);
		usdFrag = new USDFragment();
		gbpFrag = new GBPFragment();
		cadFrag = new CADFragment();
		eurFrag = new EURFragment();
	}
	
	/**
	 * getter that gets item based on index
	 * 
	 * @param index the index being changed to
	 */
	@Override
	public Fragment getItem(int index) {
		// TODO Auto-generated method stub
		switch (index) {
	        case 0:
	            return usdFrag;
	        case 1:
	            return gbpFrag;
	        case 2:
	            return cadFrag;
	        case 3:
	            return eurFrag;
	        }
		return null;
	}

	/**
	 * total amount of tabs that will be there
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	} 
}
