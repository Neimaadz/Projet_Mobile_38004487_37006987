//
//  ViewController.swift
//  UnivMap
//
//  Created by Damien on 02/04/2021.
//

import UIKit
import Mapbox
import FirebaseCore
import FirebaseFirestore


class MapController: UIViewController, MGLMapViewDelegate {
    
    var db: Firestore!
    var listPlanning = [Planning]()
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let url = URL(string: "mapbox://styles/mapbox/streets-v11")
        let mapView = MGLMapView(frame: view.bounds, styleURL: url)
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        mapView.setCenter(CLLocationCoordinate2D(latitude: -20.90180283795172, longitude: 55.48438641154759), zoomLevel: 15, animated: false)
        
        
        // Enable heading tracking mode so that the arrow will appear.
        mapView.userTrackingMode = .followWithHeading
        // Enable the permanent heading indicator, which will appear when the tracking mode is not `.followWithHeading`.
        mapView.showsUserHeadingIndicator = true
        mapView.resetNorth()
        
        
        
        view.addSubview(mapView)
        
        db = Firestore.firestore()
        
        db.collection("planning").getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                for document in querySnapshot!.documents {
                    if let nom = document.data()["nom"] as? String,         //recup data selon clé
                        let filiere = document.data()["filiere"] as? String,
                        let enseignant = document.data()["enseignant"] as? String,
                        let horaire = document.data()["horaire"] as? String,
                        let salle = document.data()["salle"] as? String,
                        let latitude = document.data()["latitude"] as? String,
                        let longitude = document.data()["longitude"] as? String{
                    
                        self.listPlanning.append(Planning(nom: nom, filiere: filiere, enseignant: enseignant, horaire: horaire, salle: salle, latitude: latitude, longitude: longitude ))
                    }
                    
                    
                    for planning in self.listPlanning {
                        
                        let indexLatitude:Double? = Double(planning.latitude)    //convertir en double
                        let indexLongitude:Double? = Double(planning.longitude)
                        // Initialize and add the marker annotation.
                        let marker = MGLPointAnnotation()
                        marker.coordinate = CLLocationCoordinate2D(latitude: indexLatitude ?? -20.90180283795172, longitude: indexLongitude ?? 55.48438641154759)
                        marker.title = planning.nom + "\n" +  planning.enseignant + "\n" +  planning.salle + "\n" +  planning.horaire
                        
                        // Add marker to the map.
                        mapView.addAnnotation(marker)
                        
                        // Select the annotation so the callout will appear.
                        mapView.selectAnnotation(marker, animated: false, completionHandler: nil)
                    }
                    
                }
                
                mapView.delegate = self
            }
        }
        
        
    }
    
    
    
    
    
    
    
    
    
    
    
    // ======================================== User Location ===================================================
    
    func mapView(_ mapView: MGLMapView, viewFor annotation: MGLAnnotation) -> MGLAnnotationView? {
        // Substitute our custom view for the user location annotation. This custom view is defined below.
        if annotation is MGLUserLocation && mapView.userLocation != nil {
            return CustomUserLocationAnnotationView()
        }
        return nil
    }
     
    // Optional: tap the user location annotation to toggle heading tracking mode.
    func mapView(_ mapView: MGLMapView, didSelect annotation: MGLAnnotation) {
        if annotation is MGLUserLocation {
            if mapView.userTrackingMode != .followWithHeading {
                mapView.userTrackingMode = .followWithHeading
            } else {
                mapView.resetNorth()
            }
            // We're borrowing this method as a gesture recognizer, so reset selection state.
            mapView.deselectAnnotation(annotation, animated: false)
        }
    }
    
    
    // ======================================== Custom Annotation ===================================================
    
    func mapView(_ mapView: MGLMapView, annotationCanShowCallout annotation: MGLAnnotation) -> Bool {
        // Only show callouts for `Hello world!` annotation.
        return true
    }
     
    func mapView(_ mapView: MGLMapView, calloutViewFor annotation: MGLAnnotation) -> MGLCalloutView? {
        // Instantiate and return our custom callout view.
        return CustomCalloutView(representedObject: annotation)
    }
     
    func mapView(_ mapView: MGLMapView, tapOnCalloutFor annotation: MGLAnnotation) {
        // Optionally handle taps on the callout.
        print("Tapped the callout for: \(annotation)")
     
        // Hide the callout.
        mapView.deselectAnnotation(annotation, animated: true)
    }
    
}

