package ollitos.gui.event;

import ollitos.geom.IBTransformHolder;


public interface IBEventListener extends IBTransformHolder{
	boolean handle(IBEvent e);
}
