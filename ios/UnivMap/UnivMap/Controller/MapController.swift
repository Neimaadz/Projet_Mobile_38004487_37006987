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
    var listPlanning: [Planning] = []
    var listNextCours: [Planning] = []
    // Fill an array with point annotations and add it to the map.
    var pointAnnotations = [MGLPointAnnotation]()
    
    
    @IBOutlet var mapView: MGLMapView!
    @IBOutlet weak var buttonPosition: UIImageView!
    @IBOutlet weak var buttonCours: UIImageView!
    @IBOutlet weak var buttonNext: UIImageView!
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let date = Date()
        let calendar = Calendar.current
        let hour = calendar.component(.hour, from: date)
        let minutes = calendar.component(.minute, from: date)
        
        let url = URL(string: "mapbox://styles/mapbox/streets-v11")
        mapView = MGLMapView(frame: view.bounds, styleURL: url)
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        mapView.setCenter(CLLocationCoordinate2D(latitude: -20.90180283795172, longitude: 55.48438641154759), zoomLevel: 16, animated: false)
        
        // Enable heading tracking mode so that the arrow will appear.
        mapView.userTrackingMode = .followWithHeading
        // Enable the permanent heading indicator, which will appear when the tracking mode is not `.followWithHeading`.
        mapView.showsUserHeadingIndicator = true
        mapView.resetNorth()
        mapView.delegate = self
        
        mapView.logoView.isHidden = true    //hide lgoo "Mapbox"
        mapView.attributionButton.isHidden = true   //hide button i
        
        view.addSubview(mapView)
        
        
        db = Firestore.firestore()
        
        db.collection("planning").getDocuments() { [self] (querySnapshot, err) in
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
                        
                        
                // =============== Récupère la liste des prochains cours =======================================
                        let hActuelConverted = (hour * 60 + minutes)
                        let hDebutConverted = (Int(hDebut)! * 60 + Int(mDebut)!)
                        
                        if hActuelConverted <= hDebutConverted && hDebutConverted <= hActuelConverted+120{
                            self.listNextCours.append(Planning(nom: nom, filiere: filiere, enseignant: enseignant, hDebut: hDebut, hFin: hFin, mDebut: mDebut, mFin: mFin, salle: salle, latitude: latitude, longitude: longitude ))
                        }
                // =================================================================================================
                        
                        
                        let horaire = hDebut + "h" + mDebut + " - " + hFin + "h" + mFin
                        
                        let indexLatitude:Double? = Double(latitude)    // Convertir en double
                        let indexLongitude:Double? = Double(longitude)
                        let marker = MGLPointAnnotation()   // Initialisation et ajout de l'annotation
                        
                        marker.coordinate = CLLocationCoordinate2D(latitude: indexLatitude ?? -20.90180283795172, longitude: indexLongitude ?? 55.48438641154759)
                        marker.title = nom + "\n" +  enseignant + "\n" +  salle + "\n" +  horaire
                        
                        //mapView.addAnnotation(marker)
                        // Pour faire apparaitre l'annotation au lancement de l'app
                        //mapView.selectAnnotation(marker, animated: false, completionHandler: nil)
                        
                        pointAnnotations.append(marker)
                    }
                    
                    // Ajout du marqueur sur la map
                    self.mapView.addAnnotations(pointAnnotations)
                    
                }
                
            }
        }
        
        // Bouton centrer sur la localisation de l'utilisateur
        let centerUserLocation = UITapGestureRecognizer(target: self, action: #selector(imageTapped(tapGestureRecognizer:)))
        buttonPosition.isUserInteractionEnabled = true
        buttonPosition.addGestureRecognizer(centerUserLocation)
        view.addSubview(buttonPosition)
        
        
        // Bouton pour switch sur le prochain cours qui va commencer
        self.buttonCours.tintColor = UIColor.orange
        let nextCours = UITapGestureRecognizer(target: self, action: #selector(nextCours(tapGestureRecognizer:)))
        buttonCours.isUserInteractionEnabled = true
        buttonCours.addGestureRecognizer(nextCours)
        view.addSubview(buttonCours)
        
        
        // Bouton pour switch entre tous les cours
        let parcoursCours = UITapGestureRecognizer(target: self, action: #selector(parcoursCours(tapGestureRecognizer:)))
        buttonNext.isUserInteractionEnabled = true
        buttonNext.addGestureRecognizer(parcoursCours)
        view.addSubview(buttonNext)
    }
    
    
    override func viewDidAppear(_ animated: Bool) {
        self.buttonCours.tintColor = UIColor.orange
    }
    
    
    
    
    
    
    
    
    
    
    
    
    // Fonction permettant de trier dans l'ordre croissant hDebut
    func sortList(this:Planning, that:Planning) -> Bool {
        return Int(this.hDebut)! < Int(that.hDebut)!
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        for i in self.pointAnnotations {
            mapView.deselectAnnotation(i, animated: true)
        }
    }
    
    
    // =====================================================================================================
    // ====================           Fonction Paroucrir TOUS les cours                 ====================
    // =====================================================================================================

    var indexParcours = 0
    
    // Fonction pour le parcours de TOUS les cours
    @objc func parcoursCours(tapGestureRecognizer: UITapGestureRecognizer) {
        listPlanning.sort(by: self.sortList)
        
        if(indexParcours == self.pointAnnotations.count){
            indexParcours = 0
        }
        for point in self.pointAnnotations{ // On parcours les différents points des cours
            
            let planningLatitude:Double? = Double(listPlanning[indexParcours].latitude)
            let planningLongitude:Double? = Double(listPlanning[indexParcours].longitude)
            let pointLatitude = point.coordinate.latitude
            let pointLongitude = point.coordinate.longitude
            
            if planningLatitude == pointLatitude && planningLongitude == pointLongitude {   // On test les coordonnées
                
                // On renvoie sur le points correspondant
                mapView.setCenter(CLLocationCoordinate2D(latitude: pointLatitude , longitude: pointLongitude ), zoomLevel: 18, animated: true)
                // Avec son annotation
                mapView.selectAnnotation(point, animated: false, completionHandler: nil)
                
            }
        }
        indexParcours += 1  // Incrémentation du compteur pour parcourir 1 par 1 tout les cours
        
    }
    
    
    
    
    // =====================================================================================================
    // ====================           Fonction Paroucrir les Prochains cours            ====================
    // =====================================================================================================
    
    var indexNext = 0
    
    // Fonction pour parcourir les prochains cours qui vont commencer
    @objc func nextCours(tapGestureRecognizer: UITapGestureRecognizer) {
        listPlanning.sort(by: self.sortList)
        
        let date = Date()
        let calendar = Calendar.current
        let hour = calendar.component(.hour, from: date)
        let minutes = calendar.component(.minute, from: date)
        //print(hour,minutes)
        
        if(indexNext == self.listNextCours.count){
            indexNext = 0
        }
        for (indexPlanning, planning) in self.listPlanning.enumerated(){    // On parcours la liste Planning
            for nextCours in self.listNextCours{    // On parcours la liste des prochain cours qui vont commencer
                for point in self.pointAnnotations{ // On parcours tous les points
                    
                    let hActuel = (hour * 60 + minutes)
                    let hDebutNextCours = (Int(nextCours.hDebut)! * 60 + Int(nextCours.mDebut)!)
                    
                    let planningLatitude:Double? = Double(planning.latitude)
                    let planningLongitude:Double? = Double(planning.longitude)
                    let nextCoursLatitude:Double? = Double(listNextCours[indexNext].latitude)
                    let nextCoursLongitude:Double? = Double(listNextCours[indexNext].longitude)
                    let pointLatitude = point.coordinate.latitude
                    let pointLongitude = point.coordinate.longitude
                    
                    // Si l'heure du début du prochain cours est dans moins de 2 heures par rapport à heure actuel
                    // et que les coordoonées des prochains cours correspondent aux coordonnées des points
                    if hActuel <= hDebutNextCours && hDebutNextCours <= hActuel+120 && nextCoursLatitude == pointLatitude && nextCoursLongitude == pointLongitude {
                        
                        mapView.setCenter(CLLocationCoordinate2D(latitude: pointLatitude , longitude: pointLongitude ), zoomLevel: 18, animated: true)
                        mapView.selectAnnotation(point, animated: false, completionHandler: nil)
                        
                        // Récupère l'indice afin d'avoir le cours qui suit trier dans l'ordre croissant selon l'heure du début
                        if hActuel <= hDebutNextCours && hDebutNextCours <= hActuel+120 && planningLatitude == pointLatitude && planningLongitude == pointLongitude {
                            indexParcours = indexPlanning+1     //on récupère l'indice
                        }
                    }
                    
                }
            }
        }
        indexNext += 1
        
    }
    
    
    // =====================================================================================================
    // ====================           Fonction Center User Location                     ====================
    // =====================================================================================================
    
    //function permettant de centrer sur la position de l'utilisateur
    @objc func imageTapped(tapGestureRecognizer: UITapGestureRecognizer){
        mapView.userTrackingMode = .follow
    }
    
    
    
    
    

    // =====================================================================================================
    // ====================                 Fonction Mapbox                             ====================
    // =====================================================================================================
    
    func mapView(_ mapView: MGLMapView, viewFor annotation: MGLAnnotation) -> MGLAnnotationView? {
        
        // ======================= User Location =============================================
        if annotation is MGLUserLocation && mapView.userLocation != nil {
            return CustomUserLocationAnnotationView()
        }
        
        // ======================= Annotation ============================================
        guard annotation is MGLPointAnnotation else {
            return nil
        }

        // Use the point annotation’s longitude value (as a string) as the reuse identifier for its view.
        let reuseIdentifier = "\(annotation.coordinate.longitude)"

        // For better performance, always try to reuse existing annotations.
        var annotationView = mapView.dequeueReusableAnnotationView(withIdentifier: reuseIdentifier)
        
        if annotationView == nil {
            annotationView = CustomAnnotationView(reuseIdentifier: reuseIdentifier)
            annotationView!.bounds = CGRect(x: 0, y: 0, width: 25, height: 25)  // création du cercle
            
            
            let date = Date()
            let calendar = Calendar.current
            let hour = calendar.component(.hour, from: date)
            let minutes = calendar.component(.minute, from: date)
            
            // ============================= Couleur des points ===============================
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
        // Instancie et on retourne l'annotation personnaliser
        return CustomCalloutView(representedObject: annotation)
    }
     
    func mapView(_ mapView: MGLMapView, tapOnCalloutFor annotation: MGLAnnotation) {
        // Optionally handle taps on the callout.
        print("Tapped the callout for: \(annotation)")
     
        // Hide the callout.
        mapView.deselectAnnotation(annotation, animated: true)
    }
    
}

