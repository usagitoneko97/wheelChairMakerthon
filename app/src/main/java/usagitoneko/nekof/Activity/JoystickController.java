package usagitoneko.nekof.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import mehdi.sakout.fancybuttons.FancyButton;
import usagitoneko.nekof.R;
import usagitoneko.nekof.Widget.Croller;
import usagitoneko.nekof.controller.MySingleton;


public class JoystickController extends AppCompatActivity implements View.OnClickListener{
    View lineIndicator;
    private TickerView kmperH;
    private Croller croller;
    private FancyButton uTurnButton;
    private FancyButton forceStopButton;
    int[] location = new int[2];

    private static final String UTURN = "uturn";
    private static final String FORCESTOP = "forcestop";
    private static final String MOVE = "body";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joystick_wrapper);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.joystickToolbar);
        setSupportActionBar(myToolbar);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);*/
        lineIndicator = findViewById(R.id.lineIndicator);
        /*Bundle data = getIntent().getExtras();
        String s = data.getString("NAME");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();*/
        kmperH = (TickerView)findViewById(R.id.kmPerH);
        kmperH.setCharacterList(TickerUtils.getDefaultNumberList());
        kmperH.setText("Km/H");
        kmperH.bringToFront();

        croller = (Croller)findViewById(R.id.croller);
        croller = initCroller(croller);
        croller.setMax(100);

        uTurnButton = (FancyButton)findViewById(R.id.uTurnButton);
        forceStopButton = (FancyButton)findViewById(R.id.forceStopButton);
        uTurnButton.setOnClickListener(this);
        forceStopButton.setOnClickListener(this);

        final TickerView speedIndicator =(TickerView) findViewById(R.id.speedIndicator);
        speedIndicator.setCharacterList(TickerUtils.getDefaultNumberList());
        speedIndicator.bringToFront();

        lineIndicator.bringToFront();

        //sendCommand(1);
        Joystick joystick = (Joystick) findViewById(R.id.joystick);
        joystick.setJoystickListener(new JoystickListener() {
            @Override
            public void onDown() {
                // ..
                speedIndicator.setText("45");
            }

            @Override
            public void onDrag(float degrees, float offset) {
                // ..
                float finalDegrees;
                lineIndicator.getLocationOnScreen(location);
                lineIndicator.setRotation(360-degrees);
                lineIndicator.setLayoutParams(new ConstraintLayout.LayoutParams( Math.round(offset*556),11));
                lineIndicator.setX(701);
                lineIndicator.setY(726);
                croller.setProgress(Math.round(offset*100));
                if(degrees>0){
                    finalDegrees = (360-(degrees+180))+180;

                }
                else {
                    finalDegrees = degrees*-1;
                }

                sendCommand("body",offset, degrees);
                Log.v("angle", String.valueOf(finalDegrees));
            }

            @Override
            public void onUp() {
                lineIndicator.setLayoutParams(new ConstraintLayout.LayoutParams(1,1 ));
                speedIndicator.setText("23");
                sendCommand(MOVE, 0, 0);
                // ..
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.uTurnButton:
                sendCommand(UTURN, 0,0);
                break;
            case R.id.forceStopButton:
                sendCommand(FORCESTOP,0,0);
                break;
        }
    }

    private Croller initCroller(Croller croller){
        croller.setIndicatorWidth(10);

        return croller;
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
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean sendCommand (final String operation, final float offset, final float degrees){
        // TODO: 3/28/2017 send degrees and offset as json object

        StringBuilder uRLBuilder = new StringBuilder();
        uRLBuilder.append("http://192.168.178.87/");
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
