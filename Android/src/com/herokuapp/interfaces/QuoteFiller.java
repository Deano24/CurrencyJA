package com.herokuapp.interfaces;

import java.util.LinkedHashMap;

import android.util.Pair;

public interface QuoteFiller {
	void complete(LinkedHashMap < String, Pair < Pair< String, String >, String > > quotes);
	void errorThrown();
}
