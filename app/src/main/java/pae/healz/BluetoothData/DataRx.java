
package pae.healz.BluetoothData;
/**
 * Created by Mariona on 3/05/2016.
 */

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.SQLException;
import java.util.ArrayList;

import com.csr.btsmart.BtSmartService;
import com.csr.btsmart.BtSmartService.BtSmartUuid;

import com.csr.view.DataView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import pae.healz.Activities.Processing;
import pae.healz.R;
import pae.healz.SQLite.DataSourceDAO;
import pae.healz.SQLite.ModelClassSQL;

public class DataRx extends Activity implements Animation.AnimationListener {

    private BluetoothDevice mDeviceToConnect = null;
    private BtSmartService mService = null;
    private boolean mConnected = false;

    ProgressBar pb;

    private int mProgressStatus = 0;

    // For connect timeout.
    private static Handler mHandler = new Handler();

    // Data Views to update on the display.
    DataView modulTest = null;
    DataView faseTest = null;

    DataView totalBodyWaterDW = null;
    DataView fatFreeMassDW = null;
    DataView freeMassDW = null;
    //Heart Rate Activity
    DataView heartRateData = null;
    DataView rrData = null;
    DataView energyData = null;
    DataView locationData = null;

    EditText editView = null;
    //UART info
    public byte[] newModule;
    public byte[] newPhase;
    public int cont = 0;
    public ArrayList<Float> moduls;
    public ArrayList<Float> fases;

    //Database   taula heartrate-moduuul, taula ffreemass-phase
    private ModelClassSQL modeltbw, modelfm, modelffm;
    private DataSourceDAO BD;
    private float data = 0;
    private long date = System.currentTimeMillis();
    private Processing pro;
    private int numMesuresErronies = 0;

    private ArrayList<Double> listaFM = new ArrayList<>();
    private ArrayList<Double> listaFFM = new ArrayList<>();
    private ArrayList<Double> listaTBW = new ArrayList<>();


    private static final int REQUEST_MANUFACTURER = 0;
    private static final int REQUEST_BATTERY = 1;
    private static final int REQUEST_HEART_RATE = 2;
    private static final int REQUEST_LOCATION = 3;
    private static final int REQUEST_HARDWARE_REV = 4;
    private static final int REQUEST_FW_REV = 5;
    private static final int REQUEST_SW_REV = 6;
    private static final int REQUEST_SERIAL_NO = 7;
    private static final int REQUEST_NEW_VALUES = 8;

    private static final int CONNECT_TIMEOUT_MILLIS = 5000;
    private static final int TIMEOUT_MEASURING_MILLIS = 10000;


    private static final int INFO_ACTIVITY_REQUEST = 1;

    //public GestorBLE mGestorBLE = new GestorBLE(this,this);
    //private final Handler mHeartHandler = mGestorBLE.mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prevent screen rotation.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        BD = new DataSourceDAO(this.getApplicationContext());
        modeltbw = new ModelClassSQL(2, 0, data, date);
        modelfm = new ModelClassSQL(4, 0, data, date);
        modelffm = new ModelClassSQL(1, 0, data, date);


        setContentView(R.layout.loading_data);
        //TEST
        //modulTest = (DataView) findViewById(R.id.modul);
        //faseTest = (DataView) findViewById(R.id.fase);

        pb = (ProgressBar) findViewById(R.id.progressBar);

        ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", 0, 60);

