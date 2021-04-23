//
//  SettingControllerTableViewController.swift
//  UnivMap
//
//  Created by Dylan Cherrier on 02/04/2021.
//

import UIKit

class SettingController: UITableViewController{
    // MARK: Proprieties
    // chaque valeur correspond au nombre de ligne/cellule d'un section
    let data = [["unavailable"],["appearence","languages","confidentiality"],["univMap","assistance"],["legalNotice"]]
    // chaque String correspond au header d'un section
    var header = ["account","general","learnMore",""]
    
    // MARK: View
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UserDefaults.standard.color(key: "viewBackground")
        self.navigationItem.title = "settings".localized(str: nil)
        
        tableView.delegate = self
        tableView.dataSource = self
        
        // Create footer for display version of app
        createFooter(size: nil)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        view.backgroundColor = UserDefaults.standard.color(key: "viewBackground")
        
        self.navigationItem.title = "settings".localized(str: nil)
        self.tableView.reloadData()
    }
    
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        if UIDevice.current.orientation.isLandscape {
            //print("Landscape \(size.width)")
            self.createFooter(size: size.width)
        } else {
            //print("Portrait")
            self.createFooter(size: size.width)
        }
    }
    
    func createFooter(size: CGFloat?){
        let footer = UIView(frame: CGRect(x: 0, y: 0, width: size ?? view.frame.size.width, height: 50))
        let text = UILabel(frame: footer.bounds)
        text.text = "UnivMap 1.0.0 (version)"
        text.font = UIFont(name: "Arial", size: 12.0)
        text.textAlignment = .center
        footer.addSubview(text)
        tableView.tableFooterView = footer
    }

    // MARK: - Table view data source
    
    /*  Definit le nombre section.
        On le definit soit dans le controller de la tableView,
        soit dans l'inspecteur apres avoir selectionner la tableView section dans le main.storyboard.
        Si un controlleur existe pour la tableView (comme ici), le nombre de ligne de chaque section doit y etre definit,
        ou et/ou correspondre au nombre definit dans l'inspecteur(comme ici),
        sinon le programme plante.
    */
    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return data.count     
    }

    /*  Definit le nombre de lignes de chaque section.
        On le definit soit dans le controller de la tableView,
        soit dans l'inspecteur apres avoir selectionner la tableView section dans le main.storyboard.
        Si un controlleur existe pour la tableView (comme ici), le nombre de ligne de chaque section doit y etre definit,
        ou et/ou correspondre au nombre definit dans l'inspecteur(comme ici),
        sinon le programme plante.
    */
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return data[section].count
    }
    
    override func tableView(_ tableView: UITableView, didDeselectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    
    // Permet de modifier le header de chaque section
    override func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        // Créer un label, donne un titre, une color, une police et taille, et enfin un margin
        let cell = UILabel()
        cell.text = header[section].localized(str: nil)
        cell.textColor = UIColor.black
        cell.font = UIFont(name: "Arial", size: CGFloat(16.0))
        cell.layoutMargins.left = 2000
        
        return cell
    }

    // Permet de modifie le tableViewCell(chaque ligne/cellule de chaque section)
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as UITableViewCell
        //let cell = data[indexPath.section][indexPath.row]
        cell.textLabel?.text = data[indexPath.section][indexPath.row].localized(str: nil)
        
        
        if(indexPath.section == 0){
            cell.accessoryType = UITableViewCell.AccessoryType.none
            cell.selectionStyle = UITableViewCell.SelectionStyle.none
        }else if(indexPath.section == 1 && indexPath.row == 2) || (indexPath.section == 3){
            cell.accessoryType = UITableViewCell.AccessoryType.detailButton
            cell.selectionStyle = UITableViewCell.SelectionStyle.none
        }else{
            cell.accessoryType = UITableViewCell.AccessoryType.disclosureIndicator
        }
        
        return cell
    }
    
    override func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        // Permet de definir la hauteur de cette header pour chaque section
        return 40.0
    }
    
    
    override func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        // Permet de definir la hauteur de cette footer pour chaque section
        return 15.0
    }
    
    // to go other viewController in click on Cell
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        switch data[indexPath.section][indexPath.row] {
        case "appearence":
            performSegue(withIdentifier: "segApparence", sender: nil)
            
        case "languages":
            performSegue(withIdentifier: "segLanguage", sender: nil)
            
        case "univMap":
            performSegue(withIdentifier: "segUniv", sender: nil)
            
        case "assistance":
            performSegue(withIdentifier: "segHelp", sender: nil)
            tableView.deselectRow(at: indexPath, animated: true)
            
        default: break
            
        }
            
        
    }
    
    // to go other viewController since accessory button in Cell
    override func tableView(_ tableView: UITableView, accessoryButtonTappedForRowWith indexPath: IndexPath) {
        switch data[indexPath.section][indexPath.row] {
        case "confidentiality":
            performSegue(withIdentifier: "segConfident", sender: nil)
            tableView.deselectRow(at: indexPath, animated: true)
            
            
        case "legalNotice":
            performSegue(withIdentifier: "segRight", sender: nil)
            tableView.deselectRow(at: indexPath, animated: true)
            
        default: break
            
        }
    
    }
}

