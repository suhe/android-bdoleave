package com.example.bdo.androidbdoleave;

/**
 * Created by suhe on 2/11/2016.
 */
import android.app.Activity;
import android.os.AsyncTask;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bdo.androidbdoleave.config.app;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity  {
    EditText edNik;
    EditText edPassword;
    Button btnLogin;
    //TextView txtresult;
    protected String employeeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        edNik = (EditText) findViewById(R.id.nik);
        edPassword = (EditText) findViewById(R.id.password);
        //txtresult = (TextView) findViewById(R.id.resultTextView);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //status variabel for field login
                boolean isNik = true;
                boolean isPassword = true;

                if (edNik.getText().toString().trim().equalsIgnoreCase("")) {
                    edNik.setError("The Field NIK is Required");
                    isNik = false;
                }

                if (edPassword.getText().toString().trim().equalsIgnoreCase("")) {
                    edPassword.setError("The Field Password is Required");
                    isPassword = false;
                }

                //if nik and password is fill
                if (isNik == true && isPassword == true) {
                    employeeid = edNik.getText().toString();
                    String passtext = edPassword.getText().toString();
                    AsyncDataClass asyncRequestObject = new AsyncDataClass();
                    asyncRequestObject.execute(app.serverUrl + "login/", employeeid, passtext);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private class AsyncDataClass extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(params[0]);
            String jsonResult = "";

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("employeeid", params[1]));
                nameValuePairs.add(new BasicNameValuePair("passtext", params[2]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
            } catch (ClientProtocolException ex) {
                ex.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonResult;
        }

        /**
         *  Function input stream
         */
        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {
                while ((rLine = br.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return answer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("Resulted Value: " + result);
            if(result.equals("") || result == null){
                Toast.makeText(LoginActivity.this, "Server connection failed", Toast.LENGTH_LONG).show();
                return;
            }

            int jsonResult = returnParsedJsonObject(result);
            if(jsonResult == 0){
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                return;
            }
            else if(jsonResult == 1){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("EMPLOYEEID", employeeid);
                intent.putExtra("MESSAGE", "You have been successfully login");
                startActivity(intent);
            }
        }

        private int returnParsedJsonObject(String result) {
            JSONObject resultObject = null;
            int returnedResult = 0;
            try {
                resultObject = new JSONObject(result);
                returnedResult = resultObject.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return  returnedResult;
        }

    }



}
