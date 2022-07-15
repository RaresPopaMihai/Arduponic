package ro.ase.arduponic.Fragments;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import ro.ase.arduponic.Activities.MasterActivity;
import ro.ase.arduponic.R;

public class NotConnectedFragment extends Fragment {

    View view ;
    Button btnConnect;
    private BluetoothAdapter bleAdapter;
    private static final int REQUEST_LOCATION_ID = 2;
    private boolean isPermitted = false;
    public LottieAnimationView loadingAnimation;
    private MasterActivity masterActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        masterActivity = (MasterActivity) getActivity();
        masterActivity.notConnectedFragment = NotConnectedFragment.this;
        locationCheck();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_not_connected, container, false);
        btnConnect = view.findViewById(R.id.btnConnect);
        loadingAnimation = view.findViewById(R.id.ltNC);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPermitted){
                    locationCheck();
                    if(isPermitted){
                        MasterActivity masterActivity = (MasterActivity) getActivity();
                        masterActivity.connectBLE();
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(),"Not permitted to access location...",Toast.LENGTH_LONG);
                    }
                }
                else{
                    MasterActivity masterActivity = (MasterActivity) getActivity();
                  masterActivity.connectBLE();

                  };

            }
        });

        return view;
    }



    private void locationCheck() {
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Location permission has not been granted.
            ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION_ID);
            isPermitted = true;
            return;
        }
        isPermitted = true;
    }




}