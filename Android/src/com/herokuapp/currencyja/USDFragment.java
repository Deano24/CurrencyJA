package com.herokuapp.currencyja;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class USDFragment extends Fragment {
	private LinearLayout itemsLinearLayout;
	private EditText amountEditText;
	private static final String TAG_CODE = "usd";
	public USDFragment() {
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater.inflate(R.layout.fragment_usd, container, false);
		itemsLinearLayout = (LinearLayout)localView.findViewById(R.id.itemsLinearLayout);
		amountEditText = (EditText)localView.findViewById(R.id.amountEditText);
		updateList(inflater);
		return localView;
	}
	
	public void updateList(LayoutInflater inflater){
		GetQoutes qoutes = new GetQoutes();
		qoutes.setTagCode(TAG_CODE);
		qoutes.setAmountText(amountEditText);
		qoutes.setItemsLayout(itemsLinearLayout);
		qoutes.setInflator(inflater);
		qoutes.execute();
	}
	
}
