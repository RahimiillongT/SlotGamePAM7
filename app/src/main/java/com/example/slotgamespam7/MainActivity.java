package com.example.slotgamespam7;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView imgSlot1;
    private ImageView imgSlot2;
    private ImageView imgSlot3;
    private Button btnGet;
    private TextView tvHasil;
    boolean isPlay = false;
    int slot1, slot2, slot3;
    Random random = new Random();

    ExecutorService execGetImage;
    Handler handler;
    ArrayList<String> arrayUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGet = findViewById(R.id.btn_get);
        imgSlot1 = findViewById(R.id.img_slot1);
        imgSlot2 = findViewById(R.id.img_slot2);
        imgSlot3 = findViewById(R.id.img_slot3);
        tvHasil = findViewById(R.id.tv_hasil);

        execGetImage = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        btnGet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnGet.getId()){
            if (!isPlay){
                isPlay = true;
                btnGet.setText("Stop");
                execGetImage.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String txt = loadStringFromNetwork("https://662e87fba7dda1fa378d337e.mockapi.io/api/v1/fruits");
                            try {
                                JSONArray jsonArray = new JSONArray(txt);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    arrayUrl.add(jsonObject.getString("url"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            while(isPlay){
                                slot1 = random.nextInt(3);
                                slot2 = random.nextInt(3);
                                slot3 = random.nextInt(3);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide.with(MainActivity.this).load(arrayUrl.get(slot1)).into(imgSlot1);
                                        Glide.with(MainActivity.this).load(arrayUrl.get(slot2)).into(imgSlot2);
                                        Glide.with(MainActivity.this).load(arrayUrl.get(slot3)).into(imgSlot3);
                                    }
                                });
                                try {
                                    Thread.sleep(80);
                                } catch (InterruptedException e){
                                    e.printStackTrace();
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }else {
                isPlay = false;
                btnGet.setText("Ambil Gambar");
                if (slot1 == slot2 && slot2 == slot3){
                    tvHasil.setText("MENANG BOSQUE");
                } else {
                    tvHasil.setText("KALAH, COBA LAGI");
                }
            }
        }
    }
    private String loadStringFromNetwork(String s) throws IOException {
        final URL myUrl = new URL(s);
        final InputStream in = myUrl.openStream();
        final StringBuilder out = new StringBuilder();
        final byte[] buffer = new byte[1024];
        try {
            for (int ctr; (ctr = in.read(buffer)) != -1; ) {
                out.append(new String(buffer, 0, ctr));
            }
        } catch (IOException e) {
            throw new RuntimeException("Gagal mendapatkan text", e);
        }
        final String yourFileAsAString = out.toString();
        return yourFileAsAString;
    }


}

