package bongard.gui.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import bongard.animation.BAnimator;
import bongard.animation.BCompoundTransformAnimation;
import bongard.animation.BConcatenateAnimation;
import bongard.animation.BRotateAnimation;
import bongard.animation.BScaleAnimation;
import bongard.animation.BTranslateAnimation;
import bongard.animation.BWaitForAnimation;
import bongard.animation.IBAnimation;
import bongard.animation.IBTransformAnimable;
import bongard.geom.BRectangle;
import bongard.geom.IBPoint;
import bongard.geom.IBRectangle;
import bongard.geom.IBTransform;
import bongard.gui.basic.BBox;
import bongard.gui.basic.BLabel;
import bongard.gui.basic.BSprite;
import bongard.gui.basic.IBCanvas;
import bongard.gui.container.BDrawableContainer;
import bongard.gui.container.BFlippableContainer;
import bongard.gui.container.IBDrawableContainer;
import bongard.gui.container.IBFlippableDrawable;
import bongard.gui.event.BEventAdapter;
import bongard.platform.BFactory;
import bongard.problem.BProblem;

@SuppressWarnings("serial")
public class BGameField extends BDrawableContainer implements IBFlippableDrawable, Serializable{
	
	private static final int FOCUS_DELAY = 100;
	private static final double FOCUS_ZOOM = 1.3;
	private static final double SPRITE_OVER_ZOOM = 1.15;
	private static final int TILE_SIZE = 105;
	private static final boolean SHOW_POINTER = false;
	

	private BProblem _problem;
	private boolean _badAnswer;
	private boolean _correctAnswer;

	private BGameModel _model;
	
	transient private BFlippableContainer _container;

	transient private BBox _icon;

	transient private BBox _correctIcon;

	transient private BBox _badIcon;
	transient private BSprite[] _set1Sprites;
	transient private BSprite[] _set2Sprites;
	transient private BSprite _questionSprite;
	transient private BSprite[] _allSprites;
	transient private IBRectangle _size;
	transient private BLabel _pointer = BFactory.instance().label("O");


	transient private IBAnimation _pickUpAnimation;
	transient private IBAnimation _dropAnimation;
	transient private IBAnimation _set1OverAnimation;
	transient private IBAnimation _set2OverAnimation;
	transient private BWaitForAnimation _set1DropAnimation;
	transient private BWaitForAnimation _set2DropAnimation;

	transient private BEventAdapter _adapter;
	


	public void autoSolve(){
		
		boolean isOfSet1 = _problem.isOfSet1(_questionSprite.raster());
		
		IBPoint orig = spritePosition(-1,1);
		IBPoint dest = spritePosition(isOfSet1?0:1, 2);
		dest = BFactory.instance().point((dest.x()*2+orig.x())/3, dest.y()+20);
		
		IBAnimation animation = createPickUpAnimation();
		animation = new BConcatenateAnimation( animation,createDragAnimation(dest,900) );
		animation = new BConcatenateAnimation( animation,createOverSetAnimation( isOfSet1?_set1Sprites:_set2Sprites) );
		animation = new BConcatenateAnimation( animation,createDropAnimation(dest) );
		animation = new BConcatenateAnimation( animation,createOutSetAnimation(isOfSet1?_set1Sprites:_set2Sprites, null) );
		
		BAnimator a = BFactory.instance().game().animator();
		a.finishAnimations();
		a.addAnimation(animation);
	}
	
	private BEventAdapter adapter(){
		if (_adapter == null) {
			_adapter = new MyEventAdapter();
		}
		return _adapter;
	}
	
	
	private class MyEventAdapter extends BEventAdapter{
		
		private boolean _dragQuestion = false;
		private boolean _set1Over;
		private boolean _set2Over;
		
		public MyEventAdapter(){
			super(BGameField.this);
		}
		
		@Override
		public boolean pointerDown(IBPoint p) {
			if( _questionSprite.inside(p, null) ){
				_pickUpAnimation = createPickUpAnimation();
				animator().addAnimation( _pickUpAnimation );
				_dragQuestion = true;
				return true;
			}
			return false;
		}

