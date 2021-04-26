package com.cherrierlaoussing.univmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {

            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
                List<Circle> circleIconList = new ArrayList<>();

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

                                circle = circleManager.create(new CircleOptions()
                                        .withLatLng(new LatLng(planningLatitude, planningLongitude))
                                        .withCircleColor(ColorUtils.colorToRgbaString(Color.RED))   // Couleur du cercle
                                        .withCircleRadius(10f)  // Taille du cercle
                                        .withCircleStrokeColor(ColorUtils.colorToRgbaString(Color.WHITE))   // Couleur du conteur
                                        .withCircleStrokeWidth(3f)  // Taille du conteur
                                        .withDraggable(false)
                                );
                                circleIconList.add(circle);
                            }
                            else if(hFinConvert < hActuelConvert){  //Si des cours sont finis
                                circleManager = new CircleManager(mapView, mapboxMap, style);

                                circle = circleManager.create(new CircleOptions()
                                        .withLatLng(new LatLng(planningLatitude, planningLongitude))
                                        .withCircleColor(ColorUtils.colorToRgbaString(Color.LTGRAY))
                                        .withCircleRadius(10f)
                                        .withCircleStrokeColor(ColorUtils.colorToRgbaString(Color.WHITE))
                                        .withCircleStrokeWidth(3f)
                                        .withDraggable(false)
                                );
                                circleIconList.add(circle);
                            }
                            else if(hActuelConvert <= hDebutConvert && hDebutConvert <= hActuelConvert+120){    //Si des cours commencent dans 2 heures
                                circleManager = new CircleManager(mapView, mapboxMap, style);

                                circle = circleManager.create(new CircleOptions()
                                        .withLatLng(new LatLng(planningLatitude, planningLongitude))
                                        .withCircleColor(ColorUtils.colorToRgbaString(Color.YELLOW))
                                        .withCircleRadius(10f)
                                        .withCircleStrokeColor(ColorUtils.colorToRgbaString(Color.WHITE))
                                        .withCircleStrokeWidth(3f)
                                        .withDraggable(false)
                                );
                                circleIconList.add(circle);
                            }
                            else{
                                circleManager = new CircleManager(mapView, mapboxMap, style);

                                circle = circleManager.create(new CircleOptions()
                                        .withLatLng(new LatLng(planningLatitude, planningLongitude))
                                        .withCircleColor(ColorUtils.colorToRgbaString(Color.BLUE))
                                        .withCircleRadius(10f)
                                        .withCircleStrokeColor(ColorUtils.colorToRgbaString(Color.WHITE))
                                        .withCircleStrokeWidth(3f)
                                        .withDraggable(false)
                                );
                                circleIconList.add(circle);
                            }


                            // Initialisation de l'annotation
                            markerViewManager = new MarkerViewManager(mapView, mapboxMap);


                            // Ajout du click Lister et change l'annotation quand on click
                            circleManager.addClickListener(new OnCircleClickListener() {
                                @Override
                                public boolean onAnnotationClick(Circle circle) {

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
                                    markerViewManager.addMarker(markerView);

                                    customView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            markerViewManager.removeMarker(markerView);
                                        }

                                    });


                                    return false;
                                }
                            }); //[FIN circleManager.addClickListener]


                        } //[FIN BOUCLE for]

                    } //[FIN DATA CALLBACK]
                }); //[FIN getAllDATA]



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





}