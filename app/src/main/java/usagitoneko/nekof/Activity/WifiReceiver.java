package usagitoneko.nekof.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by user on 8/1/2017.
 */

public class WifiReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {


            // e.g. To check the Network Name or other info:
            WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String realSsid = wifiInfo.getSSID();
            //TODO should check for ssid String rather than the length, but its a quick fix for the situation
            if(realSsid.length() == 10){
                Log.v("result", "joy stick controller launching....");
                Intent i = new Intent();
                i.setClassName("usagitoneko.nekof", "usagitoneko.nekof.Activity.JoystickController");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

        }
    }

}
