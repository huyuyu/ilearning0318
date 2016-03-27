package hpecl.uclass.ilearning;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.ilearning.R;

import java.util.ArrayList;

import hpecl.uclass.Bean.Article;
import hpecl.uclass.adapter.ConcernReadingListAdapter;
import hpecl.uclass.analysis.JsoupAnalysisNotification;
import hpecl.uclass.uti.Constant;
import hpecl.uclass.uti.CustomListview;
import hpecl.uclass.uti.GetHtmlData;

/**
 * Created by yuyu on 2016/3/21.
 */
public class ReferenceActivity  extends Activity {
    String content = new String ();
    String getinfo = new String();
    //List<Article> articles = new ArrayList<Article>();
    CustomListview lv;
    ConcernReadingListAdapter adapter;
    ProgressDialog progressDialog;
    String cookie;
    String url;
    ArrayList<Article> referenceArr = new ArrayList<Article>();
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reference);

        lv = (CustomListview) findViewById(R.id.lv);
        sp = getSharedPreferences("cookie", MODE_PRIVATE);
        cookie = sp.getString("cookie", "");
        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        new MyAsyncTask().execute();//执行异步线程

        progressDialog = ProgressDialog.show(ReferenceActivity.this, "请稍等...", "获取数据中...", true);
    }

    public class MyAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... arg0) {

            JsoupAnalysisNotification jaf = new JsoupAnalysisNotification();
            referenceArr = (ArrayList<Article>)jaf.analysisInfo(GetHtmlData.doInBackground(Constant.REFERENCE, "gbk", cookie));

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            adapter = new ConcernReadingListAdapter(ReferenceActivity.this, referenceArr);
            lv.setAdapter(adapter);
            super.onPostExecute(result);

            progressDialog.dismiss();

            lv.setonRefreshListener(new CustomListview.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    // TODO Auto-generated method stub
                    new AsyncTask<Void, Void, Void> () {

                        @Override
                        protected Void doInBackground(Void... params) {
                            JsoupAnalysisNotification jaf = new JsoupAnalysisNotification();
                            referenceArr = (ArrayList<Article>)jaf.analysisInfo(GetHtmlData.doInBackground(Constant.REFERENCE, "gbk", cookie));
                            return null;
                        }

                        protected void onPostExecute(Void result) {
                            adapter.notifyDataSetChanged();
                            lv.onRefreshComplete();
                        }
                    }.execute();
                }
            });

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    String href = referenceArr.get(position-1).getHref();
//                    Log.i("cq", "1109href"+href);
                    Intent intent = new Intent();
                    intent.setClass(ReferenceActivity.this, RefenceText.class);
                    intent.putExtra("Href", href);
                    intent.putExtra("cookie", cookie);
                    startActivity(intent);
                }});
        }
    }
}

