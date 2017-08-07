package com.example.finalproject;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by ketmany on 16/03/2017.
 */

public class ResultPage extends Activity{
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "ScannerResult";
    final static String key = "AIzaSyCVUJya7KSvIbGpq6WSbhle67Pj5LbUYD0";
    final static String customSearh = "005363878473631321198:vhodr_d9m-e";
    final static String searchURL = "https://www.googleapis.com/customsearch/v1?key=";

    ProgressBar progressBar;
    private TextView productTitle;
    private TextView productLink;
    private ImageView productImage;
    String query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_result);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Log.d("Search", "*** Search Start");

        productImage = (ImageView) findViewById(R.id.productImage);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        productTitle = (TextView) findViewById(R.id.productTitle);
        productLink = (TextView) findViewById(R.id.productLink);


        Intent intent = getIntent();
        String input = intent.getExtras().getString("input_value");
        assert input != null;
        query = input.replaceAll("\\s","");
        if (query.contains("/")){
            if (query.length()>7){
                query = query.replaceAll("/[^/]+$", "");
            }

        }

        checkInternetConnection();
        new RetrieveInfo().execute();



    }

    private class RetrieveInfo extends AsyncTask <Void, Void, String> {
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            productTitle.setText("");
        }

        protected String doInBackground(Void... urls){
            try{
            URL url = new URL(searchURL+key + "&cx=" + customSearh + "&q=" + query);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {

                BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder buff = new StringBuilder();
                String output;
                while ((output = buffer.readLine()) != null) {
                    buff.append(output);
                }
                buffer.close();
                return buff.toString();
            }
            finally {
                conn.disconnect();
            }
            }
            catch(Exception e){
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response){
            if (response == null){
                response = "THERE WAS AN ERROR";

            }

            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);

            try {
                JSONObject object = new JSONObject (response);
                JSONObject queries = object.getJSONObject("queries");
                JSONArray requests = queries.getJSONArray("request");
                for (int i = 0; i < requests.length(); i++){
                    JSONObject request = requests.getJSONObject(i);
                    int totalResults = request.getInt("totalResults");
                    if (totalResults > 0) {
                        JSONArray items = object.getJSONArray("items");
                        for (int j = 0; j < items.length(); j++) {
                            JSONObject item = items.getJSONObject(j);
                            String title = item.getString("title");
                            String link = item.getString("link");
                            String displayLink = item.getString("displayLink");
                            JSONObject pagemap = item.getJSONObject("pagemap");

                            JSONArray cse = pagemap.getJSONArray("cse_image");
                            for (int x=0; x < cse.length(); x++){
                                JSONObject image = cse.getJSONObject(x);
                                String img = image.getString("src");
                                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(img).getContent());
                                productImage.setImageBitmap(bitmap);
                            }
                            productLink.setText(link);
                            productTitle.setText(title);

                            Spanned urlLink = Html.fromHtml("Visit the product page: " + "<a href='"+ link +"'>"+ displayLink +"</a>");
                            productLink.setMovementMethod(LinkMovementMethod.getInstance());
                            productLink.setText(urlLink);
                        }

                    }
                    else {
                        productTitle.setText("No data found");
                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }


}
