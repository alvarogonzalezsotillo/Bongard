package purethought.gui;

import purethought.animation.BAnimator;
import purethought.animation.BFlipAnimation;
import purethought.animation.BRotateAnimation;
import purethought.animation.BTransformAnimation;
import purethought.animation.BCompoundTransformAnimation;
import purethought.animation.IBTransformAnimable;
import purethought.problem.BCardExtractor;
import purethought.problem.BProblem;
import purethought.problem.BProblemLocator;

public class BGameField {
	private IBCanvas _canvas;
	private BProblem _problem;
	
	private BSprite[] _set1Sprites;
	private BSprite[] _set2Sprites;
	private BSprite _questionSprite;
	private BSprite[] _allSprites;

	/**
	 * 
	 * @param canvas
	 */
	public BGameField( IBCanvas canvas ){
		_canvas = canvas;
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
		_set1Sprites[0].translate(s, s*1);
		_set1Sprites[1].translate(s, s*2);
		_set1Sprites[2].translate(s, s*3);
		_set1Sprites[3].translate(s, s*4);
		_set1Sprites[4].translate(s, s*5);

		_set2Sprites[0].translate(s*3, s*1);
		_set2Sprites[1].translate(s*3, s*2);
		_set2Sprites[2].translate(s*3, s*3);
		_set2Sprites[3].translate(s*3, s*4);
		_set2Sprites[4].translate(s*3, s*5);
		
		_questionSprite.translate(s*2, s*3);
		
		BFactory f = BFactory.instance();
		BAnimator animator = f.animator();
//		animator.addAnimation(
//			new BCompoundTransformAnimation( 
//				new IBTransformAnimable[]{ _questionSprite },
//				new BRotateAnimation(Math.PI/2),
//				new BFlipAnimation(Math.PI/4000, 10*1000)
//			)
//		);
		
		animator.addAnimation( new BFlipAnimation(2*Math.PI/1000, 4*1000, _questionSprite ) );
	}
	
	/**
	 * 
	 */
	public void draw(){
		if( _allSprites == null ){
			return;
		}
		
		IBCanvas canvas = canvas();
		for( BSprite s: _allSprites ){
			s.draw( canvas );
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public IBCanvas canvas(){
		return _canvas;
	}
}
