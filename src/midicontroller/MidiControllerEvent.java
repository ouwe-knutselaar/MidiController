package midicontroller;


/**
 * Class met de inhoud van het event
 * @author Erwin
 *
 */
public class MidiControllerEvent 
{
	private byte[] midiMessageString;
	private String msg1;
	private String msg2;
	private int Int1;
	private int Int2;
	
	public MidiControllerEvent(byte[] midiMessageString,String msg1,String msg2,int Int1,int Int2)
	{
		this.midiMessageString=midiMessageString;
		this.msg1=msg1;
		this.msg2=msg2;
		this.Int1=Int1;
		this.Int2=Int2;
	}

	public byte[] getMidiMessageString() {
		return midiMessageString;
	}

	public String getMsg1() {
		return msg1;
	}

	public String getMsg2() {
		return msg2;
	}

	public int getInt1() {
		return Int1;
	}

	public int getInt2() {
		return Int2;
	}
	
	
	
}