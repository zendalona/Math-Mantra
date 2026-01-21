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
import com.zendalona.mathmantra.ui.DashboardFragment;
import com.zendalona.mathmantra.utils.FragmentNavigation;
import com.zendalona.mathmantra.utils.PermissionManager;

import java.util.Optional;

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
                Log.d("PermissionManager.PermissionCallback", "Permission granted!");
            }
            @Override
            public void onPermissionDenied() {
                Log.w("PermissionManager.PermissionCallback", "Permission denied!");
            }
        });
        
        // Request necessary permissions for app functionality
        permissionManager.requestMicrophonePermission();
        permissionManager.requestAccelerometerPermission();
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
        
        // Set appropriate toolbar title based on fragment type
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            String title = getFragmentTitle(fragment);
            actionBar.setTitle(title);
        }
        
        fragmentTransaction.commit();
    }
    
    private String getFragmentTitle(Fragment fragment) {
        String className = fragment.getClass().getSimpleName();
        switch (className) {
            case "DashboardFragment":
                return getString(R.string.app_name);
            case "ChooseLessonFragment":
                return "Choose Lesson";
            case "MathQuizFragment":
                return "Math Quiz";
            case "MCQFragment":
                return "Multiple Choice";
            case "CountNumbersFragment":
                return "Count Numbers";
            case "NumberLineFragment":
                return "Number Line";
            case "NarratorFragment":
                return "Narrator";
            case "RingBellFragment":
                return "Ring Bell";
            case "TapTablaFragment":
                return "Tap Tabla";
            default:
                return getString(R.string.app_name);
        }
    }

}