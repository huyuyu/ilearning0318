package hpecl.uclass.download;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTabCursor {
	private SQLiteDatabase db = null;

	public MyTabCursor(SQLiteDatabase db) {
		this.db = db;
	}

	public List<Map<String, String>> find() {
		List<Map<String, String>> allData = new ArrayList<Map<String, String>>();
		String sql = "SELECT file_name,file_size,file_begin,save_time,chapter,id FROM file_setting";

		boolean isExist = tabIsExist("file_setting", db);
		if (isExist) {
			Cursor result = this.db.rawQuery(sql, null);
			for (result.moveToFirst(); !result.isAfterLast(); result
					.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("file_name", result.getString(0));
				map.put("file_size", result.getString(1));
				map.put("file_begin", result.getString(2));
				map.put("save_time", result.getString(4));
				map.put("chapter", result.getString(5));
				map.put("id", result.getString(6));
				allData.add(map);
			}
//			this.db.close();
		}
		return allData;
	}

	public String filterByCountingAndChapter(String counting, String chapter,
			String course_name) {
		String sql = "SELECT file_begin FROM file_setting where chapter=? and file_name=? and course_name=?";
		String args[] = new String[] { chapter, counting, course_name };

		String file_begin = null;
		boolean isExist = tabIsExist("file_setting", db);
		if (isExist) {
			Cursor result = this.db.rawQuery(sql, args);
			result.moveToFirst();
			file_begin = result.getString(0);
//			this.db.close();
		}
		return file_begin;
	}

	public int getFileCounting(String chapter, String course_name) {
		String sql = "SELECT file_count FROM file_setting where chapter=? and course_name=?";
		String args[] = new String[] { chapter, course_name };

		int file_count = 0;
		boolean isExist = tabIsExist("file_setting", db);
		if (isExist) {
			Cursor result = this.db.rawQuery(sql, args);
			result.moveToFirst();
			file_count = result.getInt(0);
//			this.db.close();
		}
		return file_count;
	}

	public boolean isCourseExist(String course_name) {
		String sql = "SELECT count(*) FROM file_setting where course_name=?";
		String args[] = new String[] { course_name };

		boolean isExist = tabIsExist("file_setting", db);
		if (isExist) {
			Cursor result = this.db.rawQuery(sql, args);
			result.moveToFirst();
			if (result.getInt(0) != 0) {
				return true;
			}
//			this.db.close();
		}
		return false;
	}

	public boolean filter(String counting, String chapter) {
		String file_size = null;
		String sql = "SELECT file_size where chapter=? and file_name=?";
		String args[] = new String[] { chapter, counting };

		boolean isExist = tabIsExist("file_setting", db);
		if (isExist) {
			Cursor result = this.db.rawQuery(sql, args);
			for (result.moveToFirst(); !result.isAfterLast(); result
					.moveToNext()) {
				file_size = result.getString(0);
			}
//			this.db.close();
		}

		if (file_size == null) {
			return false;
		} else {
			if (file_size != null && file_size.length() != 0) {
				return true;
			}
		}
		return true;
	}

	public boolean tabIsExist(String tabName, SQLiteDatabase db) {
		boolean result = false;
		if (tabName == null) {
			return false;
		}
		Cursor cursor;
		try {
			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
					+ tabName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}