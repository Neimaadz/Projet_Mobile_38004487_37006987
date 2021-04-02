//
//  ViewController.swift
//  UnivMap
//
//  Created by Damien on 02/04/2021.
//

import UIKit
import Mapbox

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        
        let url = URL(string: "mapbox://styles/mapbox/streets-v11")
        let mapView = MGLMapView(frame: view.bounds, styleURL: url)
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        mapView.setCenter(CLLocationCoordinate2D(latitude: -20.90180283795172, longitude: 55.48438641154759), zoomLevel: 15, animated: false)
        view.addSubview(mapView)

    }
    
}

