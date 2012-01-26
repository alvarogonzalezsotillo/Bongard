package purethought.gui.game;

import purethought.animation.BAnimator;
import purethought.animation.BCompoundTransformAnimation;
import purethought.animation.BRotateAnimation;
import purethought.animation.BScaleAnimation;
import purethought.animation.BTranslateAnimation;
import purethought.animation.BWaitForAnimation;
import purethought.animation.IBAnimation;
import purethought.animation.IBTransformAnimable;
import purethought.geom.BRectangle;
import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.gui.basic.BBox;
import purethought.gui.basic.BLabel;
import purethought.gui.basic.BSprite;
import purethought.gui.basic.IBCanvas;
import purethought.gui.basic.IBDrawable;
import purethought.gui.container.BDrawableContainer;
import purethought.gui.container.BFlippableContainer;
import purethought.gui.container.IBFlippableDrawable;
import purethought.gui.event.BEventAdapter;
import purethought.platform.BFactory;
import purethought.platform.BImageLocator;
import purethought.problem.BCardExtractor;
import purethought.problem.BProblem;

public class BGameField extends BDrawableContainer implements IBFlippableDrawable{
	
	private static final int FOCUS_DELAY = 100;

	private static final double FOCUS_ZOOM = 1.3;

	private static final boolean SHOW_POINTER = false;

	private BProblem _problem;
	
	private BSprite[] _set1Sprites;
	private BSprite[] _set2Sprites;
	private BSprite _questionSprite;
	private BSprite[] _allSprites;
	private IBRectangle _size;
	private BLabel _pointer = BFactory.instance().label("O");
	
