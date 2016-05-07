package pae.healz.NotUsed;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.widget.Toast;

import com.csr.btsmart.BtSmartService;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Array;
import java.util.UUID;

/**
 * Created by marionacaros on 28/4/16.
 */
public class GestorBLE {

    private static final int REQUEST_MANUFACTURER = 0;
    private static final int REQUEST_BATTERY = 1;
    private static final int REQUEST_HEART_RATE = 2;
    private static final int REQUEST_LOCATION = 3;
    private static final int REQUEST_HARDWARE_REV = 4;
    private static final int REQUEST_FW_REV = 5;
    private static final int REQUEST_SW_REV = 6;
    private static final int REQUEST_SERIAL_NO = 7;
    private static final int REQUEST_COMPOSITION = 8;//
    public Handler mHandler;



    /**
     * This is the handler for characteristic value updates.
     */

    public GestorBLE(Activity a, IBLEDATA interfaz) {

        mHandler = new HeartRateHandler(a, interfaz);
    }


    public interface IBLEDATA {

        //void batteryNotificationHandler(byte value); //@param value The battery percentage value.
        void sensorLocationHandler(int locationIndex); //@param locationIndex Value received in location characteristic - indexes into locations array.

        void newHeartRate(int hrm);

        void newEnergyData(int energy);

        void newRRvalue(int lastRR);

        void newImpedance(int z);


    }

    private static class HeartRateHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public IBLEDATA mInterfaz;
        private String mBatteryPercent;
        public byte[] newModule;
        public byte[] newPhase;
        public int cont=0;
        public float[] moduls = new float[1000];
        public float[] fases = new float[1000];

        public HeartRateHandler(Activity activity, IBLEDATA interfaz) {
            mInterfaz = interfaz;
            mActivity = new WeakReference<Activity>(activity);
        }


        public void handleMessage(Message msg) {
            Activity parentActivity = mActivity.get();
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
                        Bundle msgExtra = msg.getData();
                        UUID serviceUuid =
                                ((ParcelUuid) msgExtra.getParcelable(BtSmartService.EXTRA_SERVICE_UUID)).getUuid();
                        UUID characteristicUuid =
                                ((ParcelUuid) msgExtra.getParcelable(BtSmartService.EXTRA_CHARACTERISTIC_UUID)).getUuid();

                        // Heart rate notification.
                        if (serviceUuid.compareTo(BtSmartService.BtSmartUuid.HRP_SERVICE.getUuid()) == 0
                                && characteristicUuid.compareTo(BtSmartService.BtSmartUuid.HEART_RATE_MEASUREMENT.getUuid()) == 0) {
                            heartRateHandler(msgExtra.getByteArray(BtSmartService.EXTRA_VALUE));
                        }
                        //AÑADIR SERVICES Y CARACTERÍSTICAS

                        //Body Composition
                        else if (serviceUuid.compareTo(BtSmartService.BtSmartUuid.BCS_SERVICE.getUuid()) == 0
                                && characteristicUuid.compareTo(BtSmartService.BtSmartUuid.BODY_COMPOSITION_MEASUREMENT.getUuid()) == 0) {
                            CompositionHandler(msgExtra.getByteArray(BtSmartService.EXTRA_VALUE));
                        }

                        // Device information
                        else if (serviceUuid.compareTo(BtSmartService.BtSmartUuid.DEVICE_INFORMATION_SERVICE.getUuid()) == 0) {
                            String value;
                            try {
                                value = new String(msgExtra.getByteArray(BtSmartService.EXTRA_VALUE), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                value = "--";
                            }
                            if (characteristicUuid.compareTo(BtSmartService.BtSmartUuid.MANUFACTURER_NAME.getUuid()) == 0) {
                                //parentActivity.mManufacturer = value;
                            } else if (characteristicUuid.compareTo(BtSmartService.BtSmartUuid.HARDWARE_REVISION.getUuid()) == 0) {
                                //parentActivity.mHardwareRev = value;
                            } else if (characteristicUuid.compareTo(BtSmartService.BtSmartUuid.FIRMWARE_REVISION.getUuid()) == 0) {
                                //parentActivity.mFwRev = value;
                            } else if (characteristicUuid.compareTo(BtSmartService.BtSmartUuid.SOFTWARE_REVISION.getUuid()) == 0) {
                                //parentActivity.mSwRev = value;
                            } else if (characteristicUuid.compareTo(BtSmartService.BtSmartUuid.SERIAL_NUMBER.getUuid()) == 0) {
                                // parentActivity.mSerialNo = value;
                            }

                        }
                        // Battery level notification.
                        else if (serviceUuid.compareTo(BtSmartService.BtSmartUuid.BATTERY_SERVICE.getUuid()) == 0
                                && characteristicUuid.compareTo(BtSmartService.BtSmartUuid.BATTERY_LEVEL.getUuid()) == 0) {
                            batteryNotificationHandler(msgExtra.getByteArray(BtSmartService.EXTRA_VALUE)[0]);
                        }
                        // Sensor location
                        else if (serviceUuid.compareTo(BtSmartService.BtSmartUuid.HRP_SERVICE.getUuid()) == 0
                                && characteristicUuid.compareTo(BtSmartService.BtSmartUuid.HEART_RATE_LOCATION.getUuid()) == 0) {
                            mInterfaz.sensorLocationHandler(msgExtra.getByteArray(BtSmartService.EXTRA_VALUE)[0]);
                        }

                        // Nordic UART Service (NUS) android
                        else if (serviceUuid.compareTo(BtSmartService.BtSmartUuid.UART_SERVICE.getUuid()) == 0
                                && characteristicUuid.compareTo(BtSmartService.BtSmartUuid.RX_CHARACTERISTIC.getUuid()) == 0) {
                            UARTHandler(msgExtra.getByteArray(BtSmartService.EXTRA_VALUE));
                        }
                        break;
                    }
                }
            }
        }


        private void UARTHandler(byte[] value) {

            BluetoothGattCharacteristic characteristic =
                    new BluetoothGattCharacteristic(BtSmartService.BtSmartUuid.RX_CHARACTERISTIC.getUuid(), 0, 0);
            characteristic.setValue(value);

            byte numT = characteristic.getValue()[0]; //número Trama
            byte r0 = characteristic.getValue()[1];
            byte r1 = characteristic.getValue()[2];
            byte r2 = characteristic.getValue()[3];
            byte r3 = characteristic.getValue()[4];
            newModule = new byte[]{r0, r1, r2, r3};

            float module = ByteBuffer.wrap(newModule).order(ByteOrder.BIG_ENDIAN).getFloat();
            //float R2 = characteristic.getFloatValue(BluetoothGattCharacteristic.FORMAT_SFLOAT,1);

            byte i0 = characteristic.getValue()[5];
            byte i1 = characteristic.getValue()[6];
            byte i2 = characteristic.getValue()[7];
            byte i3 = characteristic.getValue()[8];
            newPhase = new byte[]{i0,i1,i2,i3};

            float phase = ByteBuffer.wrap(newModule).order(ByteOrder.BIG_ENDIAN).getFloat();
            //float R2 = characteristic.getFloatValue(BluetoothGattCharacteristic.FORMAT_SFLOAT,1);

            moduls[cont]=module;
            fases[cont]=phase;
            cont++;

        }





        public void batteryNotificationHandler(byte value) {

            mBatteryPercent = String.valueOf(value + "%");
        }

        /**
         * Extract the various values from the heart rate characteristic and display in the UI.
         * <p>
         * <p>
         * Value received in the characteristic notification.
         */
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
                    new BluetoothGattCharacteristic(BtSmartService.BtSmartUuid.HEART_RATE_MEASUREMENT.getUuid(), 0, 0);
            characteristic.setValue(value);

            byte flags = characteristic.getValue()[INDEX_FLAGS]; //COJE BYTE 0 PARA LEER FLAGS ARRAY

            // Check the flags of the characteristic to find if the heart rate number format is UINT16 or UINT8.
            int hrm = 0;
            if ((flags & FLAG_HRM_FORMAT) != 0) {
                hrm = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, INDEX_HRM_VALUE); //BYTE 1
                // Need to offset all the energy value index by 1 as the heart rate value is taking up two bytes.
                energyIndexOffset++;
            } else {
                hrm = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, INDEX_HRM_VALUE);
            }
            mInterfaz.newHeartRate(hrm);
