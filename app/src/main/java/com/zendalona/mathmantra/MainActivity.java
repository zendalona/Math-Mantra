package com.zendalona.mathmantra;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.zendalona.mathmantra.databinding.ActivityMainBinding;
import com.zendalona.mathmantra.ui.learn.DashboardFragment;
import com.zendalona.mathmantra.utils.FragmentNavigation;
import com.zendalona.mathmantra.utils.PermissionManager;

public class MainActivity extends AppCompatActivity implements FragmentNavigation {
    private ActivityMainBinding binding;
    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set immersive full-screen mode
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        if (savedInstanceState == null) loadFragment(new DashboardFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);


        //Permissions management
        permissionManager = new PermissionManager(this, new PermissionManager.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                Log.d("PermissionManager.PermissionCallback", "Granted!");
            }
            @Override
            public void onPermissionDenied() {
                Log.w("PermissionManager.PermissionCallback", "Denied!");
            }
        });
        // TODO : ask for the sensor permissions
        permissionManager.requestMicrophonePermission();
//        permissionManager.requestAccelerometerPermission();
    }

    @Override
    public boolean onSupportNavigateUp() {
        loadFragment(new DashboardFragment(), FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.handlePermissionsResult(requestCode, permissions, grantResults);
    }

    public void loadFragment(Fragment fragment, int transition) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(transition);
        fragmentTransaction.replace(binding.fragmentContainer.getId(), fragment);
        // TODO : binding.toolbar.setTitle();
        fragmentTransaction.commit();
    }

}