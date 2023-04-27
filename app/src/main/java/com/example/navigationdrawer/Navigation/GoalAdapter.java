package com.example.navigationdrawer.Navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationdrawer.MainActivity;
import com.example.navigationdrawer.R;

import java.util.ArrayList;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {

    private ArrayList<Goal> mGoals;
    private Context mContext;

    public GoalAdapter(Context context) {
        mContext = context;
        mGoals = new ArrayList<>();
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goal, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Goal goal = mGoals.get(position);
        holder.goalTextView.setText(goal.getText());
        holder.timerTextView.setText(formatTime(goal.getTimerValue()));
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGoal(position);
            }
        });
        startTimer(goal.getTimerValue(), holder.timerTextView);
    }

    @Override
    public int getItemCount() {
        return mGoals.size();
    }

    public void addGoal(Goal goal) {
        mGoals.add(goal);
        notifyItemInserted(mGoals.size() - 1);
    }

    public void deleteGoal(int position) {
        mGoals.remove(position);
        notifyItemRemoved(position);
    }

    private String formatTime(int timerValue) {
        int hours = timerValue / 3600;
        int minutes = (timerValue % 3600) / 60;
        int seconds = timerValue % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void startTimer(int timerValue, TextView timerTextView) {
        new CountDownTimer(timerValue * 60 * 60 * 1000, 1000) { // convert seconds to milliseconds
            @Override
            public void onTick(long millisUntilFinished) {
                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                timerTextView.setText(time);
            }

            @Override
            public void onFinish() {
                // Create an intent to launch the app when the notification is clicked
                Intent intent = new Intent(mContext, HomeFragment.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

                // Create the notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "goal_timer")
                        .setContentTitle("Goal Timer")
                        .setContentText("Time's up!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                // Get the notification manager and show the notification
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                notificationManager.notify(0, builder.build());
            }
        }.start();
    }

    static class GoalViewHolder extends RecyclerView.ViewHolder {

        TextView goalTextView;
        TextView timerTextView;
        ImageButton deleteButton;

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            goalTextView = itemView.findViewById(R.id.goalTextView);
            timerTextView = itemView.findViewById(R.id.timerTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
