/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：DBOpenHelper.java
* Author：郭威
* Version：1.0
* Date：2015/09/24
* Description：This java file is to be used for showing file upload interface, not used now.
* Modify history：
* 郭威,create file,2015/09/24
*/
package hpecl.uclass.noteDAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by guo on 2015/9/24.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DBNAME="notes.db";


    public DBOpenHelper(Context context) {
        super(context, DBNAME,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tb_notes(_id integer primary key autoincrement, user varchar(10), name varchar(100),time varchar(20), content varchar(1100), wordcount integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
