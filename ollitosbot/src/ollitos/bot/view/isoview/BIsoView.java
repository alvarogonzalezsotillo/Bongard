package ollitos.bot.view.isoview;

import static ollitos.bot.geom.BDirection.down;
import static ollitos.bot.geom.BDirection.east;
import static ollitos.bot.geom.BDirection.north;
import static ollitos.bot.geom.BDirection.south;
import static ollitos.bot.geom.BDirection.up;
import static ollitos.bot.geom.BDirection.west;
import static ollitos.bot.geom.IBRegion.Vertex.gVertex;
import static ollitos.bot.physics.BPlayerAction.*;

import java.util.Arrays;
import java.util.Comparator;

import ollitos.bot.geom.BLocation;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.geom.IBMovableRegion;
import ollitos.bot.map.BItemType;
import ollitos.bot.map.BRoom;
import ollitos.bot.map.IBMapReader;
import ollitos.bot.physics.BMapToPhysical;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysicalListener;
import ollitos.bot.physics.behaviour.BMovableThingBehaviour;
import ollitos.bot.physics.behaviour.BSpriteBehaviour;
import ollitos.bot.view.IBPhysicsView;
import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.container.BDrawableContainer;
import ollitos.gui.event.BEventAdapter;
import ollitos.gui.event.IBEvent;
import ollitos.gui.event.IBEventListener;
import ollitos.platform.BCanvasContext;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBRaster;
import ollitos.platform.raster.BNewRasterProvider;
import ollitos.platform.raster.IBRasterProvider;


public class BIsoView extends BDrawableContainer implements IBPhysicsView{

	private static final double COS30 = Math.cos(30*Math.PI/180);
	private static final boolean ANTIALIAS = true;
	private static final boolean POST_DRAW_BOXES = false;
	private static final boolean IN_DRAW_BOXES = true;
	
	private IBEventListener _listener = new BEventAdapter(){
		public boolean keyPressed(IBEvent e) {
			switch(e.keyChar()){
				case 'q': physics().playerAction(moveForward); return true;
				case 'o': physics().playerAction(turnLeft); return true;
				case 'p': physics().playerAction(turnRight); return true;
				case ' ': physics().playerAction(jump); return true;
			}
			return false;
		}

	};
	
	private IBPhysicalListener _physicalListener = new IBPhysicalListener.Default(){
		public void itemAdded(IBPhysicalItem i) {
			_items = physics().items();
		}
		
		public void itemRemoved(IBPhysicalItem i) {
			_items = physics().items();
		}
	};
	
	public BIsoView( IBMapReader reader ){
		this( 480, 640, reader );
	}
	
	private BIsoView( int width, int height, IBMapReader reader ){
		super( new BRectangle( width, height ) );
		_mapReader = reader;
		addListener(_listener);
	}
	
	public static class BFlatPoint{
		private int _x;
		private int _y;

		public BFlatPoint(int x, int y){
			set(x,y);
		}
		
		public BFlatPoint(){
			this(0,0);
		}

		public int x() {
			return _x;
		}

		public int y() {
			return _y;
		}
		
		public void set(int x, int y){
			_x = (x);
			_y = (y);
		}
		
		@Override
		public String toString() {
			return "(" + _x +","+ _y+")";
		}

	}
	

	private BRoom _room;


	private IBPhysicalItem[] _items;
	
	private Comparator<IBMovableRegion> _viewComparator = new Comparator<IBMovableRegion>(){
		public int compare(IBMovableRegion r1, IBMovableRegion r2){
			IBLocation c1 = r1.region().vertex(gVertex);
			IBLocation c2 = r2.region().vertex(gVertex);
			return zIndex(c2) - zIndex(c1);
			
		}
		
			/*
			 * N  E    U     
			 *  \/     |
			 *  /\     |
			 * W  S    D
			 * 
			 *         a
			 *        / \     
			 *       b   c
			 *       |\g/|
			 *       d | e
			 *        \ /
			 *         f 
			 */
	};
	private BPhysics _physics;
	private IBRasterProvider _doubleBuffer;
	private IBMapReader _mapReader;

