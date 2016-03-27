package hpecl.uclass.download;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper extends SQLiteOpenHelper {

	private static final String DatabaseName = "umeeting.db";

	private static final int DatabaseVersion = 1;

	public MyDatabaseHelper(Context context) {
		super(context, DatabaseName, null, DatabaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql1 = "create table if not exists file_setting ("
				+ "file_name varchar(20) not null,"
				+ "file_size varchar(20) not null,"
				+ "file_begin varchar(20) not null,"
				+ "save_time varchar(20) not null,"
				+ "chapter varchar(10) not null,"
				+ "file_count integer not null,"
				+ "course_name varchar(10) not null,"
				+ "id integer primary key)";

		db.execSQL(sql1);
		Log.i("file_setting", "created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql1 = "drop table if exists file_setting";

		db.execSQL(sql1);
		this.onCreate(db);
	}
}
