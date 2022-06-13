package ro.ase.arduponic.Fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ro.ase.arduponic.Activities.MasterActivity;
import ro.ase.arduponic.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotConnectedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotConnectedFragment extends Fragment {

    View view ;
    Button btnConnect;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_not_connected, container, false);
        btnConnect = view.findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_MAIN, null);
//                ComponentName cn = new ComponentName("com.android.settings",
//                        "com.android.settings.bluetooth.BluetoothSettings");
//                intent.setComponent(cn);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity( intent);
                MasterActivity.IS_CONNECTED_TO_BLUETOOTH_DEVICE = true;
            }
        });
        return view;
    }


}