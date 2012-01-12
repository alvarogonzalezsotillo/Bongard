package purethought.gui;

import purethought.animation.BAnimator;
import purethought.animation.BCompoundTransformAnimation;
import purethought.animation.BRotateAnimation;
import purethought.animation.BTranslateAnimation;
import purethought.animation.IBTransformAnimable;
import purethought.geom.BRectangle;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.problem.BCardExtractor;
import purethought.problem.BProblem;
import purethought.problem.BProblemLocator;
import purethought.util.BFactory;

public class BGameField implements IBCanvasDrawable{
	private BProblem _problem;
	
	private BSprite[] _set1Sprites;
	private BSprite[] _set2Sprites;
	private BSprite _questionSprite;
	private BSprite[] _allSprites;
	private IBRectangle _size;

	/**
	 * 
	 * @param canvas
	 */
	public BGameField(){
	}
	
	/**
	 * 
	 * @param test
	 */
	public void setProblem( BProblemLocator test ){
		BFactory f = BFactory.instance();
		_problem = BCardExtractor.extract(test);
		
		_set1Sprites = new BSprite[_problem.set1().length];
		for( int i = 0 ; i < _set1Sprites.length ; i++ ){
			_set1Sprites[i] = f.create(_problem.set1()[i]);
		}
		_set2Sprites = new BSprite[_problem.set2().length];
		for( int i = 0 ; i < _set2Sprites.length ; i++ ){
			_set2Sprites[i] = f.create(_problem.set2()[i]);
		}
		_questionSprite = f.create( _problem.image1() );
		
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
		BAnimator animator = f.animator();
		
		
		_size = new BRectangle(0, 0, s*4, s*6);
				
		
		for (int i = 0; i < _set1Sprites.length; i++) {
			IBTransformAnimable a = _set1Sprites[i];
			animator.addAnimation( new BTranslateAnimation( f.point(s, s*(i+1) ), millis, a ) );
		}

		for (int i = 0; i < _set2Sprites.length; i++) {
			IBTransformAnimable a = _set2Sprites[i];
			animator.addAnimation( new BTranslateAnimation( f.point(s*3, s*(i+1) ), millis, a ) );
		}

		animator.addAnimation(
			new BCompoundTransformAnimation(
				new IBTransformAnimable[]{_questionSprite },
				new BTranslateAnimation( f.point(s*2, s*3) , millis ),
				new BRotateAnimation(2*Math.PI/millis, millis)
			)
		);
		
	}

	private void initialAnimation() {
		/*
		BFactory f = BFactory.instance();
		BAnimator animator = f.animator();
		
		animator.addAnimation(
			new BConcatenateAnimation( 
				new IBAnimable[]{ _questionSprite },
				new BFlipAnimation(Math.PI/500, 1000),
				new BRotateAnimation(Math.PI/500, 1000)
			)
		);
		
		animator.addAnimation( 
			new BConcatenateAnimation( 
				_set1Sprites,
				new BScaleAnimation(1.1,1.1, 500),
				new BScaleAnimation(1/1.1,1/1.1, 500),
				new BScaleAnimation(1.1,1.1, 500),
				new BScaleAnimation(1/1.1,1/1.1, 500)
			)
		);

		animator.addAnimation( 
			new BCompoundTransformAnimation(
				_set2Sprites,
				 new BRotateAnimation(Math.PI/500, 2000),
				 new BFlipAnimation(Math.PI/500, 2000)
			)
		);
		*/
	}
	
	/**
	 * 
	 */
	@Override
	public void draw(IBCanvas canvas, IBTransform aditionalTransform){
		if( _allSprites == null ){
			return;
		}
		
		for( BSprite s: _allSprites ){
			s.draw( canvas, aditionalTransform );
		}
	}
	
	@Override
	public IBRectangle size() {
		return _size;
	}
}
