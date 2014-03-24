package ar.com.overflowdt.minekkit.onlineList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ar.com.overflowdt.minekkit.R;
import ar.com.overflowdt.minekkit.pms.PmListAdapter;
import ar.com.overflowdt.minekkit.util.HttpHandler;
import ar.com.overflowdt.minekkit.util.MenuHandler;
import ar.com.overflowdt.minekkit.util.Session;
import ar.com.overflowdt.minekkit.util.ShowAlertMessage;

/**
 * Created by Fede on 23/03/14.
 */
public class OnlineListActivity extends ListActivity {
    // Progress Dialog
    private ProgressDialog pDialog;


    List<OnlineListAdapter.Player> playerList;
    // url to get all playersOn list
    private static String url = "http://minekkit.com/api/listOnline.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PLAYERS = "players";
    private static final String TAG_NAME = "";
    private static final String TAG_DATE = "date";
    private static final String TAG_LOGO = "Logo";
    private static final String TAG_FROM = "from";
    private static final String TAG_ID = "pmid";

    // playersOn JSONArray
    JSONArray playersOn = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_list);


        // Loading playersOn in Background Thread
        new LoadPlayersOnline().execute();

        // Get listview
        ListView lv = getListView();

//         on seleting single product
//         launching Edit Product Screen
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // getting values from selected ListItem
//                String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();
//
//                // Starting new intent
//                Intent in = new Intent(OnlineListActivity.this, SinglePMActivity.class);
//                // sending pid to next activity
//                in.putExtra(TAG_ID, pid);
//
//
//                startActivityForResult(in, 100);
//                // starting new activity and expecting some response back
////                startActivityForResult(in, 100);
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MenuHandler menuHandler = new MenuHandler();
        return menuHandler.bindearLogica(item, this);
    }

    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (requestCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadPlayersOnline extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OnlineListActivity.this);
            pDialog.setMessage("Cargando jugadores en linea. Por favor, espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }



        /**
         * getting All playersOn from url
         * */
        protected String doInBackground(String... args) {
            HttpHandler handler = new HttpHandler();
            JSONObject json = null;

            try{
                json = handler.post(url, Session.getInstance());
                Log.d("Players", json.toString());
            }catch(Exception ex){
                ex.printStackTrace();
            }

            playerList = new ArrayList<OnlineListAdapter.Player>();

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                switch(success) {
                    case -100:
                        ShowAlertMessage.showMessage("No se puede conectar con el servidor. Intente más tarde.", OnlineListActivity.this);
                        break;
                    case 1:
                        // playersOn found
                        // Getting Array of Products
                        playersOn = json.getJSONArray(TAG_PLAYERS);

                        // looping through All Products
                        for (int i = 0; i < playersOn.length(); i++) {
                            String c = playersOn.getString(i);

                            // Storing each json item in variable
                            OnlineListAdapter.Player item = new OnlineListAdapter.Player();
                            item.name = c;
                            try {
                                URL newurl = new URL("https://minotar.net/avatar/"+c+"/50");
                                item.face = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                            } catch (IOException e) {
                                item.face = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.steve);

                                e.printStackTrace();
                            }
                            playerList.add(item);
                        }
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all playersOn
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    OnlineListAdapter adapter = new OnlineListAdapter();
                    adapter.activity=OnlineListActivity.this;
                    adapter.playersOn= playerList;
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}


