package com.example.schedulemanager.LoginAndUser;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avatarfirst.avatargenlib.AvatarConstants;
import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.example.schedulemanager.MainFragments.HomePage;
import com.example.schedulemanager.R;
import com.example.schedulemanager.Setting.Settings_Main;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.schedulemanager.Setting.Settings_Main.LogoutDeleteUserData;

public class UserProfile extends AppCompatActivity {


    ImageView profileImg;
    TextView user_email_id_txt, username_txt_view;
    Button logout_btn, delete_account_btn;
    public static FirebaseAuth mAuth;
    public static FirebaseUser user;
    public static String uid;
    String processedUserName;
    public static DatabaseReference mDatabase;
    private static List<String> AllData = new ArrayList<>();
    File imageUri;
    ImageButton back_button, edit_Name_btn, change_email_btn, change_password_btn;
    LinearLayout.LayoutParams params;
    ConnectivityManager conMan;
    public static ProgressDialog pdialog;
    public static Activity activity;
    private GoogleSignInClient mGoogleSignInClient;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Settings_Main.LoadChangedSettings(UserProfile.this);
        activity = this;
        profileImg = findViewById(R.id.profileImageView);
        username_txt_view = findViewById(R.id.profile_FullName);
        user_email_id_txt = findViewById(R.id.profile_EmailAddress);
        change_password_btn = findViewById(R.id.profile_ChangePassword_imgBtn);
        change_email_btn = findViewById(R.id.profile_ChangeEmail_imgBtn);
        logout_btn = findViewById(R.id.profile_logoutbtn);
        delete_account_btn = findViewById(R.id.profile_delete_account_btn);
        back_button = findViewById(R.id.profile_back_button);
        edit_Name_btn = findViewById(R.id.profile_ChangeName_imgBtn);
        conMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(UserProfile.this, gso);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        pdialog = new ProgressDialog(UserProfile.this);
        pdialog.setCancelable(false);
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20, 0, 20, 0);

        if (user != null) {
            user_email_id_txt.setText(user.getEmail());
            uid = user.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference();

            if (Settings_Main.User_Name != null && !Settings_Main.User_Name.isEmpty()) {
                username_txt_view.setText(Settings_Main.User_Name);
                processedUserName = Settings_Main.User_Name;
            } else if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
                username_txt_view.setText(user.getDisplayName());
                processedUserName = user.getDisplayName();
                Settings_Main.User_Name = user.getDisplayName();
                Settings_Main.SaveSettings(UserProfile.this);

            } else if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                username_txt_view.setText("Set User Name");
                processedUserName = user.getEmail();
                Settings_Main.SaveSettings(UserProfile.this);
            } else {
                username_txt_view.setText("Set User Name");
            }
            imageUri = Settings_Main.loadImageFromStorage(UserProfile.this);

            if (imageUri.exists()) {
                Picasso.get().load(imageUri).fit().centerCrop().transform(
                        new CircleTransform()).into(profileImg);
                Picasso.get().load(imageUri).fit().centerCrop().transform(
                        new CircleTransform()).into(HomePage.profileImg);
                HomePage.imageUri = imageUri;
            } else if (processedUserName != null && !processedUserName.isEmpty()) {
                profileImg.setImageDrawable(
                        AvatarGenerator.Companion.avatarImage(UserProfile.this, 100,
                                AvatarConstants.Companion.getCIRCLE(), processedUserName));
            } else {
                profileImg.setImageDrawable(
                        AvatarGenerator.Companion.avatarImage(UserProfile.this, 100,
                                AvatarConstants.Companion.getCIRCLE(), "Schedule Manager"));
            }
            profileImg.setOnClickListener(view -> selectImage(UserProfile.this));

            change_email_btn.setOnClickListener(view -> {
                if (conMan.getActiveNetworkInfo() != null
                        && conMan.getActiveNetworkInfo().isAvailable()
                        && conMan.getActiveNetworkInfo().isConnected()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this)
                            .setTitle("Update Email Id")
                            .setMessage("Are you sure you want to update email id. This will step"
                                    + " cannot be reverted and you need to login "
                                    + "again with new email id?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes,
                                    (dialogInterface, i) -> CreateDialog(UserProfile.this, 2));

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    Button nButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    nButton.setLayoutParams(params);
                    nButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    Button yButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    yButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    yButton.setLayoutParams(params);
                } else {
                    Toast.makeText(UserProfile.this, "Please check your Internet connection ",
                            Toast.LENGTH_SHORT).show();
                }
            });

            change_password_btn.setOnClickListener(view -> {
                if (conMan.getActiveNetworkInfo() != null
                        && conMan.getActiveNetworkInfo().isAvailable()
                        && conMan.getActiveNetworkInfo().isConnected()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this)
                            .setTitle("Update Password")
                            .setMessage("Are you sure you want to update password. This will step"
                                    + " cannot be reverted and you need to login "
                                    + "again with new password?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes,
                                    (dialogInterface, i) -> CreateDialog(UserProfile.this, 3));
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    Button nButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    nButton.setLayoutParams(params);
                    nButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    Button yButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    yButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    yButton.setLayoutParams(params);
                } else {
                    Toast.makeText(UserProfile.this, "Please check your Internet connection ",
                            Toast.LENGTH_SHORT).show();
                }
            });

            logout_btn.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this)
                        .setTitle("Log Out")
                        .setMessage("Are you sure you want to Log Out. This will delete all data "
                                + "you didn't backed up in server?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes,
                                (dialogInterface, i) -> {
                                    Settings_Main.deleteImageFromStorage(UserProfile.this);
                                    LogoutDeleteUserData(UserProfile.this);
                                    mGoogleSignInClient.signOut();
                                    mAuth.signOut();
                                    user = mAuth.getCurrentUser();
                                    if (user == null) {
                                        startActivity(new Intent(UserProfile.this,
                                                MainActivity.class));
                                        finish();
                                        Toast.makeText(UserProfile.this,
                                                "Signed Out successfully ...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.show();
                Button nButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                nButton.setLayoutParams(params);
                nButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                Button yButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                yButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                yButton.setLayoutParams(params);
            });

            delete_account_btn.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this)
                        .setTitle("Delete Account")
                        .setMessage("Are you sure you want to delete account. This will remove "
                                + "all your data from server?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes,
                                (dialogInterface, i) -> {
                                    mDatabase.child(uid).removeValue();
                                    mGoogleSignInClient.revokeAccess();
                                    user = mAuth.getCurrentUser();
                                    user.delete();
                                    LogoutDeleteUserData(UserProfile.this);
                                    Settings_Main.deleteImageFromStorage(UserProfile.this);
                                    if (user == null) {
                                        Toast.makeText(UserProfile.this,
                                                "Account Deleted Successfully",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(UserProfile.this,
                                                MainActivity.class).setFlags(
                                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                        | Intent.FLAG_ACTIVITY_NEW_TASK));
                                        finish();
                                    } else {
                                        user.delete();
                                        Toast.makeText(UserProfile.this,
                                                "Some error occurred while deleting "
                                                        + "account",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(UserProfile.this,
                                                MainActivity.class).setFlags(
                                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                        | Intent.FLAG_ACTIVITY_NEW_TASK));
                                        finish();
                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.show();
                Button nButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                nButton.setLayoutParams(params);
                nButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                Button yButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                yButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                yButton.setLayoutParams(params);
            });

            edit_Name_btn.setOnClickListener(view -> CreateDialog(UserProfile.this, 1));

        } else {
            username_txt_view.setText("User not Signed In");
            user_email_id_txt.setText("Please sign in to use all features");
            delete_account_btn.setEnabled(false);
            logout_btn.setText("Login");

            logout_btn.setOnClickListener(view -> {
                startActivity(new Intent(UserProfile.this, MainActivity.class).setFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            });
        }
        back_button.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });
    }

    private void selectImage(Context context) {
        final CharSequence[] options =
                {"Take Photo", "Choose from Gallery", "Remove image", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                try {
                    startActivityForResult(pickPhoto, 1);
                } catch (Error ignored) {
                    ignored.printStackTrace();
                    Toast.makeText(context, "No app found for selecting images ",
                            Toast.LENGTH_LONG).show();
                }
            } else if (options[item].equals("Remove image")) {
                if (Settings_Main.deleteImageFromStorage(UserProfile.this)) {
                    if (processedUserName != null && !processedUserName.isEmpty()) {
                        profileImg.setImageDrawable(
                                AvatarGenerator.Companion.avatarImage(UserProfile.this, 120,
                                        AvatarConstants.Companion.getCIRCLE(),
                                        processedUserName));
                    } else {
                        profileImg.setImageDrawable(
                                AvatarGenerator.Companion.avatarImage(UserProfile.this, 120,
                                        AvatarConstants.Companion.getCIRCLE(),
                                        "Schedule Manager"));
                    }
                } else {
                    Toast.makeText(UserProfile.this, "Image doesn't exist on device",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        profileImg.setImageBitmap(
                                getCroppedBitmap(selectedImage, UserProfile.this, user));
                        new Settings_Main.saveToInternalStorage(selectedImage,
                                UserProfile.this).execute();
                        UserProfileChangeRequest profileUpdates =
                                new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(data.getData())
                                        .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d("UserPhoto_Update", "User profile updated.");
                                    }
                                });
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        try {
                            final Uri imageUri = data.getData();
                            final InputStream imageStream = getContentResolver().openInputStream(
                                    imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            if (selectedImage.getAllocationByteCount() / 1024 < 9000) {
                                Picasso.get().load(imageUri).fit().centerCrop().transform(
                                        new CircleTransform()).into(profileImg);
                                if (selectedImage != null) {
                                    new Settings_Main.saveToInternalStorage(selectedImage,
                                            UserProfile.this).execute();
                                    UserProfileChangeRequest profileUpdates =
                                            new UserProfileChangeRequest.Builder()
                                                    .setPhotoUri(data.getData())
                                                    .build();
                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Log.d("UserPhoto_Update",
                                                            "User profile updated.");
                                                }
                                            });
                                } else {
                                    Toast.makeText(UserProfile.this, "Please select image again",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(UserProfile.this,
                                        "Please select image with smaller size",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(UserProfile.this, "Something went wrong",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                    break;
            }
        }
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap, Context context, FirebaseUser user) {
        if (bitmap != null) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                    bitmap.getWidth() / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        } else {
            Toast.makeText(context, "Please Select image again", Toast.LENGTH_SHORT).show();
            return AvatarGenerator.Companion.avatarImage(context, 125,
                    AvatarConstants.Companion.getCIRCLE(), user.getEmail()).getBitmap();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserProfile.this, HomePage.class).putExtra("num", 0).putExtra(
                "nav", R.id.nav_home));
        finish();
    }

    public static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    public void CreateDialog(Context context, int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.profile_user_setting_dialog, null);
        builder.setView(view);
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button save = view.findViewById(R.id.Profile_Dialog_btn_save);
        Button cancel = view.findViewById(R.id.Profile_Dialog_btn_cancel);
        TextView head = view.findViewById(R.id.Profile_Dialog_head_txt_view);
        EditText data = view.findViewById(R.id.Profile_Dialog_Edit_txt);
        if (type == 1) {
            head.setText("Change Name");
        } else if (type == 2) {
            head.setText("Change Email-id");
        } else if (type == 3) head.setText("Change Password");
        save.setOnClickListener(v -> {
            if (type == 1) {
                if (!data.getText().toString().isEmpty()) {
                    Settings_Main.User_Name = data.getText().toString();
                    Settings_Main.SaveSettings(UserProfile.this);
                    username_txt_view.setText(data.getText().toString());
                    HomePage.username_txt_view.setText(data.getText().toString());
                    HomePage.processedUserName = data.getText().toString();
                    Toast.makeText(UserProfile.this, "User name changed successfully",
                            Toast.LENGTH_SHORT).show();
                    UserProfileChangeRequest profileUpdates =
                            new UserProfileChangeRequest.Builder()
                                    .setDisplayName(data.getText().toString())
                                    .build();
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d("UserNAME_Update", "User profile updated.");
                                }
                            });
                } else {
                    Toast.makeText(UserProfile.this, "Please enter non empty name",
                            Toast.LENGTH_SHORT).show();
                }
                imm.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), 0);
                dialog.dismiss();
            } else if (type == 2) {
                if (!data.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(
                        data.getText().toString().trim()).matches()) {
                    if (!data.getText().toString().equals(user.getEmail())) {
                        verifyAndUpdateEmail(data.getText().toString(), UserProfile.this);
                    } else {
                        Toast.makeText(UserProfile.this,
                                "Please enter new email id. You are already logged in with "
                                        + "this email-id",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(UserProfile.this, "Please enter valid email id",
                            Toast.LENGTH_SHORT).show();
                }

                imm.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), 0);
                dialog.dismiss();

            } else if (type == 3) {
                if (!data.getText().toString().isEmpty()
                        && data.getText().toString().trim().length() >= 6) {
                    verifyAndUpdatePassword(data.getText().toString(), UserProfile.this);
                } else {
                    Toast.makeText(UserProfile.this,
                            "Please enter password of minimum 6 characters",
                            Toast.LENGTH_SHORT).show();
                }
                imm.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), 0);
                dialog.dismiss();

            }
        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
            imm.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), 0);
        });
        dialog.show();
    }

    public static Boolean verifyAndUpdateEmail(String email, final Context context) {
        user.updateEmail(email).addOnFailureListener(e -> {
            Log.e("ERROREMAIL", e.getMessage());
            e.printStackTrace();
            Toast.makeText(context, "Error in updating Email id. Please Log in again and try. ",
                    Toast.LENGTH_LONG).show();
            mAuth.signOut();
            user = mAuth.getCurrentUser();
            if (user == null) {
                context.startActivity(new Intent(context, MainActivity.class).setFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                activity.finish();
            }
        }).addOnSuccessListener(aVoid -> {
            pdialog.setTitle("Email id Updated Successfully");
            pdialog.setMessage("Working on user data. Please wait!");
            pdialog.show();
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            mAuth.signOut();
            user = mAuth.getCurrentUser();
            Toast.makeText(context,
                    "Email id Updated Successfully. You need to login with new email id.",
                    Toast.LENGTH_LONG).show();
            if (pdialog.isShowing()) pdialog.dismiss();
            if (user == null) {
                context.startActivity(new Intent(context, MainActivity.class).setFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                activity.finish();
            }
        });
        return true;
    }

    public static void verifyAndUpdatePassword(String password, final Context context) {
        user.updatePassword(password).addOnFailureListener(e -> {
            e.printStackTrace();
            Log.e("ERRORPASS", e.getMessage());
            Toast.makeText(context, "Error in updating Password. Please try again later. ",
                    Toast.LENGTH_LONG).show();
        }).addOnSuccessListener(aVoid -> {
            Toast.makeText(context,
                    "Password Updated Successfully. Please login again with new password",
                    Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(context, SplashScreen.class).setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            activity.finish();
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}