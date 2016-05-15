package pae.healz.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;

import pae.healz.BluetoothData.DataRx;
import pae.healz.R;
import pae.healz.SQLite.DataSourceDAO;
import pae.healz.SQLite.ModelClassSQL;

public class Calculs extends AppCompatActivity {


    // Initializes Bluetooth adapter.
    private Button button;
    private ModelClassSQL model;
    private DataSourceDAO BD;
    private long date = System.currentTimeMillis();
    private float data = 0;


    // private BluetoothAdapter mBluetoothAdapter;
   // private final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculs);
        BD = new DataSourceDAO(this.getApplicationContext());
        model = new ModelClassSQL(3, 0, data, date);
        button_FCFR();
        button_Composition();
        button_Conditions();
        button_savedata();

        //BLE
        /*
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            //finish();
        }

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }*/


    }

    private void button_Conditions() {
        button = (Button) findViewById(R.id.button_conditions);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Calculs.this, ConditionsActivity.class);
                Intent intent = new Intent(Calculs.this, DataRx.class);
                startActivity(intent);
            }
        });


    }

    private void button_FCFR() {
        button = (Button) findViewById(R.id.button_FCFR);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calculs.this, FC_FR.class);
                startActivity(intent);
            }
        });


    }

    private void button_Composition() {
        button = (Button) findViewById(R.id.button_C);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calculs.this, Composition.class);
                startActivity(intent);
            }
        });

    }

    private void button_savedata() {
        button = (Button) findViewById(R.id.button_saveweight);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText weight = (EditText) findViewById(R.id.weight_info);
                Float data = Float.valueOf(weight.getText().toString());
                long date = System.currentTimeMillis();
                model = new ModelClassSQL(3, 0, data, date);

                try {
                    BD.addparameters(model);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                CharSequence text = "The weight has been saved okay";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
        });

    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

*/
    }
