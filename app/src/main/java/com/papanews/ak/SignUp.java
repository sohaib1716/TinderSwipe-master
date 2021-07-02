package com.papanews.ak;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.papanews.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class SignUp extends AppCompatActivity {
    ScrollView cs;
    TextInputEditText textInputEditTextFullName, textInputEditTextUsername, TextInputEditTextPassowrd, TextInputEditTextEmail;
    TextInputEditText textInputNumber, textInputLocation, textInputProffesion;

    Button buttonSignUp;
    TextView textViewLogin;
    ProgressBar progressBar;
    private Context context = null;
    boolean defaultValue = false;
    Button select, upload_php;
    ImageView phpImage;
    Bitmap bitmap;

    private static final int STORAGE_PERMISSION_CODE = 4655;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filepath;

    String fullName, userName, password, email, location, number, profession;





    String URL = "http://papanews.in/PapaNews/insertImage.php";

    final int CODE_GALLERY_REQUEST = 999;
    RadioButton male, female, other;
    String usergender="male",dateofbirth;

//    datepicker

    DatePickerDialog picker;
    EditText eText;
    Button btnGet;
    TextView tvw;

    TextInputLayout patext;
    String pushusername;
    String pushemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final ConnectionDectector cd = new ConnectionDectector(this);
        if (!(cd.isConnected())) {
            Toast.makeText(SignUp.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

        phpImage = findViewById(R.id.profileImagephp);
        select = findViewById(R.id.choose);
        upload_php = findViewById(R.id.upload);
        cs = findViewById(R.id.signmain);
        patext = findViewById(R.id.textInputLayoutPassword);


        SharedPreferences sharedPreferences;
        sharedPreferences = this.getSharedPreferences("autoLogin", Context.MODE_PRIVATE);

        if(global.logincheck==2){
            final String fb_full = sharedPreferences.getString("facebook_fullname", "");
            final String fb_user = sharedPreferences.getString("facebook_username", "");
            final String fb_email = sharedPreferences.getString("facebook_email", "");
            Log.e("bakwastime name", fb_full);
            Log.e("bakwastime email", fb_email);
            pushemail = fb_email;
            pushusername = fb_full;
        }


        if(global.logincheck==1){
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SignUp.this);
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Log.e("bakwastime name", personName);
            Log.e("bakwastime email", personEmail);
            pushemail = personEmail;
            pushusername = personName;
        }




        tvw=(TextView)findViewById(R.id.textView1);
        eText=(EditText) findViewById(R.id.editText1);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(SignUp.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                dateofbirth = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                            }
                        }, year, month, day);
                picker.show();
            }
        });
        btnGet=(Button)findViewById(R.id.button1);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvw.setText("Selected Date: "+ eText.getText());
            }
        });



//        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//
//                Rect rect = new Rect();
//                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
//                int screenHeight = getWindow().getDecorView().getRootView().getHeight();
//
//                int keyboardHeight = screenHeight - rect.bottom;
//
//                if (keyboardHeight > screenHeight * 0.15) {
//                    hideSystemUI();
//                }
//            }
//        });


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        SignUp.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST

                );
            }
        });

        upload_php.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error : " + error, Toast.LENGTH_LONG).show();
                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        String imageData = imageToString(bitmap);
                        params.put("image", imageData);

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
                requestQueue.add(stringRequest);
            }
        });


        textInputEditTextFullName = findViewById(R.id.fullname);
        textInputEditTextUsername = findViewById(R.id.username);
        TextInputEditTextPassowrd = findViewById(R.id.password);
        TextInputEditTextEmail = findViewById(R.id.email);
        textInputLocation = findViewById(R.id.locationin);
        textInputNumber = findViewById(R.id.number);
        textInputProffesion = findViewById(R.id.profession);

        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        other = findViewById(R.id.other);

        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progress);


        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
                finish();
            }
        });



        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fullName = String.valueOf(textInputEditTextFullName.getText());
                userName = String.valueOf(textInputEditTextUsername.getText());
                password = String.valueOf(TextInputEditTextPassowrd.getText());
                email = String.valueOf(TextInputEditTextEmail.getText());

                location = String.valueOf(textInputLocation.getText());
                number = String.valueOf(textInputNumber.getText());
                profession = String.valueOf(textInputProffesion.getText());



                Log.e("jbasijd", String.valueOf(textInputEditTextFullName.getText()));


                if(!pushemail.equals("") && !pushusername.equals("")
                        && !location.equals("") && !number.equals("")&& !profession.equals("")
                        && !usergender.equals("") && !dateofbirth.equals("")){

                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            StringRequest request = new StringRequest(Request.Method.POST,
                                    "http://papanews.in/PapaNews/signUpnew.php", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equals("File Uploaded Successfully")) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignUp.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(SignUp.this, select_category.class);
                                        startActivity(i);
                                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignUp.this, "Username Already in use.please try another", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("fullname", pushusername);
                                    map.put("username", userName);
                                    map.put("email", pushemail);
                                    map.put("password", password);
                                    map.put("location", location);
                                    map.put("gender", usergender);
                                    map.put("number", number);
                                    map.put("profession", profession);
                                    map.put("dob", dateofbirth);
                                    return map;
                                }
                            };
                            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            queue.add(request);
                        }

                    }, 1000);

                }else{
                    Toast.makeText(SignUp.this, "Please Input all fields" , Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.male:
                if (checked)
                    usergender = "Male";
                break;
            case R.id.female:
                if (checked)
                    usergender = "female";
                break;
            case R.id.other:
                if (checked)
                    usergender = "other";
                break;
        }
    }

    private void requestStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_GALLERY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void ShowFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {

            Uri filePath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                phpImage.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImages = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImages;
    }


    public boolean SetValidation(String email, String password) {
        boolean isEmailValid, isPasswordValid;
        // Check for a valid email address.
        if (email.isEmpty()) {
//            email.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            email.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else {
            isEmailValid = true;
        }

        // Check for a valid password.
        if (password.isEmpty()) {
//            password.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (password.length() < 6) {
//            password.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        } else {
            isPasswordValid = true;
        }

        if (isEmailValid && isPasswordValid) {
            Toast.makeText(SignUp.this, "Successfully", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        hideSystemUI();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        hideSystemUI();
//    }
//
//
//    private void hideSystemUI() {
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_FULLSCREEN |
//                        View.SYSTEM_UI_FLAG_LOW_PROFILE |
//                        View.SYSTEM_UI_FLAG_IMMERSIVE
//        );
//    }

}