package com.example.exp1;

import java.text.DecimalFormat;
import java.time.format.DecimalStyle;
import java.util.ArrayList;

public class DataManager {
	private ArrayList<Integer> picture_ids;
	private ArrayList<ArrayList<String>> comments;
	private ArrayList<Float> ratings;

	private int current_id;

	public DataManager(ArrayList<Integer> picture_ids) {
		current_id = 0;
		this.picture_ids = picture_ids;
		comments = new ArrayList<>();
		ratings = new ArrayList<>();
		for (int i = 0; i < picture_ids.size(); i++) {
			comments.add(new ArrayList<String>());
			ratings.add(0.0f);
		}
	}

	public void add_comment(String comment) {
		comments.get(current_id).add(comment);
	}

	public ArrayList<String> get_comments() {
		return get_comments(current_id);
	}

	public ArrayList<String> get_comments(int id) {
		return comments.get(id);
	}

	public void to_next() {
		current_id = (current_id + 1) % picture_ids.size();
	}

	public void to_prev() {
		current_id = (current_id - 1 + picture_ids.size()) % picture_ids.size();
	}

	public int get_picture_id() {
		return get_picture_id(current_id);
	}

	public int get_picture_id(int id) {
		return picture_ids.get(id);
	}

	public int get_current_id() {
		return current_id;
	}

	public int get_max_id() {
		return picture_ids.size() - 1;
	}

	public void add_rating(float rating) {
		ratings.set(current_id, ratings.get(current_id) + rating);
	}

	public float get_rating() {
		return get_rating(current_id);
	}

	public float get_rating(int id) {
		if (comments.get(id).isEmpty()) {
			return 0;
		} else {
			return ratings.get(id) / comments.get(id).size();
		}
	}
}
