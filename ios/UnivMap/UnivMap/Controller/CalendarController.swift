//
//  CalendarController.swift
//  UnivMap
//
//  Created by Dylan Cherrier on 09/04/2021.
//

import UIKit

class CalendarController: UIViewController {

    @IBOutlet weak var nav: UINavigationBar!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    

    @IBAction func testNav(_ sender: UIButton) {
        let map = MapController()
        //present(map, animated: true, completion: nil)
        show(map, sender: nil)
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
