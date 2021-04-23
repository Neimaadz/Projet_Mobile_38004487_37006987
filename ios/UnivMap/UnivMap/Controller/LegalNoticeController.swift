//
//  LegalNoticeController.swift
//  UnivMap
//
//  Created by Dylan Cherrier on 23/04/2021.
//

import UIKit

class LegalNoticeController: UIViewController {
    // MARK: Outlet proprieties
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var titleViewText: UILabel!
    @IBOutlet weak var textLegalNotice: UILabel!
    
    // View
    override func viewDidLoad() {
        super.viewDidLoad()
        // background View
        self.view.backgroundColor = UserDefaults.standard.color(key: "viewBackground")
        
        // text Label
        backButton.setTitle("back".localized(str: nil), for: .normal)
        textLegalNotice.text = "lorem".localized(str: nil)
        titleViewText.text = "legalNotice".localized(str: nil)
    }
    
    // Methode Button
    @IBAction func dismissbutton(_ sender: Any) {
        dismiss(animated: true)
    }
}
