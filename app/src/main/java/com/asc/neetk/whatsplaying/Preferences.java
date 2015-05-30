package com.asc.neetk.whatsplaying;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.Slider;
import com.gc.materialdesign.views.Switch;


public class Preferences extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Slider time = (Slider) findViewById(R.id.slider);

        final TextView texttime =(TextView) findViewById(R.id.texttime);


        final SharedPreferences prefs = this.getSharedPreferences(
                "com.asc.neetk.whatsplaying", Context.MODE_PRIVATE);

       final Integer timedur = prefs.getInt("time", 5);

        texttime.setText(timedur.toString()+" seconds");

        time.setValue(timedur);


        time.setOnValueChangedListener(new Slider.OnValueChangedListener() {
            @Override
            public void onValueChanged(int i) {
                prefs.edit().putInt("time", i).apply();

                Toast.makeText(getApplicationContext(), "Saved !", Toast.LENGTH_SHORT).show();
                texttime.setText(String.valueOf(i) + " seconds");

            }


        });



        Switch vib = (Switch) findViewById(R.id.switchView);
        Boolean onoff = prefs.getBoolean("vibrate", true);

         vib.setChecked(onoff);






        vib.setOncheckListener(new Switch.OnCheckListener() {
             @Override
             public void onCheck(Switch aSwitch, boolean b) {

                 prefs.edit().putBoolean("vibrate", b).apply();


                 Toast.makeText(getApplicationContext(), "Saved !", Toast.LENGTH_SHORT).show();

             }
         });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_preferences, menu);
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
}
