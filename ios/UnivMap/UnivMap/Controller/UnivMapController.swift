//
//  UnivMapController.swift
//  UnivMap
//
//  Created by Dylan Cherrier on 23/04/2021.
//

import UIKit

class UnivMapController: UIViewController {
    // MARK: Outlet proprities
    @IBOutlet weak var firstTitle: UILabel!
    @IBOutlet weak var firstAbstract: UILabel!
    @IBOutlet weak var secondTitle: UILabel!
    @IBOutlet weak var firstSubView: UIView!
    @IBOutlet weak var firstSubViewLabel: UILabel!
    @IBOutlet weak var firstSubViewButton: UIButton!
    @IBOutlet weak var secondSubView: UIView!
    @IBOutlet weak var secondSubViewLabel: UILabel!
    @IBOutlet weak var secondSubViewButton: UIButton!
    
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
        
        // Button and subView
        initButton(button: firstSubViewButton)
        initButton(button: secondSubViewButton)
        initSubView(view: firstSubView)
        initSubView(view: secondSubView)
        
        // Label
        firstTitle.text = "univMap.firstTitle".localized(str: nil)
        firstAbstract.text = "univMap.firstAbstract".localized(str: nil)
        secondTitle.text = "univMap.by".localized(str: nil)
        firstSubViewLabel.text = "univMap.damien".localized(str: nil)
        secondSubViewLabel.text = "univMap.dylan".localized(str: nil)
        firstSubViewButton.setTitle("univMap.about".localized(str: nil), for: .normal)
        secondSubViewButton.setTitle("univMap.about".localized(str: nil), for: .normal)
    }
    
    // Methode
    func initButton(button: UIButton){
        button.backgroundColor = defaults.color(key: COLOR_BACKGROUND)
        button.layer.cornerRadius = button.bounds.height/4
       
        button.tintColor = .systemGray5
        button.layer.borderWidth = 2
        button.layer.borderColor = defaults.color(key: COLOR_BACKGROUND)?.cgColor
    }
    
    func initSubView(view: UIView){
        view.layer.cornerRadius = view.frame.size.height/3
        view.backgroundColor = .systemGray6
    }
    
    @IBAction func damienButton(_ sender: Any) {
        let email = "https://damienlaoussing.com/"
        if let url = URL(string: "\(email)") {
          if #available(iOS 10.0, *) {
            UIApplication.shared.open(url)
          } else {
            UIApplication.shared.openURL(url)
          }
        }
    }
    
    @IBAction func dylanButton(_ sender: Any) {
    }
    
    
}
