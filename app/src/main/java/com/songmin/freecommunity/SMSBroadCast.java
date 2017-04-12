package com.songmin.freecommunity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 송민 on 2017-04-01.
 */
public class SMSBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if("android.provider.Telephony.SMS_RECEIVED".equals(action)){

            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[])bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for(int i=0; i<messages.length; i++){

                smsMessage[i] = SmsMessage.createFromPdu((byte[])messages[i]);
            }

            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초", Locale.KOREA);

            String originDate = mDateFormat.format(curDate);
            String origNumber = smsMessage[0].getOriginatingAddress();
            String message = smsMessage[0].getMessageBody().toString();

            TelephonyManager telManager = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
            String phoneNum = telManager.getLine1Number();

            Log.e("ssong", originDate);
            Log.e("ssong", origNumber);
            Log.e("ssong", message);
            Log.e("ssong", phoneNum);

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference();

            MessageData messageData = new MessageData(originDate, message, origNumber, phoneNum);
            databaseReference.child(phoneNum).push().setValue(messageData);
        }

    }

}
