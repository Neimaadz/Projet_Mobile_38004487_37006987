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
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
