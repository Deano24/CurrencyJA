package com.herokuapp.currencyja;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
	private USDFragment usdFrag;
	private GBPFragment gbpFrag;
	private CADFragment cadFrag;
	private EURFragment eurFrag;
	public TabAdapter(FragmentManager fm) {
		super(fm);
		usdFrag = new USDFragment();
		gbpFrag = new GBPFragment();
		cadFrag = new CADFragment();
		eurFrag = new EURFragment();
	}
	
	

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

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	} 
}
