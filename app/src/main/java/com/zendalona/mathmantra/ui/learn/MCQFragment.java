package com.zendalona.mathmantra.ui.learn;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.DialogResultBinding;
import com.zendalona.mathmantra.databinding.FragmentMCQBinding;
import com.zendalona.mathmantra.enums.Difficulty;
import com.zendalona.mathmantra.utils.RandomValueGenerator;
import com.zendalona.mathmantra.utils.TTSUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MCQFragment extends Fragment {

    private FragmentMCQBinding binding;
    private RandomValueGenerator random;
    private TTSUtility tts;

    public MCQFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMCQBinding.inflate(inflater, container, false);
        random = new RandomValueGenerator();
        tts = new TTSUtility(requireActivity());
        generateNewQuestion();
        return binding.getRoot();
    }

    private void generateNewQuestion() {
        int topic = random.generateQuestionTopic();
        int[] numbers;
        String operator = "+";
        switch (topic){
            case 1 :
                numbers = random.generateSubtractionValues(Difficulty.EASY);
                operator = "-";
                break;
            case 2 :
                numbers = random.generateMultiplicationValues(Difficulty.EASY);
                operator = "*";
                break;
            case 3 :
                numbers = random.generateDivisionValues(Difficulty.EASY);
                operator = "/";
                break;
            default: numbers = random.generateAdditionValues(Difficulty.EASY);
        }
        StringBuilder questionBuilder = new StringBuilder();
        questionBuilder.append(numbers[0])
                .append(operator)
                .append(numbers[1])
                .append(" = ?");
        binding.questionTv.setText(questionBuilder);
        int[] options = random.generateDivisionValues(Difficulty.EASY);
        List<Integer> choices = new ArrayList<>();
        choices.add(options[0]);
        choices.add(options[1]);
        choices.add(options[2]);
        choices.add(numbers[2]);

        Collections.shuffle(choices);
        binding.optionA.setText(String.valueOf(choices.get(0)));
        binding.optionB.setText(String.valueOf(choices.get(1)));
        binding.optionC.setText(String.valueOf(choices.get(2)));
        binding.optionD.setText(String.valueOf(choices.get(3)));

        binding.optionA
                .setOnClickListener(v -> showResultDialog(Integer.parseInt(binding.optionA.getText().toString()) == numbers[2]));
        binding.optionB
                .setOnClickListener(v -> showResultDialog(Integer.parseInt(binding.optionB.getText().toString()) == numbers[2]));
        binding.optionC
                .setOnClickListener(v -> showResultDialog(Integer.parseInt(binding.optionC.getText().toString()) == numbers[2]));
        binding.optionD
                .setOnClickListener(v -> showResultDialog(Integer.parseInt(binding.optionD.getText().toString()) == numbers[2]));
    }

    private void showResultDialog(boolean isCorrect) {
        String message = isCorrect ? "Right Answer" : "Wrong Answer";
        int gifResource = isCorrect ? R.drawable.right : R.drawable.wrong;
        tts.speak(message);

        LayoutInflater inflater = getLayoutInflater();
        DialogResultBinding dialogBinding = DialogResultBinding.inflate(inflater);
        View dialogView = dialogBinding.getRoot();

        // Load the GIF using Glide
        Glide.with(this)
                .asGif()
                .load(gifResource)
                .into(dialogBinding.gifImageView);

        dialogBinding.messageTextView.setText(message);

        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Continue", (dialog, which) -> {
                    dialog.dismiss();
                    generateNewQuestion();
                })
                .create()
                .show();
    }

}