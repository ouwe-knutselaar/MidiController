package midicontroller.database;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import midicontroller.guiobjects.ControllerApplet;
import midicontroller.guiobjects.MidiSlider;

/**
 * This class builds the effect controller applets
 * @author erwin
 *
 */
public class AppletFromFileLoader 
{

  private String jsonString;			// De string met json code
  private JsonNode rootObject;			// De root van het Json obejct
  
  ArrayList<ControllerApplet> controllerAppletList=new ArrayList<>();
  
  /**
   * This function reads filename and returns a full array of applet objects
   * @param fileName
   * @return
   * @throws IOException
   */
  public ArrayList<ControllerApplet> GetAppletsFromFile(String fileName) throws IOException 
  {
	  readFileToString(fileName);		// Laad de File in een String
	  readStringToJsonObject();			// Zet de string om naar een jsonObject
	  getEffectsFromJson();
	  
	  return controllerAppletList;
	}
  
  
  /**
   *  This function reads the JSON file and builds all the effect applets
   */
  private void getEffectsFromJson()
  {
	  int tel=0;
	  ArrayNode effectArray=(ArrayNode)rootObject.get("effects");	// Open de effecten array
	  for(JsonNode elm : effectArray)
	  {
		  //System.out.println(elm.get("name").textValue());
		  ControllerApplet singleApplet=new ControllerApplet(tel);			// Maak een controller applet met een vast nummer
		  singleApplet.setHeadText(elm.get("name").textValue());				// Zet de header
		  singleApplet.setHexSelectString(elm.get("hexAccess").textValue());	// Importeer de hex selectie string
		  ArrayNode sliderArray=(ArrayNode)elm.get("controlers");			// Open de controller array
		  for(JsonNode slider : sliderArray)								//Open de slider array
		  	{
			  MidiSlider midiSlider=new MidiSlider(); 							// Maak een sliderobject
			  midiSlider.setText(slider.get("name").textValue());				// Voeg de naam toe
			  midiSlider.setMidiOutputHexString(slider.get("hex").textValue());	// Laadt de hex string
			  singleApplet.add(midiSlider);										// Voeg het sliderobject toe
		  	}
		  singleApplet.setFootText(elm.get("type").textValue());		// Zet de footer
		  controllerAppletList.add(singleApplet);					// Voeg het applet opbject to aan de lijst met effect applets
		  tel++;													// Verhoog de index teller
	  }
  }
  
  
  /**
   * 
   */
  private void readStringToJsonObject() 
  {
	//Gson gson=new Gson();
	
	ObjectMapper mapper = new ObjectMapper();
	try {
		 rootObject = mapper.readTree(jsonString);
		 //System.out.println("Device is "+rootObject.get("deviceName").asText());
	} catch (IOException e) {
		System.out.println("Error in the JSON");
		e.printStackTrace();
	}
	
  }




/**
 *  read the json files
 */
private void readFileToString(String fileName) throws IOException
  {
	 System.out.println("Open file "+fileName);
	 byte[] buffer=new byte[1024];							// De buffer
	 StringBuilder jsonStringBD=new StringBuilder();		// Object voor String opbouw
	 File infile=new File(fileName);						// Maak een file object
	 FileInputStream fis=new FileInputStream(infile);		// Maak er een inputstream van
	 BufferedInputStream bis=new BufferedInputStream(fis);	// Maak er een buffered input van
	 while(fis.read(buffer)!=-1)							// lees blokken van 1k
	 {
		 jsonStringBD.append(new String(buffer));			// Plak deze er aan vast
		 buffer=new byte[1024];								// reset de buffer
	 }
	 jsonString=jsonStringBD.toString();					// Stop alles in de jsonString
	 //System.out.println("Json string \n"+jsonString);
	 bis.close();											// Sluit de handlers
	 fis.close();
	 
  }


	
}
