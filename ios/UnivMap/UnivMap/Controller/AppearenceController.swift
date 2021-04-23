//
//  AppearenceController.swift
//  UnivMap
//
//  Created by Dylan Cherrier on 23/04/2021.
//

import UIKit

class AppearenceController: UIViewController {
    // MARK: Outlet & Proprieties
    @IBOutlet weak var titleView: UILabel!
    @IBOutlet weak var firstSubTitle: UILabel!
    @IBOutlet weak var abstractFirstSubTitle: UILabel!
    @IBOutlet weak var colorBackground: UIButton!
    @IBOutlet weak var cellView: UIView!
    
    // MARK: Data Persist
    let defaults = UserDefaults.standard
    let COLOR_BACKGROUND = "viewBackground"
    
    // MARK: View
    override func viewDidLoad() {
        super.viewDidLoad()
        // Theme
        view.backgroundColor = defaults.color(key: COLOR_BACKGROUND)
        
        // Navigation Bar
        self.navigationItem.title = "appearence".localized(str: nil)
        
        // button
        initButton()
        
        //Label
        initLabelAndContener()
    }
    
    @IBAction func tapToButtonBackgroundColor(_ sender: Any) {
        chooseColor()
    }
    
    func initButton(){
        colorBackground.layer.cornerRadius = self.colorBackground.bounds.height/2
        colorBackground.backgroundColor = .systemGray6
        colorBackground.tintColor = defaults.color(key: COLOR_BACKGROUND)
        colorBackground.layer.borderWidth = 2
        colorBackground.layer.borderColor = defaults.color(key: COLOR_BACKGROUND)?.cgColor
    }
    
    func initLabelAndContener(){
        titleView.text = "appearenceView.titleView".localized(str: nil)
        firstSubTitle.text = "appearenceView.firstSubTitle".localized(str: nil)
        abstractFirstSubTitle.text = "appearenceView.abstractFirstSubTitle".localized(str: nil)
        cellView.layer.cornerRadius = self.colorBackground.bounds.height/2
    }
    
    func update(){
        view.backgroundColor = defaults.color(key: COLOR_BACKGROUND)
        colorBackground.tintColor = defaults.color(key: COLOR_BACKGROUND)
        colorBackground.layer.borderColor = defaults.color(key: COLOR_BACKGROUND)?.cgColor
    }
    
    // MARK: Color
    private func chooseColor(){
        let colorPicker = UIColorPickerViewController()
        colorPicker.delegate = self
        colorPicker.selectedColor = .black
        colorPicker.title = "eyesPicker".localized(str: nil)
        present(colorPicker, animated: true, completion: nil)
    }

}
    //MARK: Extension UserDefault
extension UserDefaults {
    func setColor(color: UIColor?, forkey key: String){
        var colorData: Data?
        if let color = color{
            do{
                colorData = try NSKeyedArchiver.archivedData(withRootObject: color, requiringSecureCoding: false) as Data?
                set(colorData, forKey: key)
            }catch let err{
                print("error archiving color data", err)
            }
        }
    }
    
    func color(key: String) -> UIColor?{
        var color: UIColor?
        if let colorData = data(forKey: key){
            do{
                color = try NSKeyedUnarchiver.unarchivedObject(ofClass: UIColor.self, from: colorData)!
            }catch let err{
                print("error unachiving color data" , err)
            }
        }
        return color
    }
}

//MARK: Extension ThemeController
extension AppearenceController : UIColorPickerViewControllerDelegate{
    func colorPickerViewControllerDidSelectColor(_ viewController: UIColorPickerViewController) {
        //let color:UIColor = viewController.selectedColor
        defaults.setColor(color: viewController.selectedColor, forkey: COLOR_BACKGROUND)
        update()
    }
    
    func colorPickerViewControllerDidFinish(_ viewController: UIColorPickerViewController) {
        
    }
}
