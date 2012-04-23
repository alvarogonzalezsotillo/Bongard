package ollitos.gui.event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Formatter;
import java.util.Locale;
import java.util.Scanner;

import ollitos.animation.IBAnimable;
import ollitos.animation.IBAnimation;
import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BSprite;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.basic.IBRaster;
import ollitos.gui.event.IBEvent.Type;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.util.BException;



public class BLogListener implements IBEventListener{
	
	public static class ReplayAnimation implements IBAnimation{

		private BufferedReader _reader;
		private ParsedEvent _nextEvent;
		private long _acumMillis;
		private IBEventListener _listener;
		private BSprite _cursor;
		
		public ReplayAnimation( Reader r, IBEventListener listener ){
			if( r instanceof BufferedReader ){
				_reader = (BufferedReader) r;
			}
			else{
				_reader = new BufferedReader(r);
			}
			_listener = listener;
			_nextEvent = readNextEvent();
			
			BResourceLocator rl = new BResourceLocator( "/images/examples/cursor.png" );
			IBRaster raster = BPlatform.instance().raster(rl, true);
			_cursor = BPlatform.instance().sprite(raster);
			_cursor.transform().scale(.5, .5);
			_cursor.setVisible(false);
		}
		
		public IBDrawable cursor(){
			return _cursor;
		}
		
		private ParsedEvent readNextEvent() {
			ParsedEvent ret = null;
			String line = "not readed";
			try {
				line = _reader.readLine();
				ret = parseEvent(line);
			}
			catch (IOException e) {
				throw new BException( "Bad event:" + line , e );
			}
			return ret;
		}

		@Override
		public void stepAnimation(long millis) {
			if( endReached() ){
				return;
			}
			_acumMillis += millis;
			while( _nextEvent != null && _acumMillis > _nextEvent.millis() ){
				final IBEvent e = _nextEvent.event();
				BPlatform.instance().game().animator().post( new Runnable(){
					@Override
					public void run() {
						switch (e.type()) {
						case pointerDown:
							_cursor.setVisible(true);
							break;
						case pointerUp:
							_cursor.setVisible(false);
							break;
						}
						_cursor.transform().toIdentity();
						_cursor.transform().translate(e.point().x(), e.point().y());
						_listener.handle(e);
					}
				});
				_acumMillis -= _nextEvent.millis();
				_nextEvent = readNextEvent();
			}
		}

		@Override
		public IBAnimable[] animables() {
			return null;
		}

		@Override
		public void setAnimables(IBAnimable... a) {
		}

		@Override
		public boolean endReached() {
			return _nextEvent == null;
		}

		@Override
		public boolean needsUpdate(){
			return !endReached();
		}
	}

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
	
	private static String formatEvent(long millis, IBEvent e){
		
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

		return String.format( "%d %s %s %s", millis, t.name(), sPoint, sRectangle );
	}
	
	private static class ParsedEvent{
		private IBEvent _e;
		private long _millis;
		public IBEvent event(){ return _e; }
		public long millis(){ return _millis; }
		ParsedEvent( long millis, IBEvent e ){ _e = e; _millis = millis; }

	}
	
	private static ParsedEvent parseEvent( String s ){
		if( s == null ){
			return null;
		}
		
		Scanner scanner = new Scanner(s);
		scanner.useLocale(Locale.US);
		scanner.useDelimiter(" |,|\\t");
		
		String next = scanner.next();
		long millis = Long.parseLong(next);
		
		String sType = scanner.next();
		IBEvent.Type t = IBEvent.Type.valueOf(sType);
		
		double x = Float.parseFloat(scanner.next());
		double y = Float.parseFloat(scanner.next());
		IBPoint p = BPlatform.instance().point(x, y);
		
		x = Float.parseFloat(scanner.next());
		y = Float.parseFloat(scanner.next());
		double w = Float.parseFloat(scanner.next());
		double h = Float.parseFloat(scanner.next());
		IBRectangle r = new BRectangle(x, y, w, h);

		scanner.close();
		return new ParsedEvent( millis, new IBEvent(t, p, r) );
	}
	


	@Override
	public IBTransform transform() {
		return null;
	}

}
	