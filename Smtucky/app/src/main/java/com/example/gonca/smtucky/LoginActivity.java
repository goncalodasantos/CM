package com.example.gonca.smtucky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

/**
 * Created by filipemendes on 26/12/17.
 */

public class LoginActivity extends Activity {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* remove action bar */
        requestWindowFeature(Window.FEATURE_NO_TITLE);



        /* remove status bar */
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());


        setContentView(R.layout.welcome_login);

        Button yourButton = (Button) findViewById(R.id.button_login);

        yourButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });


        if(isLoggedIn()) {




            Log.v("stuff:","Going to main activity");
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        } else {
            Log.v("stuff:","Needing authetication");

            callbackManager = CallbackManager.Factory.create();

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {


                            GraphRequest.newMeRequest(
                                    loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject me, GraphResponse response) {
                                            if (response.getError() != null) {
                                                // handle error
                                            } else {
                                                // get email and id of the user
                                                String email = me.optString("email");
                                                String id = me.optString("id");
                                                Log.v("stuff-login",email);
                                            }
                                        }
                                    }).executeAsync();


                            // App code
                            Log.d("stuff-Login", "Success");
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }

                        @Override
                        public void onCancel() {
                            // App code
                            Log.d("stuff-Login", "Cancel");
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                            Log.d("stuff-Login", "Error");
                        }
                    });
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

}
