package com.jossessoft.deletesentsmss;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String gDefaultSmsApp;

    private static final int PERMISSION_REQUEST_SMS = 123;

    private int indiceSpinnerUT = 0;

    private Switch s1;
    private Spinner spinnerUT;
    private NumberPicker npTiempo;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

        Intent setSmsAppIntent =
                new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        setSmsAppIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                getPackageName());
        startActivityForResult(setSmsAppIntent, PERMISSION_REQUEST_SMS);

        }

        spinnerUT = findViewById(R.id.spiUnidadTiempo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.unidad_tiempo,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUT.setAdapter(adapter);
        spinnerUT.setOnItemSelectedListener(this);

        npTiempo = findViewById(R.id.npTiempo);
        npTiempo.setMinValue(10);
        npTiempo.setMaxValue(60);

        s1 = (Switch) findViewById(R.id.switch1);
        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean activo) {

                if(activo){

                    Intent setSmsAppIntent =
                            new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                    setSmsAppIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                            getPackageName());
                    startActivityForResult(setSmsAppIntent, PERMISSION_REQUEST_SMS);

                    spinnerUT.setEnabled(false);
                    npTiempo.setEnabled(false);

                    mProgramarTarea.run();

                }else{

                    mHandler.removeCallbacks(mProgramarTarea);

                    spinnerUT.setEnabled(true);
                    npTiempo.setEnabled(true);

                }

            }
        });

    }

    private Runnable mProgramarTarea = new Runnable() {
        @Override
        public void run() {

            int milisegundos = 10000;

            if(spinnerUT.getSelectedItemPosition()==0){
                milisegundos = npTiempo.getValue()*1000;
            }else{
                milisegundos = npTiempo.getValue()*60000;
            }
            borrarMensajes();
            mHandler.postDelayed(this,milisegundos);

        }
    };

    private void borrarMensajes(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

            int countSentSMSs = 0;

            Uri deleteUri = null;
            deleteUri = Telephony.Sms.CONTENT_URI;

            Cursor c = getApplicationContext().getContentResolver().query(deleteUri, null, null, null, null);

            int countSMSs = c.getCount();

            if(countSMSs > 0){
                try {

                    while (c.moveToNext()) {

                        if (!c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {

                            countSentSMSs = countSentSMSs + 1;

                            String pid = c.getString(0);
                            Uri.Builder uri = Telephony.Sms.CONTENT_URI.buildUpon().appendPath(pid);
                            getApplicationContext().getContentResolver().delete(Uri.parse(uri.toString()),
                                    null, null);
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }finally{
                    c.close();
                    Snackbar.make(findViewById(R.id.myRelativeLayout), countSentSMSs>1?countSentSMSs + " SMSs borrados": countSentSMSs + " SMS borrado",
                            Snackbar.LENGTH_SHORT)
                            .show();

                }
            }
            System.out.println("UM: " + spinnerUT.getSelectedItemPosition() + " Tiempo: " + npTiempo.getValue());
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(spinnerUT.getSelectedItemPosition()==0){
            npTiempo.setMinValue(10);
        }else{
            npTiempo.setMinValue(1);
        }
        npTiempo.setValue(npTiempo.getMinValue());

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}