/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：notesDBO.java
* Author：郭威
* Version：1.1
* Date：2015/09/24
* Description：This java file is to be used for showing file upload interface, not used now.
* Modify history：
* 郭威,create file,2015/09/24
*/
package hpecl.uclass.noteDAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guo on 2015/9/24.
 */
public class notesDAO {
    private SQLiteDatabase db;
    private DBOpenHelper helper;

    public notesDAO(Context context) {
        helper = new DBOpenHelper(context);
    }



    /*添加

     */
    public void add(Tb_note tb_note) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into tb_notes ( _id, user, name,time, content, wordcount) values(?,?,?,?,?,?)", new Object[]{
                tb_note.getId(), tb_note.getUser(),tb_note.getName(), tb_note.getTime(), tb_note.getContent(), tb_note.getWordcount()
        });
    }


    /*
    更新
     */
    public void update(Tb_note tb_note) {
        db = helper.getWritableDatabase();
        db.execSQL("update tb_notes set  user=?,name=?, time=?, content=?, wordcount=? where _id=?", new Object[]{
                 tb_note.getUser(),tb_note.getName(), tb_note.getTime(), tb_note.getContent(), tb_note.getWordcount(),tb_note.getId()
        });
    }
    /*
    按id索引

     */

    public Tb_note find(int _id){
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from tb_notes where _id=?",new String[]{String.valueOf(_id)});
        if (cursor.moveToLast()){
            return new Tb_note(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("user")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getInt(cursor.getColumnIndex("wordcount")));
        }
        return null;

    }

    public void delete (int _id){
        if (_id<=getMaxid()){
            SQLiteDatabase dbtmp = helper.getWritableDatabase();
            db.delete("tb_notes","_id=?", new String[]{String.valueOf(_id)});
        }

    }

    public int getMaxid(){
        db= helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select max(_id) from tb_notes",null);
        while (cursor.moveToLast()){
            return cursor.getInt(0);
        }
       return 0;
    }
    /*
    按用户名索引note
     */
    public List<Tb_note> getUserData(String user){
        List<Tb_note> userinfo =new ArrayList<Tb_note>();
        db= helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from tb_notes where user=?",new String[]{String.valueOf(user)});
        while (cursor.moveToNext()){
            userinfo.add(new Tb_note(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("user")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getInt(cursor.getColumnIndex("wordcount"))));
        }
        return  userinfo;
    }

    
    public long getCount (){
        db=helper.getWritableDatabase();
        Cursor cursor= db.rawQuery("select count(_id) from tb_notes",null);
        if (cursor.moveToNext()){
            return cursor.getLong(0);
        }
        return 0;
    }

}
