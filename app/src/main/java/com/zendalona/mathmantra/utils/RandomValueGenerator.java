package com.zendalona.mathmantra.utils;

import com.zendalona.mathmantra.enums.Difficulty;

import java.util.Random;

public class RandomValueGenerator {
    private Random random;

    public RandomValueGenerator() {
        this.random = new Random();
    }

    public int[] generateAdditionValues(Difficulty difficulty) {
        int[] values = new int[3];
        switch (difficulty) {
            case EASY:
                values[0] = random.nextInt(10) + 1;
                values[1] = random.nextInt(10) + 1;
                break;
            case MEDIUM:
                values[0] = random.nextInt(50) + 10;
                values[1] = random.nextInt(50) + 10;
                break;
            case HARD:
                values[0] = random.nextInt(500) + 17;
                values[1] = random.nextInt(500) + 17;
                break;
        }
        values[2] = values[0] + values[1];
        return values;
    }

    public int[] generateSubtractionValues(Difficulty difficulty) {
        int[] values = new int[2];
        switch (difficulty) {
            case EASY:
                values[0] = random.nextInt(10) + 1;
                values[1] = random.nextInt(10) + 1;
                if (values[1] > values[0]) {
                    int temp = values[0];
                    values[0] = values[1];
                    values[1] = temp;
                }
                break;
            case MEDIUM:
                values[0] = random.nextInt(50) + 10;
                values[1] = random.nextInt(50) + 10;
                if (values[1] > values[0]) {
                    int temp = values[0];
                    values[0] = values[1];
                    values[1] = temp;
                }
                break;
            case HARD:
                values[0] = random.nextInt(500) + 17;
                values[1] = random.nextInt(500) + 17;
                if (values[1] > values[0]) {
                    int temp = values[0];
                    values[0] = values[1];
                    values[1] = temp;
                }
                break;
        }
        return values;
    }

    public int[] generateMultiplicationValues(Difficulty difficulty) {
        int[] values = new int[2];
        switch (difficulty) {
            case EASY:
                values[0] = random.nextInt(10) + 1;
                values[1] = random.nextInt(10) + 1;
                break;
            case MEDIUM:
                values[0] = random.nextInt(20) + 1;
                values[1] = random.nextInt(20) + 1;
                break;
            case HARD:
                values[0] = random.nextInt(50) + 1;
                values[1] = random.nextInt(50) + 1;
                break;
        }
        return values;
    }

    public int[] generateDivisionValues(Difficulty difficulty) {
        int[] values = new int[3];
        switch (difficulty) {
            case EASY:
                values[1] = random.nextInt(9) + 1; // Avoid division by zero
                values[0] = values[1] * (random.nextInt(10) + 1);
                break;
            case MEDIUM:
                values[1] = random.nextInt(19) + 1; // Avoid division by zero
                values[0] = values[1] * (random.nextInt(20) + 1);
                break;
            case HARD:
                values[1] = random.nextInt(49) + 1; // Avoid division by zero
                values[0] = values[1] * (random.nextInt(50) + 1); //TODO : Scope for decimal values?
                break;
        }
        values[2] = values[0] / values[1];
        return values;
    }
}