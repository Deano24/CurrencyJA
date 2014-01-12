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
 * @author Deano
 * Fragment for us amount USD
 */
public class USDFragment extends Fragment {
	private final static String OPEN_SOURCE = "An Open Source Project By: Kenrick Beckett.";
	private final static String OPEN_SOURCE_URL = "https://github.com/kenrick/currencyja";
	private final static String KENRICK_NAME_URL = "https://github.com/kenrick";
	private final static String BUILT = "Application developed by Rohan Malcolm.";
	private final static String DEANO_URL = "https://github.com/Deano24/";
	private LinearLayout itemsLinearLayout;
	private EditText amountEditText;
	private static final String TAG_CODE = "usd";
	
	/**
	 * Constructor
	 */
	public USDFragment() {
	}
	
	/**
	 * onCreateView called only once when the fragment is instantiated while 
	 * onCreate on the other hand is called every-time the view comes on screen
	 * 
	 * @param inflater the LayoutInflater to inflate each layout
	 * @param container the ViewGroup that all belongs to
	 * @param savedInstanceState saved state for the application
	 * 
	 * @return the view that should be rendered
	 * 
	 */
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
		amountEditText = (EditText)localView.findViewById(R.id.amountEditText);
		updateList(inflater);
		return localView;
	}
	
	/**
	 * 
	 * Create a new instance of GetQoutes to get all quotes
	 * 
	 * @param inflater inflater to be passed to getQoutes
	 */
	public void updateList(LayoutInflater inflater){
		GetQoutes qoutes = new GetQoutes();
		qoutes.setTagCode(TAG_CODE);
		qoutes.setContext(getActivity());
		qoutes.setAmountText(amountEditText);
		qoutes.setItemsLayout(itemsLinearLayout);
		qoutes.setInflator(inflater);
		qoutes.execute();
	}
	
}
