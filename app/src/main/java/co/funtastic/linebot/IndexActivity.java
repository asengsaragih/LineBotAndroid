package co.funtastic.linebot;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class IndexActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    private ListView listView;
    private String JSON_STRING;
    private View emptyView, emptyConnection;

    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        floatingActionButton = findViewById(R.id.fab_data);
        listView = (ListView) findViewById(R.id.dataListView);
        emptyView = (View) findViewById(R.id.emptyView);
        emptyConnection = (View) findViewById(R.id.emptyConnection);

        checkInternet();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void checkInternet(){
        ConnectivityManager concMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = concMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            floatingActionButton.setVisibility(View.VISIBLE);
            listView.setOnItemClickListener(this);
            listView.setEmptyView(emptyView);
            emptyConnection.setVisibility(View.GONE);
            getJSON();
        } else {
            listView.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            emptyConnection.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_history:
                Toast.makeText(this,"Masih Dalam Proses Pengembangan", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_refresh:
                checkInternet();
                break;
        }
        return true;
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(IndexActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showEmployee(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Konfigurasi.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String tanggal = jo.getString(Konfigurasi.TAG_TANGGAL);
                String keterangan = jo.getString(Konfigurasi.TAG_KETERANGAN);

                //kalau eror hapus disini
                String id = jo.getString(Konfigurasi.TAG_ID);

                HashMap<String,String> employees = new HashMap<>();
                employees.put(Konfigurasi.TAG_TANGGAL,tanggal);
                employees.put(Konfigurasi.TAG_KETERANGAN,keterangan);

                //kalau eror hapus disini
                employees.put(Konfigurasi.TAG_ID, id);
                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                IndexActivity.this, list, R.layout.list_item_data,
                new String[]{Konfigurasi.TAG_TANGGAL,Konfigurasi.TAG_KETERANGAN},
                new int[]{R.id.textView_tanggal, R.id.textView_keterangan});

        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, FormActivity.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String empId = map.get(Konfigurasi.TAG_ID);
        intent.putExtra(Konfigurasi.JADWAL_ID,empId);
        startActivity(intent);
    }
}
