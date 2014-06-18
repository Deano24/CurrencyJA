package com.herokuapp.fragments;

import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.herokuapp.api.GetQoutes;
import com.herokuapp.currencyja.R;
import com.herokuapp.interfaces.QuoteFiller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Pair;
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
 * Fragment for canadian amount CAD
 * 
 */
public class CADFragment extends Fragment implements QuoteFiller {

	private final static String OPEN_SOURCE = "An Open Source Project By: Kenrick Beckett.";
	private final static String OPEN_SOURCE_URL = "https://github.com/kenrick/currencyja";
	private final static String KENRICK_NAME_URL = "https://github.com/kenrick";
	private final static String BUILT = "Application developed by Rohan Malcolm.";
	private final static String DEANO_URL = "https://github.com/Deano24/";
	private LinearLayout mItemsLinearLayout;
	private static final String TAG_CODE = "cad";
	private ProgressDialog mProcessDialog;
	private LinkedHashMap < String, Pair < Pair< String, String >, String > > defaultQuotes;
	
	/**
	 * Constructor
	 */
	public CADFragment() {
		// Required empty public constructor
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
		View localView = inflater.inflate(R.layout.fragment_cad, container, false);
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
		mItemsLinearLayout = (LinearLayout)localView.findViewById(R.id.itemsLinearLayout);
		((EditText)localView.findViewById(R.id.amountEditText)).addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			@Override
			public void afterTextChanged(Editable edit) {
				if( edit.length() == 0 ) {
					CADFragment.this.updateQuotes(defaultQuotes);
				} else {
					LinkedHashMap < String, Pair < Pair< String, String >, String > > updatedQuotes = new LinkedHashMap < String, Pair < Pair< String, String >, String > > ();
					for(String key : defaultQuotes.keySet()){
						Pair < Pair< String, String >, String > pairs = defaultQuotes.get(key);
						Double buying = Double.parseDouble(pairs.first.first);
						Double selling = Double.parseDouble(pairs.first.second);
						Double text = Double.parseDouble(edit.toString().trim());
						buying *= text;
						selling *= text;
	                    Pair < String, String > pairRates = new Pair< String, String > ( String.valueOf(buying), String.valueOf(selling));
	                    Pair < Pair < String, String >, String > pairName = new Pair < Pair < String, String >, String >(pairRates,pairs.second);
	                    updatedQuotes.put(key, pairName);
					}
					CADFragment.this.updateQuotes(updatedQuotes);
				}
			}
		});
		updateList();
		return localView;
	}
	
	/**
	 * Create a new instance of GetQoutes to get all quotes
	 * 
	 */
	public void updateList(){
		mProcessDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Downloading Quotes ...", true);
		mProcessDialog.setCancelable(false);
		GetQoutes qoutes = new GetQoutes();
		qoutes.setTagCode(TAG_CODE);
		qoutes.setCallBack(this);
		qoutes.execute();
	}

	@Override
	public void complete(LinkedHashMap < String, Pair < Pair< String, String >, String > > quotes) {
		defaultQuotes = quotes;
		NumberFormat format = NumberFormat.getCurrencyInstance();
		for(String key : quotes.keySet()){
			Pair < Pair< String, String >, String > pairs = quotes.get(key);
			View placeInfo = this.getActivity().getLayoutInflater().inflate(R.layout.currency_info, null);
			TextView name = (TextView) placeInfo.findViewById(R.id.placeNameTextView);
			TextView buying = (TextView) placeInfo.findViewById(R.id.buyingPlaceTextView);
			TextView selling = (TextView) placeInfo.findViewById(R.id.sellingPlaceTextView);
			name.setText(pairs.second);
			Double buyDouble = Double.valueOf(pairs.first.first);
			Double sellDouble = Double.valueOf(pairs.first.second);
			buying.setText(format.format(buyDouble));
			selling.setText(format.format(sellDouble));
			placeInfo.setTag(key);
			mItemsLinearLayout.addView(placeInfo);
		}
		if(mProcessDialog.isShowing()){
			mProcessDialog.dismiss();
		}
	}

	@Override
	public void errorThrown() {
		if(mProcessDialog.isShowing()){
			mProcessDialog.dismiss();
		}
		new AlertDialog.Builder(getActivity())
	    .setTitle("Application Error")
	    .setMessage("Sorry :(, an error has occured while retrieving quotes. I will get on fixing it right away please try again later.")
	    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int which) {}
        })
	     .show();
	}
	
	private void updateQuotes(LinkedHashMap < String, Pair < Pair< String, String >, String > > quotes){
		NumberFormat format = NumberFormat.getCurrencyInstance();
		mItemsLinearLayout.removeAllViews();
		for(String key : quotes.keySet()){
			Pair < Pair< String, String >, String > pairs = quotes.get(key);
			View placeInfo = this.getActivity().getLayoutInflater().inflate(R.layout.currency_info, null);
			TextView name = (TextView) placeInfo.findViewById(R.id.placeNameTextView);
			TextView buying = (TextView) placeInfo.findViewById(R.id.buyingPlaceTextView);
			TextView selling = (TextView) placeInfo.findViewById(R.id.sellingPlaceTextView);
			name.setText(pairs.second);
			Double buyDouble = Double.valueOf(pairs.first.first);
			Double sellDouble = Double.valueOf(pairs.first.second);
			buying.setText(format.format(buyDouble));
			selling.setText(format.format(sellDouble));
			placeInfo.setTag(key);
			mItemsLinearLayout.addView(placeInfo);
		}
	}
}
