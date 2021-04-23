//
//  LangagueController.swift
//  UnivMap
//
//  Created by Dylan Cherrier on 23/04/2021.
//

import UIKit

class LangagueController: UIViewController {
    // MARK: Outlet variable
    @IBOutlet weak var languageSegment: UISegmentedControl!
    @IBOutlet weak var languageSwitch: UISwitch!
    @IBOutlet weak var titleView: UILabel!
    @IBOutlet weak var firstSubTitle: UILabel!
    @IBOutlet weak var abstractFirstSubTitle: UILabel!
    @IBOutlet weak var secondSubTitle: UILabel!
    @IBOutlet weak var abstractSecondSubTitle: UILabel!
    
    // MARK: Persist data
    let defaults = UserDefaults.standard
    
    let LANGUAGE = "language"
    let SWICTH_STATE = "switchState"
    let BACKROUND_COLOR = "viewBackground"
 
    // MARK: View
    override func viewDidLoad() {
        super.viewDidLoad()
        // Theme
        //view.backgroundColor = defaults.color(key: BACKROUND_COLOR)
        
        // State Button
        checkSwitchState()
        initSegmentState()
        
        // Navigation Bar
        self.navigationItem.title = "languages".localized(str: nil)
        
        // Label
        initLabel()
    }
    
    func initLabel(){
        languageSegment.setTitle("languageView.english".localized(str: nil), forSegmentAt: 0)
        languageSegment.setTitle("languageView.french".localized(str: nil), forSegmentAt: 1)
        
        titleView.text = "languageView.titleView".localized(str: nil)
        firstSubTitle.text = "languageView.firstSubTitle".localized(str: nil)
        abstractFirstSubTitle.text = "languageView.abstractFirstSubTitle".localized(str: nil)
        secondSubTitle.text = "languageView.secondSubTitle".localized(str: nil)
        abstractSecondSubTitle.text = "languageView.abstractSecondSubTitle".localized(str: nil)
    }
    
    func updateTextLanguage() {
        // Navigation Bar
        self.navigationItem.title = "languages".localized(str: nil)
        self.navigationController?.navigationBar.backItem?.title = "settings".localized(str: nil)
        
        // Tab Bar
        tabBarController?.titleAndImage()   // update title tabBarItems
        
        // Label
        initLabel()
    }
    
    //MARK: Switch Button
    // Switch init
    func checkSwitchState(){
        if(defaults.bool(forKey: SWICTH_STATE)){
            languageSwitch.setOn(true, animated: false)
            defaults.set(self.getDeviceLanguage(), forKey: LANGUAGE)
            languageSegment.isEnabled = false
        }else{
            languageSwitch.setOn(false, animated: false)
            languageSegment.isEnabled = true
        }
    }
    
    // Action button Switch
    @IBAction func defaultLangague(_ sender: Any) {
        if languageSwitch.isOn == true{
            defaults.set(true, forKey: SWICTH_STATE)
            resetDefaultLanguage()
            initSegmentState()
            languageSegment.isEnabled = false   // disenable others langagues
            updateTextLanguage()
        }else{
            defaults.set(false, forKey: SWICTH_STATE)
            languageSegment.isEnabled = true
        }
    }
    
    // MARK: Segmented Button
    // Action button Segment
    @IBAction func change(_ sender: Any) {
        if languageSegment.selectedSegmentIndex == 1 {
            defaults.set("fr", forKey: LANGUAGE)
        }else if languageSegment.selectedSegmentIndex == 0 {
            defaults.set("en", forKey: LANGUAGE)
        }
        updateTextLanguage()
    }
    
    func initSegmentState() {
        if defaults.string(forKey: LANGUAGE) == "fr"{
            languageSegment.selectedSegmentIndex = 1
        }else{
            languageSegment.selectedSegmentIndex = 0
        }
    }
    
    //MARK: Language
    
    func resetDefaultLanguage(){
        defaults.set(self.getDeviceLanguage(), forKey: LANGUAGE)
    }
    
    func getDeviceLanguage() -> String{
        let currentDeviceLanguage = Locale.preferredLanguages[0]
        if currentDeviceLanguage.contains("-"){
            let language = currentDeviceLanguage.components(separatedBy: "-")
            return language[0]
        }
        return currentDeviceLanguage
    }
}

extension String {
    func localized(str:String?) -> String{
        // display a language choose by dev (constant language for users)
        if let language = str{
            let path = Bundle.main.path(forResource: language, ofType: "lproj")
            let bundle = Bundle(path: path!)
            return NSLocalizedString(self, tableName: "Localizable", bundle: bundle!, value: self, comment: "nil")
        }
        // display a Device language
        if(UserDefaults.standard.bool(forKey: "switchState")){
            //print("\(UserDefaults.standard.bool(forKey: "switchState")) vkrhke")
            return NSLocalizedString(self, tableName: "Localizable", bundle: .main, value: self, comment: "nil")
        }
        // display a language choose by users
        let path = Bundle.main.path(forResource: UserDefaults.standard.string(forKey: "language"), ofType: "lproj")
        let bundle = Bundle(path: path!)
        return NSLocalizedString(self, tableName: "Localizable", bundle: bundle!, value: self, comment: "nil")
    }
}
