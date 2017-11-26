package midicontroller.guiobjects;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import midicontroller.MidiControllerEvent;
import midicontroller.MidiControllerEventDispatcher;
import midicontroller.database.Globals;


public class MidiSlider extends HBox 
{
	
	private Text sliderName=new Text("slider");		// De text van de slider
	private Slider slider=new Slider(0,127,64);		// De lengte en positie van de slider
	private Text sliderValue=new Text("64");
	private int channel=Globals.channel;			// HEt channel waar de slider naar zend							
	
	String midiOutputHexString;						// String met de hex representatie van de midi call
	
	
	public MidiSlider()
	{
		//this.setMinWidth(1000);
		//this.setPrefWidth(1000);
		this.setMaxWidth(Double.MAX_VALUE);
		
		sliderName.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
		//sliderName.width
		sliderName.prefWidth(200);
		sliderName.setWrappingWidth(200);
		
		
		slider.setMinWidth(300);
		//slider.prefWidth(300);
		slider.setMaxWidth(Double.MAX_VALUE);
		slider.setShowTickMarks(true);
		
		this.getChildren().add(sliderName);
		this.getChildren().add(slider);
		this.getChildren().add(sliderValue);
		//this.setHgap(10);
		
		slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,Number  old_val, Number new_val) 
            	{// Er is een event dus stuur alle info van deze slider
            	 sliderValue.setText(""+new_val.intValue());
            	 MidiControllerEventDispatcher.SendEvent(new MidiControllerEvent(null, "slider_change", midiOutputHexString, new_val.intValue(), channel));      
                }
			}
           );
		
	}
	
	
	public void setText(String text)
	{
		sliderName.setText(text);
	}
	
	public byte getValue()
	{
		return (byte)slider.getValue();
	}
	
	public void setMidiOutputHexString(String hexString)
	{
		midiOutputHexString=hexString;
	}
	
	public String getMidiOutputHexString()
	{
		return midiOutputHexString;
	}
	
	public void setChannel(int channel)		// Zet het channel
	{
		this.channel=channel;
	}

}
