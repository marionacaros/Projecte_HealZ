
package pae.healz.BluetoothData;
/**
 * Created by Mariona on 3/05/2016.
 */

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.csr.btsmart.BtSmartService;
import com.csr.btsmart.BtSmartService.BtSmartUuid;
import com.csr.view.DataView;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.SQLException;
import java.util.ArrayList;

import pae.healz.Activities.MainActivity;
import pae.healz.Activities.Processing;
import pae.healz.R;
import pae.healz.SQLite.DataSourceDAO;
import pae.healz.SQLite.ModelClassSQL;

public class ShowData extends Activity {

    private Button button;


    // Data Views to update on the display.
    DataView heartRateDW = null;
    DataView breathingDW = null;
    DataView impedanceDW = null; //SE TIENE QUE TRATAR PARA MOSTRAR TOTAL BODY WATER, FAT FREE MASS PERCENTAGE, FAT MASS PERCENTAGE
    DataView totalBodyWaterDW = null;
    DataView fatFreeMassDW = null;
    DataView freeMassDW = null;

    //Heart Rate Activity
    DataView heartRateData = null;
    DataView rrData = null;
    DataView energyData = null;
    DataView locationData = null;

    //Database   taula heartrate-modul, taula ffreemass-phase
    private ModelClassSQL modelmodule;
    private ModelClassSQL modelphase;
    private DataSourceDAO BD;
    private float data = 0;
    private long date = System.currentTimeMillis();
    private Processing pro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prevent screen rotation.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        // Display back button in action bar.
        // getActionBar().setDisplayHomeAsUpEnabled(true);

        BD = new DataSourceDAO(this.getApplicationContext());
        modelmodule = new ModelClassSQL(0, 0, data, date);
        modelphase = new ModelClassSQL(1, 0, data, date);


        setContentView(R.layout.activity_dades);

        heartRateDW = (DataView) findViewById(R.id.heartRateDW);
        breathingDW = (DataView) findViewById(R.id.breathingDW);
        totalBodyWaterDW = (DataView) findViewById(R.id.totalBodyWaterDW);
        fatFreeMassDW = (DataView) findViewById(R.id.fatFreeMassDW);
        freeMassDW = (DataView) findViewById(R.id.freeMassDW);
        Button_Home();
        Button_Repeat();

    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }


    private void Button_Home() {
        button = (Button) findViewById(R.id.button_home);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowData.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Button_Repeat() {
        button = (Button) findViewById(R.id.button_repeat);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowData.this, ScanResultsActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        ShowData.super.onBackPressed();
                        //onDestroy()???
                    }
                }).create().show();
    }
}