	private int zIndex(IBLocation l){
		return (int)(-l.du()+(l.we()+l.sn())*COS30);
	}
	
	private static final IBLocation BASIC_VIEW_CELL = BLocation.l(32, 32, 24);

	
	/**
	 * N  E    U     
	 *  \/     |
	 *  /\     |
	 * W  S    D
	 * 
	 * 	        (0,0,1)
	 * (1,0,0) N   U   E (0,1,0)
	 * 	        \  |  /
	 * 	   	     \ | /
	 * 	   	      \ /
	 * 		    (0,0,0)
	 * 
	 * Basic block: 24 pixels vertical arista
	 *              32 pixels "vertical" diagonal
	 *              64 pixels "horizontal" diagonal
	 *              
	 * @param l
	 * @return
	 */
	private static BFlatPoint toFlatPoint(IBLocation l, BFlatPoint dest){
		IBLocation mapBC = BItemType.BASIC_MAP_CELL;
		int pixelsSN = BASIC_VIEW_CELL.sn()/mapBC.sn();
		int pixelsWE = BASIC_VIEW_CELL.we()/mapBC.we();
		int pixelsDU = BASIC_VIEW_CELL.du()/mapBC.du();
		int pixelsDU_SNWE = 1;
		
		if( dest == null ){
			dest = new BFlatPoint();
		}
		
		int x = - pixelsSN*l.sn() + pixelsWE*l.we();
		int y = - pixelsDU*l.du() - pixelsDU_SNWE*( l.sn() + l.we() );
		
		dest.set( x, y );
		return dest;
	}
	
	private void toScreenA(BFlatPoint ... src){
		for (BFlatPoint p : src) {
			toScreen(p,p);
		}
	}
	
	private BFlatPoint toScreen(BFlatPoint src, BFlatPoint dest ){
		int srcx = src.x();
		int srcy = src.y();
		
		IBRectangle os = originalSize();
		int x = (int)os.x() + srcx + (int)os.w()/2;
		int y = (int)os.y() + srcy + (int)os.h();
		if( dest == null ){
			dest = new BFlatPoint();
		}
		dest.set(x, y);
		
		return dest;
	}
	
	/**
	 *   ---->
	 *  |  X
	 *  |Y
	 *  V
	 * 
	 */
    private BFlatPoint spritePosition(IBPhysicalItem i, BFlatPoint ret){
		/*
		 * N  E    U     
		 *  \/     |
		 *  /\     |
		 * W  S    D
		 * 
		 *         a
		 *        / \     
		 *       b   c
		 *       |\g/|
		 *       d | e
		 *        \ /
		 *         f 
		 */      
		BFlatPoint a = toFlatPoint( i.region().vertex(east, north, up), null );
		BFlatPoint b = toFlatPoint( i.region().vertex(west, north, up), null );
    	
    	int x = b.x();
    	int y = a.y();
    	
    	if( ret == null ){
    		ret = new BFlatPoint(x, y);
    	}
    	else{
    		ret.set(x, y);
    	}
    	toScreen(ret,ret);
    	return ret;
    }

	
	@Override
	protected void draw_internal(IBCanvas c) {
		drawInCanvas(c, canvasContext(), ANTIALIAS );
	}

