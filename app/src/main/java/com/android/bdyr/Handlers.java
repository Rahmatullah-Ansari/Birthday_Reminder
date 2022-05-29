package com.android.bdyr;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.net.URLEncoder;

public class Handlers implements ActivityCompat.OnRequestPermissionsResultCallback {
    @SuppressLint ("StaticFieldLeak")
    public Context context;
    private String num;

    public Handlers(Context context){
        this.context =context;
    }
    public  void openWhatsApp(String number,String text){
        String url;
        try {
            url = "https://api.whatsapp.com/send?phone="+number + "&text="+ URLEncoder.encode(text,"UTF-8");
            PackageManager pm=context.getPackageManager();
            pm.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        }catch (Exception e){
            Toast.makeText(context, "whatsapp not installed on your device", Toast.LENGTH_SHORT).show();
        }

    }
    public  void makeCall(String number){
        num=number;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context, new String[]{ Manifest.permission.CALL_PHONE },31);
        }else {
            call();
        }
    }

    private void call() {
        context.startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+num)));
    }

    public  void openMessenger(String number,String text){
        context.startActivity(new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms",number,null)).putExtra("sms_body",text));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 31){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                call();
            }else {
                Toast.makeText(context, "Permission required for call", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
