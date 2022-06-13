package ro.ase.arduponic.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ro.ase.arduponic.R;

public class MasterActivity extends AppCompatActivity {

    public static boolean IS_CONNECTED_TO_BLUETOOTH_DEVICE = false;
    public static BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        NavController navController= Navigation.findNavController(MasterActivity.this,R.id.fragmentref);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    }
}