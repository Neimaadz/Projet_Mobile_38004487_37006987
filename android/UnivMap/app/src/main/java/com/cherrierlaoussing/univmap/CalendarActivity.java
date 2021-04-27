package com.cherrierlaoussing.univmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.model.univmap.Planning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CalendarActivity extends AppCompatActivity implements MyAdapter.OnNoteListener {

    BottomNavigationView navBar;

    private FirebaseFirestore db;
    private static final String TAG = "CalendarActivity";
    private Context context = this;
    private MyAdapter.OnNoteListener thisOnNoteListener = this::onNoteClick;

    RecyclerView recyclerViewPlanning;

    String nomPlanning[] = {}, enseignantPlanning[] = {}, filierePlanning[] = {}, sallePlanning[] = {}, horaireDebutPlanning[] = {}, horaireFinPlanning[] = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        db = FirebaseFirestore.getInstance();

        /// =============================== TabBar =============================
        navBar = findViewById(R.id.tabBar);

        navBar.setSelectedItemId(R.id.calendar);

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });




        getAllData(new ListPlanningCallback() {
            @Override
            public void onCallback(List<Planning> listPlanning) {
                recyclerViewPlanning = findViewById(R.id.recyclerViewPlanning);

                for (int i = 0; i < listPlanning.size(); i++) {
                    Collections.sort(listPlanning, new SortHdebut() );

                    String nom = listPlanning.get(i).getNom();
                    String filiere = listPlanning.get(i).getFiliere();
                    String enseignant = listPlanning.get(i).getEnseignant();
                    String salle = listPlanning.get(i).getSalle();
                    String hDebut = listPlanning.get(i).getHdebut();
                    String hFin = listPlanning.get(i).getHfin();
                    String mDebut = listPlanning.get(i).getMdebut();
                    String mFin = listPlanning.get(i).getMfin();
                    String horaireDebut = hDebut + ":" + mDebut;
                    String horaireFin = hFin + ":" + mFin;

                    nomPlanning = Arrays.copyOf(nomPlanning, nomPlanning.length+1); // On ajoute au tableau nomPlanning
                    nomPlanning[nomPlanning.length-1] = nom;

                    enseignantPlanning = Arrays.copyOf(enseignantPlanning, enseignantPlanning.length+1);
                    enseignantPlanning[enseignantPlanning.length-1] = enseignant;

                    filierePlanning = Arrays.copyOf(filierePlanning, filierePlanning.length+1);
                    filierePlanning[filierePlanning.length-1] = filiere;

                    sallePlanning = Arrays.copyOf(sallePlanning, sallePlanning.length+1);
                    sallePlanning[sallePlanning.length-1] = salle;

                    horaireDebutPlanning = Arrays.copyOf(horaireDebutPlanning, horaireDebutPlanning.length+1);
                    horaireDebutPlanning[horaireDebutPlanning.length-1] = horaireDebut;

                    horaireFinPlanning = Arrays.copyOf(horaireFinPlanning, horaireFinPlanning.length+1);
                    horaireFinPlanning[horaireFinPlanning.length-1] = horaireFin;

                    // Appel de la classe MyAdapter pour afficher et mettre "en forme" les infos des cours
                    MyAdapter myAdapter = new MyAdapter(context, nomPlanning, filierePlanning, enseignantPlanning, sallePlanning,
                            horaireDebutPlanning, horaireFinPlanning, thisOnNoteListener);
                    recyclerViewPlanning.setAdapter(myAdapter);
                    recyclerViewPlanning.setLayoutManager(new LinearLayoutManager(context));


                }
            }

        });




    }







    @Override
    public void onNoteClick(int position) {
        System.out.println(nomPlanning[position]);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("indiceCalendar", position);
        intent.putExtra("comeFromCalendar", 1); // permet de récuperer la valeur et comparer afin de savoir si on vient de calendar
        startActivityForResult(intent, 0);

    }





        /*
    ===========================================================================================================================
    ===============                  Récupération des données de firecore                                       ===============
    ===========================================================================================================================
     */

    // Fonction pour récupéerer les données sur Firecore en Callback
    public void getAllData(ListPlanningCallback listPlanningCallback) {
        db.collection("planning")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Planning> listLocal = new ArrayList<Planning>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());

                                String nom = document.getString("nom");
                                String filiere = document.getString("filiere");
                                String enseignant = document.getString("enseignant");
                                String hDebut = document.getString("hDebut");
                                String hFin = document.getString("hFin");
                                String mDebut = document.getString("mDebut");
                                String mFin = document.getString("mFin");
                                String salle = document.getString("salle");
                                String latitude = document.getString("latitude");
                                String longitude = document.getString("longitude");

                                Planning planning = new Planning(nom, filiere, enseignant, hDebut, hFin, mDebut, mFin, salle, latitude, longitude);
                                listLocal.add(planning);

                            }
                            listPlanningCallback.onCallback(listLocal);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    public interface ListPlanningCallback {
        void onCallback(List<Planning> listPlanning);
    }




    /*
    ===========================================================================================================================
    ===============               Classe pour trier                                                             ===============
    ===========================================================================================================================
    */

    private class SortHdebut implements Comparator<Planning> {
        public int compare(Planning a, Planning b) {
            return Integer.parseInt(a.getHdebut()) - Integer.parseInt(b.getHdebut());
        }
    }


}