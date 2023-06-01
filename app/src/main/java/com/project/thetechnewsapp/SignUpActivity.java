package com.project.thetechnewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.project.thetechnewsapp.models.Root;
import com.project.thetechnewsapp.retrofit.APIInterface;
import com.project.thetechnewsapp.retrofit.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    private EditText nameEt;
    @NotEmpty
    @Email
    private EditText emailEt;
    @NotEmpty
    @Length(max = 10, min = 10, message = "Enter a valid mobile number")
    private EditText phoneNumberEt;
    @NotEmpty
    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    private EditText passwordEt;
    @NotEmpty
    @ConfirmPassword
    private EditText retypePasswordEt;
    private Button saveBtn;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        validator = new Validator(this);
        validator.setValidationListener(this);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

    }

    @Override
    public void onValidationSucceeded() {
        regApi();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }

        }
    }

    private void initView() {
        nameEt = findViewById(R.id.name_et);
        emailEt = findViewById(R.id.email_et);
        phoneNumberEt = findViewById(R.id.phone_number_et);
        passwordEt = findViewById(R.id.password_et);
        retypePasswordEt = findViewById(R.id.retype_password_et);
        saveBtn = findViewById(R.id.save_btn);
    }

    void regApi() {
        String name = nameEt.getText().toString();
        String email = emailEt.getText().toString();
        String phone = phoneNumberEt.getText().toString();
        String password = retypePasswordEt.getText().toString();

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.REG_API(name, email, phone, password).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignUpActivity.this, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}