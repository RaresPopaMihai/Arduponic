package ro.ase.arduponic.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.UUID;

import ro.ase.arduponic.R;

public class MasterActivity extends AppCompatActivity {

    public static boolean IS_CONNECTED_TO_BLUETOOTH_DEVICE = false;
    public static BottomNavigationView navigationView;
    public static BluetoothAdapter myBluetoothAdapter;
    public static BluetoothGatt myBluetoothGatt;
    public final static String ACTION_DATA_AVAILABLE = "ro.ase.arduponic.Activities.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "ro.ase.arduponic.Activities.EXTRA_DATA";
    private static String DEVICE_ADDRESS = "98:FB:D3:1F:7E:44";
    public static final UUID RX_SERVICE_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public static final UUID MY_CHAR_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        final BluetoothManager myBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        myBluetoothAdapter=myBluetoothManager.getAdapter();

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        NavController navController= Navigation.findNavController(MasterActivity.this,R.id.fragmentref);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    }

    public void connect(){
        Toast.makeText(MasterActivity.this,"Connecting...",Toast.LENGTH_LONG).show();
        BluetoothDevice device = myBluetoothAdapter.getRemoteDevice(DEVICE_ADDRESS);

    }

    private final BluetoothGattCallback btCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if(newState != BluetoothProfile.STATE_CONNECTED){
                IS_CONNECTED_TO_BLUETOOTH_DEVICE = true;
                gatt.discoverServices();
                Toast.makeText(MasterActivity.this,"Not connected",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(MasterActivity.this,"Connected",Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status==BluetoothGatt.GATT_SUCCESS){
                //all Services have been discovered
                List<BluetoothGattService> list = gatt.getServices();
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //we are still connected to the service
            if (status==BluetoothGatt.GATT_SUCCESS){
                //send the characteristic to broadcastupdate
                broadcastupdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            broadcastupdate(ACTION_DATA_AVAILABLE, characteristic);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }
    };

    //Get the 'real' data out of characteristic
    private void broadcastupdate(final String action,final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        //only  when it is the right characteristic?/service?
        //get the 'real' data from the stream
        intent.putExtra(EXTRA_DATA, characteristic.getValue());
        //send the extracted data via LocalBroadcastManager
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
};