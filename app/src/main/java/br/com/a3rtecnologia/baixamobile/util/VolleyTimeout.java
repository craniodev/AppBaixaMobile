package br.com.a3rtecnologia.baixamobile.util;

import com.android.volley.DefaultRetryPolicy;

public class VolleyTimeout {

	private static int IMEOUT_MS = 60000;
	
	
	public static DefaultRetryPolicy recuperarTimeout(){
		
		return (new DefaultRetryPolicy(
				IMEOUT_MS, 
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}
	
}
