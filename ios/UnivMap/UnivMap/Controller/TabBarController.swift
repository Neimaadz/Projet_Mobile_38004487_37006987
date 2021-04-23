//
//  TabBarController.swift
//  UnivMap
//
//  Created by Dylan Cherrier on 08/04/2021.
//

import UIKit

class TabBarController: UITabBarController, UITabBarControllerDelegate {
    override func viewDidLoad() {
        self.tabBarController?.delegate = self
        
        titleAndImage()
    }
}

extension UITabBarController{
    func titleAndImage(){
        // frist items/onglets
        tabBar.items?[0].title = "map".localized(str: nil)
        tabBar.items?[0].image = UIImage(systemName: "map")
        // second items/onglets
        tabBar.items?[1].title = "calendar".localized(str: nil)
        tabBar.items?[1].image = UIImage(systemName: "calendar")
        // thridth items/onglets
        tabBar.items?[2].title = "settings".localized(str: nil)
        tabBar.items?[2].image = UIImage(systemName: "gearshape")
    }
}

