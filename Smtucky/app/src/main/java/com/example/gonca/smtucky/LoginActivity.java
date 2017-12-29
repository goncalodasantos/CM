package com.example.gonca.smtucky;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by filipemendes on 26/12/17.
 */

public class LoginActivity extends AppCompatActivity {

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
        LoginButton LoginButton = (LoginButton) findViewById(R.id.login_button);
        LoginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));


        Button yourButton = (Button) findViewById(R.id.button_login);

        yourButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){


                EditText mEdit = (EditText) findViewById(R.id.username);

                String emailInText=mEdit.getText().toString();


                if(!emailInText.equals("")) {

                    UserDB user_db = Room.databaseBuilder(getApplicationContext(), UserDB.class, "userxgxssxnnnnn").allowMainThreadQueries().build();


                    List<User> listOfUsers = user_db.UserDAO().getUsers();


                    int successInFindingUserInBD = 0;
                    for (int i = 0; i < listOfUsers.size(); i++) {
                        Log.v("stuff-users in bd", listOfUsers.get(i).getMail());
                        if (listOfUsers.get(i).getMail().equals(emailInText)) {
                            successInFindingUserInBD = 1;
                        }
                    }
                    if (successInFindingUserInBD == 1) {

                        Log.v("stuff:", "Going to main activity, email in database");

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("email", emailInText);
                        LoginActivity.this.startActivity(intent);
                    } else {


                        Log.v("stuff-login", "no email in Database");

                        User newUser = new User();
                        newUser.setMail(emailInText);
                        user_db.UserDAO().insert(newUser);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("email", emailInText);
                        LoginActivity.this.startActivity(intent);

                    }


                    
                }
                else{

                    Toast.makeText(LoginActivity.this, "Email cannot be in blank", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if(isLoggedIn()) {
            Log.v("stuff-login","Trying to get email");



            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {
                            if (response.getError() != null) {
                                // handle error
                            } else {
                                // get email and id of the user
                                String email = me.optString("email");
                                String id = me.optString("id");

                                Log.v("stuff-login",email);


                                UserDB user_db = Room.databaseBuilder(getApplicationContext(), UserDB.class, "userxgxssxnnnnn").allowMainThreadQueries().build();



                                List<User> listOfUsers = user_db.UserDAO().getUsers();


                                int successInFindingUserInBD=0;
                                for (int i=0;i<listOfUsers.size();i++){
                                    Log.v("stuff-users in bd", listOfUsers.get(i).getMail());
                                    if(listOfUsers.get(i).getMail().equals(email)){
                                        successInFindingUserInBD=1;
                                    }
                                }
                                if(successInFindingUserInBD==1){

                                    Log.v("stuff:","Going to main activity, email in database");

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("email",email);
                                    LoginActivity.this.startActivity(intent);
                                }
                                else{


                                    Log.v("stuff-login","no email in Database");

                                    User newUser=new User();
                                    newUser.setMail(email);
                                    user_db.UserDAO().insert(newUser);

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("email",email);
                                    LoginActivity.this.startActivity(intent);

                                }


                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,email");
            request.setParameters(parameters);
            request.executeAsync();









        } else {
            Log.v("stuff:","Needing authetication");

            callbackManager = CallbackManager.Factory.create();

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {


                            GraphRequest request = GraphRequest.newMeRequest(
                                    AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject me, GraphResponse response) {
                                            if (response.getError() != null) {
                                                // handle error
                                            } else {
                                                // get email and id of the user
                                                String email = me.optString("email");
                                                String id = me.optString("id");

                                                Log.v("stuff-login",email);


                                                UserDB user_db = Room.databaseBuilder(getApplicationContext(), UserDB.class, "userxgxssxnnnnn").allowMainThreadQueries().build();



                                                List<User> listOfUsers = user_db.UserDAO().getUsers();


                                                int successInFindingUserInBD=0;
                                                for (int i=0;i<listOfUsers.size();i++){
                                                    Log.v("stuff-users in bd", listOfUsers.get(i).getMail());
                                                    if(listOfUsers.get(i).getMail().equals(email)){
                                                        successInFindingUserInBD=1;
                                                    }
                                                }
                                                if(successInFindingUserInBD==1){

                                                    Log.v("stuff:","Going to main activity, email in database");

                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    intent.putExtra("email",email);
                                                    user_db.close();
                                                    LoginActivity.this.startActivity(intent);
                                                }
                                                else{


                                                    Log.v("stuff-login","no email in Database");

                                                    User newUser=new User();
                                                    newUser.setMail(email);
                                                    user_db.UserDAO().insert(newUser);

                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    intent.putExtra("email",email);
                                                    user_db.close();
                                                    LoginActivity.this.startActivity(intent);

                                                }


                                            }
                                        }
                                    });

                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,email");
                            request.setParameters(parameters);
                            request.executeAsync();



                            // App code
                            Log.d("stuff-Login", "Success");
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
