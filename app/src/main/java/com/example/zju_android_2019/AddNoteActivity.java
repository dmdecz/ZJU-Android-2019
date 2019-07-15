package com.example.zju_android_2019;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.zju_android_2019.db.Note;
import com.example.zju_android_2019.db.NoteDatabase;
import com.example.zju_android_2019.db.NotePriority;
import com.example.zju_android_2019.db.NoteState;

import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

	private EditText mEditText;
	private Button mAddBtn;
	private RadioGroup mRadioGroup;
	private RadioButton mLowRadio;

	private NoteDatabase mNoteDatabase;

	private AddNoteTask mAddNoteTask;

	public static void launch(Activity activity) {
		Intent intent = new Intent(activity, AddNoteActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_note);
		setTitle("Take a note");

		mNoteDatabase = Room.databaseBuilder(this, NoteDatabase.class, "todo.db")
				.addMigrations(NoteDatabase.ALL_MIGRATIONS).build();

		mEditText = findViewById(R.id.add_note_edit_text);
		mAddBtn = findViewById(R.id.add_note_btn_add);
		mRadioGroup = findViewById(R.id.add_note_radio_group);
		mLowRadio = findViewById(R.id.add_note_rbtn_low);

		mEditText.setFocusable(true);
		mEditText.requestFocus();
//		InputMethodManager inputManager = (InputMethodManager)
//				getSystemService(Context.INPUT_METHOD_SERVICE);
//		if (inputManager != null) {
//			inputManager.showSoftInput(mEditText, 0);
//		}

		mLowRadio.setChecked(true);

		mAddBtn.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("StaticFieldLeak")
			@Override
			public void onClick(View v) {
				mAddBtn.setClickable(false);
				String content = mEditText.getText().toString();
				if (content.isEmpty()) {
					Toast.makeText(AddNoteActivity.this, "Please add content.", Toast.LENGTH_SHORT).show();
					return;
				}
				Note note = new Note();
				note.setContent(content);
				note.setPriority(getSelectedPriority());
				note.setState(NoteState.from(false));
				note.setDate(new Date(System.currentTimeMillis()));

				mAddNoteTask = new AddNoteTask();
				mAddNoteTask.execute(note);
			}
		});
	}

	private NotePriority getSelectedPriority() {
		switch (mRadioGroup.getCheckedRadioButtonId()) {
			case R.id.add_note_rbtn_high:
				return NotePriority.High;
			case R.id.add_note_rbtn_medium:
				return NotePriority.Medium;
			default:
				return NotePriority.Low;
		}
	}

	@Override
	protected void onDestroy() {
		mNoteDatabase.close();
		if (mAddNoteTask != null && !mAddNoteTask.isCancelled()) {
			mAddNoteTask.cancel(true);
		}
		super.onDestroy();
	}

	private class AddNoteTask extends AsyncTask<Note, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(Note... notes) {
			return mNoteDatabase.noteDao().insertNote(notes[0]) != -1;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			super.onPostExecute(success);
			if (success) {
				Toast.makeText(AddNoteActivity.this,
						"Note added", Toast.LENGTH_SHORT).show();
				setResult(Activity.RESULT_OK);
			} else {
				Toast.makeText(AddNoteActivity.this,
						"Error", Toast.LENGTH_SHORT).show();
			}
			mAddBtn.setClickable(true);
			finish();
		}
	}
}
