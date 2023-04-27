package com.example.navigationdrawer.Navigation;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationdrawer.MainActivity;
import com.example.navigationdrawer.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeFragment extends Fragment {

    private List<String> mList;
    private EditText mEditText;
    private Button mButton;
    private RecyclerView mRecyclerView;
    private ListAdapter mAdapter;

    SharedPreferences sharedPreferences;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mList = getListFromSharedPreferences();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mEditText = view.findViewById(R.id.edit_text);
        mButton = view.findViewById(R.id.add_button);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        mAdapter = new ListAdapter(mList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        mButton.setOnClickListener(v -> {
            String text = mEditText.getText().toString().trim();
            if (!text.isEmpty()) {
                mList.add(text);
                addItemToList(text);
                mAdapter.notifyItemInserted(mList.size() - 1);
                mEditText.setText("");
            }
        });

        return view;
    }

    public void addItemToList(String item) {
        // Get the current list from SharedPreferences
        List<String> list = getListFromSharedPreferences();

        // Add the new item to the list
        list.add(item);

        // Save the updated list to SharedPreferences
        saveListToSharedPreferences(list);
    }

    private List<String> getListFromSharedPreferences() {
        // Get the list from SharedPreferences
        Set<String> set = sharedPreferences.getStringSet("list", new HashSet<String>());

        // Convert the Set to a List
        return new ArrayList<>(set);
    }

    private void saveListToSharedPreferences(List<String> list) {
        // Convert the List to a Set
        Set<String> set = new HashSet<>(list);

        // Save the Set to SharedPreferences
        sharedPreferences.edit().putStringSet("list", set).apply();
    }

    private class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

        private List<String> mList;

        public ListAdapter(List<String> list) {
            mList = list;
        }

        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            String text = mList.get(position);
            holder.textView.setText(text);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    private class ListViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        Button sendNotificationButton;
        Button sendNotificationIn1HourButton;
        Button sendNotificationIn3HoursButton;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view_list);
            sendNotificationButton = itemView.findViewById(R.id.send_notification_button);
            sendNotificationIn1HourButton = itemView.findViewById(R.id.send_notification_in_1_hour_button);
            sendNotificationIn3HoursButton = itemView.findViewById(R.id.send_notification_in_3_hours_button);

            sendNotificationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = textView.getText().toString();
                    // Send a notification with the text of the item
                    NotificationHelper.showNotification(requireContext(), "List Item", text);
                }
            });

            sendNotificationIn1HourButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = textView.getText().toString();
                    // Send a notification with the text of the item after 1 hour
                    NotificationHelper.showNotificationDelayed(requireContext(), "List Item", text, 1 * 60 * 60 * 1000L);
                }
            });

            sendNotificationIn3HoursButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = textView.getText().toString();
                    // Send a notification with the text of the item after 3 hours
                    NotificationHelper.showNotificationDelayed(requireContext(), "List Item", text, 3 * 60 * 60 * 1000L);
                }
            });
        }
    }

    // clasa care imi afiseaza notificari, ori acum, ori in una sau 3 ore
    public static class NotificationHelper {
        private static final int NOTIFICATION_ID = 0;
        private static final String NOTIFICATION_CHANNEL_ID = "channel_id";
        private static final String NOTIFICATION_CHANNEL_NAME = "channel_name";

        public static void showNotification(Context context, String title, String message) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Create a notification channel for Android Oreo and higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            // Create an explicit intent to launch the activity when the notification is tapped
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("fragment", "home");
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Create a notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.google_signin_button)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            // Show the notification
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }

        public static void showNotificationDelayed(Context context, String title, String message, long delay) {
            // Create an intent that will be triggered when the notification is shown
            Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
            intent.putExtra(NotificationBroadcastReceiver.TITLE_EXTRA, title);
            intent.putExtra(NotificationBroadcastReceiver.MESSAGE_EXTRA, message);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Schedule the notification to be shown after the specified delay
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
            }
        }
    }
}
