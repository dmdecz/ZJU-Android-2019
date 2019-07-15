package com.example.zju_android_2019.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zju_android_2019.db.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteViewHolder> {

	private List<Note> mNotes = new ArrayList<>();

	private NoteOperator mNoteOperator;

	public NoteListAdapter(NoteOperator noteOperator) {
		mNoteOperator = noteOperator;
	}

	@NonNull
	@Override
	public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return NoteViewHolder.create(parent.getContext(), parent, mNoteOperator);
	}

	@Override
	public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
		holder.bind(mNotes.get(position));
	}

	@Override
	public int getItemCount() {
		return mNotes.size();
	}

	public void refresh(List<Note> notes) {
		if (notes == null) {
			return;
		}
		mNotes = notes;
		notifyDataSetChanged();
	}
}
