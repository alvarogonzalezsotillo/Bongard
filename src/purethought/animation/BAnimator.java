package purethought.animation;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class BAnimator {

	private ArrayList<IBAnimation> _animations = new ArrayList<IBAnimation>();

	public void addAnimation(IBAnimation a){
		_animations.add(a);
	}
	
	/**
	 * 
	 * @param millis
	 */
	public void stepAnimations(long millis){
		for (Iterator<IBAnimation> ai = _animations.iterator(); ai.hasNext();) {
			IBAnimation a = ai.next();
			if( a.endReached() ){
				ai.remove();
			}
			else{
				a.stepAnimation(millis);
			}
		}
	}
}
