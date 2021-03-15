package com.example.schedulemanager;import static com.google.firebase.auth.FirebaseAuth.AuthStateListener;import android.content.Context;import android.content.Intent;import android.content.SharedPreferences;import android.net.ConnectivityManager;import android.os.Bundle;import android.util.Log;import android.view.View;import android.widget.Button;import android.widget.ProgressBar;import android.widget.TextView;import android.widget.Toast;import androidx.annotation.NonNull;import androidx.appcompat.app.AppCompatActivity;import com.example.schedulemanager.Intro.Introduction;import com.example.schedulemanager.LoginandUser.Register;import com.example.schedulemanager.MainFragments.HomePage;import com.example.schedulemanager.email.AuthPreferences;import com.firebase.ui.auth.AuthUI;import com.google.android.gms.auth.api.signin.GoogleSignIn;import com.google.android.gms.auth.api.signin.GoogleSignInAccount;import com.google.android.gms.auth.api.signin.GoogleSignInClient;import com.google.android.gms.auth.api.signin.GoogleSignInOptions;import com.google.android.gms.common.SignInButton;import com.google.android.gms.common.api.ApiException;import com.google.android.gms.tasks.OnCompleteListener;import com.google.android.gms.tasks.Task;import com.google.android.material.snackbar.Snackbar;import com.google.firebase.auth.AuthCredential;import com.google.firebase.auth.AuthResult;import com.google.firebase.auth.FirebaseAuth;import com.google.firebase.auth.FirebaseUser;import com.google.firebase.auth.GoogleAuthProvider;import java.util.Collections;public class MainActivity extends AppCompatActivity {    public static final String TAG = "MainActivity";    private static final int APP_PERMISSION_REQUEST = 102;    GoogleSignInClient mGoogleSignInClient;    Button login, new_account_btn;    FirebaseAuth mAuth;    FirebaseUser user;    AuthStateListener authStateListener;    SignInButton google_SignInBtn;    TextView skipSignIn_Btn;    ProgressBar progressBar;    public static final int RC_SIGN_IN = 1;    public static SharedPreferences FirstUseSharedPreferences;    final private static String FIRST_USE_DB = "First_use_DB";    public static Boolean isFirstUse;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_main);        login = findViewById(R.id.login);        progressBar = findViewById(R.id.progressBar);        new_account_btn = findViewById(R.id.new_account_btn);        skipSignIn_Btn = findViewById(R.id.skipSignIn);        FirstUseSharedPreferences = getSharedPreferences("First.db", MODE_PRIVATE);        isFirstUse = FirstUseSharedPreferences.getBoolean(FIRST_USE_DB, true);        google_SignInBtn = findViewById(R.id.Google_Sign_In_btn_Main);        mAuth = FirebaseAuth.getInstance();        user = mAuth.getCurrentUser();        authStateListener = firebaseAuth -> {            user = mAuth.getCurrentUser();            if (user != null) {                if (user.getDisplayName() != null) {                    Toast.makeText(MainActivity.this, "Welcome " + user.getDisplayName(),                            Toast.LENGTH_SHORT).show();                }                startActivity(new Intent(MainActivity.this, HomePage.class));                CheckFirstUse();            }        };        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(                GoogleSignInOptions.DEFAULT_SIGN_IN)                .requestIdToken(getString(R.string.default_web_client_id))                .requestEmail()                .build();        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(                Context.CONNECTIVITY_SERVICE);        if (account != null) {            mAuth = FirebaseAuth.getInstance();            user = mAuth.getCurrentUser();        }        google_SignInBtn.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                signIn();            }        });        login.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                if (conMgr.getActiveNetworkInfo() != null                        && conMgr.getActiveNetworkInfo().isAvailable()                        && conMgr.getActiveNetworkInfo().isConnected()) {                    AuthUI.IdpConfig provider = new AuthUI.IdpConfig.EmailBuilder().build();                    startActivityForResult(                            AuthUI.getInstance()                                    .createSignInIntentBuilder()                                    .setAvailableProviders(Collections.singletonList(provider))                                    .setTheme(R.style.FireBase)                                    .build(),                            RC_SIGN_IN);                } else {                    makeSnackBar("Please Check Your Internet Connection");                }            }        });        new_account_btn.setOnClickListener(v -> {            if (conMgr.getActiveNetworkInfo() != null                    && conMgr.getActiveNetworkInfo().isAvailable()                    && conMgr.getActiveNetworkInfo().isConnected()) {                startActivity(new Intent(getApplicationContext(), Register.class));            } else {                makeSnackBar("Please Check Your Internet Connection");            }        });        skipSignIn_Btn.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();                CheckFirstUse();            }        });    }    private void signIn() {        Intent signInIntent = mGoogleSignInClient.getSignInIntent();        startActivityForResult(signInIntent, RC_SIGN_IN);    }    @Override    public void onActivityResult(int requestCode, int resultCode, Intent data) {        super.onActivityResult(requestCode, resultCode, data);        if (requestCode == RC_SIGN_IN) {            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);            try {                GoogleSignInAccount account = task.getResult(ApiException.class);                AuthPreferences authPreferences = new AuthPreferences(MainActivity.this);                authPreferences.setUser(account.getEmail());                authPreferences.setToken(account.getIdToken());                firebaseAuthWithGoogle(account.getIdToken());            } catch (ApiException e) {                Log.e(TAG, "Google sign in failed", e);            }        }    }    private void firebaseAuthWithGoogle(String idToken) {        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);        mAuth.signInWithCredential(credential)                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {                    @Override                    public void onComplete(@NonNull Task<AuthResult> task) {                        if (task.isSuccessful()) {                            user = mAuth.getCurrentUser();                        } else {                            makeSnackBar("Some Error Occurred");                        }                    }                });    }    @Override    public void onStart() {        super.onStart();//        mGoogleApiClient.connect();        mAuth.addAuthStateListener(authStateListener);        FirebaseUser currentUser = mAuth.getCurrentUser();    }    @Override    public void onStop() {        super.onStop();        if (authStateListener != null) {            mAuth.removeAuthStateListener(authStateListener);        }    }    public void CheckFirstUse() {        if (isFirstUse) {            startActivity(new Intent(MainActivity.this, Introduction.class).setFlags(                    Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));            FirstUseSharedPreferences.edit().putBoolean(FIRST_USE_DB, false).apply();        } else {            startActivity(new Intent(MainActivity.this, HomePage.class).setFlags(                    Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));        }        finish();    }    public void makeSnackBar(String message) {        Snackbar snackbar = Snackbar.make(progressBar, message, Snackbar.LENGTH_SHORT);        snackbar.show();    }}