	private BEventAdapter _adapter = new BEventAdapter(this) {
		
		private boolean _dragQuestion = false;
		private IBAnimation _pickUpAnimation;
		private IBAnimation _dropAnimation;
		private IBAnimation _dragAnimation;
		private IBAnimation _set1OverAnimation;
		private boolean _set1Over;
		private boolean _set2Over;
		private IBAnimation _set2OverAnimation;
		private BWaitForAnimation _set1DropAnimation;
		private BWaitForAnimation _set2DropAnimation;
		
		@Override
		public boolean pointerDown(IBPoint p) {
			if( _questionSprite.inside(p, null) ){
				_pickUpAnimation = new BScaleAnimation(FOCUS_ZOOM, FOCUS_ZOOM, FOCUS_DELAY, _questionSprite);
				animator().addAnimation( _pickUpAnimation );
				_dragQuestion = true;
				return true;
			}
			return false;
		}

		@Override
		public boolean pointerDrag(IBPoint p){
			if( _dragQuestion ){
				_dragAnimation = new BTranslateAnimation(p, 50, _questionSprite );
				animator().addAnimation( new BWaitForAnimation(_dragAnimation, _pickUpAnimation) );
				checkSets();
				return true;
			}
			return false;
		}

		private void checkSets() {
			boolean set1Over = near( _questionSprite, _set1Sprites );
			if( !_set1Over &&  set1Over ){
				_set1OverAnimation = new BScaleAnimation( FOCUS_ZOOM, FOCUS_ZOOM, FOCUS_DELAY, _set1Sprites );
				animator().addAnimation(_set1OverAnimation);
			}
			if( _set1Over && !set1Over ){
				animator().addAnimation( new BWaitForAnimation( new BScaleAnimation(1/FOCUS_ZOOM,1/FOCUS_ZOOM,FOCUS_DELAY,_set1Sprites), _set1OverAnimation) );
			}
			_set1Over = set1Over;
			
			boolean set2Over = near( _questionSprite, _set2Sprites );
			if( !_set2Over &&  set2Over ){
				_set2OverAnimation = new BScaleAnimation( FOCUS_ZOOM, FOCUS_ZOOM, FOCUS_DELAY, _set2Sprites );
				animator().addAnimation(_set2OverAnimation);
			}
			if( _set2Over && !set2Over ){
				animator().addAnimation( new BWaitForAnimation( new BScaleAnimation(1/FOCUS_ZOOM,1/FOCUS_ZOOM,FOCUS_DELAY,_set2Sprites ), _set2OverAnimation) );
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
				
				if( correctAnswer && !_badAnswer ) _correctAnswer = true;
				if( badAnswer && !_correctAnswer ) _badAnswer = true;
				
				IBPoint dest = f.point(105*2, 105*3);
				if( correctAnswer ){
					dest = _questionSprite.temporaryPosition();
				}
				
				_dropAnimation = 
						new BCompoundTransformAnimation(
								new IBTransformAnimable[]{ _questionSprite }, 
								new BScaleAnimation(1/FOCUS_ZOOM, 1/FOCUS_ZOOM, FOCUS_DELAY),
								new BTranslateAnimation( dest, FOCUS_DELAY)
						);
				animator().addAnimation( new BWaitForAnimation(_dropAnimation, _pickUpAnimation) );
				
				if( _set1Over ){
					_set1DropAnimation = new BWaitForAnimation( new BScaleAnimation(1/FOCUS_ZOOM,1/FOCUS_ZOOM,FOCUS_DELAY,_set1Sprites ), _set1OverAnimation);
					animator().addAnimation( _set1DropAnimation );
				}
				if( _set2Over ){
					_set2DropAnimation = new BWaitForAnimation( new BScaleAnimation(1/FOCUS_ZOOM,1/FOCUS_ZOOM,FOCUS_DELAY,_set2Sprites ), _set2OverAnimation);
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
<<<<<<< HEAD

		@Override
		public boolean pointerClick(IBPoint p) {
			//BFactory instance = BFactory.instance();
			//BImageLocator test = instance.cardExtractor().randomProblem();
			//setProblem(test);
			return true;
		}
=======
>>>>>>> 44cf15fe139ff4a37780c070e16bbc9cf734c9d1
	};

	private BFlippableContainer _container;

	private BGameModel _model;

	private boolean _badAnswer;

	private boolean _correctAnswer;

	private BBox _icon;

	private BBox _correctIcon;

	private BBox _badIcon;

	/**
	 * 
	 * @param canvas
	 */
	public BGameField(){
		this(null,null);
	}
	

	public BGameField(BImageLocator test,BGameModel model){
		listener().addListener( _adapter );
		setProblem(test);
		setModel(model);
		IBRectangle r = new BRectangle(0, 0, BFlippableContainer.ICON_SIZE, BFlippableContainer.ICON_SIZE);
		_icon = BFactory.instance().box(r, "ffffff");
		_icon.setFilled(false);
		_correctIcon = BFactory.instance().box(r, "ffffff");
		_badIcon = BFactory.instance().box(r, "000000");
	}
	
	private void setModel(BGameModel model) {
		_model = model;
	}
	
	private BGameModel model(){
		return _model;
	}

	/**
	 * 
	 * @param test
	 */
	public void setProblem( BImageLocator test ){
		if( test == null ){
			return;
		}
		BFactory f = BFactory.instance();
		_problem = BCardExtractor.extract(test);
		
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
		
		alignSprites();
	}
	
	private void alignSprites(){
		int s = 105;
		int millis = 400;
		
		BFactory f = BFactory.instance();
		BAnimator animator = animator();
		
		
		_size = new BRectangle(0, 0, s*4, s*6);
		
				
		
		for (int i = 0; i < _set1Sprites.length; i++) {
			IBTransformAnimable a = _set1Sprites[i];
			animator.addAnimation( new BTranslateAnimation( f.point(s*.8, s*(i+1) ), millis, a ) );
		}

		for (int i = 0; i < _set2Sprites.length; i++) {
			IBTransformAnimable a = _set2Sprites[i];
			animator.addAnimation( new BTranslateAnimation( f.point(s*3.2, s*(i+1) ), millis, a ) );
		}

		animator.addAnimation(
			new BCompoundTransformAnimation(
				new IBTransformAnimable[]{_questionSprite },
				new BTranslateAnimation( f.point(s*2, s*3) , millis ),
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
		
		super.draw_internal(canvas, aditionalTransform);
	}
	
	@Override
	public IBRectangle originalSize() {
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
	
	public boolean goodAnswer(){
		return _correctAnswer;
	}
	
	public boolean badAnswer(){
		return _badAnswer;
	}

	
	@Override
	public BBox icon(){
		if( goodAnswer() ){
			return _correctIcon;
		}
		if( badAnswer() ){
			return _badIcon;
		}
		return _icon;
	}

}
