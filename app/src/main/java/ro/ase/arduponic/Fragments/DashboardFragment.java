package ro.ase.arduponic.Fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ro.ase.arduponic.Activities.MasterActivity;
import ro.ase.arduponic.R;
import ro.ase.arduponic.services.BluetoothLeService;


public class DashboardFragment extends Fragment {

    View view;
    Button btnDash;
    MasterActivity masterActivity;
    public TextView tvDash;
    public BluetoothLeService bleService;
    DonutProgress dStanga;
    DonutProgress dCentru;
    DonutProgress dDreapta;
    ArcProgress aBazin;
    List<DonutProgress> donuts = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        masterActivity =(MasterActivity) getActivity();
        masterActivity.dashboardFragment = DashboardFragment.this;
        bleService = masterActivity.mBluetoothLeService;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //btnDash = view.findViewById(R.id.btnDash);
        //tvDash = view.findViewById(R.id.tvDash);
        dStanga = view.findViewById(R.id.progLeft);
        dCentru = view.findViewById(R.id.progCenter);
        dDreapta = view.findViewById(R.id.progRight);
        aBazin = view.findViewById(R.id.progBazin);

        donuts = Arrays.asList(dStanga,dCentru,dDreapta);

        if(masterActivity.doAsyncTask!=null){
            masterActivity.doAsyncTask.cancel();
            masterActivity.timer.cancel();
        }

        masterActivity.timer = new Timer();
        masterActivity.doAsyncTask = new TimerTask() {
            @Override
            public void run() {
                masterActivity.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        masterActivity.refresh("Dashboard");
                    }
                });
            }
        };
        masterActivity.timer.schedule(masterActivity.doAsyncTask,1400,1400);
        return view;
    }

    public void updateGraphs(List<Integer> graphValues){

        aBazin.setProgress(graphValues.get(0));
        aBazin.setFinishedStrokeColor(getColorBazin(graphValues.get(0)));
        dStanga.setDonut_progress(graphValues.get(1).toString());
        dStanga.setText(graphValues.get(1).toString()+"%");
        dStanga.setFinishedStrokeColor(getColor(graphValues.get(1)));

        dCentru.setDonut_progress(graphValues.get(2).toString());
        dCentru.setText(graphValues.get(2).toString()+"%");
        dCentru.setFinishedStrokeColor(getColor(graphValues.get(2)));

        dDreapta.setDonut_progress(graphValues.get(3).toString());
        dDreapta.setText(graphValues.get(3).toString()+"%");
        dDreapta.setFinishedStrokeColor(getColor(graphValues.get(3)));


    }

    int getColor(int value){
        if(value <= 20){
                return masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_blue_light);
        } else if ( value > 20 && value < 75 ){
            return masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_green_light);
        }else {
            return masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light);
        }
    }

    int getColorBazin(int value){
        if(value <= 20){
            return masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light);
        } else if ( value > 20 && value < 75 ){
            return masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_green_light);
        }else {
            return masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_blue_light);
        }
    }

}