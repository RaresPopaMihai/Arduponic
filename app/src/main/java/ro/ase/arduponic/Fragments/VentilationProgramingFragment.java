package ro.ase.arduponic.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;

import ro.ase.arduponic.Activities.MasterActivity;
import ro.ase.arduponic.R;
import ro.ase.arduponic.services.AlerterService;


public class VentilationProgramingFragment extends Fragment {
   MasterActivity masterActivity;
   TextView tvTimeProgramed;
   Spinner spinMinBegin;
   Spinner spinTime;
   TextView tvTime;
   Button btnProgram;
   LottieAnimationView loadingAnimation;

   int minutStart;
   int numarMinute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        masterActivity = (MasterActivity) getActivity();
        masterActivity.ventilationProgramingFragment = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ventilation_programing, container, false);
        tvTimeProgramed=view.findViewById(R.id.tvVentProg);
        spinMinBegin = view.findViewById(R.id.spinVentMinut);
        spinTime = view.findViewById(R.id.spinVentDurata);
        tvTime = view.findViewById(R.id.tvDurata);
        btnProgram = view.findViewById(R.id.btnProgram);
        loadingAnimation = view.findViewById(R.id.ltPA);

        String[] minutes = new String[60];
        for(int i = 0; i<60;i++){
            minutes[i]= i+"";
        }

        String[] duration = new String[59];
        for(int j = 0; j<59;j++){
            duration[j]= (j+1)+"";
        }

        ArrayAdapter<String> adapterMinute = new ArrayAdapter<String>(masterActivity, android.R.layout.simple_spinner_item, minutes);
        adapterMinute.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMinBegin.setAdapter(adapterMinute);

        ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(masterActivity, android.R.layout.simple_spinner_item, duration);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTime.setAdapter(adapterTime);

        btnProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingAnimation.setVisibility(View.VISIBLE);
                int minutStartSelected = Integer.parseInt(spinMinBegin.getSelectedItem().toString().trim());
                int timeSelected = Integer.parseInt(spinTime.getSelectedItem().toString().trim());
                if(minutStartSelected == minutStart && numarMinute == timeSelected){
                    AlerterService.createInfoAlert(masterActivity,"Programare Identica","Selectatia programarii este identica cu cea deja setata!",R.drawable.ic_baseline_error_24);
                }else{
                    String programSent ="Vent/";
                    if(minutStartSelected<10){
                        programSent+="00"+minutStartSelected+"/";
                    }
                    else{
                        programSent+="0"+minutStartSelected+"/";
                    }

                    if(timeSelected<10){
                        programSent+="00"+timeSelected;
                    }
                    else{
                        programSent+="0"+timeSelected;
                    }
                    masterActivity.refresh(programSent);
                    setUnclickableButtons(true);
                    AlerterService.createInfoAlert(masterActivity,"Programare Setata","Programarea aerisirii a fost setata cu succes!",R.drawable.ic_baseline_cloud_done_24);

                }
                loadingAnimation.setVisibility(View.GONE);
            }
        });

        if(masterActivity.doAsyncTask!=null){
            masterActivity.doAsyncTask.cancel();
            masterActivity.timer.cancel();
        }

        final Handler handle = new Handler(Looper.getMainLooper());
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                setUnclickableButtons(true);
                masterActivity.refresh("ProgramReq");
            }
        },1000);

        return view;
    }

    public void setUnclickableButtons(boolean isUnclickable){
        if(isUnclickable){
           loadingAnimation.setVisibility(View.VISIBLE);
           btnProgram.setClickable(false);
           spinMinBegin.setClickable(false);
           spinTime.setClickable(false);
        }else{
            loadingAnimation.setVisibility(View.GONE);
            btnProgram.setClickable(true);
            spinMinBegin.setClickable(true);
            spinTime.setClickable(true);
        }
    }

    public void intitializeData(List<Integer> programData){
        minutStart = programData.get(0);
        numarMinute = programData.get(1);
        tvTimeProgramed.setText("Minut Start: "+minutStart+" Durata: "+numarMinute+" minute");
    }

}