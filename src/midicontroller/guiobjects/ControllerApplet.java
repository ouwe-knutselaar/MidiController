package midicontroller.guiobjects;

import java.util.ArrayList;

import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import midicontroller.MidiControllerEvent;
import midicontroller.MidiControllerEventDispatcher;
import midicontroller.MidiControllerEventHandler;
import midicontroller.database.Globals;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


/**
 * This class is a GUI part and show one controller unit
 * It contains the sliders and channel for this effect
 * @author Erwin
 *
 */
public class ControllerApplet extends VBox implements MidiControllerEventHandler
{
	
	private Text headText=new Text("Head");		// Koptext
	private Text footText=new Text("foot");		// Voettext
	private ChoiceBox<Integer> channelSelector=new ChoiceBox<>(FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16));
	private int channel=16;						// Het default channel
	
	private ArrayList <MidiSlider> midiSliderArray=new ArrayList<>();
	private final int appletNumber;				// Index nummer van de applet
	private String hexSelectString="6600";		// The hexadecimal selection string 
	
	
	public ControllerApplet(int number)
	{
		channelSelector.setValue(Globals.channel);
		headText.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		headText.prefWidth(300);
		headText.setTextAlignment(TextAlignment.CENTER);
		this.setPadding(new Insets(10));
		this.setSpacing(5);
		this.getChildren().add(headText);
		this.getChildren().add(channelSelector);
		this.getChildren().add(footText);
		appletNumber=number;											// Zet het appletNummer vast
		
		MidiControllerEventDispatcher.addListener(this);                // Zorg dat er events worden ontvangen
		
		// Voeg een listener toe aan de channel selector
		channelSelector.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				MidiControllerEventDispatcher.SendEvent(new MidiControllerEvent(null, "channel_change", "", newValue.intValue(), 0));
				
			}});
	}

	public int getAppletNumber()	// Retourneet het applcat nummer
	{
		return appletNumber;
	}
	
	public void setHeadText(String headText) {
		this.headText.setText(headText);
	}

	public void setFootText(String footText) {
		this.footText.setText(footText);
	}

	public void add(MidiSlider midiSlider)
	{
		midiSlider.setChannel(channel);		// Zet het midi kanaal
		midiSliderArray.add(midiSlider);	// Voeg een midi slider toe 
		rebuild();							// Vul de applet opnieuw
	}
	
	public void clear()						// Haal alle sliders weer weg
	{
		midiSliderArray.clear();
	}
	/**
	 * Provide the name of the effect
	 * @return String with the name of the current effect
	 */
	public String getEffectName()
	{
		return headText.getText();
	}
	
	public int getChannel()
	{
		return channelSelector.getValue();
	}
	
	
	public String getHexSelectString()
	{
		return hexSelectString;
	}
	
	public void setHexSelectString(String hexCode)
	{
		this.hexSelectString=hexCode;
	}
	
	/**
	 * Recreate this applet
	 */
	private void rebuild()
	{
		this.getChildren().clear();						// Maak alles schoon
		this.getChildren().add(headText);				// Voeg de koptekst toe
		this.getChildren().add(channelSelector);        // Voeg de channel selector toe
		for(MidiSlider midiSlider : midiSliderArray)    // 
		{
			midiSlider.setChannel(channel);
			this.getChildren().add(midiSlider);			// Voeg de inhoud van de array met midisliders toe
		}
		this.getChildren().add(footText);				// Voeg de voettekst toe
	}


	@Override
	public void HandleEvent(MidiControllerEvent e) {
		if(e.getMsg1().equals("channel_change"))           // Kijk of het een change_channel event is
		{
			System.out.println("Midi channel change to "+e.getInt1());
			channel=e.getInt1();                           // Zo ja, wijzig het channel
			rebuild();                                     // En herbouw dit object
		}
		
	}
	
}
