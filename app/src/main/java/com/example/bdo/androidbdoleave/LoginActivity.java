package com.example.bdo.androidbdoleave;

/**
 * Created by suhe on 2/11/2016.
 */
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bdo.androidbdoleave.config.app;
import com.example.bdo.androidbdoleave.helpers.Auth;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends Activity  {
    private EditText txtemployeeid;
    private EditText txtpassword;
    private Button btnLogin;

    public static final String EMPLOYEEID = "EMPLOYEEID";

    String employeeid;
    String password;

    private Auth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //auth for check login
        auth = new Auth(getApplicationContext());
        if(auth.isLogin()) {
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }

        txtemployeeid = (EditText) findViewById(R.id.nik);
        txtpassword = (EditText) findViewById(R.id.password);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employeeid = txtemployeeid.getText().toString();
                password = txtpassword.getText().toString();

                boolean isEmployeeID = true;
                boolean isPassword = true;
                if(employeeid.length() == 0 ) {
                    isEmployeeID = false;
                    txtemployeeid.setError("required, Please fill field NIK");
                }

                if(password.length() == 0) {
                    isPassword = false;
                    txtpassword.setError("required, Please fill field Password");
                }

                if(isEmployeeID == true && isPassword == true) {
                    getLogin(employeeid, password);
                }
            }
        });
    }

    private void getLogin(final String employeeid,String password) {
        class doLogin extends AsyncTask<String,Void,String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(LoginActivity.this,"Please Wait","Loading");
            }

            @Override
            protected String doInBackground(String... params) {
                String eid = params[0];
                String epass = params[1];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("employeeid",eid));
                nameValuePairs.add(new BasicNameValuePair("passtext",epass));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(app.serverUrl+"login");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                String s = result.trim();
                loadingDialog.dismiss();

                JSONObject object = null;
                int jsonResult  = 0;
                String jsonID = "";
                String jsonName = "";
                String jsonEmail = "";
                try {
                    object = new JSONObject(s);
                    jsonResult = object.getInt("success");
                    jsonID = object.getString("employee_id");
                    jsonName = object.getString("employee_name");
                    jsonEmail = object.getString("employee_email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("Resulted Value: " + jsonName);

                if(s.equals("") || s == null){
                    Toast.makeText(LoginActivity.this, "Server connection failed", Toast.LENGTH_LONG).show();
                    return;
                } else if(jsonResult == 0) {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                    return;
                } else if(jsonResult == 1) {
                    //put to auth
                    auth.login(true);
                    auth.setName(jsonName);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }

        doLogin login = new doLogin();
        login.execute(employeeid,password);

    }
}
