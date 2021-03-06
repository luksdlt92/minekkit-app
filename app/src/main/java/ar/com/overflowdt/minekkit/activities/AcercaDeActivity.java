package ar.com.overflowdt.minekkit.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ar.com.overflowdt.minekkit.R;

public class AcercaDeActivity extends ActionBarActivity {

    public static final String TAG = "AcercaDe";
    private Button btn_volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acercade);

        ((TextView) findViewById(R.id.tv_version)).setText("Versión: " + getString(R.string.version));
        
        btn_volver = (Button) findViewById(R.id.btn_volver);
        //Fuentes custom
        Typeface mecha_Condensed_Bold = Typeface.createFromAsset(getAssets(),
                "fonts/Mecha_Condensed_Bold.ttf");

        btn_volver.setTypeface(mecha_Condensed_Bold);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    public void cerrar(View view) {
        finish();
    }

}
