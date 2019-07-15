package com.example.zju_android_2019;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.zju_android_2019.db.Note;
import com.example.zju_android_2019.db.NoteDatabase;
import com.example.zju_android_2019.ui.NoteListAdapter;
import com.example.zju_android_2019.ui.NoteOperator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class MainActivity extends AppCompatActivity {

//	private Toolbar mToolBar;
	private RecyclerView mRecyclerView;
	private NoteListAdapter mNoteListAdapter;
	private FloatingActionButton mAddButton;

	private NoteDatabase mNoteDatabase;

	private RefreshNoteTask mRefreshNoteTask;
	private UpdateNoteTask mUpdateNoteTask;
	private DeleteNoteTask mDeleteNoteTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mRecyclerView = findViewById(R.id.main_rv_note);
		mAddButton = findViewById(R.id.main_fab_add);

		mNoteDatabase = Room.databaseBuilder(this, NoteDatabase.class, "todo.db")
				.addMigrations(NoteDatabase.ALL_MIGRATIONS).build();

		initUI();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_settings:
				return true;
			case R.id.action_debug:
				DebugActivity.launch(this);
				return true;
			default:
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void initUI() {
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
		mNoteListAdapter = new NoteListAdapter(new NoteOperator() {
			@Override
			public void updateNote(Note note) {
				MainActivity.this.updateNote(note);
			}

			@Override
			public void deleteNote(Note note) {
				MainActivity.this.deleteNote(note);
			}
		});
		mRecyclerView.setAdapter(mNoteListAdapter);

		mAddButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AddNoteActivity.launch(MainActivity.this);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		refreshRecyclerView();
	}

	@Override
	protected void onDestroy() {
		mNoteDatabase.close();
		super.onDestroy();
	}

	private void refreshRecyclerView() {
		mRefreshNoteTask = new RefreshNoteTask();
		mRefreshNoteTask.execute();
	}

	private void updateNote(Note note) {
		if (mNoteDatabase == null) {
			return;
		}
		mUpdateNoteTask = new UpdateNoteTask();
		mUpdateNoteTask.execute(note);
	}

	private void deleteNote(Note note) {
		if (mNoteDatabase == null) {
			return;
		}
		mDeleteNoteTask = new DeleteNoteTask();
		mDeleteNoteTask.execute(note);
	}

	@SuppressLint("StaticFieldLeak")
	private class RefreshNoteTask extends AsyncTask<Void, Integer, List<Note>> {
		@Override
		protected List<Note> doInBackground(Void... voids) {
			return mNoteDatabase.noteDao().getAllNotes();
		}

		@Override
		protected void onPostExecute(List<Note> notes) {
			super.onPostExecute(notes);
			mNoteListAdapter.refresh(notes);
		}
	}

	@SuppressLint("StaticFieldLeak")
	private class UpdateNoteTask extends AsyncTask<Note, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(Note... notes) {
			return mNoteDatabase.noteDao().updateNote(notes[0]) > 0;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			super.onPostExecute(success);
			refreshRecyclerView();
		}
	}

	@SuppressLint("StaticFieldLeak")
	private class DeleteNoteTask extends AsyncTask<Note, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(Note... notes) {
			return mNoteDatabase.noteDao().deleteNote(notes[0]) > 0;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			super.onPostExecute(success);
			refreshRecyclerView();
		}
	}
}
