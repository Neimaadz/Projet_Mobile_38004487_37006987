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

class CalendarController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    var db: Firestore!
    @IBOutlet weak var tableView: UITableView!
    
    
    var listPlanning = [Planning]()    //une listes d'étudiants
    
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
                        let horaire = document.data()["horaire"] as? String,
                        let salle = document.data()["salle"] as? String,
                        let latitude = document.data()["latitude"] as? String,
                        let longitude = document.data()["longitude"] as? String{
                    
                        self.listPlanning.append(Planning(nom: nom, filiere: filiere, enseignant: enseignant, horaire: horaire, salle: salle, latitude: latitude, longitude: longitude ))
                    }
                    
                }
                self.tableView.reloadData()
            }
        }
        
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
        cell.horaireLabel.text = listPlanning[indexPath.row].horaire
        cell.salleLabel.text = listPlanning[indexPath.row].salle
        
        return cell
    }
    
    
    // Affiche le point de repere sur la carte si on appuie sur une cellue table view
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        //tabBarController?.selectedIndex = 0   retourne sur la carte de la tabBarController
        let otherMap = UIViewController()
        let indexLatitude:Double? = Double(listPlanning[indexPath.row].latitude)    //convertir en double
        let indexLongitude:Double? = Double(listPlanning[indexPath.row].longitude)
        //print(type(of: indexLatitude))
        
        // Ajout de la carte
        let url = URL(string: "mapbox://styles/mapbox/streets-v11")
        let mapView = MGLMapView(frame: view.bounds, styleURL: url)
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        mapView.setCenter(CLLocationCoordinate2D(latitude: indexLatitude ?? -20.90180283795172, longitude: indexLongitude ?? 55.48438641154759), zoomLevel: 18, animated: false)
        
        otherMap.view.addSubview(mapView)
        show(otherMap, sender: true)
        
        
        // Ajout du repere sur la carte
        let annotation = MGLPointAnnotation()
        annotation.coordinate = CLLocationCoordinate2D(latitude: indexLatitude ?? -20.90180283795172, longitude: indexLongitude ?? 55.48438641154759)
        annotation.title = "mon marqueur"
        annotation.subtitle = "L'évènement se trouve ici"
        mapView.addAnnotation(annotation)
    }
    
    
}
