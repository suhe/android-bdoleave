package com.example.bdo.androidbdoleave;

/**
 * Created by suhe on 14/02/16.
 */
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bdo.androidbdoleave.models.Leave;
import com.example.bdo.androidbdoleave.adapters.MyLeave;
import com.example.bdo.androidbdoleave.config.app;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class MyLeaveActivity extends Fragment {
    ArrayList<Leave> records;
    MyLeave adapter;
    ListView listMyLeave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.myleave_listview,container,false);
        records = new ArrayList<Leave>();
        listMyLeave = (ListView) v.findViewById(R.id.custom_list);
        Context context = getActivity().getApplicationContext();
        adapter = new MyLeave(context, R.layout.myleave_rows,R.id.date_from, records);
        listMyLeave.setAdapter(adapter);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        MyLeaveList();
    }

    private void MyLeaveList() {
        class doLeave extends AsyncTask<String,Void,String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(getActivity(),"Please Wait", "Loading");
            }

            @Override
            protected String doInBackground(String... strings) {
                InputStream is = null;
                String result = "";
                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(app.serverUrl+"myleave");
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    is.close();
                    result = sb.toString();
                    //result = result.substring(result.indexOf("["));
                    JSONArray jArray =new JSONArray(result);
                    for(int i = 0;i < jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        Leave leave = new Leave();
                        leave.setDateFrom(json_data.getString("leave_date_from"));
                        leave.setDateTo(json_data.getString("leave_date_to"));
                        leave.setDescription(json_data.getString("leave_description"));
                        leave.setStatus(json_data.getString("leave_status"));
                        records.add(leave);
                    }


                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(loadingDialog !=null) {
                    loadingDialog.dismiss(); //close dialog
                }

                Log.e("size", records.size() + "");
                adapter.notifyDataSetChanged(); //notify the ListView to get new records

            }
        }

        doLeave leave = new doLeave();
        leave.execute();
    }
}
