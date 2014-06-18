package com.herokuapp.classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnection {
	public static boolean isConnected(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
	        return false;
	    }else{
	    	return true;
	    }
    }
}
