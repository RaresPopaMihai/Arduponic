package ro.ase.arduponic.services;

import android.app.Activity;
import com.tapadoo.alerter.Alerter;

import ro.ase.arduponic.Activities.MasterActivity;
import ro.ase.arduponic.R;


public class AlerterService {
    public static void createAlertNotConnectedError(MasterActivity activity){
        Alerter.create(activity)
                .setTitle("Neconectat!")
                .setText("Asigurati-va ca dispozitivul este pornit si in raza de conectare")
                .setBackgroundColorRes(R.color.design_default_color_error)
                .setDuration(5000)
                .enableSwipeToDismiss()
                .setIcon(R.drawable.ic_baseline_error_24)
                .enableIconPulse(true)
                .show();
    }

    public static void createInfoAlert(MasterActivity activity, String title, String text, int icon){
        Alerter.create(activity)
                .setTitle(title)
                .setText(text)
                .setBackgroundColorRes(R.color.design_default_color_primary_dark)
                .setDuration(5000)
                .enableSwipeToDismiss()
                .setIcon(icon)
                .enableIconPulse(true)
                .show();
    }

    public static void createErrorAlert(MasterActivity activity, String title, String text){
        Alerter.create(activity)
                .setTitle(title)
                .setText(text)
                .setBackgroundColorRes(R.color.design_default_color_error)
                .setDuration(5000)
                .enableSwipeToDismiss()
                .setIcon(R.drawable.ic_baseline_error_24)
                .enableIconPulse(true)
                .show();
    }
}
