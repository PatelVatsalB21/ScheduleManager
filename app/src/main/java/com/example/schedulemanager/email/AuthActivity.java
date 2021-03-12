//package com.example.schedulemanager.email;
//
//import android.accounts.AccountManager;
//import android.app.Activity;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//
//import com.example.schedulemanager.R;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;
//
//import static com.example.schedulemanager.MainActivity.RC_SIGN_IN;
//
//
//public class AuthActivity extends Activity {
//
//    private static final int AUTHORIZATION_CODE = 1993;
//    private static final int ACCOUNT_CODE = 1601;
//    private AuthPreferences authPreferences;
//    private AccountManager accountManager;
//    GoogleSignInAccount account;
//    public static final String MAIL_GOOGLE_COM = "https://mail.google.com";
//    public static final String GMAIL_COMPOSE = "https://www.googleapis.com/auth/gmail.compose";
//    public static final String GMAIL_MODIFY = "https://www.googleapis.com/auth/gmail.modify";
//
//    private final String SCOPE = GMAIL_COMPOSE + " " + GMAIL_MODIFY + " " + MAIL_GOOGLE_COM;
//    private GoogleSignInClient mGoogleSignInClient;
//    private String TAG= "AUTHActivity";
//    private FirebaseAuth mAuth;
//    private FirebaseUser user;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.auth_activity);
//        Toast.makeText(this, "Called", Toast.LENGTH_SHORT).show();
//        mAuth = FirebaseAuth.getInstance();
//        user = mAuth.getCurrentUser();
//
//        accountManager = AccountManager.get(this);
//
//        account = GoogleSignIn.getLastSignedInAccount(this);
//        authPreferences = new AuthPreferences(this);
//
//        if (authPreferences.getToken() != null) {
//            sendMail();
//        } else {
//            chooseAccount();
//        }
//
//    }
//
//    private void sendMail() {
//        Toast.makeText(this, "Sending Mail", Toast.LENGTH_SHORT).show();
//        new senmailAsync().execute();
//        onBackPressed();
//    }
//
//
//    private void chooseAccount() {
//        Toast.makeText(this, "Choose Account", Toast.LENGTH_SHORT).show();
//        mGoogleSignInClient.signOut();
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//         account = GoogleSignIn.getLastSignedInAccount(this);
//        signIn();
//
//    }
//
//    private void signIn() {
//        Toast.makeText(this, "Sign In", Toast.LENGTH_SHORT).show();
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    private void requestToken() {
//        if (account!=null){
//            authPreferences.setToken(account.getIdToken());
//            sendMail();
//        }else Toast.makeText(this, "NULL ACCOUNT", Toast.LENGTH_SHORT).show();
//
//    }
//
//    /**
//     * call this method if your token expired, or you want to request a new
//     * token for whatever reason. call requestToken() again afterwards in order
//     * to get a new token.
//     */
//    private void invalidateToken() {
//        AccountManager accountManager = AccountManager.get(this);
//        accountManager.invalidateAuthToken("com.google", authPreferences.getToken());
//        Log.e("ranjapp", "invalidating token............");
//
//        authPreferences.setToken(null);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Toast.makeText(this, "On Activity Result", Toast.LENGTH_SHORT).show();
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                authPreferences.setUser(account.getEmail());
//                Log.e(TAG, "firebaseAuthWithGoogle:" + account.getId());
//                firebaseAuthWithGoogle(account.getIdToken());
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.e(TAG, "Google sign in failed", e);
//                // ...
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(String idToken) {
//        Toast.makeText(this, "Firebase With Google", Toast.LENGTH_SHORT).show();
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.e(TAG, "signInWithCredential:success");
//                            user = mAuth.getCurrentUser();
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.e(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(AuthActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//
//    private class senmailAsync extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//            Log.e("SENDMAIL","CALLED FROM AUTHACTIVITY");
//            SendmailTest gMailSender = new SendmailTest();
//            gMailSender.sendMail("hi", "hi", authPreferences.getUser(), authPreferences.getToken(), "patelvatsalb21@gmail.com");
//            Log.v("ranjapp", "sent mail " + authPreferences.getUser() + "  " + authPreferences.getToken());
//            return null;
//        }
//    }
//}