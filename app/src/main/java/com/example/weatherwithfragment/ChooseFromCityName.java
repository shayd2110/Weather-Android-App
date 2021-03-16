package com.example.weatherwithfragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.newweatherapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;

public class ChooseFromCityName extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String city;
    protected DbHandler handler;
    ProgressDialog progressDialog;
    ArrayList<ForecastObject> forecastObjArr;
    ListView listView;
    FirebaseFirestore db;
    private static AdapterForecast adapter;
    private String TAG  = "ChooseFromCityName";
    private static final int RESULT_OK = 1;
    private static final int RESULT_NOT_EXIST = -1;
    private static final int RESULT_CANCELED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_from_city_name);
        Intent intent = getIntent();
        city = intent.getStringExtra("city_name");
         db = FirebaseFirestore.getInstance();

        listView = (ListView) findViewById(R.id.from_city_list_view);
        callDb();


    }

    public void renderRes()
    {
        if(forecastObjArr == null || forecastObjArr.size() == 0 )
        {
            backWithNoSuccess();
        }
        adapter = new AdapterForecast(forecastObjArr,getApplicationContext());
        adapter.sort(new Comparator<ForecastObject>() {
            @Override
            public int compare(ForecastObject o1, ForecastObject o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        listView.setAdapter(adapter);
//        listView.onNothingSelected
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ForecastObject forecastObj= forecastObjArr.get(position);
                if (forecastObj == null){

                }
                returnValueBackActivity(forecastObj,RESULT_OK);

//                Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getType()+" API: "+dataModel.getVersion_number(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
            }
        });
         if(listView.getSelectedItem() == null){

         }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Utils.showToast("Nothing Selected",ChooseFromCityName.this);
    }
    private void backWithNoSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("City Not Found ")
                .setMessage( city +" Not Found In Our DataBase\n");
        builder.setPositiveButton("OK :(", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                returnValueBackActivity(null,RESULT_NOT_EXIST);
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnValueBackActivity(null,RESULT_CANCELED);
    }

    private void returnValueBackActivity(ForecastObject forecastObj, int  resCode) {
        Intent i = new Intent(this,ChooseFromCityName.class);
        switch (resCode){
            case RESULT_OK:
                if (forecastObj != null)
                {
                    ForecastObjectRet objectRet =  new ForecastObjectRet(forecastObj);
                    i.putExtra("isSuccess",true);
                    i.putExtra("forecastObj",objectRet);
                    setResult(RESULT_OK,i);
                    finish();
                }
                break;
            case RESULT_NOT_EXIST:
                i.putExtra("isSuccess",false);
                setResult(RESULT_NOT_EXIST,i);
                finish();
                break;

            case RESULT_CANCELED:
                i.putExtra("isSuccess",false);
                setResult(RESULT_CANCELED,i);
                finish();
                break;


        }



    }

    public class GetDbAsync extends AsyncTask<String, String, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ChooseFromCityName.this);
            progressDialog.setMessage("Please wait.. getting data from db");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            renderRes();
            progressDialog.dismiss();


        }

        @Override
        protected Void doInBackground(String... strings) {
//            String cityVal = strings[0];
            db.collection("Forecasts")
                    .whereEqualTo("cityName",city)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                forecastObjArr = (ArrayList<ForecastObject>) task.getResult().toObjects(ForecastObject.class);
                                renderRes();
                            }
                            else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                        }
                    });
            return null;


        }
    }
    public void callDb(){
        progressDialog = new ProgressDialog(ChooseFromCityName.this);
        progressDialog.setMessage("Please wait.. getting data from db");
        progressDialog.setCancelable(false);
        progressDialog.show();
        db.collection("Forecasts")
                .whereEqualTo("cityName",city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            forecastObjArr = (ArrayList<ForecastObject>) task.getResult().toObjects(ForecastObject.class);
                            progressDialog.dismiss();
                            renderRes();
                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



}


