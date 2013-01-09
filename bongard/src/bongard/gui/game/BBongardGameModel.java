package bongard.gui.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ollitos.gui.basic.IBDrawable;
import ollitos.gui.container.IBSlidableModel;
import ollitos.gui.container.IBSlidablePage;
import ollitos.platform.BResourceLocator;
import bongard.problem.BCardExtractor;


@SuppressWarnings("serial")
public class BBongardGameModel implements IBSlidableModel{

	transient private BResourceLocator[] _resources;
	private BBongardTestField[] _drawables;
	
	private BResourceLocator[] resources(){
		if (_resources == null) {
			_resources = BCardExtractor.allProblems();
		}

		return _resources;
	}

	private BBongardTestField[] drawables(){
		if (_drawables == null) {
			_drawables = new BBongardTestField[resources().length];
			
		}

		return _drawables;
	}
	
	@Override
	public int width() {
		return resources().length;
	}

	@Override
	public IBSlidablePage page(int x) {
		BBongardTestField ret = drawables()[x];
		if( ret == null ){
			ret = new BBongardTestField(resources()[x]);
			drawables()[x] = ret;
		}
		return ret;
	}

	@Override
	public void dispose(int x) {
		if( drawables()[x] != null ){
			drawables()[x].dispose();
		}
	}

	@Override
	public IBDrawable background() {
		return null;
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
	}

	private void writeObject(ObjectOutputStream stream)	throws IOException {
		stream.defaultWriteObject();
	}

}
