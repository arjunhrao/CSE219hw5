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
import rvme.data.SubRegion;
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
    //might need this if i need to get it from the junit testing
    //public static DataManager dataManager;
    
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
        System.out.println(dataManager.getPolygonList().size());
        fileManager.loadData(dataManager, rawMapPath);
        System.out.println(dataManager.getPolygonList().size());
        //add a subregion for each polygon
        for (int i = 0; i < dataManager.getPolygonList().size(); i++) {
            SubRegion temp = new SubRegion();
            temp.setSubregionBorderThickness(1.0);
            dataManager.getSubregions().add(temp);
        }
        //put the relevant data inside each subregion of the subregions list of the dataManager
        SubRegion sub1 = dataManager.getSubregions().get(0);
        sub1.setSubregionName("Ordino");
        sub1.setCapitalName("Ordino (town)");
        sub1.setLeaderName("Ventura Espot");
        sub1.setSubregionColor(Color.rgb(200, 200, 200));
        sub1.setFlagImagePath("./export/The World/Europe/Andorra/" + sub1.getSubregionName() + " Flag.png");
        sub1.setLeaderImagePath("./export/The World/Europe/Andorra/" + sub1.getLeaderName() + ".png");
        //
        SubRegion sub2 = dataManager.getSubregions().get(1);
        
        
        SubRegion sub3 = dataManager.getSubregions().get(2);
        
        SubRegion sub4 = dataManager.getSubregions().get(3);
        
        SubRegion sub5 = dataManager.getSubregions().get(4);
        
        SubRegion sub6 = dataManager.getSubregions().get(5);
        
        SubRegion sub7 = dataManager.getSubregions().get(6);
        
        
        
        dataManager.setBackgroundColor(Color.rgb(220,110,0));
        dataManager.setBorderColor(Color.BLACK);
        dataManager.setBorderThickness(1.0);
        dataManager.setMapName("Andorra");
        dataManager.setRawMapPath("./raw_map_data/Andorra.json");
        dataManager.setParentDirectory("to be set when the new button is pressed, not needed for HW5");
        dataManager.setRegionFlagImagePath("./export/The World/Europe/Andorra Flag.png");
        dataManager.setCoatOfArmsImagePath("./export/The World/Europe/Andorra Coa.png");
        //the above Coa pic was found online and saved in the data
        
        // SAVE IT TO A FILE
	fileManager.saveData(dataManager, "./work/Andorra.json");
    }
}
