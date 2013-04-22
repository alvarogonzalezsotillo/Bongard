package ollitos.bot.map.bsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;

import ollitos.bot.map.BRoom;
import ollitos.bot.map.BRoomReader;
import ollitos.bot.map.IBMap;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.util.BException;
import bsh.EvalError;
import bsh.Interpreter;

public class BBeanShellRoomReader extends BRoomReader {

	
	public BBeanShellRoomReader(IBMap map, BResourceLocator script) {
		super(map);
		_script = script;
	}

	private Interpreter _interpreter;
	private BResourceLocator _script;

	private Interpreter interpreter() throws EvalError{
		if (_interpreter == null) {
			_interpreter = new Interpreter();
			setCommonVars(_interpreter);
		}

		return _interpreter;
	}
	
	private void setCommonVars(Interpreter i) throws EvalError{
		i.set( "room", room() );
		i.set( "self", this );
	}

	public static void main(String[] args) throws EvalError {
		Interpreter i = new Interpreter();
		i.set("foo", 5);                    // Set variables
		i.set("date", new Date() ); 

		Date date = (Date)i.get("date");    // retrieve a variable

		// Eval a statement and get the result
		i.eval("bar = foo*10");             
		System.out.println( i.get("bar") );
		
	}

	@Override
	public BRoom populateRoom(){
		try {
			Interpreter i = interpreter();
			Reader r = reader();
			i.eval(r);
		}
		catch (Exception e) {
			throw new BException(e.toString(), e );
		}
		return room();
	}

	private Reader reader() throws IOException {
		InputStream in = BPlatform.instance().open( script() );
		return new InputStreamReader(in);
	}

	private BResourceLocator script() {
		return _script;
	}
}
