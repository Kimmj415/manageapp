package com.teamproject.manageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText code_et;
    private Button button_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        code_et=findViewById(R.id.code);
        button_bt=findViewById(R.id.button);

        button_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code_et.getText().toString().equals("1234")){
                    Toast.makeText(MainActivity.this, "인증 성공", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, CsActivity.class));
                }
                else{
                    Toast.makeText(MainActivity.this, "인증 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}