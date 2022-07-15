package ro.ase.arduponic.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.stream.Collectors;

import ro.ase.arduponic.Fragments.DashboardFragment;
import ro.ase.arduponic.Fragments.MainFragment;
import ro.ase.arduponic.Fragments.NotConnectedFragment;
import ro.ase.arduponic.Fragments.PumpControlFragment;
import ro.ase.arduponic.Fragments.UmiditateFragment;
import ro.ase.arduponic.Fragments.VentilationProgramingFragment;
import ro.ase.arduponic.R;
import ro.ase.arduponic.services.AlerterService;
import ro.ase.arduponic.services.BluetoothLeService;

public class MasterActivity extends AppCompatActivity {
    private final static String TAG = MasterActivity.class.getSimpleName();
    public static Boolean IS_CONNECTED_TO_BLUETOOTH_DEVICE ;
    public static String MESSAGE_RECEIVED;

    public static final UUID SERVICE_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public static final UUID HM10_CHARACTERISTIC = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    public static final UUID NOTIFICATION_CHARACTERISTIC = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    public NotConnectedFragment notConnectedFragment;
    public MainFragment mainFragment;
    public DashboardFragment dashboardFragment;
    public PumpControlFragment pumpControlFragment;
    public VentilationProgramingFragment ventilationProgramingFragment;
    public UmiditateFragment umiditateFragment;

    NavController navController;
    BottomNavigationView bottomNavigationView;

    public BluetoothManager manager;
    public BluetoothAdapter adapter;

    final public Handler handler = new Handler();
    public Timer timer = new Timer();
    public TimerTask doAsyncTask;

    public BluetoothLeService mBluetoothLeService;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBluetoothLeService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        if(IS_CONNECTED_TO_BLUETOOTH_DEVICE == null){
            IS_CONNECTED_TO_BLUETOOTH_DEVICE = new Boolean(false);
        }

        manager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        adapter = manager.getAdapter();

        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        navController= Navigation.findNavController(MasterActivity.this,R.id.fragmentref);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);



    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
//        if (mBluetoothLeService != null) {
//            final boolean result = mBluetoothLeService.connect();
//            Log.d(TAG, "Connect request result=" + result);
//        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public void connectBLE(){
        notConnectedFragment.loadingAnimation.setVisibility(View.VISIBLE);
        try{
            if(mBluetoothLeService!=null)
                mBluetoothLeService.connect();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                if(notConnectedFragment != null){
                    notConnectedFragment.loadingAnimation.setVisibility(View.GONE);
                }
                IS_CONNECTED_TO_BLUETOOTH_DEVICE = true;
                mainFragment.showCorrectFragment();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                IS_CONNECTED_TO_BLUETOOTH_DEVICE = false;
                if(notConnectedFragment != null){
                    notConnectedFragment.loadingAnimation.setVisibility(View.GONE);
                }
                AlerterService.createAlertNotConnectedError(MasterActivity.this);
                navController.navigate(R.id.mainFragment);
                NavigationUI.setupWithNavController(bottomNavigationView,navController);

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                MESSAGE_RECEIVED = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                if(MESSAGE_RECEIVED.startsWith("D")){
                    List<String> dashData = Arrays.asList(MESSAGE_RECEIVED.split("/"));
                    int NivelBazin = Integer.parseInt(dashData.get(1));
                    int uPlantaStanga = Integer.parseInt(dashData.get(2));
                    int uPlantaCentru = Integer.parseInt(dashData.get(3));
                    int uPlantaDreapta = Integer.parseInt(dashData.get(4));

                    if(dashboardFragment!=null){
                        List<Integer> dashValues = Arrays.asList(NivelBazin,uPlantaStanga,uPlantaCentru,uPlantaDreapta);
                        dashboardFragment.updateGraphs(dashValues);
                    }
                }
                if(MESSAGE_RECEIVED.startsWith("Contr")){
                    List<String> controlReceivedData = Arrays.asList(MESSAGE_RECEIVED.split("/"));
                    List<Boolean> controlData = new ArrayList<>();
                    int i = 0;
                    for(String value: controlReceivedData){
                        if(i==7)
                            continue;
                        if(i == 0){
                            i++;
                            continue;
                        }
                        if(Integer.parseInt(value)==1){
                            controlData.add(true);
                        }else{
                            controlData.add(false);
                        }
                        i++;
                    }
                    if(pumpControlFragment!=null){
                        pumpControlFragment.initializeButtons(controlData);
                        pumpControlFragment.setUnclickableButtons(false);
                    }

                }
                if(MESSAGE_RECEIVED.contains("ContOk")){
                    if(pumpControlFragment!=null){
                        pumpControlFragment.setUnclickableButtons(false);
                    }
                }
                if(MESSAGE_RECEIVED.contains("Prog")){
                    List<String> programReceivedData = Arrays.asList(MESSAGE_RECEIVED.split("/"));
                    List<Integer> programData = new ArrayList<>();
                    programData.add(Integer.parseInt(programReceivedData.get(1)));
                    programData.add(Integer.parseInt(programReceivedData.get(2)));

                    if(ventilationProgramingFragment !=null){
                        ventilationProgramingFragment.intitializeData(programData);
                        ventilationProgramingFragment.setUnclickableButtons(false);
                    }
                }
                if(MESSAGE_RECEIVED.contains("U:")){
                    String umidReceivedBuffer = MESSAGE_RECEIVED.split(":")[1];
                    List<String> umidReceivedData = new ArrayList<>();
                    List<Integer> umidData = new ArrayList<>();
                    for (int i = 0; i < umidReceivedBuffer.length(); i += 3) {
                        umidReceivedData.add(umidReceivedBuffer.substring(i, Math.min(umidReceivedBuffer.length(), i + 3)));
                    }
                    for (String data :umidReceivedData) {
                        umidData.add(Integer.parseInt(data));
                    }

                    if(umiditateFragment !=null){
                        umiditateFragment.intitializeData(umidData);
                        umiditateFragment.setUnclickableButtons(false);
                    }
                }
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };


    private void displayData(String data) {
        if (data != null) {
            Log.d("Received: ",data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mBluetoothLeService.disconnect();
    }

    public void refresh(String command){
        mBluetoothLeService.writeCharacteristic(command);
    }
};