package hpecl.uclass.download;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class MyTabOperate {

	private SQLiteDatabase db = null;

	private static final String TABLENAME = "file_setting";

	public MyTabOperate(SQLiteDatabase db) {
		this.db = db;
	}

	public void insertByCV(String file_name, String file_size,
			String file_begin, String save_time, String chapter,
			int file_count, String course_name) {
		ContentValues cv = new ContentValues();
		cv.put("file_name", file_name);
		cv.put("file_size", file_size);
		cv.put("file_begin", file_begin);
		cv.put("save_time", save_time);
		cv.put("chapter", chapter);
		cv.put("file_count", file_count);
		cv.put("course_name", course_name);
		this.db.insert(TABLENAME, null, cv);
//		this.db.close();
	}

	public void deleteByName(String file_name) {
		String whereClause = "file_name=?";
		String whereArgs[] = new String[] { file_name };
		this.db.delete(TABLENAME, whereClause, whereArgs);
//		this.db.close();
	}

	public void update(String file_name, String save_time) {
		String whereClause = "file_name=?";
		String whereArgs[] = new String[] { file_name };
		ContentValues cv = new ContentValues();
		cv.put("save_time", save_time);
		this.db.update(TABLENAME, cv, whereClause, whereArgs);
//		this.db.close();
	}

	public void updateNor(long length, int id) {
		String sql = "update " + TABLENAME
				+ " set file_size=(file_size-?) where id>=?";
		Object[] args = new Object[] { length, id };
		this.db.execSQL(sql, args);
//		this.db.close();
	}
}