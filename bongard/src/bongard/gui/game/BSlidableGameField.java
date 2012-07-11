package bongard.gui.game;

import ollitos.geom.IBRectangle;
import ollitos.gui.container.BSlidableContainer;
import ollitos.platform.state.BState;

public class BSlidableGameField extends BSlidableContainer{

	public BSlidableGameField(){
		super(BGameField.computeOriginalSize(), BGameModel.initialLevel());
	}
	
	public BSlidableGameField(IBRectangle r, BGameModel m) {
		super(r,m);
	}
	
	@SuppressWarnings("serial")
	private static class MyState extends BState{
		private BGameModel _myModel;
		private int _index;
		public MyState(BSlidableGameField fc) {
			_myModel = (BGameModel) fc.model();
			_index = fc.currentIndex();
		}

		@Override
		public BSlidableGameField create() {
			BSlidableGameField ret = new BSlidableGameField(BGameField.computeOriginalSize(),_myModel);
			ret.setCurrent( _index );
			return ret;
		}
	};

	@Override
	public BState save() {
		return new MyState(this);
	}


}
