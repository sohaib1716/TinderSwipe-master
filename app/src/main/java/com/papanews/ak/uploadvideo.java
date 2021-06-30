package com.papanews.ak;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.load.resource.bytes.BytesResource;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;
import com.papanews.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okio.ByteString;

public class uploadvideo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText t1, t2;
    Button browse, upload, srcselect;
    ImageView img, srcImage;
    Bitmap bitmap, srcBitmap;
    String encodeImageString = "", encodeSourceImage = "", Document = "";
    EditText longphp, videophp, newsphp, locationphp;
    String catSelected, image_location;
    Button saved;
    CheckBox eng, hind;
    int[] check = {0};
    String audio = "0";
    DatePicker picker;
    Button btnGet;
    TextView tvw;
    ProgressBar progressBar;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    Button setdate;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String username, google_image, image_fb;
    Button browseVideo;
    TextView pathofvideo;

    private static final int SELECT_VIDEO = 5;
    private String selectedVideoPath;

    private static final String url = "http://papanews.in/PapaNews/userUploads.php";

    String[] users = {"Politics", "Technology", "Sports", "Startup", "Entertaintment",
            "Business", "International", "Influence", "Miscellaneous"};

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadvideo);

        img = (ImageView) findViewById(R.id.img);
        srcImage = (ImageView) findViewById(R.id.srcimage);
        srcselect = (Button) findViewById(R.id.selectImage);
        upload = (Button) findViewById(R.id.upload);
        browse = (Button) findViewById(R.id.browse);
        saved = (Button) findViewById(R.id.savedPosts);
        eng = findViewById(R.id.English);
        hind = findViewById(R.id.hindi);
        progressBar = findViewById(R.id.prog);

        tvw = (TextView) findViewById(R.id.selectDate);
        picker = (DatePicker) findViewById(R.id.datePicker1);
        browseVideo = findViewById(R.id.browusevideo);
        pathofvideo = findViewById(R.id.pathvideo);


        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        username = sharedPreferences.getString("user_final", "");
        google_image = sharedPreferences.getString("google_image", "");
        image_fb = sharedPreferences.getString("image_fb", "");

//        btnGet=(Button)findViewById(R.id.button1);
//        btnGet.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onClick(View v) {
//                picker.setVisibility(View.VISIBLE);
//                picker.bringToFront();
//                tvw.setText(picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear());
//
//            }
//        });

        Spinner spin = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);

        browseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_VIDEO);
            }
        });


        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(uploadvideo.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Browse Image"), 1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.setVisibility(View.VISIBLE);
                picker.bringToFront();
                tvw.setText(picker.getDayOfMonth() + "/" + (picker.getMonth() + 1) + "/" + picker.getYear());
                uploaddatatodb();
            }
        });

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);
                pathofvideo.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
            } catch (Exception ex) {
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                selectedVideoPath = getPath(data.getData());
                try {
                    if (selectedVideoPath == null) {
                        Log.e("videoPath", "selected video path = null!");
                        finish();
                    } else {
                        pathofvideo.setVisibility(View.VISIBLE);
                        img.setVisibility(View.GONE);
                        pathofvideo.setText(selectedVideoPath);
                        Log.e("videoPath", selectedVideoPath);
                    }
                } catch (Exception ex) {
                    //#debug
                    ex.printStackTrace();
                    Log.e("error video :", String.valueOf(ex));
                }
            }

        }

        if (!image_fb.equals("")) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(Uri.parse(image_fb));
                bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
                encodeSource(bitmap);
            } catch (Exception ex) {
            }
        }
        if (!google_image.equals("")) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(Uri.parse(image_fb));
                bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
                encodeSource(bitmap);
            } catch (Exception ex) {
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        ConvertToString(uri);
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else return null;
    }

    public void ConvertToString(Uri uri) {
        String uriString = uri.toString();
        Log.d("data uri", "onActivityResult: uri" + uriString);
        //            myFile = new File(uriString);
        //            ret = myFile.getAbsolutePath();
        //Fpath.setText(ret);
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            byte[] bytes = getBytes(in);
            Log.d("data length", "onActivityResult: bytes size=" + bytes.length);
            Log.d("data encode", "onActivityResult: Base64string=" + Base64.encodeToString(bytes, Base64.DEFAULT));
            String ansValue = Base64.encodeToString(bytes, Base64.DEFAULT);
            Log.d("data ansValue", ansValue);
            Document = Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.d("error", "onActivityResult: " + e.toString());
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
        encodeImageString = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }


    private void encodeSource(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
        encodeSourceImage = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }


    private void uploaddatatodb() {
        progressBar.setVisibility(View.VISIBLE);
        longphp = (EditText) findViewById(R.id.longPhp);
        videophp = (EditText) findViewById(R.id.videoId);
        newsphp = (EditText) findViewById(R.id.NewsSource);
        locationphp = (EditText) findViewById(R.id.location);
        t1 = (EditText) findViewById(R.id.t1);
        t2 = (EditText) findViewById(R.id.t2);

        Toast.makeText(getApplicationContext(), "English", Toast.LENGTH_LONG).show();
        final String title = t1.getText().toString().trim();
        final String shdesc = t2.getText().toString().trim();
        final String longDescription = longphp.getText().toString().trim();
        final String videoyoutube = videophp.getText().toString().trim();
        final String newsSourcename = newsphp.getText().toString().trim();
        final String location = locationphp.getText().toString().trim();
        final String date = tvw.getText().toString().trim();

        if(!title.equals("") || !shdesc.equals("") || !longDescription.equals("") || !location.equals("")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("File Uploaded Successfully")){
                                progressBar.setVisibility(View.GONE);
                                t1.setText("");
                                t2.setText("");
                                longphp.setText("");
                                locationphp.setText("");
                                pathofvideo.setText("");
                                img.setImageResource(R.drawable.ic_launcher_foreground);
                            }
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
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
                            map.put("t1", title);
                            map.put("t2", shdesc);
                            map.put("long", longDescription);
                            map.put("sorname", username);
                            map.put("upload", encodeImageString);
                            map.put("location", location);
                            map.put("date", date);
                            map.put("sorimage", encodeSourceImage);
                            map.put("videou", Document);
                            return map;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(request);
                }
            }, 2000);

        }


    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rg5r1:
                if (checked)
                    audio = "0";
                break;
            case R.id.rg5r2:
                if (checked)
                    audio = "1";
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Selected User: " + users[position], Toast.LENGTH_SHORT).show();
//        String[] users = { "Politics", "Technology", "Sports", "Startup", "Entertaintment", "Business" };
        if (users[position].equals("Politics")) {
            catSelected = "politics";
        }
        if (users[position].equals("Technology")) {
            catSelected = "Tech";
        }
        if (users[position].equals("Sports")) {
            catSelected = "sports";
        }
        if (users[position].equals("Startup")) {
            catSelected = "sports";
        }
        if (users[position].equals("Entertaintment")) {
            catSelected = "entertain";
        }
        if (users[position].equals("Business")) {
            catSelected = "business";
        }
        if (users[position].equals("International")) {
            catSelected = "international";
        }
        if (users[position].equals("Influence")) {
            catSelected = "influence";
        }
        if (users[position].equals("Miscellaneous")) {
            catSelected = "miscell";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}