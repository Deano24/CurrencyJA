package com.herokuapp.currencyja;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GetQoutes extends AsyncTask<Void, Void, Void>{

	private LinearLayout itemsLinearLayout;
	private LayoutInflater inflater;
	private EditText amountEditText;
	private NumberFormat format = NumberFormat.getCurrencyInstance();
	private static String url = "http://currencyja.herokuapp.com/api/traders.json";
	private static final String TAG_NAME = "name";
	private static final String TAG_ID = "id";
	private String tagCode;
	private static final String TAG_BUYING = "buying";
	private static final String TAG_Selling = "selling";
	private static final String TAG_CURRENCIES = "currencies";
	private Context cont;
	private ArrayList<Pair< Pair< Pair<String,String>, String>, String> > qoutes;
	private static boolean dialogShouldShow = false;
	private static boolean dialogHasShown = false;
	private static boolean dialogShouldShowError = false;
	private static boolean dialogHasShownError = false;

	public GetQoutes(){
        qoutes = new ArrayList<Pair< Pair< Pair<String,String>, String>, String> >();
	}
	public void setTagCode(String tagCode){
		this.tagCode = tagCode;
	}
	public void setInflator(LayoutInflater inflater){
		this.inflater = inflater;
	}
	public void setContext(Context cont){
		this.cont = cont;
	}

	public void setItemsLayout(LinearLayout itemsLinearLayout){
		this.itemsLinearLayout = itemsLinearLayout;
	}
	public void setAmountText(EditText amountText){
		this.amountEditText = amountText;
		this.amountEditText.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if(s.length() == 0){
					for(int counting = 0; counting < itemsLinearLayout.getChildCount(); counting++){
						LinearLayout row = (LinearLayout)itemsLinearLayout.getChildAt(counting);
						String tag = (String)row.getTag();
						TextView buying = (TextView)row.getChildAt(1);
						TextView selling = (TextView)row.getChildAt(2);
						for(Pair< Pair< Pair<String,String>, String>, String> qoute : qoutes){
							if(qoute.second.equals(tag)){
								Double buyDouble = Double.valueOf(qoute.first.first.first);
								Double sellDouble = Double.valueOf(qoute.first.first.second);
								buying.setText(format.format(buyDouble));
								selling.setText(format.format(sellDouble));
							}
						}
					}
				}else{
					double amount = Double.valueOf(s.toString());
					for(int counting = 0; counting < itemsLinearLayout.getChildCount(); counting++){
						LinearLayout row = (LinearLayout)itemsLinearLayout.getChildAt(counting);
						String tag = (String)row.getTag();
						TextView buying = (TextView)row.getChildAt(1);
						TextView selling = (TextView)row.getChildAt(2);
						for(Pair< Pair< Pair<String,String>, String>, String> qoute : qoutes){
							if(qoute.second.equals(tag)){
								Double buyDouble = Double.valueOf(qoute.first.first.first)*amount;
								Double sellDouble = Double.valueOf(qoute.first.first.second)*amount;
								buying.setText(format.format(buyDouble));
								selling.setText(format.format(sellDouble));
							}
						}
					}
				}
			}
		});
	}

	@Override
	protected Void doInBackground(Void... params) {
		ServiceHandler sh = new ServiceHandler();
        String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
        Log.d("jsonStr",jsonStr); 
        if (jsonStr != null) {
            try {
            	dialogShouldShow = false;
            	dialogHasShown = false;
            	JSONArray jsonArr = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject innerObject = jsonArr.getJSONObject(i);
                    String id = innerObject.getString(TAG_ID);
                    String traderName = innerObject.getString(TAG_NAME).toUpperCase(Locale.getDefault());
                    JSONObject currencies = innerObject.getJSONObject(TAG_CURRENCIES);
                    try{
                        JSONObject code = null;
                    	if(tagCode.equals("eur")){
                    		if(currencies.has(tagCode)){
                            	code = currencies.getJSONObject(tagCode);
                    		}else if(currencies.has("euro")){
                            	code = currencies.getJSONObject("euro");
                    		}
                    	}else{
                        	code = currencies.getJSONObject(tagCode);
                    	}
                        String buyingAmount = code.getString(TAG_BUYING);
	                    String sellingAmount = code.getString(TAG_Selling);
	                    Pair<String, String> pairRates = new Pair<String, String>(buyingAmount, sellingAmount);
	                    Pair<Pair<String,String>, String> pairName = new Pair<Pair<String,String>, String>(pairRates,traderName);
	                    Pair< Pair< Pair<String,String>, String>, String> pairId = new Pair< Pair< Pair<String,String>, String>, String>(pairName,id);
	                    qoutes.add(pairId);
	                    dialogShouldShowError = false;
	                    dialogHasShownError = false;
                    }catch(Exception ex){
                    	continue;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dialogShouldShowError = true;
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            dialogShouldShow = true;
        }
		return null;
	}
	
	@Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if(dialogShouldShow){
        	if(!dialogHasShown){
        		new AlertDialog.Builder(this.cont)
	    	    .setTitle("Error")
	    	    .setMessage("We are sorry but there seems to be an error with connecting to our server please try again later.")
	    	    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
	    	    	public void onClick(DialogInterface dialog, int which) {}
	            })
		     .show();
        		dialogHasShown = true;
        	}
        }
        if(dialogShouldShowError){
        	if(!dialogHasShownError){
        		new AlertDialog.Builder(this.cont)
	    	    .setTitle("Error")
	    	    .setMessage("We are sorry but an error has occurred in our application we apologize for the inconvenience.")
	    	    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
	    	    	public void onClick(DialogInterface dialog, int which) {}
	            })
		     .show();
        		dialogHasShownError = true;
        	}
        }
        for(Pair< Pair< Pair<String,String>, String>, String> qoute : qoutes){
			View placeInfo = inflater.inflate(R.layout.currency_info, null);
			TextView name = (TextView) placeInfo.findViewById(R.id.placeNameTextView);
			TextView buying = (TextView) placeInfo.findViewById(R.id.buyingPlaceTextView);
			TextView selling = (TextView) placeInfo.findViewById(R.id.sellingPlaceTextView);
			name.setText(qoute.first.second);
			Double buyDouble = Double.valueOf(qoute.first.first.first);
			Double sellDouble = Double.valueOf(qoute.first.first.second);
			buying.setText(format.format(buyDouble));
			selling.setText(format.format(sellDouble));
			placeInfo.setTag(qoute.second);
			itemsLinearLayout.addView(placeInfo);
		}
    }
}
