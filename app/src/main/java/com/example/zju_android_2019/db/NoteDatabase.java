package com.example.zju_android_2019.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Note.class}, version = 2, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

	public abstract NoteDao noteDao();

	public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
		@Override
		public void migrate(@NonNull SupportSQLiteDatabase database) {
			database.execSQL("ALTER table notes ADD COLUMN priority INTEGER NOT NULL DEFAULT 0");
		}
	};

	public static final Migration[] ALL_MIGRATIONS = new Migration[] {MIGRATION_1_2};
}
