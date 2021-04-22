//
//  CustomAnnotationView.swift
//  UnivMap
//
//  Created by Damien on 22/04/2021.
//

import UIKit
import Mapbox


class CustomAnnotationView: MGLAnnotationView {
    override func layoutSubviews() {
        super.layoutSubviews()
         
        // Use CALayer’s corner radius to turn this view into a circle.
        layer.cornerRadius = bounds.width / 2   // forme du marqueur
        layer.borderWidth = 2
        layer.borderColor = UIColor.white.cgColor
    }
         
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
         
        // Animate the border width in/out, creating an iris effect.
        let animation = CABasicAnimation(keyPath: "borderWidth")
        animation.duration = 0.2
        // Si on selectionne le marqueur borderWidth=bounds.width/6 sinon 2
        layer.borderWidth = selected ? bounds.width / 6 : 2
        layer.add(animation, forKey: "borderWidth")
    }
}
