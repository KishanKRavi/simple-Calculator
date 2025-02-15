package com.example.simplecalculator;

import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView result;
    private String input = "";
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the app title dynamically
        setTitle("Simple Calculator");

        result = findViewById(R.id.result);

        // Initialize Vibrator
        if (android.os.Build.VERSION.SDK_INT >= 31) {
            VibratorManager vibratorManager = (VibratorManager) getSystemService(VIBRATOR_MANAGER_SERVICE);
            vibrator = vibratorManager.getDefaultVibrator();
        } else {
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        }

        setButtonListeners();
    }

    private void setButtonListeners() {
        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnAdd, R.id.btnSub, R.id.btnMul, R.id.btnDiv
        };

        View.OnClickListener numberListener = v -> {
            vibrate(); // Vibrate on button press
            Button button = (Button) v;
            input += button.getText().toString();
            result.setText(input);
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(numberListener);
        }

        findViewById(R.id.btnEqual).setOnClickListener(v -> {
            vibrate();
            calculate();
        });

        findViewById(R.id.btnClear).setOnClickListener(v -> {
            vibrate();
            clear();
        });
    }

    private void calculate() {
        if (!input.isEmpty()) {
            try {
                double resultValue = new CalculatorHelper().evaluate(input);

                // Convert to integer if it's a whole number
                if (resultValue == (int) resultValue) {
                    result.setText(String.valueOf((int) resultValue));
                    input = String.valueOf((int) resultValue);
                } else {
                    result.setText(String.valueOf(resultValue));
                    input = String.valueOf(resultValue);
                }
            } catch (Exception e) {
                result.setText("Error");
                input = "";
            }
        }
    }

    private void clear() {
        input = "";
        result.setText("0");
    }

    private void vibrate() {
        if (vibrator != null) {
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(50); // Deprecated but works for older devices
            }
        }
    }
}
