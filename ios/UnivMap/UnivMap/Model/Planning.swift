//
//  Planning.swift
//  UnivMap
//
//  Created by Damien on 08/04/2021.
//

import UIKit

class Planning{
    var nom: String
    var filiere : String
    var enseignant: String
    var horaire: String
    var salle: String
    var latitude: String
    var longitude: String
    
    init(nom: String, filiere: String, enseignant: String, horaire: String, salle: String, latitude: String, longitude: String) {
        
        self.nom = nom
        self.filiere = filiere
        self.enseignant = enseignant
        self.horaire = horaire
        self.salle = salle
        self.latitude = latitude
        self.longitude = longitude
    }
}
