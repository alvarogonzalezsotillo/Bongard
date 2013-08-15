package ollitos.platform.andr;

import ollitos.platform.IBColor;

public class AndrColor implements IBColor {

	private int _color;
	
	public AndrColor( int c ){
		_color = c;
	}
	
	public int color(){
		return _color;
	}
	
	@Override
	public String toString() {
		return ""+_color;
	}
}
