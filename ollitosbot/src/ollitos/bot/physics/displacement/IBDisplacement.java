package ollitos.bot.physics.displacement;

import ollitos.bot.geom.IBLocation;
import ollitos.bot.physics.IBPhysicalItem;

public interface IBDisplacement extends IBDisplacementCause{
	IBDisplacementCause cause();
	IBPhysicalItem item();
	IBLocation delta();
	IBLocation finalDelta();
	void setFinalDelta(IBLocation finalDelta);
	void apply();
	
	/**
	 * Discard the displacement. Discard also the root cause, if necesary
	 */
	void discard();
	boolean discarded();
}