	private void drawInCanvas(IBCanvas c, BCanvasContext cc, boolean antialias ){
		cc.setColor( BPlatform.COLOR_DARKGRAY );
		c.drawBox(cc, originalSize(), true);
		cc.setAntialias(antialias);
		IBPhysicalItem[] items = sortedPhysicalItems();
		BFlatPoint p = new BFlatPoint();
		
		for (IBPhysicalItem i : items) {
			spritePosition(i, p);
			IBRaster r;
			r = rasterFor(i);
			if( r != null ){
				c.drawRaster(cc, r, p.x(), p.y());
			}
			if( IN_DRAW_BOXES && (BMovableThingBehaviour.is(i) ) ){
				drawBox(c,cc,i);
			}
		}
		
		if( POST_DRAW_BOXES ){
			for (IBPhysicalItem i : items) {
				drawBox(c,cc,i);
			}
		}
	}
	
	private IBRasterProvider doubleBuffer(){
		if( _doubleBuffer == null ){
			int w = (int) originalSize().w();
			int h = (int) originalSize().h();
			_doubleBuffer = new BNewRasterProvider(w, h);
		}
		return _doubleBuffer;
	}
	
	private void drawBox(IBCanvas canvas, BCanvasContext cc, IBPhysicalItem i){
		/* 
		 * 
		 *         a
		 *        / \     
		 *       b   c
		 *       |\g/|
		 *       d | e
		 *        \ /
		 *         f 
		 */      
		BFlatPoint a = toFlatPoint( i.region().vertex(east, north, up), null );
		BFlatPoint b = toFlatPoint( i.region().vertex(west, north, up), null );
		BFlatPoint c = toFlatPoint( i.region().vertex(east, south, up), null );
		BFlatPoint d = toFlatPoint( i.region().vertex(west, north, down), null );
		BFlatPoint e = toFlatPoint( i.region().vertex(east, south, down), null );
		BFlatPoint f = toFlatPoint( i.region().vertex(west, south, down), null );
		BFlatPoint g = toFlatPoint( i.region().vertex(west, south, up), null );
		toScreenA(a,b,c,d,e,f,g);
		
		cc.setColor(BPlatform.COLOR_WHITE);
		
		
		canvas.drawLine(cc, a.x(), a.y(), b.x(), b.y() );
		canvas.drawLine(cc, a.x(), a.y(), c.x(), c.y() );
		
		canvas.drawLine(cc, b.x(), b.y(), g.x(), g.y() );
		canvas.drawLine(cc, b.x(), b.y(), d.x(), d.y() );
		
		canvas.drawLine(cc, c.x(), c.y(), g.x(), g.y() );
		canvas.drawLine(cc, c.x(), c.y(), e.x(), e.y() );
		
		canvas.drawLine(cc, g.x(), g.y(), f.x(), f.y() );
		
		canvas.drawLine(cc, d.x(), d.y(), f.x(), f.y() );
		
		canvas.drawLine(cc, e.x(), e.y(), f.x(), f.y() );
	}

	private BRoom room(){
		if( _room == null ){
			_room = mapReader().readMap().initialRoom();
		}
		return _room;
	}

	public IBMapReader mapReader() {
		return _mapReader;
	}
	
	private BPhysics physics(){
		if (_physics == null) {
			_physics = new BPhysics(this);
			_physics.addPhysicalListener(_physicalListener);
		}

		return _physics;
	}
	
	private IBPhysicalItem[] physicalItems() {
		if( _items == null ){
			loadRoomIntoPhysics();
			_items = physics().items();
		}
		return _items;
	}

	private void loadRoomIntoPhysics() {
		BMapToPhysical.fillFromRoom(physics(), room());
		physics().updateRoomWalls();
		physics().start();
	}
	
	private IBPhysicalItem[] sortedPhysicalItems(){
		physicalItems();
		Arrays.sort(_items, _viewComparator );
		return _items;
	}

	private static IBRaster rasterFor( IBPhysicalItem i ){
		BSpriteBehaviour b = i.behaviour( BSpriteBehaviour.class );
		if( b == null ){
			return null;
		}
		return b.currentFrame(i.direction());
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println( toFlatPoint(BLocation.l(12,12,12), null) );
		System.out.println( toFlatPoint(BLocation.l(12,0,0), null) );
	}

}
