//
//  AssistanceController.swift
//  UnivMap
//
//  Created by Dylan Cherrier on 23/04/2021.
//

import UIKit
import SafariServices

class AssistanceController: UIViewController {
    
    @IBOutlet weak var subView: UIView!
    @IBOutlet weak var labelTitle: UILabel!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var mailButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // popup View
        subView.backgroundColor = UserDefaults.standard.color(key: "viewBackground")
        subView.layer.cornerRadius = subView.frame.size.height/8
        
        // Button
        initButton(button: backButton)
        initButton(button: mailButton)
        backButton.setTitle("back".localized(str: nil), for: .normal)
        mailButton.setTitle("send".localized(str: nil), for: .normal)
        
        // Label
        labelTitle.text = "assistanceView.title".localized(str: nil)
    }
    
    @IBAction func dismissButton(_ sender: Any) {
        dismiss(animated: true, completion: nil)
        
    }
    
    @IBAction func sendButton(_ sender: Any) {
        let email = "assistance@univMap.com"
        if let url = URL(string: "mailto:\(email)") {
          if #available(iOS 10.0, *) {
            UIApplication.shared.open(url)
          } else {
            UIApplication.shared.openURL(url)
          }
        }
    }
    
    func initButton(button: UIButton){
        button.layer.cornerRadius = button.frame.height/2
        button.backgroundColor = .systemGray6
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