        animation.setDuration(TIMEOUT_MEASURING_MILLIS);
        animation.setInterpolator(new LinearInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //do something when the countdown is complete

                mHandler.post(new Runnable() {
                    public void run() {

                        try {


                            float medianValueFM = (float) pro.mean(listaFM);
                            float medianValueFFM = (float) pro.mean(listaFFM);
                            float medianValueTBW = (float) pro.mean(listaTBW);

                            listaFFM = new ArrayList<Double>();
                            listaTBW = new ArrayList<Double>();
                            listaFM = new ArrayList<Double>();

                            //Construcció d'objecte a la base de dades
                            modeltbw = new ModelClassSQL(2, 0, medianValueTBW, date);
                            modelfm = new ModelClassSQL(4, 0, medianValueFM, date);
                            modelffm = new ModelClassSQL(1, 0, medianValueFFM, date);

                            //Guardar base de dades
                            try {
                                BD.addparameters(modeltbw);
                                BD.addparameters(modelfm);
                                BD.addparameters(modelffm);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        catch(Exception e){
                            CharSequence text = "FATAL ERROR";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.show();                        }

                        finish();
                    }
                });
                //onDestroy();
                Intent intent = new Intent(DataRx.this, ShowData.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animation.start();


         //Get the device to connect to that was passed to us by the scan results Activity.
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

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {


    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    private static class DeviceHandler extends Handler {
        private final WeakReference<DataRx> mActivity;

        public DeviceHandler(DataRx activity) {
            mActivity = new WeakReference<DataRx>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            DataRx parentActivity = mActivity.get();
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
//                            smartService.requestCharacteristicValue(REQUEST_MANUFACTURER,
//                                    BtSmartUuid.DEVICE_INFORMATION_SERVICE.getUuid(),
//                                    BtSmartUuid.MANUFACTURER_NAME.getUuid(), parentActivity.mHeartHandler);
//
//                            smartService.requestCharacteristicValue(REQUEST_LOCATION, BtSmartUuid.HRP_SERVICE.getUuid(),
//                                    BtSmartUuid.HEART_RATE_LOCATION.getUuid(), parentActivity.mHeartHandler);
//
//                            smartService.requestCharacteristicValue(REQUEST_HARDWARE_REV,
//                                    BtSmartUuid.DEVICE_INFORMATION_SERVICE.getUuid(),
//                                    BtSmartUuid.HARDWARE_REVISION.getUuid(), parentActivity.mHeartHandler);
//
//                            smartService.requestCharacteristicValue(REQUEST_FW_REV,
//                                    BtSmartUuid.DEVICE_INFORMATION_SERVICE.getUuid(),
//                                    BtSmartUuid.FIRMWARE_REVISION.getUuid(), parentActivity.mHeartHandler);
//
//                            smartService.requestCharacteristicValue(REQUEST_SW_REV,
//                                    BtSmartUuid.DEVICE_INFORMATION_SERVICE.getUuid(),
//                                    BtSmartUuid.SOFTWARE_REVISION.getUuid(), parentActivity.mHeartHandler);
//
//                            smartService.requestCharacteristicValue(REQUEST_SERIAL_NO,
//                                    BtSmartUuid.DEVICE_INFORMATION_SERVICE.getUuid(), BtSmartUuid.SERIAL_NUMBER.getUuid(),
//                                    parentActivity.mHeartHandler);
//
//                            // Register to be told about battery level.
//                            smartService.requestCharacteristicNotification(REQUEST_BATTERY,
//                                    BtSmartUuid.BATTERY_SERVICE.getUuid(), BtSmartUuid.BATTERY_LEVEL.getUuid(),
//                                    parentActivity.mHeartHandler);
//
//                            // Register to be told about heart rate values.
//                            smartService.requestCharacteristicNotification(REQUEST_HEART_RATE,
//                                    BtSmartUuid.HRP_SERVICE.getUuid(), BtSmartUuid.HEART_RATE_MEASUREMENT.getUuid(),
//                                    parentActivity.mHeartHandler);
                            //REQUEST UART
                            smartService.requestCharacteristicNotification(REQUEST_NEW_VALUES,
                                    BtSmartUuid.UART_SERVICE.getUuid(), BtSmartUuid.TX_CHARACTERISTIC.getUuid(),
                                    parentActivity.mHeartHandler);

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
    }

    ;

    /**
     * This is the handler for characteristic value updates.
     */
    private final Handler mHeartHandler = new HeartRateHandler(this);

    private static class HeartRateHandler extends Handler {
        private final WeakReference<DataRx> mActivity;

        public HeartRateHandler(DataRx activity) {
            mActivity = new WeakReference<DataRx>(activity);
        }

        public void handleMessage(Message msg) { //Está corriendo en el threat de la ui
            DataRx parentActivity = mActivity.get();
            if (parentActivity != null) {
                switch (msg.what) {
                    case BtSmartService.MESSAGE_REQUEST_FAILED: {
                        // The request id tells us what failed.
                        int requestId = msg.getData().getInt(BtSmartService.EXTRA_CLIENT_REQUEST_ID);
                        switch (requestId) {
                            case REQUEST_HEART_RATE:
                                Toast.makeText(parentActivity, "Failed to register for heart rate notifications.",
                                        Toast.LENGTH_SHORT).show();
                                parentActivity.finish();
                                break;
                            default:
                                break;
                        }
                        break;
                    }
                    case BtSmartService.MESSAGE_CHARACTERISTIC_VALUE: {

                        Log.d("DataRx", "MESSAGE_CHARACTERISTIC_VALUE");
                        Bundle msgExtra = msg.getData();
//                        UUID serviceUuid =
//                                ((ParcelUuid) msgExtra.getParcelable(BtSmartService.EXTRA_SERVICE_UUID)).getUuid();
//                        UUID characteristicUuid =
//                                ((ParcelUuid) msgExtra.getParcelable(BtSmartService.EXTRA_CHARACTERISTIC_UUID)).getUuid();
                        //UART
//                            if (serviceUuid.compareTo(BtSmartUuid.UART_SERVICE.getUuid()) == 0
//                           && characteristicUuid.compareTo(BtSmartUuid.TX_CHARACTERISTIC.getUuid()) == 0) {
                        parentActivity.UARTHandler(msgExtra.getByteArray(BtSmartService.EXTRA_VALUE));

//                        }
//           /*             // Heart rate notification.
//                        if (serviceUuid.compareTo(BtSmartUuid.HRP_SERVICE.getUuid()) == 0
//                                && characteristicUuid.compareTo(BtSmartUuid.HEART_RATE_MEASUREMENT.getUuid()) == 0) {
//                            parentActivity.heartRateHandler(msgExtra.getByteArray(BtSmartService.EXTRA_VALUE));
//                        }*/
//                        // Device information
//                        else if (serviceUuid.compareTo(BtSmartUuid.DEVICE_INFORMATION_SERVICE.getUuid()) == 0) {
//                            String value;
//                            try {
//                                value = new String(msgExtra.getByteArray(BtSmartService.EXTRA_VALUE), "UTF-8");
//                            } catch (UnsupportedEncodingException e) {
//                                value = "--";
//                            }
//                            if (characteristicUuid.compareTo(BtSmartUuid.MANUFACTURER_NAME.getUuid()) == 0) {
//                                parentActivity.mManufacturer = value;
//                            } else if (characteristicUuid.compareTo(BtSmartUuid.HARDWARE_REVISION.getUuid()) == 0) {
//                                parentActivity.mHardwareRev = value;
//                            } else if (characteristicUuid.compareTo(BtSmartUuid.FIRMWARE_REVISION.getUuid()) == 0) {
//                                parentActivity.mFwRev = value;
//                            } else if (characteristicUuid.compareTo(BtSmartUuid.SOFTWARE_REVISION.getUuid()) == 0) {
//                                parentActivity.mSwRev = value;
//                            } else if (characteristicUuid.compareTo(BtSmartUuid.SERIAL_NUMBER.getUuid()) == 0) {
//                                parentActivity.mSerialNo = value;
//                            }
//
//                        }
//                        // Battery level notification.
//                        else if (serviceUuid.compareTo(BtSmartUuid.BATTERY_SERVICE.getUuid()) == 0
//                                && characteristicUuid.compareTo(BtSmartUuid.BATTERY_LEVEL.getUuid()) == 0) {
//                            parentActivity.batteryNotificationHandler(msgExtra.getByteArray(BtSmartService.EXTRA_VALUE)[0]);
//                        }
                        // Sensor location
//                        else if (serviceUuid.compareTo(BtSmartUuid.HRP_SERVICE.getUuid()) == 0
//                                && characteristicUuid.compareTo(BtSmartUuid.HEART_RATE_LOCATION.getUuid()) == 0) {
//                            parentActivity.sensorLocationHandler(msgExtra.getByteArray(BtSmartService.EXTRA_VALUE)[0]);
//                        }
                        break;

                    }
                }
            }
        }
    }

    ;

    private void UARTHandler(byte[] value) {

        BluetoothGattCharacteristic characteristic =
                new BluetoothGattCharacteristic(BtSmartService.BtSmartUuid.TX_CHARACTERISTIC.getUuid(), 0, 0);
        characteristic.setValue(value);

        Byte b = new Byte(value[0]);
        Log.d("DataRx", b.toString());

        //Módulo impedancia
        byte numT = characteristic.getValue()[0]; //número Trama
        byte r0 = characteristic.getValue()[1];
        byte r1 = characteristic.getValue()[2];
        byte r2 = characteristic.getValue()[3];
        byte r3 = characteristic.getValue()[4];
        newModule = new byte[]{r3, r2, r1, r0};

        Log.d("DataRxUART", "numtrama:" + String.valueOf(numT));
        Log.d("DataRxUART", "R0:" + String.valueOf(r0));
        Log.d("DataRxUART", "R1:" + String.valueOf(r1));
        Log.d("DataRxUART", "R2:" + String.valueOf(r2));
        Log.d("DataRxUART", "R3:" + String.valueOf(r3));
        //Log.d("DataRxUART","R4:" + String.valueOf(r4));

        //int module = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT32, 1);
        int module = ByteBuffer.wrap(newModule).order(ByteOrder.LITTLE_ENDIAN).getInt();
        Float fModule = new Float(module);
        fModule = fModule / 100.f;

        //Log.d("DataRxUART","MODULE:" + String.valueOf(module));

        //Fase impedancia
        byte i0 = characteristic.getValue()[5];
        byte i1 = characteristic.getValue()[6];
        byte i2 = characteristic.getValue()[7];
        byte i3 = characteristic.getValue()[8];
        newPhase = new byte[]{i3, i2, i1, i0};

        int phase = ByteBuffer.wrap(newPhase).order(ByteOrder.LITTLE_ENDIAN).getInt();
        Float fPhase = new Float(phase);
        fPhase = fPhase / 100.f;

        //float R2 = characteristic.getFloatValue(BluetoothGattCharacteristic.FORMAT_SFLOAT,5);

        //modulTest.setValueText(String.valueOf(fModule));
        //faseTest.setValueText(String.valueOf(fPhase));

        pro = new Processing(getApplicationContext());

        //Comprobacio errors 100<Z<1000
        if(pro.measureIsOk(fModule)){
            double real = fModule*Math.cos(fPhase);
            double imag = fModule*Math.sin(fPhase);

            //Calculo de las formulas en processing
            double tbw = pro.calcula_datos(2, real, imag);
            double ffm = pro.calcula_datos(1, real, imag);
            double fm = pro.calcula_datos(4, real, imag);


            listaFFM.add(ffm);
            listaTBW.add(tbw);
            listaFM.add(fm);
        }
        else {
            numMesuresErronies++;
            if(numMesuresErronies == 20){ //5 mostres per segon
                CharSequence text = "Attention, we recommend you to repeat the measure";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();            }
        }




    }

    /**
     * Do something with the battery level received in a notification.
     *
     */


 /*       public void batteryNotificationHandler(byte value) {
            mBatteryPercent = String.valueOf(value + "%");
        }*/


        public void sensorLocationHandler(int locationIndex) {
            final String[] locations = {"Other", "Chest", "Wrist", "Finger", "Hand", "Ear lobe", "Foot"};

            String location = "Not recognised";
            if (locationIndex > 0 && locationIndex < locations.length) {
                location = locations[locationIndex];
            }

            locationData.setValueText(location);
        }


        public void heartRateHandler(byte[] value) {
            final byte INDEX_FLAGS = 0;
            final byte INDEX_HRM_VALUE = 1;
            final byte INDEX_ENERGY_VALUE = 2;

            byte energyIndexOffset = 0;

            final byte FLAG_HRM_FORMAT = 0x01;
            final byte FLAG_ENERGY_PRESENT = (0x01 << 3);
            final byte FLAG_RR_PRESENT = (0x01 << 4);

            final byte SIZEOF_UINT16 = 2;

            // Re-create the characteristic with the received value.
            BluetoothGattCharacteristic characteristic =
                    new BluetoothGattCharacteristic(BtSmartUuid.HEART_RATE_MEASUREMENT.getUuid(), 0, 0);
            characteristic.setValue(value);

            byte flags = characteristic.getValue()[INDEX_FLAGS];

            // Check the flags of the characteristic to find if the heart rate number format is UINT16 or UINT8.
            int hrm = 0;
            if ((flags & FLAG_HRM_FORMAT) != 0) {
                hrm = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, INDEX_HRM_VALUE);
                // Need to offset all the energy value index by 1 as the heart rate value is taking up two bytes.
                energyIndexOffset++;
            } else {
                hrm = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, INDEX_HRM_VALUE);
            }
//            heartRateData.setValueText(String.valueOf(hrm));

            // Get the expended energy if present.
            int energyExpended = 0;
            if ((flags & FLAG_ENERGY_PRESENT) != 0) {
                energyExpended =
                        characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, INDEX_ENERGY_VALUE
                                + energyIndexOffset);
 //               energyData.setValueText(String.valueOf(energyExpended));
            }

            // Get RR interval values if present.
            if ((flags & FLAG_RR_PRESENT) != 0) {
                int lastRR = 0;
                // There can be multiple RR values - just get the most recent which will be the last uint16 in the array.
                lastRR =
                        characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, value.length - SIZEOF_UINT16);
//                rrData.setValueText(String.valueOf(lastRR));
            }
        }
}
