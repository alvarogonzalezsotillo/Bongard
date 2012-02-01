package purethought.gui.event;

import java.io.PrintStream;
import java.util.Formatter;
import java.util.Locale;
import java.util.Scanner;

import purethought.geom.BRectangle;
import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.gui.event.IBEvent.Type;
import purethought.platform.BFactory;

public class BLogListener implements IBEventListener{

	private PrintStream _stream;
	private long _lastMillis;
	
	public BLogListener(){
		this(System.out);
	}
	
	public BLogListener(PrintStream s){
		_stream = s;
		_lastMillis = System.currentTimeMillis();
	}
	
	@Override
	public boolean handle(IBEvent e) {
		long current = System.currentTimeMillis();
		long millis = current - _lastMillis;
		_lastMillis = current;
		
		_stream.println( formatEvent(millis,e) );
		
		return false;
	}
	
	public static String formatEvent(long millis, IBEvent e){
		
		Type t = e.type();
		IBPoint p = e.point();
		IBRectangle r = e.rectangle();
		
		String sPoint = "0,0";
		if( p != null ){
			Formatter f = new Formatter( Locale.US );
			sPoint = f.format("%f,%f", p.x(), p.y() ).toString();
		}
		String sRectangle = "0,0,0,0";
		if( r != null ){
			Formatter f = new Formatter( Locale.US );
			sRectangle = f.format("%f,%f,%f,%f", r.x(), r.y(), r.w(), r.h()).toString();
		}

		return String.format( "%s\t%s\t%s", t.name(), sPoint, sRectangle );
	}
	
	public static class ParsedEvent{
		private IBEvent _e;
		private long _millis;
		public IBEvent event(){ return _e; }
		public long millis(){ return _millis; }
		ParsedEvent( long millis, IBEvent e ){ _e = e; _millis = millis; }

	}
	
	public static ParsedEvent parseEvent( String s ){
		Scanner scanner = new Scanner(s);
		scanner.useLocale(Locale.US);
		scanner.useDelimiter(" ,\t");
		
		long millis = scanner.nextLong();
		
		String sType = scanner.next();
		IBEvent.Type t = IBEvent.Type.valueOf(sType);
		
		double x = scanner.nextDouble();
		double y = scanner.nextDouble();
		IBPoint p = BFactory.instance().point(x, y);
		
		x = scanner.nextDouble();
		y = scanner.nextDouble();
		double w = scanner.nextDouble();
		double h = scanner.nextDouble();
		
		IBRectangle r = new BRectangle(x, y, w, h);
		
		return new ParsedEvent( millis, new IBEvent(t, p, r) );
	}
	


	@Override
	public IBEventSource source() {
		return null;
	}

}
