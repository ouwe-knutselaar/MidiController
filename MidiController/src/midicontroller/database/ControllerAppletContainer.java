package midicontroller.database;

import java.io.IOException;
import java.util.ArrayList;

import midicontroller.guiobjects.ControllerApplet;

/**
 * This class is a container to store Controller applets
 * @author Erwin
 *
 */
public class ControllerAppletContainer 
{

	private ArrayList<ControllerApplet> controllerAppletList;		// The list with controller applets
	
	
	
	public void loadApplets(String fileName) throws IOException
	{
		AppletFromFileLoader appletFromFileLoader=new AppletFromFileLoader();
		controllerAppletList=appletFromFileLoader.GetAppletsFromFile(fileName);
	}
	
	public ArrayList<ControllerApplet> getList()
	{
		return controllerAppletList;
	}
	
}
