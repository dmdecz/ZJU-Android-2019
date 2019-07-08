package com.example.exp1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

	DataManager data = new DataManager(new ArrayList<>(
			Arrays.asList(R.drawable.img0, R.drawable.img1, R.drawable.img2, R.drawable.img3)
	));

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ProgressBar progress_bar = findViewById(R.id.progressBar);
		progress_bar.setMax(data.get_max_id());

		final RatingBar rating_bar = findViewById(R.id.ratingBar);
		rating_bar.setMax(8);

		final ImageView img = findViewById(R.id.imageView);
		img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				data.to_next();
				img.setImageResource(data.get_picture_id());
				progress_bar.setProgress(data.get_current_id());
				rating_bar.setRating(0);
				flush_comment_table();
				Log.i("Switch pictures", "Switch picture to " + data.get_current_id());
			}
		});

		final Button btn = findViewById(R.id.button);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				TextView text = findViewById(R.id.editText);
				String comment = text.getText().toString();
				float rating = rating_bar.getRating();
				if (!comment.isEmpty() && rating != 0) {
					data.add_comment(comment);
					data.add_rating(rating);
					text.setText("");
					rating_bar.setRating(0);
					flush_comment_table();
					Log.i("Add comments", "Add a comment to " + data.get_current_id());
				}
			}
		});

		flush_comment_table();
	}

	protected void flush_comment_table() {
		final TextView comment_information = findViewById(R.id.textView);
		final TableLayout comment_table = findViewById(R.id.commentTable);
		comment_table.removeAllViews();
		ArrayList<String> comments = data.get_comments();
		String info = "Comments (" + comments.size() + ") Average Rating (" + data.get_rating() + "/8.0)";
		comment_information.setText(info);
		for (int i = 0; i < comments.size(); i++) {
			add_comment_table_row(comments.get(i));
		}
	}

	protected void add_comment_table_row(String comment) {
		final TableLayout comment_table = findViewById(R.id.commentTable);
		int row = comment_table.getChildCount();
		TextView new_row = new TextView(this);
		new_row.setText(comment);
		comment_table.addView(new_row, row);
	}
}
