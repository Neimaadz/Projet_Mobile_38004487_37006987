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
    var hDebut : String
    var hFin : String
    var mDebut : String
    var mFin : String
    var salle: String
    var latitude: String
    var longitude: String
    
    init(nom: String, filiere: String, enseignant: String, hDebut : String, hFin : String, mDebut : String, mFin : String, salle: String, latitude: String, longitude: String) {
        
        self.nom = nom
        self.filiere = filiere
        self.enseignant = enseignant
        self.hDebut = hDebut
        self.hFin = hFin
        self.mDebut = mDebut
        self.mFin = mFin
        self.salle = salle
        self.latitude = latitude
        self.longitude = longitude
    }
}
