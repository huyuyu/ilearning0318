package hpecl.uclass.download;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class InfoProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    private static final int ALL_FILE = 1;

    static {
        uriMatcher.addURI("hpecl.uclass.download.InfoProvider", "files",ALL_FILE);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int result = uriMatcher.match(uri);
        switch (result) {
            case ALL_FILE:
                InfoDao dao = new InfoDao(this.getContext());
                return dao.findAllByCursor();
            default:
                throw new RuntimeException("无法识别该URI,出错了!!");
        }
    }

    @Override
    public String getType(Uri uri) {
        int result = uriMatcher.match(uri);
        switch (result) {
            case ALL_FILE:
                return "List<File>";
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int result = uriMatcher.match(uri);
        switch (result) {
            default:
                throw new RuntimeException("无法识别该URI,出错了!!");
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int result = uriMatcher.match(uri);
        switch (result) {
            default:
                throw new RuntimeException("无法识别该URI,出错了!!");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int result = uriMatcher.match(uri);
        switch (result) {
            default:
                throw new RuntimeException("无法识别该URI,出错了!!");
        }
    }
}
