package hallo.testproject;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity {

    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    MediaPlayer title;
    int trackTime;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        context = MainMenu.this;

        preferenceSettings = getSharedPreferences("prefs", PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        preferenceSettings.getAll();

        preferenceEditor.putBoolean("mute", false);

        if (((trackTime = preferenceSettings.getInt("trackTime",0)) != 0))
            title.seekTo(trackTime);
        else


            title.setLooping(true);

        title = MediaPlayer.create(this, R.raw.title);

        Log.d("time", String.valueOf(trackTime));


        title.start();

    }


    public void locustMauling(View v){ // launches the game

        Intent applePie = new Intent(MainMenu.this, beginning.class);
        startActivity(applePie);
        finish();

    }

    public void locustEating(View v){ // launches the game

        resetPrefs();
        Toast.makeText(getApplicationContext(), "Game State Reset", Toast.LENGTH_SHORT).show();

    }

    public void locustPicking(View v){ // launches the game

        preferenceEditor.putBoolean("mute", true);
        Toast.makeText(getApplicationContext(), "Sound Muted", Toast.LENGTH_SHORT).show();

    }

    public void resetPrefs(){

        preferenceEditor.putBoolean("torch", false);
        preferenceEditor.putBoolean("flint", false);
        preferenceEditor.putBoolean("littorch", false);
        preferenceEditor.putBoolean("book", false);
        preferenceEditor.putBoolean("rope", false);
        preferenceEditor.putBoolean("watch", false);
        preferenceEditor.putBoolean("grapple", false);
        preferenceEditor.putString("currentRoom", "start");
        preferenceEditor.commit();// Always commit

    }

    public void onPause(){

        title.pause();
        trackTime = title.getCurrentPosition();
        preferenceEditor.putInt("trackTime",trackTime);
        Log.d("time",String.valueOf(trackTime));
        super.onPause();



        finish();

    }
}
