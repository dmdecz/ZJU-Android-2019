package com.example.zju_android_2019.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zju_android_2019.R;
import com.example.zju_android_2019.db.Note;
import com.example.zju_android_2019.db.NoteState;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class NoteViewHolder extends RecyclerView.ViewHolder {

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
			new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);

	private CheckBox mCheckBox;
	private TextView mContentText;
	private TextView mDateText;
	private ImageButton mDeleteBtn;

	private NoteOperator mNoteOperator;

	public NoteViewHolder(@NonNull View itemView, NoteOperator noteOperator) {
		super(itemView);

		mCheckBox = itemView.findViewById(R.id.item_note_checkbox);
		mContentText = itemView.findViewById(R.id.item_note_tv_content);
		mDateText = itemView.findViewById(R.id.item_note_tv_date);
		mDeleteBtn = itemView.findViewById(R.id.item_note_btn_delete);

		mNoteOperator = noteOperator;
	}

	public static NoteViewHolder create(Context context, ViewGroup parent, NoteOperator noteOperator) {
		View v = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
		return new NoteViewHolder(v, noteOperator);
	}

	public void bind(final Note note) {
		mContentText.setText(note.getContent());
		mDateText.setText(SIMPLE_DATE_FORMAT.format(note.getDate()));

		mCheckBox.setChecked(note.getState().equals(NoteState.DONE));
		mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				note.setState(NoteState.from(isChecked));
				if (mNoteOperator != null) {
					mNoteOperator.updateNote(note);
				}
			}
		});

		mDeleteBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mNoteOperator != null) {
					mNoteOperator.deleteNote(note);
				}
			}
		});

		if (note.getState().equals(NoteState.DONE)) {
			mContentText.setTextColor(Color.GRAY);
			mContentText.setPaintFlags(mContentText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			mContentText.setTextColor(Color.BLACK);
			mContentText.setPaintFlags(mContentText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
		}

		itemView.setBackgroundColor(note.getPriority().getColor());
	}

}
