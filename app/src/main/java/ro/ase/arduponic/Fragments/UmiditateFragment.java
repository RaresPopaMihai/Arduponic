package ro.ase.arduponic.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ro.ase.arduponic.Activities.MasterActivity;
import ro.ase.arduponic.R;
import ro.ase.arduponic.services.AlerterService;

public class UmiditateFragment extends Fragment {
    MasterActivity masterActivity;

    EditText etMinLeft;
    EditText etMaxLeft;
    EditText etMinCenter;
    EditText etMaxCenter;
    EditText etMinRight;
    EditText etMaxRight;
    Button btnModify;
    LottieAnimationView loadingView;

    int minLeft;
    int maxLeft;
    int minCenter;
    int maxCenter;
    int minRight;
    int maxRight;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        masterActivity = (MasterActivity) getActivity();
        masterActivity.umiditateFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_umiditate, container, false);
        etMinLeft = view.findViewById(R.id.etSMin);
        etMaxLeft = view.findViewById(R.id.etSMax);
        etMinCenter = view.findViewById(R.id.etCMin);
        etMaxCenter = view.findViewById(R.id.etCMax);
        etMinRight = view.findViewById(R.id.etDMin);
        etMaxRight = view.findViewById(R.id.etDMax);
        btnModify = view.findViewById(R.id.btnModify);
        loadingView = view.findViewById(R.id.ltUL);

        etMinLeft.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkValue(etMinLeft,etMaxLeft);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etMaxLeft.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkValue(etMaxLeft,etMinLeft);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etMinCenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkValue(etMinCenter,etMaxCenter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etMaxCenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkValue(etMaxCenter,etMinCenter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etMinRight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkValue(etMinRight,etMaxRight);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etMaxRight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkValue(etMaxRight,etMinRight);
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                masterActivity.refresh("Umiditate");
            }
        },1000);

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOk = true;
               int minLeftSelected = Integer.parseInt(etMinLeft.getText().toString().trim());
               int maxLeftSelected = Integer.parseInt(etMaxLeft.getText().toString().trim());
               int minCenterSelected = Integer.parseInt(etMinCenter.getText().toString().trim());
               int maxCenterSelected = Integer.parseInt(etMaxCenter.getText().toString().trim());
               int minRightSelected = Integer.parseInt(etMinRight.getText().toString().trim());
               int maxRightSelected = Integer.parseInt(etMaxRight.getText().toString().trim());

               if(minLeft == minLeftSelected && maxLeft == maxLeftSelected &&
               minCenter == minCenterSelected && maxCenter == maxCenterSelected &&
               minRight == minRightSelected && maxRight == maxRightSelected){
                   AlerterService.createInfoAlert(masterActivity,"Nici o modificare!","Valorile selectate sunt identice cu cele deja setate",R.drawable.ic_baseline_error_24);
               }else {
                   if(minLeftSelected>=maxLeftSelected){
                       isOk = false;
                       etMinLeft.setTextColor(masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
                   }
                   if(minCenterSelected>=maxCenterSelected){
                       isOk = false;
                       etMinCenter.setTextColor(masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
                   }
                   if(minRightSelected>=maxRightSelected){
                       isOk = false;
                       etMinCenter.setTextColor(masterActivity.getApplicationContext().getResources().getColor(android.R.color.holo_red_light));
                   }
                   if(isOk){
                       String request = "U:"+formatDataForRequest(minLeftSelected)+formatDataForRequest(maxLeftSelected)+formatDataForRequest(minCenterSelected)+formatDataForRequest(maxCenterSelected)+formatDataForRequest(minRightSelected)+formatDataForRequest(maxRightSelected);
                       AlerterService.createInfoAlert(masterActivity,"Umiditate Setata!","Valorile umiditatii au fost modificate cu succes",R.drawable.ic_baseline_cloud_done_24);
                       masterActivity.refresh(request);

                   }
                   else{
                       AlerterService.createErrorAlert(masterActivity,"Valori Eronate!","Minimul trebuie sa fie mai mic ca maximul!");
                   }
               }
            }
        });

        return view;
    }

    private void checkValue(EditText editText,EditText editTextRelated){
        editText.setTextColor(Color.parseColor("#6200EE"));
        editTextRelated.setTextColor(Color.parseColor("#6200EE"));
        try {
            int value = Integer.parseInt(editText.getText().toString());
            if(value>100){
                editText.setText("100");
            }
            if(value<0){
                editText.setText("0");
            }
        }catch (Exception ex){

        }

    }

    public void setUnclickableButtons(boolean isUnclickable){
        if(isUnclickable){
            loadingView.setVisibility(View.VISIBLE);
            etMinLeft.setClickable(false);
            etMaxLeft.setClickable(false);
            etMinCenter.setClickable(false);
            etMaxCenter.setClickable(false);
            etMinRight.setClickable(false);
            etMaxRight.setClickable(false);
            btnModify.setClickable(false);
        }else{
            loadingView.setVisibility(View.GONE);
            etMinLeft.setClickable(true);
            etMaxLeft.setClickable(true);
            etMinCenter.setClickable(true);
            etMaxCenter.setClickable(true);
            etMinRight.setClickable(true);
            etMaxRight.setClickable(true);
            btnModify.setClickable(true);
        }
    }

    public void intitializeData(List<Integer> programData){
        minLeft = programData.get(0);
        maxLeft = programData.get(1);
        minCenter = programData.get(2);
        maxCenter = programData.get(3);
        minRight = programData.get(4);
        maxRight = programData.get(5);

        etMinLeft.setText(minLeft+"");
        etMaxLeft.setText(maxLeft+"");
        etMinCenter.setText(minCenter+"");
        etMaxCenter.setText(maxCenter+"");
        etMinRight.setText(minRight+"");
        etMaxRight.setText(maxRight+"");
    }

    public String formatDataForRequest(int data){
        String sent="";
        if(data<10){
            sent+="00"+data;
        }
        else if (data<100){
            sent+="0"+data;
        }else{
            sent = ""+data;
        }
        return sent;
    };
}