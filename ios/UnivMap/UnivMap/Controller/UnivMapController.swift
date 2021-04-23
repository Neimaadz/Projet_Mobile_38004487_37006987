//
//  UnivMapController.swift
//  UnivMap
//
//  Created by Dylan Cherrier on 23/04/2021.
//

import UIKit

class UnivMapController: UIViewController {
    // MARK: Data Persist
    let defaults = UserDefaults.standard
    let COLOR_BACKGROUND = "viewBackground"
    
    // MARK: View
    override func viewDidLoad() {
        super.viewDidLoad()
        // View background
        view.backgroundColor = defaults.color(key: COLOR_BACKGROUND)
        
        // Navigation Bar
        self.navigationItem.title = "univMap".localized(str: nil)
    }
}
