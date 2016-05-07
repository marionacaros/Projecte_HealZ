package pae.healz.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.csr.heartratedemo.MainActivityHeartRate;

import pae.healz.BluetoothData.ScanResultsActivity;
import pae.healz.R;


public class FC_FR extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fc__fr);
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
        button = (Button) findViewById(R.id.button_connect);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(FC_FR.this, MainActivityHeartRate.class);
                Intent intent = new Intent(FC_FR.this, ScanResultsActivity.class);
                startActivity(intent);
            }
        });

    }

}
