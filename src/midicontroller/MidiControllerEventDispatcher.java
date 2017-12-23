package midicontroller;

import java.util.ArrayList;
import java.util.List;


/**
 * Class voor het verspreiden van het event
 * @author Erwin
 *
 */
public final class MidiControllerEventDispatcher 
{

	public MidiControllerEventDispatcher()
	{
		System.out.println("MidiControllerEventDispatcher called");
	}
	
	static private List<MidiControllerEventHandler> dispatchList=new ArrayList<>();	// De lijst met event ontvangers
	
	static public void addListener(MidiControllerEventHandler e)						// Voeg een object aan de lijst toe
	{
		dispatchList.add(e);
	}
	
	static public void SendEvent(MidiControllerEvent e)								// Stuur een event
	{
		for(MidiControllerEventHandler i: dispatchList)
		{
			i.HandleEvent(e);
		}
	}
	
}
