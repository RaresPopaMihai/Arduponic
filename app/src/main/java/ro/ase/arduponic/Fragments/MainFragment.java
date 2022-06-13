package ro.ase.arduponic.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ro.ase.arduponic.Activities.MasterActivity;
import ro.ase.arduponic.R;


public class MainFragment extends Fragment {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_main, container, false);
        showCorrectFragment();
        return view;
    }

    private void showCorrectFragment(){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(MasterActivity.IS_CONNECTED_TO_BLUETOOTH_DEVICE){
            fragmentTransaction.replace(R.id.FragmentReplacer,new DashboardFragment());
            getActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.VISIBLE);
        }else{
            getActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.INVISIBLE);
            fragmentTransaction.replace(R.id.FragmentReplacer,new NotConnectedFragment());
        }
        fragmentTransaction.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        showCorrectFragment();
    }
}