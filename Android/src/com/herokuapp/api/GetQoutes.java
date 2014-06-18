package com.herokuapp.api;

import java.util.LinkedHashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.herokuapp.interfaces.QuoteFiller;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

/**
 * 
 * @author Deano24 (Rohan Malcolm)
 * class that gets quotes and displays them in layout
 *
 */
public class GetQoutes extends AsyncTask<Void, Void, Void>{

	private static String url = "http://currencyja.herokuapp.com/api/traders.json";
	private static final String TAG_NAME = "name";
	private static final String TAG_ID = "id";
	private String mTagCode;
	private static final String TAG_BUYING = "buy_cash";
	private static final String TAG_Selling = "sell_cash";
	private static final String TAG_CURRENCIES = "currencies";
	private LinkedHashMap < String, Pair < Pair< String, String >, String > > quotes;
	private QuoteFiller mFillerCallback;

	/**
	 * Constructor. instantiates list of quotes
	 */
	public GetQoutes(){
		quotes = new LinkedHashMap < String, Pair < Pair< String, String >, String > >();
	}
	
	
	public void setTagCode(String tag){
		this.mTagCode = tag;
	}
	
	public void setCallBack(QuoteFiller callback){
		this.mFillerCallback = callback;
	}

	/**
	 * Task that runs in background to connect to host and gets quotes
	 * 
	 */
	@Override
	protected Void doInBackground(Void... params) {
		ServiceHandler sh = new ServiceHandler();
        String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
        if (jsonStr != null) {
            try {
            	JSONArray jsonArr = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject innerObject = jsonArr.getJSONObject(i);
                    String id = innerObject.getString(TAG_ID);
                    String traderName = innerObject.getString(TAG_NAME).toUpperCase(Locale.getDefault());
                    JSONObject currencies = innerObject.getJSONObject(TAG_CURRENCIES);
                    try{
                        JSONObject code = null;
                    	if(mTagCode.equals("eur")){
                    		if(currencies.has(mTagCode.toUpperCase(Locale.getDefault()))){
                            	code = currencies.getJSONObject(mTagCode.toUpperCase(Locale.getDefault()));
                    		}else if(currencies.has(new String("euro").toUpperCase(Locale.getDefault()))){
                            	code = currencies.getJSONObject(new String("euro").toUpperCase(Locale.getDefault()));
                    		}
                    	}else{
                        	code = currencies.getJSONObject(mTagCode.toUpperCase(Locale.getDefault()));
                    	}
                        String buyingAmount = code.getString(TAG_BUYING);
	                    String sellingAmount = code.getString(TAG_Selling);
	                    Pair < String, String > pairRates = new Pair< String, String > (buyingAmount, sellingAmount);
	                    Pair < Pair < String, String >, String > pairName = new Pair < Pair < String, String >, String >(pairRates,traderName);
	                    quotes.put(id, pairName);
                    }catch(Exception ex){
                    	Log.e("exception", ex.getMessage());
                    	continue;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mFillerCallback.errorThrown();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
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
        mFillerCallback.complete(quotes);
    }
}
