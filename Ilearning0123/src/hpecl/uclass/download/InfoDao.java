package hpecl.uclass.download;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class InfoDao {

	MyDatabaseHelper dbHelper;

	public InfoDao(Context context) {
		dbHelper = new MyDatabaseHelper(context);
	}

	public Cursor findAllByCursor() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		if (db.isOpen()) {
			cursor = db.rawQuery("select * from file_setting",null);
		}
		return cursor;
	}
}
