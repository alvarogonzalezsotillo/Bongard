package bongard.gui.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import bongard.problem.BCardExtractor;

import ollitos.gui.container.IBFlippableDrawable;
import ollitos.gui.container.IBFlippableModel;
import ollitos.platform.BFactory;
import ollitos.platform.BResourceLocator;


@SuppressWarnings("serial")
public class BBongardGameModel implements IBFlippableModel{

	private BResourceLocator[] _resources;
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
	public IBFlippableDrawable drawable(int x) {
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
	public BResourceLocator background() {
		return null;
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
	}

	private void writeObject(ObjectOutputStream stream)	throws IOException {
	}

}
