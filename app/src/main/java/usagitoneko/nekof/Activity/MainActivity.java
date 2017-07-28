package usagitoneko.nekof.Activity;



import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.google.common.primitives.Bytes;
import com.kosalgeek.asynctask.AsyncResponse;
import com.xw.repo.BubbleSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import mehdi.sakout.fancybuttons.FancyButton;
import usagitoneko.nekof.Widget.passwordView.PwdInputView;
import usagitoneko.nekof.controller.WifiSSidPW;
import usagitoneko.nekof.fragments.Loading_dialog;
import usagitoneko.nekof.R;
import usagitoneko.nekof.fragments.FragmentLog;
import usagitoneko.nekof.fragments.MainFragment;


public class MainActivity extends AppCompatActivity implements MainFragment.onSomeEventListener, Loading_dialog.Callbacks, AsyncResponse, View.OnClickListener {
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    public final int WRITE_PERMISSION = 0;
    private final int NFC_READCPLT = 0x01;
    private final int PASSWORD_SUCCESS = 0x02;
    public TextView nfc_result;
    SimpleFragmentPagerAdapter pageAdapter;
    NfcAdapter mNfcAdapter;
    private List<Boolean> allLedStatus = new ArrayList<>();
    private ViewPager pager;
    private PwdInputView password;
    private Switch showPwSwitch;
    private FancyButton submitBut;
    private BubbleSeekBar speedSeekbar;

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{new String[]{NfcV.class.getName()}}; //added NfcV

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);

        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * @param activity The corresponding {@ink BaseActivity} requesting to stop the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wheel_chair);

        password = (PwdInputView)findViewById(R.id.password);


        showPwSwitch = (Switch) findViewById(R.id.showPwSwitch);
        submitBut = (FancyButton) findViewById(R.id.submitPassword);
        submitBut.setOnClickListener(this);
        password.setShadowPasswords(showPwSwitch.isChecked());
        password.setPwdInputViewType(PwdInputView.ViewType.DEFAULT);

        showPwSwitch.setOnClickListener(this);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);


        //listen to button clicks
        if (mNfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC. ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            //inform user NFC is disabled
        } else {
            handleIntent(getIntent());
            //display whatever title desired
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showPwSwitch:
                password.setShadowPasswords(showPwSwitch.isChecked());
                /*WifiConfiguration wifiConfig = new WifiConfiguration();
                wifiConfig.SSID = String.format("\"%s\"", "Ultimake Makerthon");
                wifiConfig.preSharedKey = String.format("\"%s\"", "Ultimake2017");

                WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
                //remember id
                int netId = wifiManager.addNetwork(wifiConfig);
                wifiManager.disconnect();
                wifiManager.enableNetwork(netId, true);
                wifiManager.reconnect();*/
                break;
            case R.id.submitPassword:
                int passwordINT = Integer.valueOf(password.getText().toString());
                if(passwordINT == 8888) {
                    Intent joystickIntent = new Intent(this, JoystickController.class);
                    joystickIntent.putExtra("password", passwordINT);
                    joystickIntent.putExtra("firstTimePassword", false);
                    startActivity(joystickIntent);
                }
                else {
                    FragmentManager fm = getSupportFragmentManager();
                    Loading_dialog loading = new Loading_dialog();
                    loading.show(fm, "loading");
                }
                // TODO: 4/12/2017 debug purpose, remove it later

        }
    }

    @Override
    public void processFinish(String s) {
        Toast.makeText(this, "dummy:"+s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void someEvent(List<Boolean> allLedStatus){
        this.allLedStatus = allLedStatus;
    }

    @Override
    public void getWriteStatus(boolean writeStatus) {
        allLedStatus.add(WRITE_PERMISSION, writeStatus);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } /*else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }*/ //TECH_DISCOVERED will filter on the onNewIntent
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        if(mNfcAdapter!=null)
            setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {

        if(mNfcAdapter!=null)
            stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewIntent(Intent intent) {

        //stop the fragment dialog

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction()))
        {

            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NfcV nfcv = NfcV.get(detectedTag);
            if(nfcv == null){

                //not nfcV type
            }
            else {
                try {
                    nfcv.connect();
                    if (nfcv.isConnected()) {
                        byte[] bufferSSIDPW;
                        byte[] buffer = {0};
                        //int passwordINT = Integer.valueOf(password.getText().toString());// TODO: 11/4/2017 check if gettext = null, display error message

                        /*begin the password part*/
                        /*______________________________________________________________________________*/
                        /*send the init byte*/ /*send the password bytes*/
                        nfcv.transceive(new byte[]{0x02, 0x21, (byte) 0, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00}); //must be 4 bytes
                        while((buffer[0] & 0x02) == 0){
                            buffer = nfcv.transceive(new byte[]{0x02, 0x20, (byte) 0});
                        }
                        String WifiSsid = getWifi(nfcv);
                        Toast.makeText(this, "Send complete!", Toast.LENGTH_SHORT).show();
                        /*buffer = nfcv.transceive(new byte[]{0x02, 0x23, (byte) 0, (byte)0x02});
                        // TODO: 12/4/2017 signed int to unsigned
                        if((buffer[1] == (((passwordINT&0xff00)>>8)))&&((buffer[2]) == (passwordINT&0xff))){
                            *//*password is correct*//*
                            WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
                            wifiManager.setWifiEnabled(true);
                            Intent joystickIntent = new Intent(this, JoystickController.class);
                            joystickIntent.putExtra("password", passwordINT);
                            joystickIntent.putExtra("firstTimePassword", false);
                            startActivity(joystickIntent);
                            finish();
                        }
                        else{
                                Toast.makeText(this, "password incorrect!", Toast.LENGTH_SHORT).show();
                        }*/

                        nfcv.close();

                    }else
                        Log.e("Error", ":nfcv not connected");
                } catch (IOException e) {
                    Log.e("Error", ":ERROR exception");
                }

            }
        }
        else {  //has NDEF inside the tag
            handleIntent(intent); //read data on the tag and display to the textview
            if (isNfcIntent(intent)) {
                NdefMessage ndefMessage = createTextMessage("hello there!");

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                writeTag(tag, ndefMessage);
            }
        }
    }

    private void writeAndroidThere(NfcV nfcv){
        try {
            nfcv.transceive(new byte[]{0x02, 0x21, (byte) 0, (byte) 0x01});
        }catch (IOException e){
            Log.e("ERROR", "nfcv connection disrupted");
        }
    }

    private String getWifi(NfcV nfcv){
        List<Byte> bufferList = new ArrayList<Byte>();
        String ERROR = "ERROR";
        byte[] buffer;


        try {
            bufferList.addAll(Bytes.asList(nfcv.transceive(new byte[]{0x02, 0x20, 0x01})));
            bufferList.addAll(Bytes.asList(nfcv.transceive(new byte[]{0x02, 0x20, 0x05})));
            buffer = Bytes.toArray(bufferList);
            return new String(buffer, "UTF-8");
        }catch (IOException e){
            Log.e("ERROR", "nfcv connection disrupted");
        }
        return ERROR;
    }


    boolean isNfcIntent(Intent intent) {
        return intent.hasExtra(NfcAdapter.EXTRA_TAG);
    }

    boolean writeTag( Tag detectedTag, NdefMessage message) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(detectedTag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(this, "Tag is read-only.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(this, "The data cannot written to tag, Tag capacity is " + ndef.getMaxSize() + " bytes, message is "
                                    + size + " bytes.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                ndef.writeNdefMessage(message);
                ndef.close();
                Toast.makeText(this, "Message is written!",
                        Toast.LENGTH_SHORT).show();
                return true;
            } else {
                NdefFormatable ndefFormat = NdefFormatable.get(detectedTag);
                if (ndefFormat != null) {
                    try {
                        ndefFormat.connect();
                        ndefFormat.format(message);
                        ndefFormat.close();
                        Toast.makeText(this, "The data is written to the tag ",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    } catch (IOException e) {
                        Toast.makeText(this, "Failed to format tag",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(this, "NDEF is not supported",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Write opreation is failed",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public NdefMessage createTextMessage(String content) {
        try {
            // Get UTF-8 byte
            byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");
            byte[] text = content.getBytes("UTF-8"); // Content in UTF-8

            int langSize = lang.length;
            int textLength = text.length;

            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);
            payload.write((byte) (langSize & 0x1F));
            payload.write(lang, 0, langSize);
            payload.write(text, 0, textLength);
            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                    NdefRecord.RTD_TEXT, new byte[0],
                    payload.toByteArray());
            return new NdefMessage(new NdefRecord[]{record});
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public int toInteger(byte[] bytes){
        int result =0;
        for(int i=3;i>0;i--){
            result<<=8;
            result +=bytes[i];
        }
        return result;
    }

    public String numberToHex(int value) {
        return String.format("0x%x",value);
    }

    private class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private Bundle fragmentBundle;

        public SimpleFragmentPagerAdapter (FragmentManager fm, Bundle data){
            super(fm);
            fragmentBundle = data;
        }
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position ==0){
                final MainFragment mainFragment = new MainFragment();
                mainFragment.setArguments(this.fragmentBundle);
                return (mainFragment);
            }
            else if(position ==1){
                return (new FragmentLog());
            }
            else{
                return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "LOG";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                nfc_result.setText("Read content: " + result);
            }
        }
    }


    class MyAsyncTask extends AsyncTask<String, String, Void> {

        InputStream inputStream = null;
        String result = "";
        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        protected void onPreExecute() {
            progressDialog.setMessage("Downloading your data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    MyAsyncTask.this.cancel(true);
                }
            });
        }

        @Override
        protected Void doInBackground(String... params) {

            //String url_select = "http://raw.githubusercontent.com/usagitoneko97/Stm32-and-nfc02A1-led-control/finalOfLayout/jsonDummy1.json";
            String url_select = "https://quarkbackend.com/getfile/hogouxian/jsontest";
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                // Set up HTTP post
                // HttpClient is more then less deprecated. Need to change to URLConnection
                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost httpPost = new HttpPost(url_select);
                httpPost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                // Read content & Log
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsupportedEncoding", e1.toString());
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }
            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

            } catch (Exception e) {
                Log.e("StringBuilding & ", "Error converting result " + e.toString());
            }
            return null;
        }
        protected void onPostExecute(Void v) {
            //parse JSON data
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            try {
                JSONArray jArray = new JSONArray(result);
                for(int i=0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);

                    String name = jObject.getString("name");
                    String tab1_text = jObject.getString("tab1_text");
                    int active = jObject.getInt("active");

                } // End Loop
                this.progressDialog.dismiss();
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)
        } // protected void onPostExecute(Void v)
    }

}

// TODO: 3/7/2017 add info on all the how the things work
// TODO: 3/8/2017 checking at read and data to be writen to minimize writing
// TODO: 9/3/2017 initialize a variable for check to display the snakbar the first time user touch