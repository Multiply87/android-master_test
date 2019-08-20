package com.example.privatchat.Services;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.example.privatchat.view.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static com.example.privatchat.Services.NetworkUtils.getResponseFromURL;

public class CheckLogin extends AsyncTask<URL, Void, String> {
  String login;
  String password;
  ProgressBar loadingIndicator;

    public CheckLogin(String login, String password, ProgressBar loadingIndicator) {
        this.login = login;
        this.password = password;
        this.loadingIndicator = loadingIndicator;
    }

    @Override
    protected void onPreExecute() {
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(URL... urls) {
        String response = null;

        try {
            response = getResponseFromURL(NetworkUtils.setRequest(urls[0], login, password));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        Boolean access = null;
        String resultText = null;
        String nowTime = null;
        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*if (response != null && response != "") {
            try {
                JSONObject jsonResponse = new JSONObject(response);

                access = jsonResponse.getBoolean("access");
                resultText = jsonResponse.getString("resultText");
                nowTime = jsonResponse.getString("nowTime");

            } catch (JSONException e) {
                e.printStackTrace();

            }

            System.out.println(response);
            if (access == false || resultText == null || nowTime == null) {
                MainActivity.showErrorTextView(resultText);
            } else {
                nextActivity("NOW: " + nowTime + "\naccess: " + access + "\nresultText: " + resultText);
            }
        } else {
            MainActivity.showErrorTextView(resultText);
        }*/
        MainActivity.nextActivity(jsonResponse);
        loadingIndicator.setVisibility(View.INVISIBLE);
    }


}
