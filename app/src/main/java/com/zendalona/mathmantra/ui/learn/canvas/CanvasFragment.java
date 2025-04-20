package com.zendalona.mathmantra.ui.learn.canvas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.FragmentCanvasBinding;

public class CanvasFragment extends Fragment {

    private FragmentCanvasBinding binding;
    private FrameLayout.LayoutParams initial_params;

    public CanvasFragment() {
        // Empty Constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCanvasBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.bottomAppBar).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.GONE);

//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) requireActivity().findViewById(R.id.fragment_container).getLayoutParams();
//        initial_params = params;
//        params.setMargins(0,0,0,0);
//        binding.constrainLayout.setLayoutParams(params);
        binding.clearBtn.setOnClickListener(v -> {
            binding.surfaceView.clearSurface();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.constrainLayout.setLayoutParams(initial_params);
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.bottomAppBar).setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.VISIBLE);
        binding = null;
    }
}
