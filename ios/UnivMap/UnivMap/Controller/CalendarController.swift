//
//  CalendarController.swift
//  UnivMap
//
//  Created by Damien on 02/04/2021.
//

import UIKit
import Mapbox
import FirebaseCore
import FirebaseFirestore

class CalendarController: UIViewController, UITableViewDelegate, UITableViewDataSource, MGLMapViewDelegate {
    
    var db: Firestore!
    @IBOutlet weak var tableView: UITableView!
    
    
    var listPlanning = [Planning]()    //une listes d'objet Planning
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: "cell")
        tableView.delegate = self
        tableView.dataSource = self
        
        
        db = Firestore.firestore()
        
        db.collection("planning").getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
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
                        
                        self.listPlanning.sort(by: self.sortList)
                        
                    }
                    
                }
                self.tableView.reloadData()
            }
        }
        
    }
    
    
    
    
    
    func sortList(this:Planning, that:Planning) -> Bool {
        return Int(this.hDebut)! < Int(that.hDebut)!
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return listPlanning.count
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "CustomCell", for: indexPath) as! CustomCell
        
        cell.nomLabel.text = listPlanning[indexPath.row].nom
        cell.filiereLabel.text = listPlanning[indexPath.row].filiere
        //cell.enseignantLabel.isHidden = true    //pour cacher
        cell.enseignantLabel.text = listPlanning[indexPath.row].enseignant
        cell.hDebutLabel.text = listPlanning[indexPath.row].hDebut + "h" + listPlanning[indexPath.row].mDebut
        cell.hFinLabel.text = listPlanning[indexPath.row].hFin + "h" + listPlanning[indexPath.row].mFin
        cell.salleLabel.text = listPlanning[indexPath.row].salle
        
        return cell
    }
    
    
    
    
    // ================================================================================================
    
    // Affiche le point de repere sur la carte si on appuie sur une cellue table view
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        //tabBarController?.selectedIndex = 0   retourne sur la carte de la tabBarController
        let otherMap = UIViewController()
        
        let horaire = listPlanning[indexPath.row].hDebut + "h" + listPlanning[indexPath.row].mDebut + " - " + listPlanning[indexPath.row].hFin + "h" + listPlanning[indexPath.row].mFin
        
        let indexLatitude:Double? = Double(listPlanning[indexPath.row].latitude)    //convertir en double
        let indexLongitude:Double? = Double(listPlanning[indexPath.row].longitude)
        //print(type(of: indexLatitude))
        
        // Ajout de la carte
        let url = URL(string: "mapbox://styles/mapbox/streets-v11")
        let mapView = MGLMapView(frame: view.bounds, styleURL: url)
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        mapView.setCenter(CLLocationCoordinate2D(latitude: indexLatitude ?? -20.90180283795172, longitude: indexLongitude ?? 55.48438641154759), zoomLevel: 18, animated: false)
        
        mapView.logoView.isHidden = true    //hide lgoo "Mapbox"
        mapView.attributionButton.isHidden = true   //hide button i
        
        otherMap.view.addSubview(mapView)
        show(otherMap, sender: true)
        
        // Enable heading tracking mode so that the arrow will appear.
        //mapView.userTrackingMode = .followWithHeading
        // Enable the permanent heading indicator, which will appear when the tracking mode is not `.followWithHeading`.
        mapView.showsUserHeadingIndicator = true
        mapView.resetNorth()
        
        
        // Si AVANT ajout du marqueur, le pop up apparaitra sans devoir cliquer
        mapView.delegate = self // NE PAS OUBLIER afin d'utiliser Custom Callout Annotation
        
        // Ajout du repere sur la carte
        let annotation = MGLPointAnnotation()
        annotation.coordinate = CLLocationCoordinate2D(latitude: indexLatitude ?? -20.90180283795172, longitude: indexLongitude ?? 55.48438641154759)
        
        annotation.title = listPlanning[indexPath.row].nom + "\n" +  listPlanning[indexPath.row].enseignant + "\n" +  listPlanning[indexPath.row].salle + "\n" + horaire
        
        mapView.addAnnotation(annotation)
        
        
        // Select the annotation so the callout will appear.
        mapView.selectAnnotation(annotation, animated: false, completionHandler: nil)
        
        
        
        // Si APRES ajout du marqueur, on devra appuyer sur le marqueur afin d'afficher pop up
        //mapView.delegate = self // NE PAS OUBLIER afin d'utiliser Custom Callout Annotation
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
    
    // ======================================== Custom annotation ===================================================
    
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