		@Override
		public boolean pointerDrag(IBPoint p){
			if( _dragQuestion ){
				IBAnimation dragAnimation = createDragAnimation(p,50);
				animator().addAnimation( dragAnimation );
				checkSets();
				return true;
			}
			return false;
		}


		private void checkSets() {
			boolean set1Over = near( _questionSprite, _set1Sprites );
			if( !_set1Over &&  set1Over ){
				_set1OverAnimation = createOverSetAnimation(_set1Sprites);
				animator().addAnimation(_set1OverAnimation);
			}
			if( _set1Over && !set1Over ){
				animator().addAnimation( createOutSetAnimation(_set1Sprites,_set1OverAnimation) );
			}
			_set1Over = set1Over;
			
			boolean set2Over = near( _questionSprite, _set2Sprites );
			if( !_set2Over &&  set2Over ){
				_set2OverAnimation = createOverSetAnimation(_set2Sprites);
				animator().addAnimation(_set2OverAnimation);
			}
			if( _set2Over && !set2Over ){
				animator().addAnimation( createOutSetAnimation(_set2Sprites, _set2OverAnimation) );
			}
			_set2Over = set2Over;
		}


		private boolean near(BSprite questionSprite, BSprite[] sprites) {
			IBPoint p = questionSprite.temporaryPosition();
			
			for( BSprite s: sprites ){
				if( s.inside(p, null) ){
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean pointerUp(IBPoint p) {
			if( _dragQuestion ){
				BFactory f = BFactory.instance();
				
				boolean correctAnswer = _set1Over && _problem.isOfSet1(_questionSprite.raster()) ||
										_set2Over && _problem.isOfSet2(_questionSprite.raster());
				
				boolean badAnswer =  _set2Over && _problem.isOfSet1(_questionSprite.raster()) ||
									 _set1Over && _problem.isOfSet2(_questionSprite.raster());
				
				if( correctAnswer && !_badAnswer ) setCorrectAnswer(true);
				if( badAnswer && !_correctAnswer ) setBadAnswer(true);
				
				IBPoint dest = f.point(105*2, 105*3);
				if( correctAnswer ){
					dest = _questionSprite.temporaryPosition();
				}
				
				_dropAnimation = createDropAnimation(dest);
				
				animator().addAnimation( _dropAnimation );
				
				if( _set1Over ){
					_set1DropAnimation = createOutSetAnimation(_set1Sprites,_set1OverAnimation);
					animator().addAnimation( _set1DropAnimation );
				}
				if( _set2Over ){
					_set2DropAnimation = createOutSetAnimation(_set2Sprites,_set2OverAnimation);
					animator().addAnimation( _set2DropAnimation );
				}
				
				if( correctAnswer ){
					BSprite[] sprites = _set1Over ? _set1Sprites : _set2Sprites;
					IBAnimation setDropAnimation = _set1Over ? _set1DropAnimation : _set2DropAnimation;
					animator().addAnimation( new BWaitForAnimation( new BRotateAnimation(2*Math.PI/400, 400, sprites), setDropAnimation ) );
					animator().addAnimation( new BWaitForAnimation( new BRotateAnimation(2*Math.PI/400, 400, _questionSprite), _dropAnimation ) );
				}

			}
			_set1Over = _set2Over = _dragQuestion = false;
			return false;
		}
	};


	/**
	 * 
	 * @param canvas
	 */
	public BGameField(){
		this(null,null);
	}
	

	protected void setBadAnswer(boolean b) {
		_badAnswer = b;
		model().answered(this);
	}

	protected void setCorrectAnswer(boolean b) {
		_correctAnswer = b;
		model().answered(this);
	}

	public BGameField(BProblem test,BGameModel model){

		setProblem(test);
		setModel(model);
		init();
	}


	private void init() {
		listener().addListener( adapter() );
		IBRectangle r = new BRectangle(0, 0, BFlippableContainer.ICON_SIZE, BFlippableContainer.ICON_SIZE);
		_icon = BFactory.instance().box(r, BFactory.COLOR_WHITE);
		_icon.setFilled(false);
		_correctIcon = BFactory.instance().box(r, BFactory.COLOR_WHITE);
		_badIcon = BFactory.instance().box(r, BFactory.COLOR_BLACK);
	}


	public static  BRectangle computeOriginalSize() {
		return new BRectangle(0, 0, TILE_SIZE*4, TILE_SIZE*6);
	}
	
	private void setModel(BGameModel model) {
		_model = model;
	}
	
	private BGameModel model(){
		return _model;
	}

	/**
	 * 
	 * @param problem
	 */
	public void setProblem( BProblem problem ){
		_problem = problem;
	}
	
	private void setProblem( BProblem problem, IBPoint questionPosition ){
		if( problem == null ){
			return;
		}
		BFactory f = BFactory.instance();
		_problem = problem;
		
		_set1Sprites = new BSprite[_problem.set1().length];
		for( int i = 0 ; i < _set1Sprites.length ; i++ ){
			_set1Sprites[i] = f.sprite(_problem.set1()[i]);
		}
		_set2Sprites = new BSprite[_problem.set2().length];
		for( int i = 0 ; i < _set2Sprites.length ; i++ ){
			_set2Sprites[i] = f.sprite(_problem.set2()[i]);
		}
		_questionSprite = f.sprite( _problem.image1() );
		
		_allSprites = new BSprite[_set1Sprites.length+_set2Sprites.length+1];
		System.arraycopy(_set1Sprites, 0, _allSprites, 0, _set1Sprites.length );
		System.arraycopy(_set2Sprites, 0, _allSprites, _set1Sprites.length, _set2Sprites.length );
		_allSprites[_set1Sprites.length+_set2Sprites.length] = _questionSprite;
		
		for( BSprite s: _allSprites ){
			s.setAntialias(true);
		}
		
		alignSprites(400, questionPosition);
	}
	
	private static IBPoint spritePosition( int column, int row ){
		if( column < 0 || row < 0 ){
			return BFactory.instance().point(TILE_SIZE*2,TILE_SIZE*3);
		}
		
		double colx = column == 0 ? .8 : 3.2;
		
		return BFactory.instance().point(TILE_SIZE*colx, TILE_SIZE*(row+1) );
	}
	
	
	
	private void alignSprites(int millis, IBPoint questionPosition ){
		_questionSprite.transform().toIdentity().translate(questionPosition.x(), questionPosition.y());
		for (int i = 0; i < _set1Sprites.length; i++) {
			IBPoint p = spritePosition(0,i);
			_set1Sprites[i].transform().toIdentity().translate(p.x(),p.y());
		}

		for (int i = 0; i < _set2Sprites.length; i++) {
			IBPoint p = spritePosition(1,i);
			_set2Sprites[i].transform().toIdentity().translate(p.x(),p.y());
		}
	}
		
		
	private void alignSprites_animation(int millis, IBPoint questionPosition ){	
		
		BAnimator animator = animator();

		
		for (int i = 0; i < _set1Sprites.length; i++) {
			IBTransformAnimable a = _set1Sprites[i];
			animator.addAnimation( new BTranslateAnimation( spritePosition(0,i), millis, a ) );
		}

		for (int i = 0; i < _set2Sprites.length; i++) {
			IBTransformAnimable a = _set2Sprites[i];
			animator.addAnimation( new BTranslateAnimation( spritePosition(1,i), millis, a ) );
		}

		animator.addAnimation(
			new BCompoundTransformAnimation(
				new IBTransformAnimable[]{_questionSprite },
				new BTranslateAnimation( questionPosition , millis ),
				new BRotateAnimation(2*Math.PI/millis, millis)
			)
		);
	}

	
	/**
	 * 
	 */
	@Override
	protected void draw_internal(IBCanvas canvas, IBTransform aditionalTransform){
		
		
		if( _allSprites == null ){
			return;
		}
		
		for( BSprite s: _allSprites ){
			s.draw( canvas, aditionalTransform );
		}
		
		if( SHOW_POINTER ){
			_pointer.draw(canvas, aditionalTransform);
		}
	}
	
	@Override
	public IBRectangle originalSize() {
		if (_size == null) {
			_size = computeOriginalSize();
		}
		return _size;
	}
	
	private BAnimator animator(){
		return BFactory.instance().game().animator();
	}

	@Override
	public BFlippableContainer flippableContainer() {
		return _container;
	}

	@Override
	public void setFlippableContainer(BFlippableContainer c) {
		_container = c;
	}
	
	public boolean correctAnswer(){
		return _correctAnswer;
	}
	
	public boolean badAnswer(){
		return _badAnswer;
	}

	
	@Override
	public BBox icon(){
		if( correctAnswer() ){
			return _correctIcon;
		}
		if( badAnswer() ){
			return _badIcon;
		}
		return _icon;
	}


	private IBAnimation createPickUpAnimation() {
		return new BScaleAnimation(FOCUS_ZOOM, FOCUS_ZOOM, FOCUS_DELAY, _questionSprite);
	}

	private IBAnimation createDragAnimation(IBPoint p, int millis) {
		return new BWaitForAnimation(new BTranslateAnimation(p, millis, _questionSprite ), _pickUpAnimation);
	}

	private BWaitForAnimation createOutSetAnimation( BSprite[] setSprites, IBAnimation setOverAnimation) {
		return new BWaitForAnimation( new BScaleAnimation(1/SPRITE_OVER_ZOOM,1/SPRITE_OVER_ZOOM,FOCUS_DELAY,setSprites), setOverAnimation);
	}

	private BScaleAnimation createOverSetAnimation( BSprite[] setSprites ) {
		return new BScaleAnimation( SPRITE_OVER_ZOOM, SPRITE_OVER_ZOOM, FOCUS_DELAY, setSprites );
	}

	private BWaitForAnimation createDropAnimation(IBPoint dest) {
		return new BWaitForAnimation( 
				new BCompoundTransformAnimation(
						new IBTransformAnimable[]{ _questionSprite }, 
						new BScaleAnimation(1/FOCUS_ZOOM, 1/FOCUS_ZOOM, FOCUS_DELAY),
						new BTranslateAnimation( dest, FOCUS_DELAY)
				),
				_pickUpAnimation );
	}

	@SuppressWarnings("serial")
	private static class MyState extends BState{
		
		private BProblem _myProblem;
		private boolean _myBadAnswer;
		private boolean _myCorrectAnswer;
		private IBPoint _myPoint;
		private BGameModel _myModel;
		private int kk;

		public MyState(BGameField gf) {
			_myProblem = gf._problem;
			_myBadAnswer = gf.badAnswer();
			_myCorrectAnswer = gf.correctAnswer();
			IBPoint p = spritePosition(-1,-1);
			if( gf._questionSprite != null){
				p = gf._questionSprite.position();
			}
			_myPoint = p;
			_myModel = gf._model;
		}

		@Override
		public IBDrawableContainer createDrawable() {
			BGameField ret = new BGameField();
			ret.restore(this);
			return ret;
		}
	}
	
	@Override
	public BState save() {
		return new MyState(this);
	}
	
	private void restore(MyState state){
		init();
		_badAnswer = state._myBadAnswer;
		_correctAnswer = state._myCorrectAnswer;
		setModel(state._myModel);
		setProblem(state._myProblem,state._myPoint);
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		MyState state = (MyState) stream.readObject();
		restore(state);
		dispose(); // TO SAVE MEMORY UNTIL DISPLAYED
	}

	private void writeObject(ObjectOutputStream stream)	throws IOException {
		stream.writeObject(save());
	}

	@Override
	public void dispose() {
		_problem.dispose();
	}
	
	@Override
	public void setUp(){
		_problem.setUp();
		IBPoint position = spritePosition(-1, -1);
		if( _questionSprite != null ){
			position = _questionSprite.position();
		}
		setProblem(_problem, position);
	}
}
