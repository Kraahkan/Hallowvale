package hallo.testproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.InputStream;

public class beginning extends AppCompatActivity { // This is the main game interface

    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    public Context c;
    TextView texter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginning);

        preferenceSettings = getSharedPreferences("prefs", PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        preferenceSettings.getAll();

        texter = (TextView)findViewById(R.id.textView4);

        c = beginning.this; // creates a context/reference
        InputStream testRoom = getResources().openRawResource(R.raw.blastoise);
        Gf.createRoom(testRoom);

        Gf.updateFlags();

        //Gf.checkInfo();

        texter.setText(Gf.firstLine + "\n" + Gf.secondLine + "\n" + Gf.thirdLine);

    }


}
