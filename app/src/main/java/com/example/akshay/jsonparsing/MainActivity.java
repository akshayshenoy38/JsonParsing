package com.example.akshay.jsonparsing;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ProgressDialog progressDialog;
    private ListView lvContacts;

    // URL to get contacts JSON
    private static String url = "https://api.androidhive.info/contacts/";
    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<>();
        lvContacts = (ListView) findViewById(R.id.lvContacts);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //to show progress dialog

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = httpHandler.makeServiceCall(url);


            Log.e(TAG, "Response from url: " + jsonStr);


            if (jsonStr!= null){


                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray contacts = jsonObject.getJSONArray("contacts");


                    // looping through all contacts
                    for (int i = 0;i<contacts.length();i++){
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String address = c.getString("address");
                        String gender = c.getString("gender");

                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");

                        //hash map for single contact
                        HashMap<String,String> contact = new HashMap<>();
                        // adding each child node to HashMap key => value
                        contact.put("id",id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "doInBackground: Json parsing error");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"json parsing Error",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Log.d(TAG, "doInBackground: Could not get json from server");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"could not get json from server",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(MainActivity.this,
                    contactList,
                    R.layout.list_item,
                    new String[]{"name","email","mobile"},
                    new int[]{R.id.tvName,R.id.tvEmail,R.id.tvMobile});

            lvContacts.setAdapter(adapter);
        }
    }
}
