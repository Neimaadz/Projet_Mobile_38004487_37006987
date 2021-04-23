//
//  NavController.swift
//  UnivMap
//
//  Created by Dylan Cherrier on 13/04/2021.
//

import UIKit

class LegalNoticeController: UIViewController{
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var titleViewText: UILabel!
    @IBOutlet weak var textLegalNotice: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // background View
        self.view.backgroundColor = UserDefaults.standard.color(key: "viewBackground")
        
        // text Label
        backButton.setTitle("back".localized(str: nil), for: .normal)
        textLegalNotice.text = "lorem".localized(str: nil)
        titleViewText.text = "legalNotice".localized(str: nil)
    }
    
    @IBAction func dismissbutton(_ sender: Any) {
        dismiss(animated: true)
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
