package com.example.zju_android_2019.GlideImage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.zju_android_2019.R;

import java.util.ArrayList;
import java.util.List;

public class GlideImageActivity extends AppCompatActivity {

	private String[] mPermissions = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE};
	private final static int REQUEST_PERMISSION = 757;

	private LayoutInflater mLayoutInflater;
	private ViewPager mViewPager;
	private List<View> mViewPages = new ArrayList<>();
	private PagerAdapter mAdapter;

	public static void launch(Activity activity) {
		activity.startActivity(new Intent(activity, GlideImageActivity.class));
	}

	@SuppressLint("SdCardPath")
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_glide_image);

		findViewById(R.id.glide_image_btn_grant).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!GlideImageActivity.this.checkAllPermissionGranted(mPermissions)) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						requestPermissions(mPermissions, REQUEST_PERMISSION);
					}
				} else {
					Toast.makeText(GlideImageActivity.this, "已经获取所有所需权限", Toast.LENGTH_SHORT).show();
				}
			}
		});

		mLayoutInflater = getLayoutInflater();
		mViewPager = findViewById(R.id.glide_image_view_pager);
		mAdapter = new PagerAdapter() {
			@Override
			public int getCount() {
				return mViewPages.size();
			}

			@Override
			public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
				return view == object;
			}

			@NonNull
			@Override
			public Object instantiateItem(@NonNull ViewGroup container, int position) {
				View view = mViewPages.get(position);
				container.addView(view);
				return view;
			}

			@Override
			public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
				container.removeView(mViewPages.get(position));
			}
		};

		loadImages();
	}

	private void loadImages() {
		mViewPages.clear();
 		addImage("/sdcard/fileimage.jpg", mPermissions);
		addImage(R.drawable.drawableimage);
		addImage(R.drawable.ic_markunread);
		addImage("file:///android_asset/assetsimage.jpg");
		addImage(R.raw.rawimage);
		addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562328963756&di=9c0c6c839381c8314a3ce8e7db61deb2&imgtype=0&src=http%3A%2F%2Fpic13.nipic.com%2F20110316%2F5961966_124313527122_2.jpg");
		mViewPager.setAdapter(mAdapter);
	}

	private void addImage(int resId) {
		ImageView imageView = (ImageView) mLayoutInflater.inflate(R.layout.layout_glide_image_item, null);
		Glide.with(this)
				.load(resId)
				.error(R.drawable.error)
				.into(imageView);
		mViewPages.add(imageView);
	}

	private void addImage(String path, String[] permissions) {
		if (checkAllPermissionGranted(permissions)) {
			ImageView imageView = (ImageView) mLayoutInflater.inflate(R.layout.layout_glide_image_item, null);
			Glide.with(this)
					.load(path)
					.apply(new RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
					.error(R.drawable.error)
					.into(imageView);
			mViewPages.add(imageView);
		}
	}

	private void addImage(String path) {
		addImage(path, null);
	}

	public boolean checkAllPermissionGranted(String[] permissions) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || permissions == null || permissions.length == 0) {
			return true;
		}
		for (String permission : permissions) {
			if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == REQUEST_PERMISSION) {
			for (int i = 0; i < permissions.length; i++) {
				if (grantResults.length > i && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, "已经授权" + permissions[i], Toast.LENGTH_LONG).show();
				}
			}
			loadImages();
		}
	}

}
