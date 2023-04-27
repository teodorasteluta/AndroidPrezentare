package com.example.navigationdrawer.Navigation;

public class Goal {
    private String text;
    private int timerValue;

    public Goal(String text, int timerValue) {
        this.text = text;
        this.timerValue = timerValue;
    }

    public String getText() {
        return text;
    }

    public int getTimerValue() {
        return timerValue;
    }
}
