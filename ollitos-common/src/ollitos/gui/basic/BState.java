package ollitos.gui.basic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import ollitos.gui.container.IBDrawableContainer;
import ollitos.util.BException;

@SuppressWarnings("serial")
public abstract class BState implements Serializable{
	public abstract IBDrawableContainer createDrawable();

	
	public byte[] bytes(){
		return bytes(this);
	}
	
	public static BState fromBytes(byte[] ba ){
		if( ba == null ){
			return null;
		}
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(ba);
			ObjectInputStream ois = new ObjectInputStream(bis);
			Object object = ois.readObject();
			return (BState)object;
		}
		catch (Exception ex) {
			throw new BException("Can't load state", ex );
		}
	}
	
	static public byte[] bytes(Object object){
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
			oos.close();
			bos.close();
			return bos.toByteArray();
		}
		catch (IOException ex) {
			throw new BException( "Can't generate state", ex );
		}
	}
	
	
}
