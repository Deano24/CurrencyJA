package com.herokuapp.currencyja;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class USDFragment extends Fragment {
	private final static String OPEN_SOURCE = "An Open Source Project By: Kenrick Beckett.";
	private final static String OPEN_SOURCE_URL = "https://github.com/kenrick/currencyja";
	private final static String KENRICK_NAME_URL = "https://github.com/kenrick";
	private final static String BUILT = "Application developed by Rohan Malcolm.";
	private final static String DEANO_URL = "https://github.com/Deano24/";
	private LinearLayout itemsLinearLayout;
	private EditText amountEditText;
	private TextView updateTime;
	private static final String TAG_CODE = "usd";
	public USDFragment() {
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater.inflate(R.layout.fragment_usd, container, false);
		TextView openSource = (TextView) localView.findViewById(R.id.kenrickTextView);
		TextView deanoOwn = (TextView) localView.findViewById(R.id.deanoTextView);
		openSource.setText(OPEN_SOURCE);
		deanoOwn.setText(BUILT);
		openSource.setGravity(Gravity.CENTER);
		deanoOwn.setGravity(Gravity.CENTER);
		Linkify.TransformFilter filter = new Linkify.TransformFilter() {
			@Override
			public String transformUrl(Matcher match, String url) {
				// TODO Auto-generated method stub
				return "";
			}
	    };
		Linkify.addLinks(openSource, Pattern.compile("Open Source") ,OPEN_SOURCE_URL,null,filter);
		Linkify.addLinks(openSource, Pattern.compile("Kenrick Beckett"),KENRICK_NAME_URL,null,filter);
	    Linkify.addLinks(deanoOwn, Pattern.compile("Rohan Malcolm"),DEANO_URL,null,filter);
		itemsLinearLayout = (LinearLayout)localView.findViewById(R.id.itemsLinearLayout);
		updateTime = (TextView)localView.findViewById(R.id.lastUpdated);
		amountEditText = (EditText)localView.findViewById(R.id.amountEditText);
		updateList(inflater);
		return localView;
	}
	
	public void updateList(LayoutInflater inflater){
		GetQoutes qoutes = new GetQoutes();
		qoutes.setTagCode(TAG_CODE);
		qoutes.setAmountText(amountEditText);
		qoutes.setItemsLayout(itemsLinearLayout);
		qoutes.setUpdatedTextView(updateTime);
		qoutes.setInflator(inflater);
		qoutes.execute();
	}
	
}
