package pae.healz.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;


import pae.healz.BluetoothData.DataRx;
import pae.healz.BluetoothData.ScanResultsActivity;
import pae.healz.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Composition extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_composition);
        Button_Connect();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
    }

    private void Button_Connect() {
        button = (Button) findViewById(R.id.button_connect_composition);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Composition.this, MainActivityHeartRate.class);
                Intent intent = new Intent(Composition.this, ScanResultsActivity.class);
                startActivity(intent);
            }
        });


    }





}
