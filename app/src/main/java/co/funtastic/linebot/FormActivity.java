package co.funtastic.linebot;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class FormActivity extends AppCompatActivity {

    private EditText mTanggalEditText, mKeteranganEditText;
    private final Calendar calendar = Calendar.getInstance();
    
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        mTanggalEditText = (EditText) findViewById(R.id.editText_date);
        mKeteranganEditText = (EditText) findViewById(R.id.editText_keterangan);

        datePicker();
        
        Intent intent = getIntent();
        id = intent.getStringExtra(Konfigurasi.JADWAL_ID);
        
        if (id != null){
            getData();
        }
    }
    
    private void getData(){
        class GetDATA extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(FormActivity.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showData(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_GET_EMP,id);
                return s;
            }
        }

        GetDATA getDATA = new GetDATA();
        getDATA.execute();
    }

    private void showData(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String tanggal = c.getString(Konfigurasi.TAG_TANGGAL).trim();
            String keterangan = c.getString(Konfigurasi.TAG_KETERANGAN).trim();

            mTanggalEditText.setText(tanggal);
            mKeteranganEditText.setText(keterangan);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addData(){
        final String tanggal = mTanggalEditText.getText().toString().trim();
        final String keterangan = mKeteranganEditText.getText().toString().trim();

        class AddData extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(FormActivity.this,"Menambahkan Data Baru","Silahkan Tunggu",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if ((loading != null) && loading.isShowing()){
                    loading.dismiss();
                }
                Toast.makeText(FormActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Konfigurasi.KEY_JADWAL_KETERANGAN, keterangan);
                params.put(Konfigurasi.KEY_JADWAL_TANGGAL, tanggal);

                RequestHandler requestHandler = new RequestHandler();
                String res = requestHandler.sendPostRequest(Konfigurasi.URL_ADD, params);
                return res;
            }
        }

        if (tanggal.isEmpty() || keterangan.isEmpty()){
            Toast.makeText(FormActivity.this, "Form Tidak Boleh Kosong!", Toast.LENGTH_LONG).show();
            return;
        } else {
            AddData aD = new AddData();
            aD.execute();
        }


    }

    private void updateData(){
        final String keterangan1 = mKeteranganEditText.getText().toString().trim();
        final String tanggal1 = mTanggalEditText.getText().toString().trim();



        class UpdateData extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(FormActivity.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if ((loading != null) && loading.isShowing()){
                    loading.dismiss();
                }
                Toast.makeText(FormActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_JADWAL_ID,id);
                hashMap.put(Konfigurasi.KEY_JADWAL_TANGGAL,tanggal1);
                hashMap.put(Konfigurasi.KEY_JADWAL_KETERANGAN,keterangan1);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Konfigurasi.URL_UPDATE_EMP,hashMap);

                return s;
            }
        }

        if (tanggal1.isEmpty() || keterangan1.isEmpty()){
            Toast.makeText(FormActivity.this, "Form Tidak Boleh Kosong!", Toast.LENGTH_LONG).show();
            return;
        } else {
            UpdateData ud = new UpdateData();
            ud.execute();
        }
    }

    private void deleteData() {
        class DeleteEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(FormActivity.this, "Updating...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(FormActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_DELETE_EMP, id);
                return s;
            }
        }

        DeleteEmployee de = new DeleteEmployee();
        de.execute();
    }

    private void confirmDeleteData(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Ingin Menghapus Data ini?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteData();
                        startActivity(new Intent(FormActivity.this,IndexActivity.class));
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        return;
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void datePicker() {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        mTanggalEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(FormActivity.this, date,  calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mTanggalEditText.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (id == null){
            MenuItem menuDelete = menu.findItem(R.id.action_delete);
            MenuItem menuUpdate = menu.findItem(R.id.action_update);
            menuDelete.setVisible(false);
            menuUpdate.setVisible(false);
        } else {
            MenuItem AddBtn = menu.findItem(R.id.action_save);
            AddBtn.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                addData();
                return true;
            case R.id.action_delete:
                confirmDeleteData();
                break;
            case R.id.action_update:
                updateData();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
