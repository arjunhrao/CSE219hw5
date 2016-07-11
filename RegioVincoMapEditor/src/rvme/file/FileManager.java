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
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import rvme.data.DataManager;
import rvme.data.SubRegion;
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
	
	//All of the datamanager data
        //also need to access the path to the JSON file - can just note this and use in the datamanager
        //the datamanager/workspace should be able to access the file and then create the subregions
        //as it does for the json files from hw2
        String rawMapPath;
        String mapName;
        String parentDirec;
        String backgroundColorRed, borderColorRed;
        String backgroundColorBlue, borderColorBlue;
        String backgroundColorGreen, borderColorGreen;
        //can always use Double.parseDouble if I want these strings to return to double values
        
        if (dataManager.getMapName() == null)
            mapName = "";
        else
            mapName = dataManager.getMapName();
        if (dataManager.getRawMapPath() == null)
            rawMapPath = "";
        else
            rawMapPath = dataManager.getRawMapPath();
        if (dataManager.getParentDirectory() == null)
            parentDirec = "";
        else
            parentDirec = dataManager.getParentDirectory();
        if (dataManager.getBackgroundColor()== null) {
            backgroundColorRed = "";backgroundColorBlue = "";backgroundColorGreen = "";
            
        }
        else {
            
            backgroundColorRed = String.valueOf(dataManager.getBackgroundColor().getRed());
            backgroundColorBlue = String.valueOf(dataManager.getBackgroundColor().getBlue());
            backgroundColorGreen = String.valueOf(dataManager.getBackgroundColor().getGreen());
        }
        if (dataManager.getBorderColor()== null) {
            borderColorRed = "";borderColorBlue = "";borderColorGreen = "";
        }
        else {
            borderColorRed = String.valueOf(dataManager.getBorderColor().getRed());
            borderColorBlue = String.valueOf(dataManager.getBorderColor().getBlue());
            borderColorGreen = String.valueOf(dataManager.getBorderColor().getGreen());
        }
        
        
	// NOW BUILD THE JSON ARRAY FOR THE LIST
	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	ObservableList<SubRegion> subregions = dataManager.getSubregions();
	for (SubRegion item : subregions) {
	    JsonObject itemJson = Json.createObjectBuilder()
		    .add("subregion_name", item.getSubregionName())
		    .add("capital_name", item.getCapitalName())
		    .add("leader_name", item.getLeaderName())
		    .add("flag_image_path", item.getFlagImagePath())
                    .add("leader_image_path", item.getLeaderImagePath())
                    .add("red", String.valueOf(item.getSubregionColor().getRed()))
                    .add("blue", String.valueOf(item.getSubregionColor().getBlue()))
                    .add("green", String.valueOf(item.getSubregionColor().getGreen()))
                    .add("border_thickness", String.valueOf(item.getSubregionBorderThickness())).build();
	    arrayBuilder.add(itemJson);
	}
	JsonArray itemsArray = arrayBuilder.build();
	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add("map_name", mapName)
                .add("parent_directory", parentDirec)
                .add("background_color_red", backgroundColorRed)
                .add("background_color_blue", backgroundColorBlue)
                .add("background_color_green", backgroundColorGreen)
                .add("border_color_blue", borderColorBlue)
                .add("border_color_red", borderColorRed)
                .add("border_color_green", borderColorGreen)
                .add("raw_map_path", dataManager.getRawMapPath())
                .add("region_flag_image_path", dataManager.getRegionFlagImagePath())
                .add("coat_of_arms_image_path", dataManager.getCoatOfArmsImagePath())
                .add("map_position_x", dataManager.getMapPositionX())
                .add("map_position_y", dataManager.getMapPositionY())
                .add("zoom", dataManager.getZoom())
		.add("subregions", itemsArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
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
    
    public void loadDataHW5(AppDataComponent data, String filePath) throws IOException {
        // CLEAR THE OLD DATA OUT
	dataManager = (DataManager)data;
	dataManager.reset();
	
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        
        //Put each relevant string/datafield into the datamanager
        //woops. should've just been doing this, not what I did for the rest... oh well, I'll do it right in the array
        dataManager.setMapName(json.getString("map_name"));
        
        JsonString parentDir = json.getJsonString("parent_directory");
        dataManager.setParentDirectory(parentDir.getString());
        
        JsonString bgColorRed = json.getJsonString("background_color_red");
        JsonString bgColorBlue = json.getJsonString("background_color_blue");
        JsonString bgColorGreen = json.getJsonString("background_color_green");
        dataManager.setBackgroundColor(Color.color(Double.parseDouble(bgColorRed.getString()),Double.parseDouble(bgColorGreen.getString()),Double.parseDouble(bgColorBlue.getString())));
        
        JsonString borderColorRed = json.getJsonString("border_color_red");
        JsonString borderColorBlue = json.getJsonString("border_color_blue");
        JsonString borderColorGreen = json.getJsonString("border_color_green");
        dataManager.setBorderColor(Color.color(Double.parseDouble(borderColorRed.getString()),Double.parseDouble(borderColorGreen.getString()),Double.parseDouble(borderColorBlue.getString())));
        
        Double posX = getDataAsDouble(json, "map_position_x");
        Double posY = getDataAsDouble(json, "map_position_y");
        Double zoom = getDataAsDouble(json, "zoom");
        dataManager.setPosX(posX);
        dataManager.setPosY(posY);
        dataManager.setZoom(zoom);
        
        dataManager.setRegionFlagImagePath(json.getString("region_flag_image_path"));
        dataManager.setCoatOfArmsImagePath(json.getString("coat_of_arms_image_path"));
        dataManager.setRawMapPath(json.getString("raw_map_path"));
        
        JsonArray list = json.getJsonArray("subregions");
	for (int i = 0; i < list.size(); i++) {
	    JsonObject subregion = list.getJsonObject(i);
            //set everything for the current subregion
            SubRegion temp = new SubRegion(subregion.getString("subregion_name"), subregion.getString("capital_name"), subregion.getString("leader_name"));
            temp.setSubregionColor(Color.color(Double.parseDouble(subregion.getString("red")), Double.parseDouble(subregion.getString("green")),Double.parseDouble(subregion.getString("blue"))));
            temp.setFlagImagePath(subregion.getString("flag_image_path"));
            temp.setLeaderImagePath(subregion.getString("leader_image_path"));
            temp.setSubregionBorderThickness(Double.parseDouble(subregion.getString("border_thickness")));
            //add it to the datamanager
	    dataManager.getSubregions().add(temp);
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