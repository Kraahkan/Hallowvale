package hallo.testproject;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class T_Rex extends AppCompatActivity { // create a game

    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    EditText editName, editDestination;
    Spinner editRace;

    private String[] races;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t__rex);

        preferenceSettings = getSharedPreferences("prefs", PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();

        preferenceSettings.getAll();

        editName = (EditText)findViewById(R.id.editText);
        editDestination = (EditText)findViewById(R.id.editText2);
        editRace = (Spinner)findViewById(R.id.spinner);

        this.races = new String[] {"Bungalo",
        "Centipede",
        "Minotaur",
        "Bird",
        "Inbred",
        "Chimera",
        "Blastoise",
        "Dead Flie",
        "Zombie"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, races);
        editRace.setAdapter(adapter);


    }

    public void sillyPuddyIChooseYou(View v){

        preferenceEditor.putString("name", editName.getText().toString());
        preferenceEditor.putString("race", editRace.getSelectedItem().toString());
        preferenceEditor.putString("destination", editDestination.getText().toString());

        preferenceEditor.commit();

        Intent i = new Intent(T_Rex.this, beginning.class);
        startActivity(i);


    }

    /*public void onPause(){
        super.onPause();

        finish();

    }*/
}
