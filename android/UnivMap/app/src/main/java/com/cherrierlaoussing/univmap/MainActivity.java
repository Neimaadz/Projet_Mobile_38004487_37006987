package com.cherrierlaoussing.univmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Circle;
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager;
import com.mapbox.mapboxsdk.plugins.annotation.CircleOptions;
import com.mapbox.mapboxsdk.plugins.annotation.OnCircleClickListener;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.model.univmap.Planning;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;



public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

    BottomNavigationView navBar;

    private FirebaseFirestore db;

    private MapView mapView;
    private MapboxMap mapboxMap;
    private static final String TAG = "MainActivity";
    private MarkerView markerView;
    private MarkerViewManager markerViewManager;
    private CircleManager circleManager;
    private Circle circle;

    private PermissionsManager permissionsManager;


    int indexParcours = 0;
    int indexNext = 0;

    List<Circle> circleIconList = new ArrayList<>();

    //========================  Language App & Data Persist ========================
    private SharedPreferences preferences;
    private Boolean language;
    private String currentLanguage, appLanguage;

    //========================  Activity ========================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //========================  Language App & Data Persist ========================

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        language = preferences.getBoolean("defaultLanguage", true);
        currentLanguage = LanguageActivity.currentLanguage();
        //System.out.println(language);     //test
        if(language){
            SharedPreferences.Editor editor = this.preferences.edit();
            editor.putBoolean("defaultLanguage", true);
            editor.putString("appLanguage",currentLanguage );
            editor.commit();
        }else{
            appLanguage = preferences.getString("appLanguage", currentLanguage);
            LanguageActivity.setLocale(this, appLanguage);
        }
        //System.out.println(preferences.getString("appLanguage", currentLanguage));        //test

        //==============================================================================

        db = FirebaseFirestore.getInstance();

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        /// =============================== TabBar =============================
        navBar = findViewById(R.id.tabBar);

        navBar.setSelectedItemId(R.id.map);

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

    }



    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MainActivity.this.mapboxMap = mapboxMap;

        // Affichage du style de la carte
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {

            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);

                getAllData(new ListPlanningCallback() {
                    @Override
                    public void onCallback(List<Planning> listPlanning) {

                        Date date = new Date();
                        Calendar calendar = GregorianCalendar.getInstance();
                        calendar.setTime(date);   // assigns calendar to given date
                        int hours = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
                        int minutes = calendar.get(Calendar.MINUTE);


                        for (int i = 0; i < listPlanning.size(); i++) {
                            int hActuelConvert = (hours * 60 + minutes);    // Converti heure actuel en minutes

                            Double planningLatitude = Double.parseDouble(listPlanning.get(i).getLatitude());
                            Double planningLongitude = Double.parseDouble(listPlanning.get(i).getLongitude());

                            String nom = listPlanning.get(i).getNom();
                            String enseignant = listPlanning.get(i).getEnseignant();
                            String salle = listPlanning.get(i).getSalle();
                            String hDebut = listPlanning.get(i).getHdebut();
                            String hFin = listPlanning.get(i).getHfin();
                            String mDebut = listPlanning.get(i).getMdebut();
                            String mFin = listPlanning.get(i).getMfin();
                            String horaire = hDebut + "h" + mDebut + " - " + hFin + "h" + mFin;

                            int hDebutConvert = Integer.parseInt(listPlanning.get(i).getHdebut()) * 60 + Integer.parseInt(listPlanning.get(i).getMdebut());
                            int hFinConvert = Integer.parseInt(listPlanning.get(i).getHfin()) * 60 + Integer.parseInt(listPlanning.get(i).getMfin());

                            if(hDebutConvert <= hActuelConvert && hActuelConvert <= hFinConvert){   // Si des cours se passe acutuellement
                                // Set up a SymbolManager instance
                                circleManager = new CircleManager(mapView, mapboxMap, style);

                                circle = CreateCircle(planningLatitude, planningLongitude, Color.RED);  // Appl de la fonction pour créer les points en couleur
                                circleIconList.add(circle);
                            }
                            else if(hFinConvert < hActuelConvert){  //Si des cours sont finis
                                circleManager = new CircleManager(mapView, mapboxMap, style);

                                circle = CreateCircle(planningLatitude, planningLongitude, Color.LTGRAY);
                                circleIconList.add(circle);
                            }
                            else if(hActuelConvert <= hDebutConvert && hDebutConvert <= hActuelConvert+120){    //Si des cours commencent dans 2 heures
                                circleManager = new CircleManager(mapView, mapboxMap, style);

                                circle = CreateCircle(planningLatitude, planningLongitude, Color.YELLOW);
                                circleIconList.add(circle);
                            }
                            else{
                                circleManager = new CircleManager(mapView, mapboxMap, style);

                                circle = CreateCircle(planningLatitude, planningLongitude, Color.BLUE);
                                circleIconList.add(circle);
                            }

                            // Initialisation de l'annotation
                            markerViewManager = new MarkerViewManager(mapView, mapboxMap);


                            // Ajout du click Lister et change l'annotation quand on click
                            circleManager.addClickListener(new OnCircleClickListener() {
                                @Override
                                public boolean onAnnotationClick(Circle circle) {
                                    markerView = annotations(nom, enseignant, salle, horaire, planningLatitude, planningLongitude);
                                    markerViewManager.addMarker(markerView);

                                    return false;
                                }
                            }); //[FIN circleManager.addClickListener]


                        } //[FIN BOUCLE for]




                    } //[FIN DATA CALLBACK]
                }); //[FIN getAllDATA]



                buttonParcoursCours();
                buttonNextCours();

                int comeFromCalendar = getIntent().getIntExtra("comeFromCalendar", 0);  // Récupère la valeur depuis CalendarActivity

                if(comeFromCalendar == 1){  // Si on vient de Calendar
                    displayCoursFromCalendar();
                }


            } //[FIN OnStyleLoad]

        });




    }




    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (markerViewManager != null) {
            markerViewManager.onDestroy();
        }
        mapView.onDestroy();
    }











    /*
    ===========================================================================================================================
    ===============                       Fonction Récupération de TOUS les cours                               ===============
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
   ===============                       Fonction Récupération des prochains cours                             ===============
   ===========================================================================================================================
    */

    // Fonction pour récupèrer sur Firecore les prochains cours qui vont commencer en Callback
    public void getAllNextCours(ListNextCoursCallback listNextCoursCallback) {
        db.collection("planning")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        Date date = new Date();
                        Calendar calendar = GregorianCalendar.getInstance();
                        calendar.setTime(date);
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);

                        if (task.isSuccessful()) {
                            List<Planning> listLocal = new ArrayList<Planning>();

                            for (QueryDocumentSnapshot document : task.getResult()) {

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


                                int hActuelConvert = (hours * 60 + minutes);
                                int hDebutConvert = (Integer.parseInt(hDebut) * 60 + Integer.parseInt(mDebut));

                                if(hActuelConvert <= hDebutConvert && hDebutConvert <= hActuelConvert+120){
                                    Planning planning = new Planning(nom, filiere, enseignant, hDebut, hFin, mDebut, mFin, salle, latitude, longitude);
                                    listLocal.add(planning);
                                }

                            }
                            listNextCoursCallback.onCallback(listLocal);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public interface ListNextCoursCallback {
        void onCallback(List<Planning> listNextCours);
    }





    /*
   ===========================================================================================================================
   ===============                     Fonction Bouton pour centrer sur User Position                          ===============
   ===========================================================================================================================
    */

    // Fonction pour centrer sur la position de l'utilisateur
    public void centerUserLocation(){
        ImageButton userLocation = (ImageButton) findViewById(R.id.userLocation);
        LatLng latLngUser = new LatLng(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude(),
                                        mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude());

        userLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPosition position = new CameraPosition.Builder()
                        .target(latLngUser)
                        .zoom(18)
                        .tilt(0)   // inclinaison de la camera max:60
                        .build();

                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 500);
            }
        });

    }


    /*
   ===========================================================================================================================
   ===============             Fonction Affichage du cours selectionner sur Calendar                           ===============
   ===========================================================================================================================
    */

    public void displayCoursFromCalendar(){

        getAllData(new ListPlanningCallback() {
            @Override
            public void onCallback(List<Planning> listPlanning) {
                Collections.sort(listPlanning, new SortHdebut() );

                int indexCalendar = getIntent().getIntExtra("indiceCalendar", 0);

                for (int i = 0; i < listPlanning.size(); i++) {

                    Double calendarLatitude = Double.parseDouble(listPlanning.get(indexCalendar).getLatitude());
                    Double calendarLongitude = Double.parseDouble(listPlanning.get(indexCalendar).getLongitude());

                    String nomCalendar = listPlanning.get(indexCalendar).getNom();
                    String enseignantCalendar = listPlanning.get(indexCalendar).getEnseignant();
                    String salleCalendar = listPlanning.get(indexCalendar).getSalle();
                    String hDebutCalendar = listPlanning.get(indexCalendar).getHdebut();
                    String hFinCalendar = listPlanning.get(indexCalendar).getHfin();
                    String mDebutCalendar = listPlanning.get(indexCalendar).getMdebut();
                    String mFinCalendar = listPlanning.get(indexCalendar).getMfin();
                    String horaireCalendar = hDebutCalendar + "h" + mDebutCalendar + " - " + hFinCalendar + "h" + mFinCalendar;

                    LatLng latLngCalendar = new LatLng(calendarLatitude, calendarLongitude);

                    CameraPosition position = new CameraPosition.Builder()
                            .target(latLngCalendar)
                            .zoom(18)
                            .tilt(0)   // inclinaison de la camera max:60
                            .build();

                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 500);

                    markerView = annotations(nomCalendar, enseignantCalendar, salleCalendar, horaireCalendar, calendarLatitude, calendarLongitude);
                    markerViewManager.addMarker(markerView);
                }
            }
        });


    }




    /*
   ===========================================================================================================================
   ===============                     Fonction Bouton pour parcours TOUS les cours                            ===============
   ===========================================================================================================================
    */

    // Fonction pour parcourir tout les cours avec un Image Button
    public void buttonParcoursCours(){
        List <Circle> circleIconList = this.circleIconList;
        ImageButton parcoursCours = (ImageButton) findViewById(R.id.parcoursCrous);
        //System.out.println(circleIconList.size());

        parcoursCours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllData(new ListPlanningCallback() {
                    @Override
                    public void onCallback(List<Planning> listPlanning) {

                        if(indexParcours == circleIconList.size()){
                            indexParcours = 0;
                        }
                        for (int i=0 ; i<circleIconList.size(); i++){
                            Collections.sort(listPlanning, new SortHdebut() );

                            double pointLatitude = circleIconList.get(i).getLatLng().getLatitude();
                            double pointLongitude = circleIconList.get(i).getLatLng().getLongitude();

                            double planningLatitude = Double.parseDouble(listPlanning.get(indexParcours).getLatitude());
                            double planningLongitude = Double.parseDouble(listPlanning.get(indexParcours).getLongitude());

                            String nom = listPlanning.get(indexParcours).getNom();
                            String enseignant = listPlanning.get(indexParcours).getEnseignant();
                            String salle = listPlanning.get(indexParcours).getSalle();
                            String hDebut = listPlanning.get(indexParcours).getHdebut();
                            String hFin = listPlanning.get(indexParcours).getHfin();
                            String mDebut = listPlanning.get(indexParcours).getMdebut();
                            String mFin = listPlanning.get(indexParcours).getMfin();
                            String horaire = hDebut + "h" + mDebut + " - " + hFin + "h" + mFin;

                            if (planningLatitude == pointLatitude && planningLongitude == pointLongitude){

                                CameraPosition position = new CameraPosition.Builder()
                                        .target(circleIconList.get(i).getLatLng())
                                        .zoom(18)
                                        .tilt(0)   // inclinaison de la camera max:60
                                        .build();

                                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 500);

                                MarkerView markerView = annotations(nom, enseignant, salle, horaire, pointLatitude, pointLongitude);
                                markerViewManager.addMarker(markerView);

                            }
                        } //[FIN boucle FOR]
                        indexParcours += 1;

                    }   //[FIN DATA CALLBACK]
                }); //[FIN getAllDATA]


            } //[FIN onClick]
        });


    }




    /*
   ===========================================================================================================================
   ===============            Fonction Bouton parcours les PROCHAINS cours qui vont commencer                  ===============
   ===========================================================================================================================
    */

    // Fonction pour aller à la position le prochain cours
    public void buttonNextCours(){
        List <Circle> circleIconList = this.circleIconList;
        ImageButton nextCours = (ImageButton) findViewById(R.id.nextCours);

        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        nextCours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // On récupère la listes de tous les cours
                getAllData(new ListPlanningCallback() {
                    @Override
                    public void onCallback(List<Planning> listPlanning) {
                        Collections.sort(listPlanning, new SortHdebut() );

                        // On récupère la liste des cours qui vont commencer
                        getAllNextCours(new ListNextCoursCallback() {
                            @Override
                            public void onCallback(List<Planning> listNextCours) {

                                if(indexNext == listNextCours.size()){
                                    indexNext = 0;
                                }
                                for(int i=0 ; i<listPlanning.size(); i++){
                                    for(int j=0 ; j<listNextCours.size(); j++){
                                        for (int k=0 ; k<circleIconList.size(); k++){

                                            int hActuelConvert = (hours * 60 + minutes);
                                            int hDebutNextCours = (Integer.parseInt(listNextCours.get(j).getHdebut()) * 60 + Integer.parseInt(listNextCours.get(j).getMdebut()) );

                                            double planningLatitude = Double.parseDouble( listPlanning.get(i).getLatitude() );
                                            double planningLongitude = Double.parseDouble(listPlanning.get(i).getLongitude() );
                                            double coursActuelLatitude = Double.parseDouble( listNextCours.get(indexNext).getLatitude() );
                                            double coursActuelLongitude = Double.parseDouble( listNextCours.get(indexNext).getLongitude() );
                                            double pointLatitude = circleIconList.get(k).getLatLng().getLatitude();
                                            double pointLongitude = circleIconList.get(k).getLatLng().getLongitude();

                                            String nom = listNextCours.get(indexNext).getNom();
                                            String enseignant = listNextCours.get(indexNext).getEnseignant();
                                            String salle = listNextCours.get(indexNext).getSalle();
                                            String hDebut = listNextCours.get(indexNext).getHdebut();
                                            String hFin = listNextCours.get(indexNext).getHfin();
                                            String mDebut = listNextCours.get(indexNext).getMdebut();
                                            String mFin = listNextCours.get(indexNext).getMfin();
                                            String horaire = hDebut + "h" + mDebut + " - " + hFin + "h" + mFin;

                                            if (hActuelConvert <= hDebutNextCours && hDebutNextCours <= hActuelConvert+120 && coursActuelLatitude == pointLatitude &&
                                            coursActuelLongitude == pointLongitude){

                                                CameraPosition position = new CameraPosition.Builder()
                                                        .target(circleIconList.get(k).getLatLng())
                                                        .zoom(18)
                                                        .tilt(0)   // inclinaison de la camera max:60
                                                        .build();

                                                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 500);

                                                MarkerView markerView = annotations(nom, enseignant, salle, horaire, pointLatitude, pointLongitude);
                                                markerViewManager.addMarker(markerView);

                                                if (hActuelConvert <= hDebutNextCours && hDebutNextCours <= hActuelConvert+120 && planningLatitude == pointLatitude &&
                                                        planningLongitude == pointLongitude){

                                                    indexParcours = i+1;
                                                }
                                            }



                                        } //[FIN boucle FOR circleIconList]

                                    } //[FIN boucle FOR listPlanningActuel]

                                } //[FIN boucle FOR listPlanning]
                                indexNext += 1;

                            }
                        });

                    }   //[FIN DATA CALLBACK]
                }); //[FIN getAllDATA]


            } //[FIN onClick]
        });


    }




    /*
   ===========================================================================================================================
   ===============                      Fonction Création des cercles                                          ===============
   ===========================================================================================================================
    */

    // Fonction pour créer les points coloré en cercle
    public Circle CreateCircle(double latitude, double longitude, int color ){

        circle = circleManager.create(new CircleOptions()
                .withLatLng(new LatLng(latitude, longitude))
                .withCircleColor(ColorUtils.colorToRgbaString(color))
                .withCircleRadius(10f)
                .withCircleStrokeColor(ColorUtils.colorToRgbaString(Color.WHITE))
                .withCircleStrokeWidth(3f)
                .withDraggable(false)
        );
        return circle;
    }




    /*
   ===========================================================================================================================
   ===============               Fonction Création de View pour l'affichage de l'annotation                    ===============
   ===========================================================================================================================
    */

    // Fonction pour ajouter les annotations des points avec les infos(nom, salle...)
    public MarkerView annotations(String nom, String enseignant, String salle, String horaire, double planningLatitude, double planningLongitude){

        // Supprime l'annotation à quand on clique sur un point différent
        markerViewManager.removeMarker(markerView);

        // Use an XML layout to create a View object
        View customView = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_marker_view, null);
        customView.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        // Set the View's TextViews with content
        TextView planningNom = customView.findViewById(R.id.planning_nom);
        planningNom.setText(nom);

        TextView planningEnseignant = customView.findViewById(R.id.planning_enseignant);
        planningEnseignant.setText(enseignant);

        TextView planningSalle = customView.findViewById(R.id.planning_salle);
        planningSalle.setText(salle);

        TextView planningHoraire = customView.findViewById(R.id.planning_horaire);
        planningHoraire.setText(horaire);

        // Use the View to create a MarkerView which will eventually be given to
        // the plugin's MarkerViewManager class
        markerView = new MarkerView(new LatLng(planningLatitude, planningLongitude), customView);

        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markerViewManager.removeMarker(markerView);
            }
        });
        return markerView;
    }




     /*
    ===========================================================================================================================
    ===============                           Géolocalisation                                                   ===============
    ===========================================================================================================================
     */

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Enable the most basic pulsing styling by ONLY using
            // the `.pulseEnabled()` method
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .pulseEnabled(true)
                    .build();

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.NORMAL);

            centerUserLocation();   // Appel de la fonction permettant de centrer sur l'utilisateur

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
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















