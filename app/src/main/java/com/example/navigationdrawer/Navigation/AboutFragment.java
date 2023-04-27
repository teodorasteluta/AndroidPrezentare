package com.example.navigationdrawer.Navigation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.navigationdrawer.R;

public class AboutFragment extends Fragment {
    TextView editTextUserPassword, textView_email;
    Button buttonLogOut;

    // pentru a selecta Shared Prefrences ce l-am primit din MainActivity
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "userPrefrences";
    private static final String KEY_EMAIL = "email";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        // iau referinta catre textview si buton
        textView_email = view.findViewById(R.id.text_email);
        buttonLogOut = view.findViewById(R.id.buton_logout);

        // selectez sa vad ce shared prefrences am
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, getActivity().MODE_PRIVATE);

        // obtin email-ul pe care il am salvat pe acest cont
        String email = sharedPreferences.getString(KEY_EMAIL, null);

        if (email != null) {
            textView_email.setText(textView_email.getText() + " " + email);
        }

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Toast.makeText(getActivity(), "Log out succesfully", Toast.LENGTH_SHORT).show();
                // ca sa ma reintorc la activitatea principala, MainActivity
                getActivity().finish();
            }
        });

        return view;
    }
}
