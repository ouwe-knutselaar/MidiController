package midicontroller.guiobjects;

import javafx.scene.control.MenuBar;


import IODevice.MidiControllerDevice;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleGroup;
import midicontroller.MidiControllerEvent;
import midicontroller.MidiControllerEventDispatcher;
import midicontroller.database.ControllerAppletContainer;
import midicontroller.database.Globals;

/**
 * This class maintains the menustructure
 * @author gebruiker
 *
 */
public class MidiMenu extends MenuBar 
{

	// Top menubar items
	Menu file=  new Menu("File       ");
	Menu effect=new Menu("Effect     ");
	Menu midi=  new Menu("Midi output");
	
	// File menu
	MenuItem load=new MenuItem("Load");
	MenuItem save=new MenuItem("Save");
	MenuItem opennew=new MenuItem("New");
	
	final ToggleGroup toggleGroup = new ToggleGroup();
	
	
	
	public MidiMenu()
	{
		file.getItems().addAll(load,save,opennew);
		this.getMenus().addAll(file,effect,midi);
	}
	
	/**
	 * Add the list of effects to the effect list in the menu bar
	 * @param controllerAppletContainer
	 */
	public void AddEffects(ControllerAppletContainer controllerAppletContainer)
	{
		for(ControllerApplet cApp : controllerAppletContainer.getList())
		{
			MenuItem tempItem=new MenuItem(cApp.getEffectName());
			
			tempItem.setOnAction(new EventHandler<ActionEvent>(){
			    @Override public void handle(ActionEvent e) {
			        
			        MidiControllerEventDispatcher.SendEvent(new MidiControllerEvent(null, "change_controller", cApp.getHexSelectString(),cApp.getAppletNumber(),Globals.channel));
			    }}
			    );
			
			effect.getItems().add(tempItem);
		}
		
	}

	/**
	 * Add the list of Midi controllers to the menu item
	 * @param midiControllerDevice
	 */
	public void addMidiDevices(MidiControllerDevice midiControllerDevice) {
		
		for(String device : midiControllerDevice.getDeviceArray())
		{
			MenuItem tempItem=new MenuItem(device);
			tempItem.setOnAction(new EventHandler<ActionEvent>(){
			    @Override public void handle(ActionEvent e) {
			        
			        MidiControllerEventDispatcher.SendEvent(new MidiControllerEvent(null, "change_mididevice", device,0,0));
			    }}
			    );
			
			midi.getItems().add(tempItem);
		}
		
	}



}
