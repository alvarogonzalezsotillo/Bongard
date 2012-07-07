package bongard.gui.game;

import ollitos.geom.BRectangle;
import ollitos.gui.container.BSlidableContainer;
import ollitos.platform.state.BState;

public class BSlidableBongardGame extends BSlidableContainer{

	private static BBongardGameModel createModel(){
		return new BBongardGameModel();
	}
	
	public BSlidableBongardGame(){
		this( BGameField.computeOriginalSize(), createModel() );
	}
	
	public BSlidableBongardGame(BRectangle s, BBongardGameModel m) {
		super(s,m);
	}

	@SuppressWarnings("serial")
	private static class MyState extends BState{
		private BBongardGameModel _myModel;
		private int _index;
		public MyState(BSlidableBongardGame fc) {
			_myModel = (BBongardGameModel) fc.model();
			_index = fc.currentIndex();
		}

		@Override
		public BSlidableContainer create() {
			BSlidableBongardGame ret = new BSlidableBongardGame(BGameField.computeOriginalSize(),_myModel);
			ret.setCurrent( _index );
			return ret;
		}
	};

	@Override
	public BState save() {
		return new MyState(this);
	}
}
