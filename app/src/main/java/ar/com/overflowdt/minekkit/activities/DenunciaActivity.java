package ar.com.overflowdt.minekkit.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ar.com.overflowdt.minekkit.MinekkitApplication;
import ar.com.overflowdt.minekkit.R;
import ar.com.overflowdt.minekkit.adapters.AttachmentsAdapter;
import ar.com.overflowdt.minekkit.models.Denuncia;
import ar.com.overflowdt.minekkit.util.HttpHandler;
import ar.com.overflowdt.minekkit.util.MenuHandler;
import ar.com.overflowdt.minekkit.util.ShowAlertMessage;

public class DenunciaActivity extends ActionBarActivity {
    public static final String TAG = "Denuncia";
    // url to get all pms list
    private static String url = "http://minekkit.com/api/crearDenuncia.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    // Progress Dialog
    private ProgressDialog pDialog;
    private EditText et_titulo, et_fecha, et_hora, et_mundo, et_ciudad, et_normas, et_solucion, et_explicacion, et_usuarios;
    private RadioButton rb_denuncia1, rb_denuncia2, rb_denuncia3, rb_denuncia4, rb_denuncia5, rb_denuncia6, rb_denuncia7;
    private Button btn_photo, btn_add_img;

    private Denuncia denuncia = new Denuncia();
    private String mCurrentImagePath;
    private AttachmentsAdapter adapter;
    private static final int SELECT_PICTURE = 2;
    private static final int TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia_tabs);

        Resources res = getResources();

        TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        //Setea la primer tab
        TabHost.TabSpec spec = tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        //Le pone un icono a la primer tab
        spec.setIndicator("1°");
        tabs.addTab(spec);

        //Setea la segunda tab
        spec = tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        //Le pone un icono a la segunda tab
        spec.setIndicator("2°");
        tabs.addTab(spec);

        //Setea la segunda tab
        spec = tabs.newTabSpec("mitab3");
        spec.setContent(R.id.tab3);
        //Le pone un icono a la segunda tab
        spec.setIndicator("3°");
        tabs.addTab(spec);

        //Setea la segunda tab
        spec = tabs.newTabSpec("mitab4");
        spec.setContent(R.id.tab4);
        //Le pone un icono a la segunda tab
        spec.setIndicator("4°");
        tabs.addTab(spec);

        //Setea la quinta tab
        spec = tabs.newTabSpec("mitab5");
        spec.setContent(R.id.tab5);
        //Le pone un icono a la segunda tab
        spec.setIndicator("5°");
        tabs.addTab(spec);

        //Setea la primer tab como default
        tabs.setCurrentTab(0);

        //Eventos de los Editboxes
        et_titulo = (EditText) findViewById(R.id.et_titulo);
        et_fecha = (EditText) findViewById(R.id.et_fecha);
        et_hora = (EditText) findViewById(R.id.et_hora);
        et_mundo = (EditText) findViewById(R.id.et_mundo);
        et_ciudad = (EditText) findViewById(R.id.et_ciudad);
        et_explicacion = (EditText) findViewById(R.id.et_explicacion);
        et_solucion = (EditText) findViewById(R.id.et_solucion);
        et_usuarios = (EditText) findViewById(R.id.et_usuarios);
        rb_denuncia1 = (RadioButton) findViewById(R.id.rb_denuncia1);
        rb_denuncia2 = (RadioButton) findViewById(R.id.rb_denuncia2);
        rb_denuncia3 = (RadioButton) findViewById(R.id.rb_denuncia3);
        rb_denuncia4 = (RadioButton) findViewById(R.id.rb_denuncia4);
        rb_denuncia5 = (RadioButton) findViewById(R.id.rb_denuncia5);
        rb_denuncia6 = (RadioButton) findViewById(R.id.rb_denuncia6);
        rb_denuncia7 = (RadioButton) findViewById(R.id.rb_denuncia7);
        et_normas = (EditText) findViewById(R.id.et_normas);
        btn_photo = (Button) findViewById(R.id.btn_take_photo);
        btn_add_img = (Button) findViewById(R.id.btn_add_img);
        ListView list_attachments = (ListView) findViewById(R.id.denuncia_list);
        adapter = new AttachmentsAdapter(denuncia.attachments, DenunciaActivity.this);
        list_attachments.setAdapter(adapter);
        btn_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File

                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, TAKE_PHOTO);
                    }
                }

            }
        });

        btn_add_img.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                // in onCreate or any event where your want the user to
                // select a file
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String storageDir = Environment.getExternalStorageDirectory() + "/picupload";
        File dir = new File(storageDir);
        if (!dir.exists())
            dir.mkdir();

        File image = new File(storageDir + "/" + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentImagePath = image.getAbsolutePath();
        Log.i("PHoto", "photo path = " + mCurrentImagePath);
        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("PHOTO", "onActivityResult: " + this);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case TAKE_PHOTO:
                    setPic();
                    break;
                case SELECT_PICTURE:
                    setCurrentImagePathFromSelection(data);
                    setPic();
                    break;
            }
    }

    private void setCurrentImagePathFromSelection(Intent data) {
        Uri selectedImageUri = data.getData();
        //OI FILE Manager
        String filemanagerstring = selectedImageUri.getPath();
        //MEDIA GALLERY
        String selectedImagePath = getPath(selectedImageUri);
        //NOW WE HAVE OUR WANTED STRING
        if (selectedImagePath != null)
            mCurrentImagePath = selectedImagePath;
        else
            mCurrentImagePath = filemanagerstring;
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else return null;
    }

    private void setPic() {
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentImagePath);
        denuncia.attachments.put(mCurrentImagePath, bitmap);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                crearDenuncia(null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void verReglas(View view) {
        Intent i = new Intent(this, BrowserActivity.class);
        i.putExtra("title", "Reglas");
        i.putExtra("direccion", "minekkit.com/foro/showthread.php?tid=2371");
        startActivity(i);
    }

    public void crearDenuncia(View view) {
        denuncia.setTitulo(et_titulo.getText().toString());
        denuncia.setCiudad(et_ciudad.getText().toString());
        denuncia.setExplicacion(et_explicacion.getText().toString());
        denuncia.setSolucion(et_solucion.getText().toString());
        denuncia.setFecha(et_fecha.getText().toString());
        denuncia.setHora(et_hora.getText().toString());
        denuncia.setMundo(et_mundo.getText().toString());
        denuncia.setNormasNoCumplidas(et_normas.getText().toString());
        denuncia.setUsuariosInvolucrados(et_usuarios.getText().toString());

        String tipo;

        if (rb_denuncia1.isChecked()) {
            tipo = rb_denuncia1.getText().toString();
        } else if (rb_denuncia2.isChecked()) {
            tipo = rb_denuncia2.getText().toString();
        } else if (rb_denuncia3.isChecked()) {
            tipo = rb_denuncia3.getText().toString();
        } else if (rb_denuncia4.isChecked()) {
            tipo = rb_denuncia4.getText().toString();
        } else if (rb_denuncia5.isChecked()) {
            tipo = rb_denuncia5.getText().toString();
        } else if (rb_denuncia6.isChecked()) {
            tipo = rb_denuncia6.getText().toString();
        } else if (rb_denuncia7.isChecked()) {
            tipo = rb_denuncia7.getText().toString();
        } else {
            tipo = "Sin tipo";
        }
        denuncia.setTipoDenuncia(tipo);

        if (denuncia.isMissingFields()) {
            Toast.makeText(this, "Te faltan llenar datos", Toast.LENGTH_LONG).show();
            return;
        } else if (denuncia.isMissingEvidence()) {
            Toast.makeText(this, "Te falta agregar alguna prueba", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d("INFO", "Paso 1");
        new SubirDenuncia().execute();
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     */
    class SubirDenuncia extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DenunciaActivity.this);
            pDialog.setMessage(getString(R.string.enviandoDenuncia));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All pms from url
         */
        protected String doInBackground(String... args) {
            HttpHandler handler = new HttpHandler();
            JSONObject json = null;

            try {
                json = handler.postWithFile(url, denuncia, denuncia.attachments.values());
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            try {
                // Checking for SUCCESS TAG
                int success;
                if (json != null) {
                    success = json.getInt(TAG_SUCCESS);
                } else
                    return null;

                switch (success) {
                    case -100:
                        ShowAlertMessage.showMessage("No se puede conectar con el servidor. Intente más tarde.", DenunciaActivity.this);
                        break;
                    case 0:
                        ShowAlertMessage.showMessage(getString(R.string.denunciaFail), DenunciaActivity.this);
                        break;
                    case 1:
                        ShowAlertMessage.showMessageAndFinishActivity(getString(R.string.denunciaExito), DenunciaActivity.this);
                        break;
                    case -1:
                        ShowAlertMessage.showMessage(getString(R.string.denunciaFailDatos), DenunciaActivity.this);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } 

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all pms
            pDialog.dismiss();
            // updating UI from Background Thread
            /*
            runOnUiThread(new Runnable() {
                public void run() {
                    (Denuncia.this).finish();
                }
            });
            */

        }

    }

}
