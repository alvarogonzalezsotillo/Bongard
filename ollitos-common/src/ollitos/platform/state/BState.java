package ollitos.platform.state;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import ollitos.platform.BPlatform;
import ollitos.util.BException;

@SuppressWarnings("serial")
public abstract class BState implements Serializable{
	
	public abstract Stateful create();

	public interface Stateful{
		BState save();
	}
	
	public byte[] bytes(){
		return bytes(this);
	}
	
	public static Serializable fromBytes(byte[] ba){
		if( ba == null ){
			return null;
		}
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(ba);
			ObjectInputStream ois = new ObjectInputStream(bis);
			Object object = ois.readObject();
			return (Serializable)object;
		}
		catch (Exception ex) {
			BPlatform.instance().logger().log("-", "Can't load state:" + ex );
		}
		return null;
	}
	
	public static byte[] bytes(Serializable object){
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
