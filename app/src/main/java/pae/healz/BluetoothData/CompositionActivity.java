/******************************************************************************
 *  Copyright (C) Cambridge Silicon Radio Limited 2014
 *
 *  This software is provided to the customer for evaluation
 *  purposes only and, as such early feedback on performance and operation
 *  is anticipated. The software source code is subject to change and
 *  not intended for production. Use of developmental release software is
 *  at the user's own risk. This software is provided "as is," and CSR
 *  cautions users to determine for themselves the suitability of using the
 *  beta release version of this software. CSR makes no warranty or
 *  representation whatsoever of merchantability or fitness of the product
 *  for any particular purpose or use. In no event shall CSR be liable for
 *  any consequential, incidental or special damages whatsoever arising out
 *  of the use of or inability to use this software, even if the user has
 *  advised CSR of the possibility of such damages.
 *
 ******************************************************************************/

package pae.healz.BluetoothData;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.csr.btsmart.BtSmartService;
import com.csr.btsmart.BtSmartService.BtSmartUuid;
import com.csr.view.DataView;

import java.lang.ref.WeakReference;

import pae.healz.R;

public class CompositionActivity extends Activity implements GestorBLE.IBLEDATA {

    private BluetoothDevice mDeviceToConnect = null;
    private BtSmartService mService = null;
    private boolean mConnected = false;

    // For connect timeout.
    private static Handler mHandler = new Handler();
    
    // Data Views to update on the display.
    DataView impedanceDW = null; //SE TIENE QUE TRATAR PARA MOSTRAR TOTAL BODY WATER, FAT FREE MASS PERCENTAGE, FAT MASS PERCENTAGE
    DataView totalBodyWaterDW = null;
    DataView fatFreeMassDW = null;
    DataView freeMassDW = null;

    public String mManufacturer;
    public String mHardwareRev;
    public String mFwRev;
    public String mSwRev;
    public String mSerialNo;
    public String mBatteryPercent;

    private static final int REQUEST_MANUFACTURER = 0;
    private static final int REQUEST_BATTERY = 1;
    private static final int REQUEST_COMPOSITION = 2;

    private static final int CONNECT_TIMEOUT_MILLIS = 5000;

    public static final String EXTRA_SERIAL = "SERIALNO";
    public static final String EXTRA_BATTERY = "BATTERY";

    private static final int INFO_ACTIVITY_REQUEST = 1;


    public GestorBLE mGestorBLE = new GestorBLE(this, this);
    private final Handler mCompositionHandler = mGestorBLE.mHandler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prevent screen rotation.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        setContentView(R.layout.activity_dades_composition);

        impedanceDW = (DataView) findViewById(R.id.impedanceDW);
        //totalBodyWaterDW = (DataView) findViewById(R.id.totalBodyWaterDW);
        //fatFreeMassDW = (DataView) findViewById(R.id.fatFreeMassDW);
        //freeMassDW = (DataView) findViewById(R.id.freeMassDW);

        // Get the device to connect to that was passed to us by the scan results Activity.
        Intent intent = getIntent();
        if (intent != null) {
            mDeviceToConnect = intent.getExtras().getParcelable(BluetoothDevice.EXTRA_DEVICE);
                
            // Make a connection to BtSmartService to enable us to use its services.
            Intent bindIntent = new Intent(this, BtSmartService.class);
            bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }


    @Override
    public void onDestroy() {
        // Disconnect Bluetooth connection.
        if (mService != null) {
            mService.disconnect();
        }
        unbindService(mServiceConnection);
        Toast.makeText(this, "Disconnected from the sensor.", Toast.LENGTH_SHORT).show();
        finishActivity(INFO_ACTIVITY_REQUEST);
        super.onDestroy();
    }

    /**
     * Callbacks for changes to the state of the connection to BtSmartService.
     */
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mService = ((BtSmartService.LocalBinder) rawBinder).getService();
            if (mService != null) {
                // We have a connection to BtSmartService so now we can connect and register the device handler.
                mService.connectAsClient(mDeviceToConnect, mDeviceHandler);
                startConnectTimer();
            }
        }

        public void onServiceDisconnected(ComponentName classname) {
            mService = null;
        }
    };

    /**
     * Start a timer to close the Activity after a fixed length of time. Used to prevent waiting for the connection to
     * happen forever.
     */
    private void startConnectTimer() {
        mHandler.postDelayed(onConnectTimeout, CONNECT_TIMEOUT_MILLIS);        
    }

    private Runnable onConnectTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mConnected) finish();            
        }
    };

    /**
     * This is the handler for general messages about the connection.
     */
    private final DeviceHandler mDeviceHandler = new DeviceHandler(this);


    private static class DeviceHandler extends Handler {
        private final WeakReference<CompositionActivity> mActivity;

        public DeviceHandler(CompositionActivity activity) {
            mActivity = new WeakReference<CompositionActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CompositionActivity parentActivity = mActivity.get();
            if (parentActivity != null) {
                BtSmartService smartService = parentActivity.mService;
                switch (msg.what) {
                case BtSmartService.MESSAGE_CONNECTED: {

                    if (parentActivity != null) {
                        parentActivity.mConnected = true;

                        // Cancel the connect timer.
                        mHandler.removeCallbacks(parentActivity.onConnectTimeout);
                        
                        parentActivity.setProgressBarIndeterminateVisibility(false);
                        // Get the device information - this will come back to
                        // us in a MESSAGE_CHARACTERISTIC_VALUE event.
                        smartService.requestCharacteristicValue(REQUEST_MANUFACTURER,
                                BtSmartUuid.DEVICE_INFORMATION_SERVICE.getUuid(),
                                BtSmartUuid.MANUFACTURER_NAME.getUuid(), parentActivity.mCompositionHandler);

                        //BODY COMPOSITION
                        smartService.requestCharacteristicValue(REQUEST_COMPOSITION, BtSmartUuid.BCS_SERVICE.getUuid(),
                                BtSmartUuid.BODY_COMPOSITION_MEASUREMENT.getUuid(), parentActivity.mCompositionHandler);


                        // Register to be told about battery level.
                        smartService.requestCharacteristicNotification(REQUEST_BATTERY,
                                BtSmartUuid.BATTERY_SERVICE.getUuid(), BtSmartUuid.BATTERY_LEVEL.getUuid(),
                                parentActivity.mCompositionHandler);

                    }
                    break;
                }
                case BtSmartService.MESSAGE_DISCONNECTED: {
                    // End this activity and go back to scan results view.
                    mActivity.get().finish();
                    break;
                }
                }
            }
        }

    };


    public void batteryNotificationHandler(byte value) {
        mBatteryPercent = String.valueOf(value + "%");
    }

    @Override
    public void sensorLocationHandler(int locationIndex) {

    }

    @Override
    public void newHeartRate(int hrm) {

    }

    @Override
    public void newEnergyData(int energy) {

    }

    @Override
    public void newRRvalue(int lastRR) {

    }


    @Override
    public void newImpedance(int z) {
        impedanceDW.setValueText(String.valueOf(z));
    }


}
