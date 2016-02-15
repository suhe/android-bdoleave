package com.example.bdo.androidbdoleave;

/**
 * Created by suhe on 2/11/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

public class LoginActivity extends Activity implements ValidationListener {

    @NotEmpty(sequence = 1, message = "Please NIK Do Not Empty")
    EditText edNik;
    @NotEmpty(sequence = 2, message = "Please Password Do Not Empty")
    EditText edPassword;
    Button btnLogin;
    TextView txtresult;

    private Validator mValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        edNik = (EditText) findViewById(R.id.nik);
        edPassword = (EditText) findViewById(R.id.password);
        txtresult = (TextView) findViewById(R.id.resultTextView);

        // Validator
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mValidator.validate();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ValidationError error : errors) {
            stringBuilder.append(error.getCollatedErrorMessage(this))
                    .append('\n');
        }
        txtresult.setText(stringBuilder.toString().trim());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
