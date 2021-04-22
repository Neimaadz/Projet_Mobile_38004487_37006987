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
        mapView.setCenter(CLLocationCoordinate2D(latitude: -20.90180283795172, longitude: 55.48438641154759), zoomLevel: 16, animated: false)
        
        
        // Enable heading tracking mode so that the arrow will appear.
        mapView.userTrackingMode = .followWithHeading
        // Enable the permanent heading indicator, which will appear when the tracking mode is not `.followWithHeading`.
        mapView.showsUserHeadingIndicator = true
        mapView.resetNorth()
        mapView.delegate = self
        view.addSubview(mapView)
        
        
        db = Firestore.firestore()
        
        db.collection("planning").getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                // Fill an array with point annotations and add it to the map.
                var pointAnnotations = [MGLPointAnnotation]()
                
                for document in querySnapshot!.documents {
                    if let nom = document.data()["nom"] as? String,         //recup data selon clé
                        let filiere = document.data()["filiere"] as? String,
                        let enseignant = document.data()["enseignant"] as? String,
                        let hDebut = document.data()["hDebut"] as? String,
                        let hFin = document.data()["hFin"] as? String,
                        let mDebut = document.data()["mDebut"] as? String,
                        let mFin = document.data()["mFin"] as? String,
                        let salle = document.data()["salle"] as? String,
                        let latitude = document.data()["latitude"] as? String,
                        let longitude = document.data()["longitude"] as? String{
                    
                        self.listPlanning.append(Planning(nom: nom, filiere: filiere, enseignant: enseignant, hDebut: hDebut, hFin: hFin, mDebut: mDebut, mFin: mFin, salle: salle, latitude: latitude, longitude: longitude ))
                        
                        
                        let horaire = hDebut + "h" + mDebut + " - " + hFin + "h" + mFin
                        
                        let indexLatitude:Double? = Double(latitude)    //convertir en double
                        let indexLongitude:Double? = Double(longitude)
                        // Initialize and add the marker annotation.
                        let marker = MGLPointAnnotation()
                        
                        marker.coordinate = CLLocationCoordinate2D(latitude: indexLatitude ?? -20.90180283795172, longitude: indexLongitude ?? 55.48438641154759)
                        marker.title = nom + "\n" +  enseignant + "\n" +  salle + "\n" +  horaire
                        
                        //mapView.addAnnotation(marker)
                        // Pour faire apparaitre l'annotation au lancement de l'app
                        //mapView.selectAnnotation(marker, animated: false, completionHandler: nil)
                        
                        pointAnnotations.append(marker)
                    }
                    
                    // Add marker to the map.
                    mapView.addAnnotations(pointAnnotations)
                    
                }
                
            }
        }
        
    }
    
    
    
    
    
    
    func sortList(this:Planning, that:Planning) -> Bool {
        return Int(this.hDebut)! < Int(that.hDebut)!
    }
    
    
    
    // ======================================== User Location ===================================================
    
    func mapView(_ mapView: MGLMapView, viewFor annotation: MGLAnnotation) -> MGLAnnotationView? {
        // Substitute our custom view for the user location annotation. This custom view is defined below.
        if annotation is MGLUserLocation && mapView.userLocation != nil {
            return CustomUserLocationAnnotationView()
        }
        //return nil
        
        
        
        //This example is only concerned with point annotations.
        guard annotation is MGLPointAnnotation else {
            return nil
        }

        // Use the point annotation’s longitude value (as a string) as the reuse identifier for its view.
        let reuseIdentifier = "\(annotation.coordinate.longitude)"

        // For better performance, always try to reuse existing annotations.
        var annotationView = mapView.dequeueReusableAnnotationView(withIdentifier: reuseIdentifier)
        
        if annotationView == nil {
            annotationView = CustomAnnotationView(reuseIdentifier: reuseIdentifier)
            annotationView!.bounds = CGRect(x: 0, y: 0, width: 25, height: 25)
            
            
            let date = Date()
            let calendar = Calendar.current
            let hour = calendar.component(.hour, from: date)
            let minutes = calendar.component(.minute, from: date)
            //print(hour,minutes)

            for planning in self.listPlanning{
                
                let hActuel = (hour * 60 + minutes)
                let hDebut = (Int(planning.hDebut)! * 60 + Int(planning.mDebut)!)
                let hFin = (Int(planning.hFin)! * 60 + Int(planning.mFin)!)
                
                if(hDebut  <= hActuel && hActuel <= hFin) {   //Si des cours se passe acutuellement = Rouge
                    let color = UIColor.red
                    annotationView!.backgroundColor = color
                }
                else if(hFin < hActuel){    //Si des cours est passé = Gris
                    let color = UIColor.lightGray
                    annotationView!.backgroundColor = color
                }
                else if(hActuel <= hDebut && hDebut <= hActuel+120){   //Si des cours commencent dans 2 heures = Orange
                    let color = UIColor.orange
                    annotationView!.backgroundColor = color
                }
                else{
                    let color = UIColor.blue
                    annotationView!.backgroundColor = color
                }
            }
        }
        
        return annotationView
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

