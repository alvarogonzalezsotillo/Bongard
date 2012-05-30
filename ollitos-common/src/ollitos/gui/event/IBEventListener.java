package ollitos.gui.event;

import ollitos.geom.Holder;


public interface IBEventListener extends Holder{
	boolean handle(IBEvent e);
}
