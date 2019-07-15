package com.example.zju_android_2019;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DebugActivity extends AppCompatActivity {

	private static int REQUEST_CODE_STORAGE_PERMISSION = 1001;

	private Button mPathBtn, mPermissionBtn, mWriteBtn, mShowBtn;
	private TextView mPathText, mShowText;
	private EditText mWriteText;

//	private final String FILEPATH = this.getExternalCacheDir() + "/test.txt";

	public static void launch(Activity activity) {
		activity.startActivity(new Intent(activity, DebugActivity.class));
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug);

		mPathBtn = findViewById(R.id.debug_btn_print_path);
		mPermissionBtn = findViewById(R.id.debug_btn_request_permission);
		mWriteBtn = findViewById(R.id.debug_btn_write);
		mShowBtn = findViewById(R.id.debug_btn_show);
		mPathText = findViewById(R.id.debug_tv_path);
		mShowText = findViewById(R.id.debug_tv_show);
		mWriteText = findViewById(R.id.debug_et_write);

		mPathBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = "===== Internal Private =====\n" + getInternalPath()
						+ "===== External Private =====\n" + getExternalPrivatePath()
						+ "===== External Public =====\n" + getExternalPublicPath();
				mPathText.setText(text);
			}
		});

		mPermissionBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int state = ActivityCompat.checkSelfPermission(DebugActivity.this,
						Manifest.permission.WRITE_EXTERNAL_STORAGE);
				if (state == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(DebugActivity.this, "already granted",
							Toast.LENGTH_SHORT).show();
					return;
				}
				ActivityCompat.requestPermissions(DebugActivity.this,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						REQUEST_CODE_STORAGE_PERMISSION);
			}
		});

		mWriteBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					writeToFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		mShowBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					readFromFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
	                                       @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (permissions.length == 0 || grantResults.length == 0) {
			return;
		}
		if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
			int state = grantResults[0];
			if (state == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(DebugActivity.this, "permission granted",
						Toast.LENGTH_SHORT).show();
			} else if (state == PackageManager.PERMISSION_DENIED) {
				Toast.makeText(DebugActivity.this, "permission denied",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void writeToFile() throws IOException {
		File file = new File(this.getExternalCacheDir() + "/test.txt");

		FileWriter fileWriter;
		BufferedWriter bufferedWriter = null;
		try {
			fileWriter = new FileWriter(file);
			bufferedWriter = new BufferedWriter(fileWriter);

			String str = mWriteText.getText().toString();
			bufferedWriter.write(str);
			mWriteText.setText("");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
		}
	}

	private void readFromFile() throws IOException {
		File file = new File(this.getExternalCacheDir() + "/test.txt");

		FileReader fileReader;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);

			StringBuilder str = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				str.append(line).append("\n");
			}
			mShowText.setText(str.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
	}

	private String getInternalPath() {
		Map<String, File> dirMap = new LinkedHashMap<>();
		dirMap.put("cacheDir", this.getCacheDir());
		dirMap.put("filesDir", this.getFilesDir());
		dirMap.put("customDir", this.getDir("custom", MODE_PRIVATE));
		return getCanonicalPath(dirMap);
	}

	private String getExternalPrivatePath() {
		Map<String, File> dirMap = new LinkedHashMap<>();
		dirMap.put("cacheDir", this.getExternalCacheDir());
		dirMap.put("filesDir", this.getExternalFilesDir(null));
		dirMap.put("picturesDir", this.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
		return getCanonicalPath(dirMap);
	}

	private String getExternalPublicPath() {
		Map<String, File> dirMap = new LinkedHashMap<>();
		dirMap.put("rootDir", Environment.getExternalStorageDirectory());
		dirMap.put("picturesDir",
				Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
		return getCanonicalPath(dirMap);
	}

	private static String getCanonicalPath(Map<String, File> dirMap) {
		StringBuilder sb = new StringBuilder();
		try {
			for (String name : dirMap.keySet()) {
				sb.append(name)
					.append(": ")
					.append(dirMap.get(name).getCanonicalPath())
					.append('\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
