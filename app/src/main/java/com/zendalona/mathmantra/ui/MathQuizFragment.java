package com.zendalona.mathmantra.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.DialogResultBinding;
import com.zendalona.mathmantra.databinding.FragmentMathQuizBinding;
import com.zendalona.mathmantra.enums.Difficulty;
import com.zendalona.mathmantra.utils.RandomValueGenerator;

public class MathQuizFragment extends Fragment {

    private FragmentMathQuizBinding binding;
    private RandomValueGenerator random;

    public MathQuizFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMathQuizBinding.inflate(inflater, container, false);
        random = new RandomValueGenerator();
        generateNewQuestion();
        return binding.getRoot();
    }

    private void generateNewQuestion() {
        int numbers[] = random.generateAdditionValues(Difficulty.EASY);
        StringBuilder questionBuilder = new StringBuilder();
        questionBuilder.append(numbers[0])
                .append("+")
                .append(numbers[1])
                .append(" = ?");
        binding.answerEt.setText("");
        binding.questionTv.setText(questionBuilder);
        binding.submitAnswerBtn.setOnClickListener(v -> {
            String answerText = binding.answerEt.getText().toString().trim();
            if (answerText.isEmpty()) {
                Toast.makeText(getContext(), "Please enter an answer", Toast.LENGTH_SHORT).show();
                return;
            }
            int answer = Integer.parseInt(answerText);
            showResultDialog(answer == numbers[2]);
        });
    }

    private void showResultDialog(boolean isCorrect) {
        String message = isCorrect ? "Right Answer" : "Wrong Answer";
        int gifResource = isCorrect ? R.drawable.right : R.drawable.wrong;

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
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    generateNewQuestion();
                })
                .create()
                .show();
    }
}