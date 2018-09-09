package com.maxim.jpr.Util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.maxim.jpr.Models.Station;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class StationSupplier {

    public static ArrayList<Station> getStations() {
        return test();
    }

    private static ArrayList<Station> test() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Station> stations = new ArrayList<>();
        db.collection("stations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                stations.add(new Station(document.get("url").toString(), document.get("name").toString(), document.get("description").toString(), document.get("imgURL").toString(), document.get("infoURL").toString()));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return stations;
    }
}
