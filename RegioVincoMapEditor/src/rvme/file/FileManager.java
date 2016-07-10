/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.shape.Polygon;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import rvme.data.DataManager;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;

/**
 *
 * @author McKillaGorilla
 */
public class FileManager implements AppFileComponent {
    DataManager dataManager;
    
    // FOR JSON SAVING AND LOADING
    static final String JSON_CATEGORY = "category";
    static final String JSON_DESCRIPTION = "description";
    static final String JSON_START_DATE = "start_date";
    static final String JSON_END_DATE = "end_date";
    static final String JSON_COMPLETED = "completed";
    
    static final String JSON_NAME = "name";
    static final String JSON_OWNER = "owner";
    static final String JSON_ITEMS = "items";
    
    
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	dataManager = (DataManager)data;
	
	// FIRST THE LIST NAME AND OWNER
        
    }
    
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        
        // CLEAR THE OLD DATA OUT
	dataManager = (DataManager)data;
	dataManager.reset();
	
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        
	JsonArray list = json.getJsonArray("SUBREGIONS");
	for (int i = 0; i < list.size(); i++) {
	    JsonObject subregion = list.getJsonObject(i);
	    ArrayList<Polygon> temp;
            temp = loadSubregion(subregion);
            for (Polygon x: temp) {
                //Polygon y = dataManager.convertPolygon(x);
                dataManager.addPolygon(x);
            }
	}
        
    }
    
    public ArrayList<Polygon> loadSubregion(JsonObject obj) {
        JsonArray list2 = obj.getJsonArray("SUBREGION_POLYGONS");
        ArrayList<Double> xyCoordinates = new ArrayList<>();
        ArrayList<Polygon> myPolygons = new ArrayList<>();
        
        for (int i = 0; i < list2.size(); i++) {
	    JsonArray myArray = list2.getJsonArray(i);
            for (int j = 0; j < myArray.size(); j++) {
                double x = getDataAsDouble(myArray.getJsonObject(j), "X");
                double y = getDataAsDouble(myArray.getJsonObject(j), "Y");
                
                xyCoordinates.add(x);
                xyCoordinates.add(y);
            }
            
            dataManager.scaleXYCoordinates(xyCoordinates);
            
            Polygon p = new Polygon();
            
            p.getPoints().addAll(xyCoordinates);
            
            xyCoordinates.clear();
            myPolygons.add(p);
	}
        return myPolygons;
    }
    
    public double getDataAsDouble(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().doubleValue();	
    }
    
    public int getDataAsInt(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonNumber number = (JsonNumber)value;
        return number.bigIntegerValue().intValue();
    }
    
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }

    
    

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}