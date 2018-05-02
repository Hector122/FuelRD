package rd.fuel.project.com.rd.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import rd.fuel.project.com.R;

import rd.fuel.project.com.rd.R;

import rd.fuel.project.com.rd.fragments.GasPriceFragment;

import rd.fuel.project.com.rd.fragments.GasPriceFragment;

public class DashboardContainerActivity extends AppCompatActivity {

    //private static final String TAG = "TAG_EXCEPTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();

        initializerVariables();
    }

    /**
     * Initializer the variable.
     */

    private void initializerVariables() {
        // Initializer and set the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the principal fragment.
        Fragment gasPriceFragment = new GasPriceFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment_container, gasPriceFragment);
        fragmentTransaction.commit();
    }
}
