package com.papanews.ak;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.papanews.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Login extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    TextInputEditText  textInputEditTextUsername, TextInputEditTextPassowrd;
    Button buttonLogin;
    TextView textViewSignup;
    ProgressBar progressBar;
    private Context context = null;
    SharedPreferences sharedpreferences;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    Set<String> set = new HashSet<String>();

    RelativeLayout loginGoogle;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    SharedPreferences.Editor editor;
    CallbackManager callbackManager;

    LoginButton faceLogin;

    RelativeLayout log;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final ConnectionDectector cd = new ConnectionDectector(this);
        if (!(cd.isConnected())) {
            Toast.makeText(Login.this, "No internet connection", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, likhaputti.class);
            intent.putExtra("EXTRA_SESSION_ID", "noi");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
            finish();
        }

        PrefManager prefManager = new PrefManager(getApplicationContext());
        if(prefManager.isFirstTimeLaunch()){
            prefManager.setFirstTimeLaunch(false);
            startActivity(new Intent(Login.this, WelcomeActivity.class));
            finish();
        }


        prefs=this.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        edit = prefs.edit();
        set  =  prefs.getStringSet("recomended", null);

        Log.e("selected set :: ", String.valueOf(set));
        loginGoogle = findViewById(R.id.googlelogin);
        faceLogin = findViewById(R.id.facebook);
        log = findViewById(R.id.facebooklogin);



        textInputEditTextUsername = findViewById(R.id.username);
        TextInputEditTextPassowrd = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignup = findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progress);
        constraintLayout = findViewById(R.id.logic);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                int screenHeight = getWindow().getDecorView().getRootView().getHeight();

                int keyboardHeight = screenHeight - rect.bottom;

                if (keyboardHeight > screenHeight * 0.15) {
                    hideSystemUI();
                }
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceLogin.performClick();
            }
        });


        sharedpreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        final int j = sharedpreferences.getInt("key", 0);
        Log.i("what is in",String.valueOf(j));


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                throw new RuntimeException("Test Crash");
                signIn();
            }
        });



        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUp.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
                finish();
            }
        });


        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
        faceLogin.setReadPermissions(Arrays.asList("email", "public_profile"));

        callbackManager = CallbackManager.Factory.create();

        if (!loggedOut) {

        }

        faceLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String name = loginResult.getAccessToken().getUserId();
                String token = loginResult.getAccessToken().getToken();

                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                Log.d("API123", loggedIn + " ??");
                getUserProfile(AccessToken.getCurrentAccessToken());
                Intent intent = new Intent(getApplicationContext(), select_category.class);
                startActivity(intent);
                finish();
//                info.setText("User ID: " + loginResult.getAccessToken().getUserId() +
//                "\n" + "Auth Token: " + loginResult.getAccessToken().getToken());
            }
            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });



        if(j > 0){
            Log.i("Loogged in","your in");
            Intent activity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(activity);
        }else{
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String userName, password;
                    userName = String.valueOf(textInputEditTextUsername.getText());
                    password = String.valueOf(TextInputEditTextPassowrd.getText());

                    if (!userName.equals("") && !password.equals("")) {

                        progressBar.setVisibility(View.VISIBLE);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Starting Write and Read data with URL
                                //Creating array for parameters
                                String[] field = new String[2];
                                field[0] = "username";
                                field[1] = "password";

                                //Creating array for data
                                String[] data = new String[2];
                                data[0] = userName;
                                data[1] = password;
                                PutData putData = new PutData("http://papanews.in/PapaNews/login.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        progressBar.setVisibility(View.GONE);
                                        String result = putData.getResult();
                                        //End ProgressBar (Set visibility to GONE)
                                        Log.i("PutData", result);

                                        if(result.equals("Login Success")){

                                            //Default is 0 so autologin is disabled
                                            editor = sharedpreferences.edit();
                                            editor.putString("username", userName);
                                            editor.putInt("checkcheck",1);
                                            Log.e("Username :: ",userName);
                                            editor.apply();

                                            Log.i("Not luck","your not in");
                                            SessionManager sessionManager = new SessionManager(getApplicationContext());
                                            sessionManager.setLogin(true);
                                            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(getApplicationContext(), select_category.class);
                                            startActivity(intent);
                                            finish();

//                                            if(set==null){
//                                                Intent intent = new Intent(getApplicationContext(), select_category.class);
//                                                startActivity(intent);
//                                                finish();
//                                            }else{
//                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                                startActivity(intent);
//                                                finish();
//                                            }

                                        }else {
                                            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                //End Write and Read data with URL
                            }
                        });

                    }else{

                        Toast.makeText(getApplicationContext(),"All fields are required", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }


    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            Log.e("facebook name :: ",first_name);
                            Log.e("facebook last_name :: ",last_name);
                            Log.e("facebook email :: ",email);
                            Log.e("facebook id :: ",id);
                            Log.e("facebook image_url :: ",image_url);


                            editor = sharedpreferences.edit();
                            editor.putInt("checkcheck",2);
                            editor.putString("facebook_fullname", first_name+" "+last_name);
                            editor.putString("facebook_username", email);
                            editor.putString("facebook_password", first_name);
                            editor.putString("facebook_email", email);
                            editor.putString("facebook_image", image_url);
                            editor.apply();



                            addDataToDatabase(first_name+" "+last_name,email,first_name,email,image_url);

//                            txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
//                            txtEmail.setText(email);
//                            Picasso.with(MainActivity.this).load(image_url).into(imageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void signIn() {
        editor = sharedpreferences.edit();
        editor.putInt("checkcheck",3);
        editor.apply();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            startActivity(new Intent(Login.this, select_category.class));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(Login.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
//            startActivity(new Intent(Login.this, MainActivity.class));
            mGoogleSignInClient.signOut();
        }
        LoginManager.getInstance().logOut();
        super.onStart();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        hideSystemUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }


    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LOW_PROFILE |
                        View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }


    private void addDataToDatabase(final String fullname, final String username,
                                   final String password,final String email,final String image) {

        String url = "http://papanews.in/PapaNews/googleSignIn.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(Login.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);

                if(response.equals("success")){
                    Toast.makeText(Login.this, "Upload " + response, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Login.this, "User Already registered", Toast.LENGTH_SHORT).show();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(Login.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(Login.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("fullname", fullname);
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                params.put("image", image);
                return params;

            }
        };

        queue.add(request);
    }
}