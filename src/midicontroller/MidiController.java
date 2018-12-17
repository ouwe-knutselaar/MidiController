package midicontroller;

import javafx.application.*;
import javafx.stage.Stage;
import midicontroller.database.ControllerAppletContainer;
import midicontroller.guiobjects.ControllerApplet;
import midicontroller.guiobjects.MidiMenu;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javax.sound.midi.*;
import IODevice.MidiControllerDevice;
import java.io.IOException;

 
public class MidiController extends Application implements MidiControllerEventHandler
{
	
	static private BorderPane mainPane=new BorderPane();											// The main client area
	static private FlowPane controllerAppletBox=new FlowPane();										// The sub client area with the controllerAppletContainer objects
	static private MidiMenu menuBar = new MidiMenu();												// The menu bar				
	private ControllerAppletContainer controllerAppletContainer=new ControllerAppletContainer();  	// Applet with the different Midi effects
	MidiControllerDevice midiControllerDevice=new MidiControllerDevice();   						// De midi connectors
	
	
	@SuppressWarnings("restriction")
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("MidiController 1.0");

		//Group root = new Group();
		
		Scene scene=new Scene(mainPane);			// Make a new scene with the main client area
		mainPane.setTop(menuBar);                  // Plaats de menubar
		mainPane.setCenter(controllerAppletBox);   // Plaats de controller
		
		primaryStage.setScene(scene);
		
		primaryStage.show();			// Show the GUI
		
	}
	
	
	public void initGUIControler() throws IOException
	{
		System.out.println("MidiController started");
		MidiControllerEventDispatcher.addListener(this);					// Voeg de main class toe aan de eventhandler
		MidiControllerEventDispatcher.addListener(midiControllerDevice);	// Voeg de midi device toe
		
		// Laadt de JSON met de controllers
		//controllerAppletContainer.loadApplets("C:\\Users\\erwin\\workspace\\MidiController\\resources\\dsp2024.json");
		controllerAppletContainer.loadApplets("D:\\onze_projecten\\MidiController\\MidiController\\resources\\dsp2024.json");
		
		// Zorg ervoor dat de eerste applet wordt getoond
		ControllerApplet cApp=controllerAppletContainer.getList().get(0);
		if(cApp!=null)
		{
			controllerAppletBox.getChildren().add(cApp);
		}else
		{
		  System.out.println("Error loading midi controller applets. No applets in de queue");
		  throw(new IllegalStateException());
		}

		menuBar.AddEffects(controllerAppletContainer);
		menuBar.addMidiDevices(midiControllerDevice);
		
	}
	
	
	public static void main(String argv[]) throws MidiUnavailableException, InvalidMidiDataException, IOException
	{
		MidiController midiController=new MidiController();
		midiController.initGUIControler();
		launch();
		
	}
	
	
	
	/**
	 * Catch the events
	 */
	@Override
	public void HandleEvent(MidiControllerEvent e) {
		
		if(e.getMsg1().equals("change_controller"))				// Wissel van controller
		{	System.out.println("MidiController message :"+e.getMsg1()+" "+e.getMsg2()+" "+e.getInt1()+" "+e.getInt2());
			ControllerApplet cApp=controllerAppletContainer.getList().get(e.getInt1());
			controllerAppletBox.getChildren().clear();
			controllerAppletBox.getChildren().add(cApp);
			
		}
		
	}
	
	
	

}
