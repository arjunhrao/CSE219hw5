/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.test_bed;

import java.io.IOException;
import javafx.scene.paint.Color;
import rvme.MapEditorApp;
import rvme.data.DataManager;
import rvme.file.FileManager;

/**
 *
 * @author ravirao
 */
public class TestSave {
    
    /**Have your driver class hard code the creation of all the necessary data values
     * such that the Andorra map and data could be created. This means everything.
     * The map data, the images, the background color, the border color and thickness, etc.
     * Note that your TestSave class would artificially initialize all the data such that
     * it can then be used by your file manager to save it to /work/Andorra.json, which again,
     * would be in your own JSON format.
     */
    public static void main(String[] args) throws IOException {
        MapEditorApp app = new MapEditorApp();
        DataManager dataManager = new DataManager(app);
        FileManager fileManager = new FileManager();
        //FileManager fileManager = (FileManager)app.getFileComponent();
        
        
        //create a new JSON file on save
        //on load, loads THAT Json file
        //call method in FileManager that does the saving of data / json file, similar to HW1
        
        //hardcoding Andorra
    
        //step 1: Create Regions
        //step 2: Import All data into data

        //step 1 - need to access the path to the JSON file - can just note this and use in the datamanager
        //the datamanager/workspace should be able to access the file and then create the subregions
        //as it does for the json files from hw2
        String rawMapPath = "./HW5SampleData/raw_map_data/Andorra.json";
        //load the data from andorra.json into the polygon list in the datamanager
        //it's okay that this part isn't exactly 'hard-coded' because who's gonna put all of the andorra points
        //into this class, amirite?
        dataManager.getPolygonList().clear();
        
        fileManager.loadData(dataManager, rawMapPath);
        
        dataManager.setBackgroundColor(Color.rgb(220,110,0));
        dataManager.setBorderColor(Color.BLACK);
        dataManager.setBorderThickness(1.0);
        dataManager.setMapName("Andorra");
        dataManager.setRawMapPath("./HW5SampleData/raw_map_data/Andorra.json");
        dataManager.setParentDirectory(rawMapPath);
        
        
        
        // SAVE IT TO A FILE
	fileManager.saveData(dataManager, "./HW5SampleData/raw_map_data/Andorra.json");
    }
}
