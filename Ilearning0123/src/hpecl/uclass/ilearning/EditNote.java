/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：EditNote.java
* Author：郭威
* Version：1.1
* Date：2015/07/06
* Description：This java file is used for notes edit
* Modify history：
* 郭靖东,create file,2015/07/06  
*/
package hpecl.uclass.ilearning;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.security.auth.PrivateCredentialPermission;


import hpecl.uclass.noteDAO.Tb_note;
import hpecl.uclass.noteDAO.notesDAO;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import com.example.ilearning.R;


public class EditNote extends Activity {

	private EditText titleEditText;
    private EditText contentEditText;
    private TextView tv;
    private ImageButton edit_done;
    private String noteId;
    private String username;
    private String chapter;
    private int FLAG=0;
    //private Tb_note tbNote;
    private boolean showKeyboard = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_note);
		
		titleEditText = (EditText) findViewById(R.id.titleEditText);
        contentEditText = (EditText) findViewById(R.id.contentEditText);
        contentEditText.requestFocus();
        edit_done = (ImageButton) findViewById(R.id.edit_done);
        tv = (TextView) findViewById(R.id.editnote);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        FLAG = bundle.getInt("flag");
        noteId = bundle.getString("noteid");
        username = bundle.getString("name");
        chapter = bundle.getString("chapter");
        tv.setText(chapter);
        initView(noteId,FLAG);
        edit_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (saveNote(noteId,FLAG)) {
                    Intent myintent = new Intent(EditNote.this, Main.class);

                    //startActivity(myintent);
                    setResult(RESULT_OK,myintent);
                    finish();
                    Log.v("guow", "11111"+"returnto main");
                }else{
                    AlertDialog.Builder myalt= new AlertDialog.Builder(EditNote.this);
                    myalt.setMessage("笔记未编辑，确定退出？");
                    myalt.setTitle("提示");
                    
                    myalt.setPositiveButton("放弃保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Intent myintent = new Intent(EditNote.this, Main.class);
                            //setResult(RESULT_OK,myintent);
                            //startActivity(myintent);
                            finish();
                        }
                    });
                    myalt.setNegativeButton("返回编辑", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    myalt.create().show();
                }
            }
        });
    }
        
	 @Override
   public boolean onKeyDown(int keyCode, KeyEvent event)
   {
       if (keyCode == KeyEvent.KEYCODE_BACK )
       {
    	   if (saveNote(noteId,FLAG)) {
               Intent myintent = new Intent(EditNote.this, Main.class);
               setResult(RESULT_OK,myintent);
               //startActivity(myintent);
               finish();
           }else{
               AlertDialog.Builder myalt= new AlertDialog.Builder(EditNote.this);
               myalt.setMessage("笔记未编辑，确定退出？");
               myalt.setTitle("提示");
               
               myalt.setPositiveButton("放弃保存", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       //Intent myintent = new Intent(EditNote.this, Main.class);

                       //startActivity(myintent);
                       finish();
                   }
               });
               myalt.setNegativeButton("返回编辑", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               });
               myalt.create().show();
           }

       }

       return false;

   }
	
        private void initView(String note_Id,int note_flag) {
            int t=note_flag;
            if (t==1)
            {
                notesDAO notesinfo = new notesDAO(this);
                Tb_note tbNote = notesinfo.find(Integer.parseInt(note_Id));
                titleEditText.setText(tbNote.getName());
                contentEditText.setText(tbNote.getContent());

            }else{
            	titleEditText.setText(getTime());
            }

        }

//       

        public Boolean saveNote(String note_Id,int note_flag) {
            String titleToSave = titleEditText.getText().toString().trim();
            String contentToSave = contentEditText.getText().toString().trim();
            notesDAO notesDAO = new notesDAO(this);
            Tb_note tbNote = new Tb_note();

            if (!contentToSave.isEmpty() || !titleToSave.isEmpty()) {

                if (note_flag==0) {
                    //ParseObject post = new ParseObject("Post");
                    titleToSave += titleToSave.isEmpty() ? "Untitled" : "";


                    tbNote.setId(notesDAO.getMaxid() + 1);
                    tbNote.setUser(username);
                    tbNote.setName(titleToSave);
                    tbNote.setTime(getTime());

                    tbNote.setContent(contentToSave);

                    tbNote.setWordcount(0);//0
                    notesDAO.add(tbNote);
                    return true;
                } else {
                    tbNote = notesDAO.find(Integer.parseInt(note_Id));
                    titleToSave += titleToSave.isEmpty() ? "Untitled" : "";

                    tbNote.setId(Integer.parseInt(note_Id));
                    tbNote.setUser(username);
                    tbNote.setName(titleToSave);
                    tbNote.setTime(getTime());

                    tbNote.setContent(contentToSave);

                    tbNote.setWordcount(0);//0
                    notesDAO.update(tbNote);
                    return true;
                }
            } else {

                Toast.makeText(EditNote.this, "请输入内容或标题", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        private String getTime() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 ");
            Date curDate = new Date(System.currentTimeMillis());
            String s = dateFormat.format(curDate);
            return s;

        }

//        @Override
//        public void onResume() {
//            super.onResume();
//            if (showKeyboard) {
//                titleEditText.requestFocus();
//                showSoftKeyboard(titleEditText);
//            }
//        }

        public void showSoftKeyboard(View view) {
            if (view.requestFocus()) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }

        public void hideSoftKeyboard(View view) {
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        @Override
        public void onPause() {
            super.onPause();
            hideSoftKeyboard(titleEditText);
        }

        //@Override
        public void onDetach() {
          //  super.onDetach();
            saveNote(noteId,FLAG);
        }
	
	
	
	
	
}
