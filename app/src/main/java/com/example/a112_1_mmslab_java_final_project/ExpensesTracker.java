package com.example.a112_1_mmslab_java_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ExpensesTracker extends AppCompatActivity {
    private Button btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_tracker);

        btn_back=findViewById(R.id.btn_back3);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpensesTracker.this, MainActivity.class);
                // 啟動 Note Activity 的 Intent
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ExpensesTracker.this.startActivity(intent);

                // 結束 MainActivity
                finish();
            }
        });
    }
}