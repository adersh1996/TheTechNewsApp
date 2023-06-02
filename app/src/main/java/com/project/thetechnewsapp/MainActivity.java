package com.project.thetechnewsapp;

import static com.project.thetechnewsapp.MyFirebaseMessagingService.getToken;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.project.thetechnewsapp.models.Root;
import com.project.thetechnewsapp.retrofit.APIInterface;
import com.project.thetechnewsapp.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView loginBtn;
    private TextView signUp;
    private EditText etPhoneNumber;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        String deviceToken = getToken(getApplicationContext());
         Log.d("msggg",deviceToken);
       // Toast.makeText(this, deviceToken, Toast.LENGTH_SHORT).show();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiCall();
            }
        });


    }

    public void apiCall() {

        String deviceToken = getToken(getApplicationContext());
       // Log.i("msggg",deviceToken);
        Toast.makeText(this, deviceToken, Toast.LENGTH_SHORT).show();

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.LOGINAPI(etPhoneNumber.getText().toString(), etPassword.getText().toString(), deviceToken).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        SharedPreferences sharedPreferences = getSharedPreferences("login_data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", root.userDetails.get(0).name);
                        editor.putString("phone", root.userDetails.get(0).phone);
                        editor.putString("email", root.userDetails.get(0).email);
                        editor.putString("userId", root.userDetails.get(0).id);
                        editor.commit();

                        SharedPreferences sP = getSharedPreferences("login_pref", MODE_PRIVATE);
                        SharedPreferences.Editor speditor = sP.edit();
                        speditor.putBoolean("session", true);
                        speditor.commit();

                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {

                    String message = "Server Error";
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.login_layout), message, Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                    //Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {

                String message = "Server Error";
                Snackbar snackbar = Snackbar.make(findViewById(R.id.login_layout), message, Snackbar.LENGTH_LONG);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                snackbar.show();

            }
        });
    }


    private void initView() {
        loginBtn = findViewById(R.id.login_btn);
        signUp = findViewById(R.id.signUp);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etPassword = findViewById(R.id.et_password);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do You Want To Exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}