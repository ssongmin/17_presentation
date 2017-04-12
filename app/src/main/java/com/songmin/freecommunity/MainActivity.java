package com.songmin.freecommunity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final String TARGET_URL = "https://openapi.naver.com/v1/language/translate";
    private EditText editText;
    private TextView textView;
    private Button btnKToE, btnEToK, btnKToJ;
    private String[] params;
    private static final int RC_PERMISSION = 31;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        editText = (EditText) findViewById(R.id.edit_input);
        textView = (TextView) findViewById(R.id.text_translate);
        btnKToE = (Button) findViewById(R.id.btn_kor_to_eng);
        btnEToK = (Button) findViewById(R.id.btn_eng_to_kor);
        btnKToJ = (Button) findViewById(R.id.btn_kor_jap);

        params = new String[3];
        btnKToE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                params[0] = editText.getText().toString();
                params[1] = "ko";
                params[2] = "en";
                new AsyncTrans().execute(params);
            }
        });
        btnEToK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                params[0] = editText.getText().toString();
                params[1] = "en";
                params[2] = "ko";
                new AsyncTrans().execute(params);
            }
        });

        btnKToJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                params[0] = editText.getText().toString();
                params[1] = "ko";
                params[2] = "ja";
                new AsyncTrans().execute(params);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION) {
            if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                checkPermission();
            } else {
                finishNoPermission();
            }
        }
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_PHONE_STATE}, RC_PERMISSION);
    }

    private void finishNoPermission() {
        Toast.makeText(this, "권환 허용을 해주세요.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public class AsyncTrans extends AsyncTask<String, Integer, String> {

        //다운 되기 전
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //다운중
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            HttpURLConnection conn = null;
            BufferedReader fromServer = null;
            StringBuilder queryBuf = new StringBuilder();
            try {
                queryBuf.append("text=" + strings[0])
                        .append("&source=" + strings[1])
                        .append("&target=" + strings[2]);

                URL target = new URL(TARGET_URL);
                conn = (HttpURLConnection) target.openConnection();
                conn.setConnectTimeout(10000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("X-Naver-Client-Id", getResources().getString(R.string.naver_client_id));
                conn.setRequestProperty("X-Naver-Client-Secret", getResources().getString(R.string.naver_client_secret));
                OutputStream toServer = conn.getOutputStream();
                toServer.write(new String(queryBuf.toString()).getBytes("UTF-8"));
                toServer.close();

                int resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String onLine = "";

                    StringBuilder jsonBuf = new StringBuilder();
                    while ((onLine = fromServer.readLine()) != null) {
                        jsonBuf.append(onLine);
                    }
                    result = MainActivity.getJSon(jsonBuf);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        //다운 완료
        @Override
        protected void onPostExecute(String s) {
            textView.setText(s);
        }
    }

    public static String getJSon(StringBuilder buf) {
        JSONObject jo = null;
        JSONObject jo2 = null;
        JSONObject jo3 = null;
        JSONArray jsonArray = null;
        String result = "";
        try {
            jo = new JSONObject(buf.toString());
            jo2 = jo.getJSONObject("message");
            jo3 = jo2.getJSONObject("result");
            result = jo3.getString("translatedText");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
