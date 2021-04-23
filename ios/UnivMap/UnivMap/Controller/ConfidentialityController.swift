//
//  ConfidentialityController.swift
//  UnivMap
//
//  Created by Dylan Cherrier on 23/04/2021.
//

import UIKit

class ConfidentialityController: UIViewController {
    // MARK: Outlet proprieties
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var titleViewText: UILabel!
    @IBOutlet weak var textConfident: UILabel!
    
    // MARK: View
    override func viewDidLoad() {
        super.viewDidLoad()
        // View
        self.view.backgroundColor = UserDefaults.standard.color(key: "viewBackground")
        
        // text Label
        backButton.setTitle("back".localized(str: nil), for: .normal)
        textConfident.text = "lorem".localized(str: nil)
        titleViewText.text = "confidentiality".localized(str: nil)
    }
    
    // MARK: Methode Button
    @IBAction func dismissButton(_ sender: Any) {
        dismiss(animated: true)
    }
}
