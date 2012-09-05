package ollitos.bot.view.isoview;

import static ollitos.bot.geom.BDirection.down;
import static ollitos.bot.geom.BDirection.east;
import static ollitos.bot.geom.BDirection.north;
import static ollitos.bot.geom.BDirection.south;
import static ollitos.bot.geom.BDirection.up;
import static ollitos.bot.geom.BDirection.west;
import static ollitos.bot.geom.IBRegion.Vertex.gVertex;

import java.util.Arrays;
import java.util.Comparator;

import javax.swing.SwingUtilities;

import ollitos.bot.geom.BLocation;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.geom.IBMovableRegion;
import ollitos.bot.map.BItemType;
import ollitos.bot.map.BRoomReader;
import ollitos.bot.map.items.BRoom;
import ollitos.bot.physics.BMapToPhysical;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysicalListener;
import ollitos.bot.physics.behaviour.BSpriteBehaviour;
import ollitos.bot.view.BPhysicsView;
import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.container.BDrawableContainer;
import ollitos.gui.event.IBEventSource;
import ollitos.platform.BCanvasContext;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBGame;
import ollitos.platform.IBRaster;


public class BIsoView extends BDrawableContainer implements BPhysicsView{

	private static final double COS30 = Math.cos(30*Math.PI/180);
	private static final boolean ANTIALIAS = true;
	private static final boolean POST_DRAW_BOXES = false;
	private static final boolean IN_DRAW_BOXES = false;
	
	private IBPhysicalListener _physicalListener = new IBPhysicalListener.Default(){
		public void itemAdded(IBPhysicalItem i) {
			_items = physics().items();
		};
		
		public void itemRemoved(IBPhysicalItem i) {
			_items = physics().items();
		};
	};
	
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
	

	private IBRectangle _originalSize = new BRectangle(0,0,640,480);
	private BRoom _room;


	private IBPhysicalItem[] _items;
	
	private Comparator<IBMovableRegion> _viewComparator = new Comparator<IBMovableRegion>(){
		private BLocation _c1 = BLocation.l(0,0,0);
		private BLocation _c2 = BLocation.l(0,0,0);
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
	private IBRaster _doubleBuffer;

	private int zIndex(IBLocation l){
		return (int)(-l.du()+(l.we()+l.sn())*COS30);
	}
	
	public static final IBLocation BASIC_VIEW_CELL = BLocation.l(32, 32, 24);





	
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
		int x = srcx + (int)originalSize().w()/2;
		int y = srcy + (int)originalSize().h();
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
    private BFlatPoint spritePosition(IBCanvas canvas, IBPhysicalItem i, BFlatPoint ret ){
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
	public IBRectangle originalSize() {
		return _originalSize;
	}

	
	@Override
	protected void draw_internal(IBCanvas c) {
		if( true ){
			IBRaster r = doubleBuffer();
			IBCanvas doubleBuffer = r.canvas();
			drawInCanvas(doubleBuffer, (BCanvasContext) platform().canvasContext(), false );
			canvasContext().setAntialias(ANTIALIAS);
			c.drawRaster(this, r, 0, 0);
		}
		else{
			drawInCanvas(c, canvasContext(), ANTIALIAS );
		}
	}

	private void drawInCanvas(IBCanvas c, BCanvasContext cc, boolean antialias ){
		cc.setColor( BPlatform.COLOR_DARKGRAY );
		c.drawBox(cc, originalSize(), true);
		cc.setAntialias(antialias);
		IBPhysicalItem[] items = sortedPhysicalItems();
		BFlatPoint p = new BFlatPoint();
		
		for (IBPhysicalItem i : items) {
			spritePosition(c,i, p);
			IBRaster r;
			r = rasterFor(i);
			if( r != null ){
				c.drawRaster(cc, r, p.x(), p.y());
			}
			if( IN_DRAW_BOXES ){
				drawBox(c,cc,i);
			}
		}
		
		if( POST_DRAW_BOXES ){
			for (IBPhysicalItem i : items) {
				drawBox(c,cc,i);
			}
		}
	}
	
	private IBRaster doubleBuffer(){
		if( _doubleBuffer == null ){
			_doubleBuffer = BPlatform.instance().rasterUtil().raster(originalSize());
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
			_room = new BRoomReader().createRoom();
		}
		return _room;
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
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				IBGame g = BPlatform.instance().game();
				g.setDefaultDrawable( new BIsoView() );
				g.restore();
			}
		} );
	}

	@Override
	public IBEventSource eventSource() {
		return this;
	}
}
