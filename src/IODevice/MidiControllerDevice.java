package IODevice;


import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;


import midicontroller.MidiControllerEvent;
import midicontroller.MidiControllerEventHandler;
import midicontroller.database.Globals;

/**
 * This class controls the MIDI devices based on events
 * @author erwin
 *
 */
public class MidiControllerDevice implements MidiControllerEventHandler
{
	
	MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();		// Lijst met midi devices
	Receiver receiverPort;											// referentie naar de midi port 
	private List<String> deviceList=new ArrayList<>();				// Lijst met midi device namen
	
	
	
	public MidiControllerDevice() 
	{
		for(MidiDevice.Info infor : infos)		// maak de device list
			{
			    try{
			        MidiDevice deviceInfo=MidiSystem.getMidiDevice(infor);
			        System.out.println("Name:"+infor.getName()+" Descriptor:"+infor.getDescription()+" Vendor:"+infor.getVendor());				
					System.out.println("Max receivers:"+deviceInfo.getMaxReceivers()+" max transmitters:"+deviceInfo.getMaxTransmitters());
					if(deviceInfo.getMaxReceivers()!=0)deviceList.add(infor.getName());		// Voeg het device toe als het een output is

			    }
			    catch(MidiUnavailableException e)
			    {
			      System.out.println("Midi device not available "+e.getMessage() );
			    }
			}
		boolean result=OpenDevice(Globals.controller);							// Open een midi device
		if(result)
		{
			System.out.println("Device succesfully opened");
		}
		else
		{
			System.out.println("Error opening device");
		}	
	}

	
	/**
	 * Method to open a MIDI device
	 * @param deviceNumber The number of the device
	 * @return true if succeeded
	 */
	public boolean OpenDevice(int deviceNumber)
	{		
		System.out.println("OpenDevice "+deviceNumber);
		MidiDevice defaultDevice=null;										// Maak een referentie naar een MidiDevice
		try {
			defaultDevice = MidiSystem.getMidiDevice(infos[deviceNumber]);	// Select het default device
			defaultDevice.open();											// Open dit device
			receiverPort=defaultDevice.getReceiver();						// verkrijg de midi port
		} catch (MidiUnavailableException e) {
			System.out.println("Midi unavailable "+e.getMessage());
			e.printStackTrace();
			return false;
		}
		
		System.out.println("Transmitters is "+receiverPort+" with name "+defaultDevice.getDeviceInfo().getName());
		return true;
	}
	
	/**
	 * Returns a List of midi devices
	 * @return List<String> with MIDI device names
	 */
	public List<String> getDeviceArray()
	{
		return deviceList;
	}
	
	
	/** 
	 * Stuur een message over midi 
	 * @param hexString	Hex string with controller messges
	 * @param sliderParameter Parameter to change
	 * @param channel THe midi channel
	 * @return
	 */
	private boolean SendMidiSevent(String hexString, int sliderParameter, int channel)
	{
		System.out.println("channel org is "+channel);
		//channel--;																// Even een correctie omdat het null het eerste kanaal is
		ShortMessage message=new ShortMessage();								// Maak een short message om te verzenden
		byte[] midiBytaArray=HexStringToByteArray(hexString,sliderParameter);	// Maak de array om te verturen uit de hex string
		try {
			 System.out.println("MidiControllerDevice message type:"+ShortMessage.CONTROL_CHANGE+" ch:"+channel+" msg1:"+midiBytaArray[0]+" msg2:"+midiBytaArray[1]);
			 message.setMessage(ShortMessage.CONTROL_CHANGE,channel,midiBytaArray[0],midiBytaArray[1]);	// vul de shortmessage
			
		} catch (InvalidMidiDataException e) {
			
			e.printStackTrace();
			return false;
		}
		receiverPort.send(message, -1);	// Stuur de message
		return true;
	}
	
	
	private byte[] HexStringToByteArray(String hexString,int paraMeter)
	{
		
		int messageSize=hexString.length()/2;		// Bepaal de lengte van de arrray
		byte[] messageList=new byte[messageSize];	// Maak de byte array 
		
		System.out.println("hexstring -"+hexString+"- and size="+messageSize);
		int byteTel=0;
		for(int tel=0;tel<hexString.length();tel+=2)
		{
			String substring=hexString.substring(tel, tel+2);
			if(substring.equals("xx"))
			{
				messageList[byteTel]=(byte)paraMeter;
			}
			else
			{	
				messageList[byteTel]=(byte) Integer.parseInt(substring,16);
			}
			byteTel++;
			
		}

		return messageList;
	}
	
	
	@Override
	public void HandleEvent(MidiControllerEvent e) {
		if(e.getMsg1().equals("slider_change"))
			{
				//System.out.println("MidiControllerDevice message :"+e.getMsg1()+" "+e.getMsg2()+" "+e.getInt1()+" "+e.getInt2());
				SendMidiSevent(e.getMsg2(),e.getInt1(),e.getInt2());
			}
		
		if(e.getMsg1().equals("change_mididevice"))
		{
			//System.out.println("MidiControllerDevice message :"+e.getMsg1()+" "+e.getMsg2()+" "+e.getInt1()+" "+e.getInt2());
			ChangeToNewMidiDevice(e.getMsg2());
		}
		
		if(e.getMsg1().equals("change_controller"))
		{
			//System.out.println("MidiControllerDevice message :"+e.getMsg1()+" "+e.getMsg2()+" "+e.getInt1()+" "+e.getInt2());
			SendMidiSevent(e.getMsg2(),e.getInt1(),e.getInt2());
		}
		
		if(e.getMsg1().equals("change_channel"))
		{
			System.out.println("MidiControllerDevice message :"+e.getMsg1()+" "+e.getMsg2()+" "+e.getInt1()+" "+e.getInt2());
			//SendMidiSevent(e.getMsg2(),e.getInt1(),e.getInt2());
			Globals.channel=e.getInt1();
		}
		
	}


	private void ChangeToNewMidiDevice(String msg1) {
		MidiDevice defaultDevice=null;
		int MidiDeviceNumber=0;
		
		receiverPort.close();
		System.out.println("Open device "+msg1);
		for(int tel=0;tel<infos.length;tel++)
		{
			if(infos[tel].getName().equals(msg1))
				{
				 MidiDeviceNumber=tel;
				}
		}
		
		try {
			defaultDevice = MidiSystem.getMidiDevice(infos[MidiDeviceNumber]);
			defaultDevice.open();
			receiverPort=defaultDevice.getReceiver();
		} catch (MidiUnavailableException e) {
			
			e.printStackTrace();
		}
		
		System.out.println("Transmitters is "+receiverPort);

		
	}

}
