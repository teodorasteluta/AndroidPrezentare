package com.example.navigationdrawer.Navigation;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import com.example.navigationdrawer.R;


public class GoalInputDialog extends Dialog {

    public interface OnGoalEnteredListener {
        void onGoalEntered(String goal, int timerValue);
    }

    private OnGoalEnteredListener listener;

    public GoalInputDialog(@NonNull Context context, OnGoalEnteredListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the layout for the dialog
        setContentView(R.layout.dialog_goal_input);

        // Get references to views in the layout
        EditText goalEditText = findViewById(R.id.goalEditText);
        NumberPicker timerPicker = findViewById(R.id.timerPicker);
        Button addButton = findViewById(R.id.addButton);

        // Set up the timer picker with values 1, 3, and 5
        timerPicker.setMinValue(1);
        timerPicker.setMaxValue(3);
        timerPicker.setDisplayedValues(new String[]{"1 hour", "3 hours", "5 hours"});

        // Set up click listener for the "add" button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user input for the new goal and timer value
                String goal = goalEditText.getText().toString();
                int timerValue = timerPicker.getValue() * 2;

                // Notify the listener that a new goal was entered
                listener.onGoalEntered(goal, timerValue);

                // Dismiss the dialog
                dismiss();
            }
        });
    }
}

