package br.com.a3rtecnologia.baixamobile.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import br.com.a3rtecnologia.baixamobile.R;
import br.com.a3rtecnologia.baixamobile.login.LoginActivity;
import br.com.a3rtecnologia.baixamobile.menu.MenuDrawerActivity;

public class SplashActivity extends Activity implements Runnable {


	private SessionManager sessionManager;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        
    	setContentView(R.layout.activity_splash);

		sessionManager = new SessionManager(getApplicationContext());
    	
    	Handler handler = new Handler();
		
		handler.postDelayed(SplashActivity.this, 2000);
    }
    
    
    
	@Override
	public void run() {

		Context mContext = getApplicationContext();

		String email = sessionManager.getValue("email");

		if(email != null && !email.equals("")){

			Intent intent = new Intent(mContext, MenuDrawerActivity.class);
			startActivity(intent);

		}else{

			Intent intent = new Intent(mContext, LoginActivity.class);
			startActivity(intent);
		}

		finish();
	}
	
}