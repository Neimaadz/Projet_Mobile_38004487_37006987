//
//  AppDelegate.swift
//  UnivMap
//
//  Created by Damien on 02/04/2021.
//

import UIKit

@main
class AppDelegate: UIResponder, UIApplicationDelegate {


    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        preload()
        return true
    }

    private func preload(){
        let firstLaunched = UserDefaults.standard.bool(forKey: "firstLaunched")
        if firstLaunched{
            print("Not first launch.")
        }else{
            let color: UIColor = UIColor.init(red: CGFloat(91)/255, green: CGFloat(107)/255, blue: CGFloat(128)/255, alpha: 1)
            print("First launch")
            UserDefaults.standard.set(true, forKey: "switchState")
            //print(UserDefaults.standard.bool(forKey: "switchState"))
            UserDefaults.standard.setColor(color: color, forkey: "viewBackground")
            UserDefaults.standard.set(true, forKey: "firstLaunched")
        }
    }
    
    
    // MARK: UISceneSession Lifecycle

    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }


}

