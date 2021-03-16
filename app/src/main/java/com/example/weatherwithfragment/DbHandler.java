package com.example.weatherwithfragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbHandler  {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;
    private ForecastObject forecastObj;
    private static final String TAG = "DbHandler";
    private List<ForecastObject> forecastObjArr, forecastObjArr2Delete;
    private CollectionReference forecastRef = db.collection("Forecasts");
    private boolean isCityFound;



    public DbHandler(FirebaseFirestore db) {
        this.db = db;
    }

    public DbHandler(Context context) {
        this.context = context;
    }

    public DbHandler() {
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public void setDb( FirebaseFirestore db2 ){
        this.db = db2;

    }
    
    public void addDummyData()
    {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first","Ada");
        user.put("last","Lovelace");
        user.put("born",1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        Toast.makeText(context,
                "Data was added",
                Toast.LENGTH_LONG);

    }
    public void deleteCity(String city,Context context){
        db.collection("Forecasts")
                .whereEqualTo("cityName",city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            forecastObjArr2Delete = (ArrayList<ForecastObject>) task.getResult().toObjects(ForecastObject.class);
                            if(forecastObjArr2Delete != null && forecastObjArr2Delete.size() != 0){
                                isCityFound = true;
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    String idDelete = document.getId();
                                    db.collection("Forecasts").document(idDelete)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error deleting document", e);
                                                }
                                            });



                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("City Found ")
                                        .setMessage( city +" Deleted\n");
                                builder.setPositiveButton("OK :)", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                });
                                builder.show();

                            }
                            else {
                                isCityFound = false;
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                if(!isCityFound)
                                {
                                    builder.setTitle("City Not Found ")
                                            .setMessage( city +" Not Found In Our DataBase\n");
                                    builder.setPositiveButton("OK :(", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    });
                                    builder.show();
                                }
                            }



                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    public boolean isCityFound() {
        return isCityFound;
    }

    public void addForecast(ForecastObject forecast) {
        forecastRef
                .add(forecast)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        ;//document(forecast.getCityName()).set(forecast)
        ;
    }

    /** unused
//    כפר חרוב
    public void getForecastByCityDummy() {

        DocumentReference docRef = forecastRef.document("כפר חרוב");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        forecastObj =  new ForecastObject(document.toObject(ForecastObject.class));
                        Toast.makeText(context, "DbHandler: Forecast: "+ forecastObj.toString(), Toast.LENGTH_LONG).show();
                    } else {
                        Log.d(TAG, "No such document");
                        Toast.makeText(context, "DbHandler: No such document", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    Toast.makeText(context, "DbHandler: get failed with: "+ task.getException(), Toast.LENGTH_SHORT).show();

                }
            }

        });


    }


    public void getForecastByCity(String city) {
        DocumentReference docRef = forecastRef.document(city);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Toast.makeText(context, "DbHandler: DocumentSnapshot data: "+ document.getData(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "No such document");
                        Toast.makeText(context, "DbHandler: No such document", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    Toast.makeText(context, "DbHandler: get failed with: "+ task.getException(), Toast.LENGTH_SHORT).show();
                }
            }

        });



    }

    public void getForecastByCityDummy2(String city) {
        forecastRef
                .whereEqualTo("cityName","חיפה")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            forecastObjArr =  task.getResult().toObjects(ForecastObject.class);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                forecastObj = document.toObject(ForecastObject.class);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public List<ForecastObject> callGetAll(String city){
//        getForecastByCityDummy2( city);
        //new callDbAsync().execute(city);
        return forecastObjArr;
    }

    public List<ForecastObject>  getAllByCity() {

        if(forecastObjArr == null){
            Toast.makeText(context, "forecastObjArr is EmptpY!", Toast.LENGTH_SHORT).show();
            return null;
        }
        for(ForecastObject forecastObj : forecastObjArr)
        {
            String str = forecastObj.toString();
            Log.i(TAG,str);
        }
        return forecastObjArr;


//        forecastObjArr = new ArrayList<>();
//        try {
//            forecastRef
//                    .whereEqualTo("cityName","חיפה")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
//                    {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task){
//
//                            if (task.isSuccessful()){
//                                forecastObjArr = task.getResult().toObjects(ForecastObject.class);
//                                Log.i(TAG, " Yay!!  forecastObjArr have" + forecastObjArr.size());
//
//                            }
//                            else{
//                                Log.d(TAG, "Error getting documents: ", task.getException());
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        for(ForecastObject forecastObj : forecastObjArr)
//        {
//            forecastObj.toString();
//        }
//
//    }

    */
/** try with Async
    public class callDbAsync extends AsyncTask<String, String,String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String  _forecastObjArr) {
            super.onPostExecute(_forecastObjArr);
//            _forecastObjArr = _forecastObjArr;
        }

        @Override
        protected String doInBackground(String... strings) {
            String city = strings[0];
            forecastRef
                    .whereEqualTo("cityName",city)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                forecastObjArr =  task.getResult().toObjects(ForecastObject.class);
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    forecastObj = document.toObject(ForecastObject.class);
//                                    Log.d(TAG, document.getId() + " => " + document.getData());
//                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
//            return forecastObjArr;
            return city;
        }
    }
*/
}
