package com.example.schedulemanager.LoginandUser;import android.content.Context;import android.content.Intent;import android.net.ConnectivityManager;import android.os.Bundle;import android.util.Patterns;import android.view.View;import android.widget.Button;import android.widget.ProgressBar;import android.widget.TextView;import android.widget.Toast;import androidx.annotation.NonNull;import androidx.appcompat.app.AppCompatActivity;import com.example.schedulemanager.MainActivity;import com.example.schedulemanager.R;import com.google.android.gms.tasks.OnCompleteListener;import com.google.android.gms.tasks.Task;import com.google.android.material.textfield.TextInputEditText;import com.google.android.material.textfield.TextInputLayout;import com.google.firebase.auth.AuthResult;import com.google.firebase.auth.FirebaseAuth;public class Register extends AppCompatActivity {    public static final String TAG = "TAG";    TextInputEditText mEmail, mPassword;    TextInputLayout passwordLayout,emailLayout;    Button mRegisterBtn;    TextView mLoginBtn;    FirebaseAuth mAuth;    ProgressBar progressBar;//    FirebaseFirestore fStore;//    String userID;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_register);//        mFullName = findViewById(R.id.fullNameEdit);        mEmail = findViewById(R.id.EmailEdit);        mPassword = findViewById(R.id.passwordEdit);        passwordLayout = findViewById(R.id.password);        emailLayout = findViewById(R.id.Email);//        mPhone = findViewById(R.id.phoneEdit);        mRegisterBtn = findViewById(R.id.registerBtn);        mLoginBtn = findViewById(R.id.createText);        mAuth = FirebaseAuth.getInstance();//        fStore = FirebaseFirestore.getInstance();        progressBar = findViewById(R.id.progressBar);        if (mAuth.getCurrentUser() != null) {            startActivity(new Intent(getApplicationContext(), MainActivity.class));            finish();        }        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);        mRegisterBtn.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {                    final String email = mEmail.getText().toString().trim();                    String password = mPassword.getText().toString().trim();//                final String fullName = mFullName.getText().toString();//                final String phone = mPhone.getText().toString();                    if (ValidateInput(email, password)) {                        progressBar.setVisibility(View.VISIBLE);                        // register the user in firebase                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {                            @Override                            public void onComplete(@NonNull Task<AuthResult> task) {                                if (task.isSuccessful()) {                                    // send verification link//                                    FirebaseUser fuser = mAuth.getCurrentUser();//                                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {//                                        @Override//                                        public void onSuccess(Void aVoid) {//                                            Toast.makeText(Register.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();//                                        }//                                    }).addOnFailureListener(new OnFailureListener() {//                                        @Override//                                        public void onFailure(@NonNull Exception e) {//                                            Log.d(TAG, "onFailure: Email not sent " + e.getMessage());//                                        }//                                    });                                    Toast.makeText(Register.this, "New user created successfully", Toast.LENGTH_SHORT).show();//                                userID = mAuth.getCurrentUser().getUid();//                                DocumentReference documentReference = fStore.collection("users").document(userID);//                                Map<String, Object> user = new HashMap<>();//                                user.put("fName", fullName);//                                user.put("com/example/firebase/email", email);//                                user.put("phone", phone);//                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {//                                    @Override//                                    public void onSuccess(Void aVoid) {//                                        Log.d(TAG, "onSuccess: user Profile is created for " + userID);//                                    }//                                }).addOnFailureListener(new OnFailureListener() {//                                    @Override//                                    public void onFailure(@NonNull Exception e) {//                                        Log.d(TAG, "onFailure: " + e.toString());//                                    }//                                });                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));                                } else {                                    Toast.makeText(Register.this, "Error Occurred " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();                                    progressBar.setVisibility(View.GONE);                                }                            }                        });                    }                } else {                    Toast.makeText(Register.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();                }            }        });        mLoginBtn.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                startActivity(new Intent(getApplicationContext(), MainActivity.class));            }        });    }    private boolean ValidateInput(String email, String password) {        emailLayout.setError("");        passwordLayout.setError("");        if (email.trim().length() == 0) {            emailLayout.setError("Please Enter Email Address");//            Toast.makeText(Register.this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();            return false;        }        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {            emailLayout.setError("Please Enter a Valid Email Address");//            Toast.makeText(Register.this, "Please Enter a Valid Email Address", Toast.LENGTH_SHORT).show();            return false;        }        if (password.trim().length() == 0) {            passwordLayout.setError("Please Enter Password");//            Toast.makeText(Register.this, "Please Enter Password", Toast.LENGTH_SHORT).show();            return false;        }        if (password.trim().length() < 6) {            passwordLayout.setError("Please Enter a Password of atleast 6 characters");//            Toast.makeText(Register.this, "Please Enter a Password of atleast 6 characters", Toast.LENGTH_SHORT).show();            return false;        }        else {            return true;        }    }//    private void requestFocus(View view){//        if(view.requestFocus()){//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);//        }////    }}