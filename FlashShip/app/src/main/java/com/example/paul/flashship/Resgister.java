package com.example.paul.flashship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.paul.flashship.Shipper.ResgisterShipper;
import com.example.paul.flashship.Store.ResgisterStore;

public class Resgister extends AppCompatActivity {
    ImageButton btnStore, btnShipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgister);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();

        btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Resgister.this,ResgisterStore.class);
                startActivity(intent);
            }
        });

        btnShipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Resgister.this,ResgisterShipper.class);
                startActivity(intent);
            }
        });

    }

    private void init() {
        btnStore = findViewById(R.id.btnStore);
        btnShipper = findViewById(R.id.btnShipper);
    }
}
