package com.example.helloandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Simple activity that is almost fully controlled by OS
public class OtherActivity extends AppCompatActivity {

    public static final String resultKey = "RESULT";
    Button bAnswer;
    EditText eAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_other);

        bAnswer = findViewById(R.id.bAnswer);
        eAnswer = findViewById(R.id.eAnswer);

        Bundle arguments = getIntent().getExtras();
        String data = (String) arguments.get(MainActivity.key).toString();

        Toast.makeText(getApplicationContext(),"Sended: " + data,Toast.LENGTH_LONG).show();

        bAnswer.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(resultKey, eAnswer.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}