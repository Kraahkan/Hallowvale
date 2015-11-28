package hallo.testproject;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        context = MainMenu.this;
    }


    public void locustMauling(View v){ // launches the game

        Intent applePie = new Intent(MainMenu.this, hallo.testproject.T_Rex.class);
        startActivity(applePie);

    }

    public void onPause(){
       // for(int i=0; i<1000; i++){
       //     Log.d("DEBUG", "Count "+i);
       // }
        super.onPause();

        finish();

    }
}
