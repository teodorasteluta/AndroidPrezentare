package com.example.navigationdrawer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.navigationdrawer.Navigation.DrawableActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    EditText editTextUserPassword, editTextUserEmail;
    // Pentru a salva numele si emailul
    Button buttonSave;

    // pentru a folosi Shared Prefrences
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "userPrefrences";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";

    // pentru autentificare cu google si firebase
    ImageView googleImageView;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // pentru a folosi shared prefrences pe username si email
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        // pentru logare folosind google si firebase
        firebaseAuth = FirebaseAuth.getInstance();
        googleImageView = findViewById(R.id.googleImageView);
        googleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        createRequest();

        // daca am desja date salvate in shared prefrences, nu are rost sa le mai pun inca o data
        String password = sharedPreferences.getString(KEY_PASSWORD, null);
        String userEmail = sharedPreferences.getString(KEY_EMAIL, null);
        if (password != null && userEmail != null) {
            Intent intent = new Intent(MainActivity.this, DrawableActivity.class);
            startActivity(intent);

            // afisez un mesaj ca sa vad ca ce am facut s-a realizat cu succes
            Toast.makeText(MainActivity.this,
                    "Login success, shared preferences are active", Toast.LENGTH_SHORT).show();
        } else {
            // daca nu am salvat, o sa afisez ecranul de login
            setContentView(R.layout.activity_main);

            // Iau referinta UserName, UserEmail, ButtonSave
            editTextUserEmail = findViewById(R.id.editTextEmail);
            editTextUserPassword = findViewById(R.id.editTextPassword);
            buttonSave = findViewById(R.id.button_save);

            // deschid o noua activitate
            buttonSave.setOnClickListener(view -> {
                // ca sa nu am campurile goale
                if (editTextUserPassword.getText().toString().equals("") || editTextUserEmail.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this,
                            "Empty fields!", Toast.LENGTH_SHORT).show();
                } else {
                    // cand apas pe buton, o sa salvez datele in shared prefrences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_PASSWORD, editTextUserPassword.getText().toString());
                    editor.putString(KEY_EMAIL, editTextUserEmail.getText().toString());
                    editor.apply();

                    // Ma duc in alta activitate
                    Intent intent = new Intent(MainActivity.this, DrawableActivity.class);
                    startActivity(intent);

                    // afisez un mesaj ca sa vad ca ce am facut s-a realizat cu succes
                    Toast.makeText(MainActivity.this, "Login success, saved preferences!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void createRequest() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

        googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
    }

    private void loginUser() {
        Intent intent = googleSignInClient.getSignInIntent();
        activityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> activityResultLauncher
            = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account=task.getResult(ApiException.class);
                auth(account.getIdToken());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    });

    private void auth(String account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(MainActivity.this, DrawableActivity.class);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                        userProfile();
                    } else {
                        Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void userProfile() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null)
        {
            user.getDisplayName();

        }
        else
        {

        }
    }
}
