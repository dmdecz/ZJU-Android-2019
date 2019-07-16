package com.example.zju_android_2019;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zju_android_2019.GlideImage.GlideImageActivity;


public class MainActivity extends AppCompatActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.main_btn_glide_image).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GlideImageActivity.launch(MainActivity.this);
			}
		});

		findViewById(R.id.main_btn_ijk_player).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				IJKPlayerActivity.launch(MainActivity.this);
			}
		});
	}

}
