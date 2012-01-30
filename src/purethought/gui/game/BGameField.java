package purethought.gui.game;

import purethought.animation.BAnimator;
import purethought.animation.BCompoundTransformAnimation;
import purethought.animation.BConcatenateAnimation;
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

	private IBAnimation _pickUpAnimation;
	private IBAnimation _dropAnimation;
	private IBAnimation _set1OverAnimation;
	private IBAnimation _set2OverAnimation;
	private BWaitForAnimation _set1DropAnimation;
	private BWaitForAnimation _set2DropAnimation;

	
	private BEventAdapter _adapter = new BEventAdapter(this) {
		
		private boolean _dragQuestion = false;
		private boolean _set1Over;
		private boolean _set2Over;
		
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
				
				if( correctAnswer && !_badAnswer ) _correctAnswer = true;
				if( badAnswer && !_correctAnswer ) _badAnswer = true;
				
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
		
		alignSprites(400);
	}
	
	private IBPoint spritePosition( int column, int row ){
		int s = 105;
		
		if( column < 0 || row < 0 ){
			return BFactory.instance().point(s*2,s*3);
		}
		
		double colx = column == 0 ? .8 : 3.2;
		
		return BFactory.instance().point(s*colx, s*(row+1) );
	}
	
	
	private void alignSprites(int millis){
		int s = 105;
		
		BAnimator animator = animator();

		_size = new BRectangle(0, 0, s*4, s*6);
		
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
				new BTranslateAnimation( spritePosition(-1,-1) , millis ),
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


	private IBAnimation createPickUpAnimation() {
		return new BScaleAnimation(FOCUS_ZOOM, FOCUS_ZOOM, FOCUS_DELAY, _questionSprite);
	}

	private IBAnimation createDragAnimation(IBPoint p, int millis) {
		return new BWaitForAnimation(new BTranslateAnimation(p, millis, _questionSprite ), _pickUpAnimation);
	}

	private BWaitForAnimation createOutSetAnimation( BSprite[] setSprites, IBAnimation setOverAnimation) {
		return new BWaitForAnimation( new BScaleAnimation(1/FOCUS_ZOOM,1/FOCUS_ZOOM,FOCUS_DELAY,setSprites), setOverAnimation);
	}

	private BScaleAnimation createOverSetAnimation( BSprite[] setSprites ) {
		return new BScaleAnimation( FOCUS_ZOOM, FOCUS_ZOOM, FOCUS_DELAY, setSprites );
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

}
