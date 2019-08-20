package com.example.privatchat.view;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.privatchat.R;
import com.example.privatchat.Services.CheckLogin;
import com.example.privatchat.Services.CustomTextWatcher;
import com.example.privatchat.Services.NetworkUtils;
import com.example.privatchat.mainclass.User;
import com.example.privatchat.model.singlton.App;
import com.example.privatchat.viewmodel.MyViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static com.example.privatchat.Services.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {
    static MyViewModel myViewModel;
    EditText editText;
    private static EditText login;
    private EditText password;
    private Button logInButton;
    private static TextView result;
    private static TextView errorMessage;
    private ProgressBar loadingIndicator;

    public static void nextActivity(JSONObject jsonResponse) {
        Log.d("resp1", "bla bla");
        try {
            if (jsonResponse.getBoolean("access")==true) {
                myViewModel.getUserldap(login.getText().toString(), App.getInstance().getApplicationContext());
            }else{
                showErrorTextView("Неправильный логин или пароль");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("");

        editText  = findViewById(R.id.LDAP);
        login = findViewById(R.id.et_login);
        password = findViewById(R.id.et_password);
        EditText[] edList = {login, password};
        logInButton = findViewById(R.id.logIn);
        result = findViewById(R.id.tv_result);
        errorMessage = findViewById(R.id.tv_error_message);
        loadingIndicator = findViewById(R.id.pb_loading_indicator);
        logInButton.setEnabled(false);

        CustomTextWatcher textWatcher = new CustomTextWatcher(edList, logInButton);
        for (EditText editText : edList) editText.addTextChangedListener(textWatcher);

        SetOnForusChangeListener onForusChangeListener = new SetOnForusChangeListener();
        login.setOnFocusChangeListener(onForusChangeListener);
        password.setOnFocusChangeListener(onForusChangeListener);

        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);//переходим во вьюмодел
        getLifecycle().addObserver(myViewModel);
    }

    public void getDataUser(View view)  {
        //String LDAP = editText.getText().toString();
        //myViewModel.getUserldap(LDAP, this);
        errorMessage.setText("");
        URL generatedUrl = NetworkUtils.generateURL(getString(R.string.url));
        if (generatedUrl != null)
            new CheckLogin(login.getText().toString(), password.getText().toString(), loadingIndicator).execute(generatedUrl);
        else
            Toast.makeText(MainActivity.this, "URL null!", Toast.LENGTH_SHORT).show();
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showErrorTextView(String errorText) {
        result.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(errorText);
    }

    public class SetOnForusChangeListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        }
    }



}
