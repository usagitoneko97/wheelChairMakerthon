package usagitoneko.nekof.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;
import com.xw.repo.BubbleSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import mehdi.sakout.fancybuttons.FancyButton;
import usagitoneko.nekof.R;
import usagitoneko.nekof.controller.MySingleton;
import usagitoneko.nekof.fragments.ChangePasswordDialog;
import usagitoneko.nekof.fragments.Loading_dialog;


public class JoystickController extends AppCompatActivity implements View.OnClickListener, Loading_dialog.Callbacks{
    private static final String UTURN = "uturn";
    private static final String FORCESTOP = "forcestop";
    private static final String MOVE = "body";
    final int[] mSeekbarProgress = new int[1];
    final int[] mAccelerationProgress = new int[1];
    View lineIndicator;
    int[] location = new int[2];
    SharedPreferences settingsPreference;
    private FancyButton uTurnButton;
    private FancyButton forceStopButton;
    private BubbleSeekBar speedSeekbar;
    private BubbleSeekBar accelerationSeekbar;
    private ImageView torchLight;
    private boolean firstTimePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joystick_wrapper);
        mSeekbarProgress[0] = 100;
        mAccelerationProgress[0] = 100;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.joystickToolbar);
        setSupportActionBar(myToolbar);
        settingsPreference = PreferenceManager.getDefaultSharedPreferences(this);


        // TODO: 12/4/2017 Bundle data bugs: null exception
        /*Bundle data = getIntent().getExtras();
        firstTimePassword = data.getBoolean("firstTimePassword");
        if(firstTimePassword){
            //prompt user to change the password
        }*/
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);*/
        lineIndicator = findViewById(R.id.lineIndicator);
        /*Bundle data = getIntent().getExtras();
        String s = data.getString("NAME");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();*/


        uTurnButton = (FancyButton)findViewById(R.id.uTurnButton);
        forceStopButton = (FancyButton)findViewById(R.id.forceStopButton);
        speedSeekbar = (BubbleSeekBar) findViewById(R.id.speedSeekbar);
        accelerationSeekbar = (BubbleSeekBar) findViewById(R.id.accelerationSeekBar);
        uTurnButton.setOnClickListener(this);
        forceStopButton.setOnClickListener(this);


        lineIndicator.bringToFront();

        speedSeekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                mSeekbarProgress[0] = progress;
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        accelerationSeekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                mAccelerationProgress[0] = progress;
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        Joystick joystick = (Joystick) findViewById(R.id.joystick);
        joystick.setJoystickListener(new JoystickListener() {
            @Override
            public void onDown() {
                // ..


            }

            @Override
            public void onDrag(float degrees, float offset) {
                // ..

                float finalDegrees = 90 - degrees;
                if(finalDegrees < 0){
                    finalDegrees += 360;
                }
                offset *= mSeekbarProgress[0]/100.00000;
                offset *= 100;
                final float offsetFinal = offset;
                Log.v("degrees", String.valueOf(finalDegrees));
                Log.v("offset", String.valueOf(offsetFinal));

                sendCommand("body",offsetFinal, Math.round(finalDegrees), mAccelerationProgress[0]);
            }

            @Override
            public void onUp() {
                lineIndicator.setLayoutParams(new ConstraintLayout.LayoutParams(1,1 ));

                sendCommand(MOVE, 0, 0, mAccelerationProgress[0]);
                sendCommand(MOVE, 0, 0, mAccelerationProgress[0]);
                sendCommand(MOVE, 0, 0, mAccelerationProgress[0]);
                sendCommand(MOVE, 0, 0, mAccelerationProgress[0]);
                sendCommand(MOVE, 0, 0, mAccelerationProgress[0]);
            }
        });



    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
    @Override
    public void getWriteStatus(boolean writeStatus) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.uTurnButton:
                sendCommand(UTURN, 0,0, mAccelerationProgress[0]);
                break;
            case R.id.forceStopButton:
                sendCommand(FORCESTOP,0,0, mAccelerationProgress[0]);
        }
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
            // TODO: 4/8/2017 go to settings activity
            Intent settingsIntent = new Intent(this, SettingAcitivity.class);
            startActivity(settingsIntent);

            return true;
        }
        else if(id == R.id.action_changePassword){
            FragmentManager fm = getSupportFragmentManager();
            ChangePasswordDialog changePassword = new ChangePasswordDialog();
            changePassword.show(fm,"loading");

        }

        return super.onOptionsItemSelected(item);
    }

    private boolean sendCommand (final String operation, final float offset, final float degrees, final int acceleration){
        StringBuilder uRLBuilder = new StringBuilder();
        uRLBuilder.append("http://192.168.4.1/");
        uRLBuilder.append(operation);
        String URL = uRLBuilder.toString();
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, URL, null,new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("response", response.toString());
                            //mTxtDisplay.setText("Response: " + response.toString());
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub

                        }
                    }) {

                @Override
                public byte[] getBody() {
                    //send body only if the operation is MOVE
                    if (operation == MOVE) {
                        JSONObject jsonObject = new JSONObject();
                        String body = null;
                        try {
                            jsonObject.put("offset", offset);
                            jsonObject.put("degrees", degrees);
                            jsonObject.put("acceleration", acceleration);

                            body = jsonObject.toString();

                            return body.toString().getBytes("utf-8");
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                        return null;
                    }
                    return null;
                }
            };
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        /*RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsObjRequest);*/

        return true;
    }

}
