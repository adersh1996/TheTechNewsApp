package com.project.thetechnewsapp;

import android.content.Context;
import android.util.Log;

import com.application.isradeleon.notify.Notify;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String headLine, subLine, message, image;

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.i("newToken", token);
        //Add your token in your sharepreferences.
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("msg", remoteMessage.getFrom());

        try {
            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            {
                headLine = object.getString("Headline");
                subLine = object.getString("Subline");
                message = object.getString("message");
                image = object.getString("photo");
                Notify.build(this)
                        .setTitle(headLine)
                        .setContent(subLine)
                        .setLargeIcon(image)
                        .setSmallIcon(R.drawable.news_app_icon)
                        .show();
//
//                title = "Request "+object.getString("status");
//                message = "Staff Name " + object.getString("Staff_name");
//                Notify.build(this)
//                        .setTitle(title)
//                        .setContent(message)
//                        .setSmallIcon(R.drawable.notification_logo)
//                        // .setLargeIcon(img)
//                        .largeCircularIcon()
//                        // .setPicture()
//                        .setColor(R.color.login_bt_bg_color)
//                        // .setAction(intent)
//                        .show();
            }


        } catch (Exception e) {
            Log.e("ERROR IN PUSH", String.valueOf(e));
        }


    }

    //Whenewer you need FCM token, just call this static method to get it.
    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fcm_token", "empty");
    }
}
