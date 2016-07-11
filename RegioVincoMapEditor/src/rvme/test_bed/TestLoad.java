/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.test_bed;

import java.io.IOException;
import rvme.MapEditorApp;
import rvme.data.DataManager;
import rvme.file.FileManager;

/**
 *
 * @author ravirao
 */
public class TestLoad {
    public static void main(String[] args) throws IOException {
        MapEditorApp app = new MapEditorApp();
        DataManager dataManager = new DataManager(app);
        FileManager fileManager = new FileManager();
        fileManager.loadData(dataManager, "./work/Andorra.json");
    }
}
