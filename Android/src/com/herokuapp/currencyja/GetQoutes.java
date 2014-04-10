package com.herokuapp.currencyja;

import java.text.NumberFormat;
import java.util.ArrayList;
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

/**
 * 
 * @author Deano
 * class that gets quotes and displays them in layout
 *
 */
public class GetQoutes extends AsyncTask<Void, Void, Void>{

	private LinearLayout itemsLinearLayout;
	private LayoutInflater inflater;
	private EditText amountEditText;
	private NumberFormat format = NumberFormat.getCurrencyInstance();
	private static String url = "http://currencyja.herokuapp.com/api/traders.json";
	private static final String TAG_NAME = "name";
	private static final String TAG_ID = "id";
	private String tagCode;
	private static final String TAG_BUYING = "buy_cash";
	private static final String TAG_Selling = "sell_cash";
	private static final String TAG_CURRENCIES = "currencies";
	private Context cont;
	private ArrayList<Pair< Pair< Pair<String,String>, String>, String> > qoutes;
	private static boolean dialogShouldShow = false;
	private static boolean dialogHasShown = false;
	private static boolean dialogShouldShowError = false;
	private static boolean dialogHasShownError = false;

	/**
	 * 
	 * Constructor. instantiates list of qoutes
	 * 
	 */
	public GetQoutes(){
        qoutes = new ArrayList<Pair< Pair< Pair<String,String>, String>, String> >();
	}
	
	/**
	 * sets tag code to be used
	 * 
	 * @param tagCode the tag code to be used eg: usd, gbp
	 */
	public void setTagCode(String tagCode){
		this.tagCode = tagCode;
	}
	
	/**
	 * 
	 * the inflater that will be used to inflate all objects and layouts
	 * 
	 * @param inflater inflater that will be used
	 */
	public void setInflator(LayoutInflater inflater){
		this.inflater = inflater;
	}
	
	/**
	 * 
	 * sets the context that will be used to show the alert dialog boxes
	 * 
	 * @param cont the context to be used
	 */
	public void setContext(Context cont){
		this.cont = cont;
	}

	/**
	 * 
	 * LinearLayout that all quotes will be placed in
	 * 
	 * @param itemsLinearLayout the layout to be used
	 */
	public void setItemsLayout(LinearLayout itemsLinearLayout){
		this.itemsLinearLayout = itemsLinearLayout;
	}
	
	/**
	 * 
	 * sets the textbox that will be used for updating and doing calculations on the qoutes
	 * 
	 * @param amountText link to text box
	 */
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

	/**
	 * 
	 * task that runs in background to connect to host and gets quotes
	 * 
	 */
	@Override
	protected Void doInBackground(Void... params) {
		ServiceHandler sh = new ServiceHandler();
        String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
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
                    		if(currencies.has(tagCode.toUpperCase(Locale.getDefault()))){
                            	code = currencies.getJSONObject(tagCode.toUpperCase(Locale.getDefault()));
                    		}else if(currencies.has(new String("euro").toUpperCase(Locale.getDefault()))){
                            	code = currencies.getJSONObject(new String("euro").toUpperCase(Locale.getDefault()));
                    		}
                    	}else{
                        	code = currencies.getJSONObject(tagCode.toUpperCase(Locale.getDefault()));
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
                    	Log.e("exception", ex.getMessage());
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
	
	/**
	 * 
	 * function called after the doInBackground is completed this is where i etiher show the dialog or
	 * updating the linear layout
	 * 
	 */
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
