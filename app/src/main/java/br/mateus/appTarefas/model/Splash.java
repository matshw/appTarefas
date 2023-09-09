package br.mateus.appTarefas.model;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import br.mateus.appTarefas.MainActivity;
import br.mateus.appTarefas.R;

public class Splash extends AppCompatActivity {
    private final Timer timer = new Timer();
    TimerTask timerTask;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gotoMainAcctivity();
                    }
                });
            }
        };
        timer.schedule(timerTask,3000);
    }

    private void gotoMainAcctivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
