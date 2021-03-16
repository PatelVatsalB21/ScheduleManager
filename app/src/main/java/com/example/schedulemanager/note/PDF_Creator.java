package com.example.schedulemanager.note;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.fonts.Font;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class PDF_Creator {

    Context context;
    String content, title;
    Boolean success = true;

    public PDF_Creator(Context c, String con , String t) {
        context = c;
        content = con;
        title = t;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createMyPDF(){

        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        int perm1 = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int perm2 = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

        if ( perm1 == PackageManager.PERMISSION_GRANTED && perm2 == PackageManager.PERMISSION_GRANTED){
        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

        Paint myPaint = new Paint();
        int x = 10, y=25;

        for (String line: content.split("\n")){
            myPage.getCanvas().drawText(line, x, y, myPaint);
            y+=myPaint.descent()-myPaint.ascent();
        }
        myPdfDocument.finishPage(myPage);
        String dirPath = Environment.getExternalStorageDirectory().getPath() + "/Schedule Manager";
        File dir = new File(dirPath);
        if (!dir.isDirectory()){
            dir.mkdirs();
        }

        String myFilePath = dirPath + "/"+title+".pdf";
        File myFile = new File(myFilePath);
        try {
            myPdfDocument.writeTo(new FileOutputStream(myFile));
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Error in generating PDF file", Toast.LENGTH_SHORT).show();
            success = false;
        }
        myPdfDocument.close();
        if (success) Toast.makeText(context, "PDF saved to" + myFilePath, Toast.LENGTH_SHORT).show();
    }else Toast.makeText(context, "Permissions not granted", Toast.LENGTH_SHORT).show();
    }
}
