package bongard.animation;

public class BWaitAnimation extends BFixedDurationAnimation{

	public BWaitAnimation(int totalMillis) {
		super(totalMillis, (IBAnimable)null);
	}

	@Override
	public void stepAnimation(long millis) {
		stepMillis(millis);
	}

}
