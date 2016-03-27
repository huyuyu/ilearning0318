package hpecl.uclass.ilearning;

import hpecl.uclass.adapter.WifiListAdapter;
import hpecl.uclass.uti.CustomListview;
import hpecl.uclass.wifihot.WifiAdmin;
import hpecl.uclass.wifihot.WifiApAdmin;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.ilearning.R;

public class WifiHotActivity extends Activity {
	
	
	public static final String TAG = "MainActivity";  
    
    private Button mBtn1, mBtn2;  
    private CustomListview listView;
    private WifiAdmin mWifiAdmin;  
    public static String ssid = "";
    private Context mContext = null;  
    WifiManager wifiManager;
    List<ScanResult> wifiList;
    ArrayList<String> wifiSSID = new ArrayList<String>();
    TextView tv,tv1;
    Handler handler ;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
          
        mContext = this;  
          
        setContentView(R.layout.wifihot);  
        Intent intent = getIntent();
        ssid = intent.getStringExtra("ssid");
        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mBtn1 = (Button)findViewById(R.id.button1);  
        mBtn2 = (Button)findViewById(R.id.button2);  
        listView = (CustomListview) findViewById(R.id.wifilistview);
        tv = (TextView) findViewById(R.id.information);
        tv1 = (TextView) findViewById(R.id.information1);
        mBtn1.setText("寻找");  
        mBtn2.setText("创建");  
        
        handler = new Handler(){@Override
        public void handleMessage(Message msg) {
        	// TODO Auto-generated method stub
        	super.handleMessage(msg);
        	if(msg.what == 11){
        		tv.setVisibility(View.INVISIBLE);
        		tv1.setVisibility(View.INVISIBLE);
        	}
        	
        }};
        
        
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message m = new Message();
				m.what = 11;
				handler.sendMessage(m);
				
			}
		}).start();
        mBtn1.setOnClickListener(new Button.OnClickListener() {  
              
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
            	
//            	WifiApAdmin.closeWifiAp(mContext);
                mWifiAdmin = new WifiAdmin(mContext) {  
                      
                    @Override  
                    public void myUnregisterReceiver(BroadcastReceiver receiver) {  
                        // TODO Auto-generated method stub  
                    	WifiHotActivity.this.unregisterReceiver(receiver);  
                    }  
                      
                    @Override  
                    public Intent myRegisterReceiver(BroadcastReceiver receiver,  
                            IntentFilter filter) {  
                        // TODO Auto-generated method stub  
                    	WifiHotActivity.this.registerReceiver(receiver, filter);  
                        return null;  
                    }  
                      
                    @Override  
                    public void onNotifyWifiConnected() {  
                        // TODO Auto-generated method stub  
                        Log.v(TAG, "have connected success!");  
                        Log.v(TAG, "###############################");  
                    }  
                      
                    @Override  
                    public void onNotifyWifiConnectFailed() {  
                        // TODO Auto-generated method stub  
                        Log.v(TAG, "have connected failed!");  
                        Log.v(TAG, "###############################");  
                    }  
                };  
                WifiApAdmin.closeWifiAp(mContext);
                mWifiAdmin.openWifi();  
                wifiList = mWifiAdmin.getWifiList();
//                Log.i("guo ", "1226    "+wifiList);
                wifiSSID.clear();
                for(ScanResult temp : wifiList){
                	if(temp.SSID.startsWith("+=")){
                		String sTemp = temp.SSID;
                		String ssid = sTemp.split("=")[1];
                		Log.i("guo ", "1226    "+ssid);
                		wifiSSID.add(ssid);               		
                	}                	
                }
                WifiListAdapter wifiAdapter = new WifiListAdapter(mContext, wifiSSID);
                listView.setAdapter(wifiAdapter);
                
                
                
            }  
        });  
          
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				mWifiAdmin.openWifi();  
				//加入指定网路
				mWifiAdmin.addNetwork(mWifiAdmin.createWifiInfo("+="+wifiSSID.get(arg2-1), "hhhhhh1234", WifiAdmin.TYPE_WPA));				
			}
		});
        
        mBtn2.setOnClickListener(new Button.OnClickListener() {  
              
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                  
                WifiApAdmin wifiAp = new WifiApAdmin(mContext);  
                wifiAp.startWifiAp("+="+ssid, "hhhhhh1234");  
                
                
                
            }  
        });  
          
    }  
  
//    @Override  
//    public boolean onCreateOptionsMenu(Menu menu) {  
//        // Inflate the menu; this adds items to the action bar if it is present.  
//        getMenuInflater().inflate(R.menu.activity_main, menu);  
//        return true;  
//    }  
//  
     @Override  
        public void onResume() {  
            super.onResume();  
              
            Log.d("Rssi", "Registered");  
        }  
  
        @Override  
        public void onPause() {  
            super.onPause();  
              
            Log.d("Rssi", "Unregistered");  
        }  
}
