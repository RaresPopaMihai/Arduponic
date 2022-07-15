package ro.ase.arduponic.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ro.ase.arduponic.Activities.MasterActivity;
import ro.ase.arduponic.R;
import ro.ase.arduponic.services.AlerterService;
import ro.ase.arduponic.services.BluetoothLeService;

public class PumpControlFragment extends Fragment {


    MasterActivity masterActivity;
    public BluetoothLeService bleService;
    public boolean isManualOn = false;
    Button leftValve;
    Button centerValve;
    Button rightValve;
    Button waterPump;
    Button airPump;
    LottieAnimationView loadingAnimation;

    Button manualControl;
    Button execute;
    List<Button> buttonList;
    List<Boolean> buttonOpenList;

    boolean isLeftValveOpen;
    boolean isCenterValveOpen;
    boolean isRightValveOpen;
    boolean isWaterPumpOn;
    boolean isAirPumpOn;
    boolean isInitialization;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        masterActivity =(MasterActivity) getActivity();
        masterActivity.pumpControlFragment = PumpControlFragment.this;
        bleService = masterActivity.mBluetoothLeService;
        buttonList = new ArrayList<>();
        buttonOpenList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pump_control, container, false);
        leftValve = view.findViewById(R.id.btnValvaStanga);
        centerValve = view.findViewById(R.id.btnValvaCentru);
        rightValve = view.findViewById(R.id.btnValvaDreapta);
        waterPump = view.findViewById(R.id.btnPompaApa);
        airPump = view.findViewById(R.id.btnPompaAer);
        manualControl = view.findViewById(R.id.btnManual);
        execute = view.findViewById(R.id.btnExecute);
        loadingAnimation = view.findViewById(R.id.ltPL);

        buttonList.add(leftValve);
        buttonList.add(centerValve);
        buttonList.add(rightValve);
        buttonList.add(waterPump);
        buttonList.add(airPump);
        buttonList.add(manualControl);

        buttonOpenList.add(isLeftValveOpen);
        buttonOpenList.add(isCenterValveOpen);
        buttonOpenList.add(isRightValveOpen);
        buttonOpenList.add(isWaterPumpOn);
        buttonOpenList.add(isAirPumpOn);
        buttonOpenList.add(isManualOn);


        leftValve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInitialization){
                    if (!isLeftValveOpen){
                        leftValve.setText("Inchis");
                        leftValve.setTextColor(masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
                        isLeftValveOpen = false;
                    }else{
                        leftValve.setText("Deschis");
                        leftValve.setTextColor(Color.parseColor("#6200EE"));
                        isLeftValveOpen = true;
                    }
                }else {
                    if(leftValve.getText().toString().equals("Inchis")){
                        leftValve.setText("Deschis");
                        leftValve.setTextColor(Color.parseColor("#6200EE"));
                        isLeftValveOpen = true;
                    }else{
                        leftValve.setText("Inchis");
                        leftValve.setTextColor(masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
                        isLeftValveOpen = false;
                    }
                }

            }
        });
        centerValve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInitialization){
                    if (!isCenterValveOpen){
                        centerValve.setText("Inchis");
                        centerValve.setTextColor(masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
                        isCenterValveOpen = false;

                    }else{
                        centerValve.setText("Deschis");
                        centerValve.setTextColor(Color.parseColor("#6200EE"));
                        isCenterValveOpen = true;
                    }
                }else{
                    if(centerValve.getText().toString().equals("Inchis")){
                        centerValve.setText("Deschis");
                        centerValve.setTextColor(Color.parseColor("#6200EE"));
                        isCenterValveOpen = true;
                    }else{
                        centerValve.setText("Inchis");
                        centerValve.setTextColor(masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
                        isCenterValveOpen = false;
                    }
                }

            }
        });

        rightValve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInitialization){
                    if (!isRightValveOpen){
                        rightValve.setText("Inchis");
                        rightValve.setTextColor(masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
                        isRightValveOpen = false;
                    }else{
                        rightValve.setText("Deschis");
                        rightValve.setTextColor(Color.parseColor("#6200EE"));
                        isRightValveOpen = true;
                    }
                }else{
                    if(rightValve.getText().toString().equals("Inchis")){
                        rightValve.setText("Deschis");
                        rightValve.setTextColor(Color.parseColor("#6200EE"));
                        isRightValveOpen = true;
                    }else{
                        rightValve.setText("Inchis");
                        rightValve.setTextColor(masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
                        isRightValveOpen = false;
                    }
                }

            }
        });

        waterPump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInitialization){
                    if (!isWaterPumpOn){
                        waterPump.setText("Inchis");
                        waterPump.setTextColor(masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
                        isWaterPumpOn = false;
                    }else{
                        waterPump.setText("Deschis");
                        waterPump.setTextColor(Color.parseColor("#6200EE"));
                        isWaterPumpOn = true;
                    }
                }else{
                    if(waterPump.getText().toString().equals("Inchis")){
                        waterPump.setText("Deschis");
                        waterPump.setTextColor(Color.parseColor("#6200EE"));
                        isWaterPumpOn = true;
                    }else{
                        waterPump.setText("Inchis");
                        waterPump.setTextColor(masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
                        isWaterPumpOn = false;
                    }
                }

            }
        });

        airPump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInitialization){
                    if (!isAirPumpOn){
                        airPump.setText("Inchis");
                        airPump.setTextColor(masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
                        isAirPumpOn = false;
                    }else{
                        airPump.setText("Deschis");
                        airPump.setTextColor(Color.parseColor("#6200EE"));
                        isAirPumpOn = true;
                    }
                }else{
                    if(airPump.getText().toString().equals("Inchis")){
                        airPump.setText("Deschis");
                        airPump.setTextColor(Color.parseColor("#6200EE"));
                        isAirPumpOn = true;
                    }else{
                        airPump.setText("Inchis");
                        airPump.setTextColor(masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
                        isAirPumpOn = false;
                    }
                }

            }
        });

        manualControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isWaterPumpOn && !isLeftValveOpen && !isCenterValveOpen && !isRightValveOpen){
                    AlerterService.createErrorAlert(masterActivity,"Toate valvele sunt inchise",
                            "Pompa de apa se poate porni doar daca cel putin o valva este deschisa!");
                    return;
                }

                if(isInitialization){
                if (!isManualOn){
                    manualControl.setText("Execută și Pornește Modul Manual");
                    manualControl.setTextColor(masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
                    execute.setVisibility(View.INVISIBLE);
                    isManualOn = false;
                }else{
                    manualControl.setText("Oprește Modul  Manual");
                    manualControl.setTextColor(Color.parseColor("#6200EE"));
                    execute.setVisibility(View.VISIBLE);
                    isManualOn = true;
                }
                }else{
                    if(manualControl.getText().toString().equals("Execută și Pornește Modul Manual")){
                        manualControl.setText("Opreste Modul  Manual");
                        manualControl.setTextColor(Color.parseColor("#6200EE"));
                        execute.setVisibility(View.VISIBLE);
                        isManualOn = true;
                        if(isManualOn){
                            String manualRequest = "Contw/"+(isLeftValveOpen?1:0)+"/"+(isCenterValveOpen?1:0)+"/"+(isRightValveOpen?1:0)+"/"+(isWaterPumpOn?1:0)+"/"+(isAirPumpOn?1:0)+"/"+(isManualOn?1:0);
                            AlerterService.createInfoAlert(masterActivity,"Mod Manual Pornit","Modul Manual a fost pornit!",R.drawable.ic_baseline_cloud_done_24);
                            masterActivity.refresh(manualRequest);
                            setUnclickableButtons(true);
                        }

                    } else{

                        manualControl.setText("Execută și Pornește Modul Manual");
                        manualControl.setTextColor(masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
                        isManualOn = false;
                        execute.setVisibility(View.INVISIBLE);
                        if(!isManualOn){
                            AlerterService.createInfoAlert(masterActivity,"Mod Manual Oprit","Modul Manual a fost oprit!",R.drawable.ic_baseline_cloud_done_24);
                            masterActivity.refresh("manualOff");
                            setUnclickableButtons(true);
                        }
                    }
                }
            }
        });
        execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isWaterPumpOn && !isLeftValveOpen && !isCenterValveOpen && !isRightValveOpen){
                    AlerterService.createErrorAlert(masterActivity,"Toate valvele sunt inchise",
                            "Pompa de apa se poate porni doar daca cel putin o valva este deschisa!");
                    return;
                }
                if(execute.getVisibility() == View.VISIBLE && isManualOn){
                    String manualRequest = "Contw/"+(isLeftValveOpen?1:0)+"/"+(isCenterValveOpen?1:0)+"/"+(isRightValveOpen?1:0)+"/"+(isWaterPumpOn?1:0)+"/"+(isAirPumpOn?1:0)+"/"+(isManualOn?1:0);
                    masterActivity.refresh(manualRequest);
                }else{
                    Toast.makeText(masterActivity.getApplicationContext(),"Manual Control turned off",Toast.LENGTH_LONG).show();
                }
            }
        });
        final Handler handle = new Handler(Looper.getMainLooper());
        if(masterActivity.doAsyncTask!=null){
            masterActivity.doAsyncTask.cancel();
            masterActivity.timer.cancel();
        }
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                setUnclickableButtons(true);
                masterActivity.refresh("ControlPompa");
            }
        },1000);

        return view;
    }

    public void initializeButtons(List<Boolean> controlData){
        isLeftValveOpen = controlData.get(0);
        isCenterValveOpen = controlData.get(1);
        isRightValveOpen = controlData.get(2);
        isWaterPumpOn = controlData.get(3);
        isAirPumpOn = controlData.get(4);
        isManualOn = controlData.get(5);

        isInitialization = true;

        for(Button button : buttonList){
            button.callOnClick();
        }
        isInitialization = false;
    }

    public void setUnclickableButtons(boolean isUnclickable){
        if(isUnclickable){
            loadingAnimation.setVisibility(View.VISIBLE);
            for(Button button : buttonList){
                button.setClickable(false);
            }
        }else{
            loadingAnimation.setVisibility(View.GONE);
            for(Button button : buttonList){
                button.setClickable(true);
            }
        }
    }


}