//            heartRateData.setValueText(String.valueOf(hrm));

            // Get the expended energy if present.
            int energyExpended = 0;
            if ((flags & FLAG_ENERGY_PRESENT) != 0) {
                energyExpended =
                        characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, INDEX_ENERGY_VALUE
                                + energyIndexOffset);
                mInterfaz.newEnergyData(energyExpended);
//                energyData.setValueText(String.valueOf(energyExpended));
            }

            // Get RR interval values if present.
            if ((flags & FLAG_RR_PRESENT) != 0) {
                int lastRR = 0;
                // There can be multiple RR values - just get the most recent which will be the last uint16 in the array.
                lastRR = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, value.length - SIZEOF_UINT16);
                mInterfaz.newRRvalue(lastRR);
//                rrData.setValueText(String.valueOf(lastRR));
            }
        }


        public void CompositionHandler(byte[] value) {

            final byte INDEX_FLAGS = 0;
            final byte INDEX_IMPEDANCE_VALUE = 9; //index por bytes

            final byte indexOffset = 1; //16 bits

            final short FLAG_IMPEDANCE_PRESENT = (0x01 << 9);

            int z = 0;

            // Re-create the characteristic with the received value.
            BluetoothGattCharacteristic characteristic =
                    new BluetoothGattCharacteristic(BtSmartService.BtSmartUuid.BODY_COMPOSITION_MEASUREMENT.getUuid(), 0, 0);
            characteristic.setValue(value);

            byte flags = characteristic.getValue()[INDEX_FLAGS];

            //AGAFAR INFO BITS

            if ((flags & FLAG_IMPEDANCE_PRESENT) != 0) {
                z = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, INDEX_IMPEDANCE_VALUE + indexOffset);
                mInterfaz.newImpedance(z);
            }

        }
    }


    ;

}
