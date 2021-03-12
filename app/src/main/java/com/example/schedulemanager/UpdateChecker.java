package com.example.schedulemanager;//package com.example.firebase;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentSender;
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//
//import com.example.firebase.LoginandUser.UserProfile;
//import com.google.android.play.core.appupdate.AppUpdateManager;
//import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
//import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;
//import com.google.android.play.core.install.InstallState;
//import com.google.android.play.core.install.InstallStateUpdatedListener;
//import com.google.android.play.core.install.model.AppUpdateType;
//import com.google.android.play.core.install.model.InstallStatus;
//import com.google.android.play.core.install.model.UpdateAvailability;
//
//public class UpdateChecker extends Activity {
//
//    Context context;
//    public final int UPDATE_REQUEST_CODE = 11111;
//    private InstallStateUpdatedListener installStateUpdateListener;
//
//    public UpdateChecker(Context context) {
//        this.context = context;
//    }
//
//    AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(UpdateChecker.this);
////    FakeAppUpdateManager appUpdateManager = (FakeAppUpdateManager) AppUpdateManagerFactory.create(context != null ? context : UpdateChecker.this); ;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        appUpdateManager.registerListener(installStateUpdateListener);
//
//        installStateUpdateListener = new
//                InstallStateUpdatedListener() {
//                    @Override
//                    public void onStateUpdate(InstallState state) {
//                        if (state.installStatus() == InstallStatus.DOWNLOADED) {
//                            //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
//                            updateInstallDialog();
//                        } else if (state.installStatus() == InstallStatus.INSTALLED) {
//                            if (appUpdateManager != null) {
//                                appUpdateManager.unregisterListener(installStateUpdateListener);
//                            }
//
//                        } else {
//                            Log.i("InstallStateUpdated", "InstallStateUpdatedListener: state: " + state.installStatus());
//                        }
//                    }
//                };
//
//        CheckUpdate();
//
//    }
//
//    public void CheckUpdate() {
//        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
//
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE )) {
//
//                updateDownloadDialog();
//
//            }else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
//
//                importantUpdateDownloadDialog();
//
//            }
//            else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
//                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
//                updateInstallDialog();
//            } else {
//                Log.e("checkForAppUpdateAvail", "checkForAppUpdateAvailability: something else");
//            }
//        });
//    }
//
//    public void importantUpdateDownloadDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Update Available");
//        builder.setMessage("This is high priority update. It might remove bugs and optimize app, alongwith new features");
//        builder.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                try {
//                    appUpdateManager.startUpdateFlowForResult(
//                            appUpdateManager.getAppUpdateInfo().getResult(), AppUpdateType.IMMEDIATE , (Activity) context, UPDATE_REQUEST_CODE);
//                } catch (IntentSender.SendIntentException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//        builder.setCancelable(false);
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//    }
//
//    public void updateDownloadDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Update Available");
//        builder.setMessage("Install now to get new and refined features");
//        builder.setNegativeButton("Remind Me later",null);
//        builder.setPositiveButton("Install", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                try {
//                    appUpdateManager.startUpdateFlowForResult(
//                            appUpdateManager.getAppUpdateInfo().getResult(), AppUpdateType.FLEXIBLE , (Activity) context, UPDATE_REQUEST_CODE);
//                } catch (IntentSender.SendIntentException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//        builder.setCancelable(false);
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//    }
//
//    private void updateInstallDialog() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Update Downloaded");
//        builder.setMessage("Your Schedule Manager is just on click away from update ");
//        builder.setPositiveButton("Install Now", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (appUpdateManager != null){
//                    appUpdateManager.completeUpdate();
//                }
//
//                if (appUpdateManager != null) {
//                    appUpdateManager.unregisterListener(installStateUpdateListener);
//                }
//            }
//        });
//        builder.setCancelable(false);
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == UPDATE_REQUEST_CODE) {
//            if (resultCode != RESULT_OK) {
//                Log.e("UpdateOnActivityResult", "onActivityResult: app download failed");
//            }
//        }
//    }
//
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (appUpdateManager != null) {
//            appUpdateManager.unregisterListener(installStateUpdateListener);
//        }
//    }
//}
