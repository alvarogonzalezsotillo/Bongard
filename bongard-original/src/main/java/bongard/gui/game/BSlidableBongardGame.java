package bongard.gui.game;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.container.BSlidableContainer;
import ollitos.gui.menu.*;
import ollitos.platform.BPlatform;
import ollitos.platform.IBGame;
import ollitos.platform.IBScreen;
import ollitos.platform.state.BState;

import java.awt.event.ActionListener;

public class BSlidableBongardGame extends BSlidableContainer implements BState.Stateful, IBMenuHolder{

	private static BBongardGameModel createModel(){
		return new BBongardGameModel();
	}
	
	public BSlidableBongardGame(){
		this( BBongardTestField.computeOriginalSize(), createModel() );
	}

    public BSlidableBongardGame(IBRectangle s, BBongardGameModel m) {
        super(s,m);
    }

    public BSlidableBongardGame(BBongardGameModel m) {
        this(BBongardTestField.computeOriginalSize(),m);
    }

    @Override
    public IBMenu menu(){
        BMenu ret = new BMenu();
        ret.addItem( new BMenuItem("Help"){
            @Override
            public void run() {
                IBScreen screen = BPlatform.instance().game().screen();
                BGameHelp d = platform().stateManager().restore(BGameHelp.class);
                screen.setDrawable( d );
            }
        });

        return ret;
    }

    @SuppressWarnings("serial")
	private static class MyState extends BState<BSlidableBongardGame>{
		private BBongardGameModel _myModel;
		private int _index;
		public MyState(BSlidableBongardGame fc) {
			_myModel = (BBongardGameModel) fc.model();
			_index = fc.currentIndex();
		}

		@Override
		public BSlidableContainer create() {
			BSlidableBongardGame ret = new BSlidableBongardGame(_myModel);
			ret.setCurrent( _index );
			return ret;
		}
	};

	@Override
	public BState save() {
		return new MyState(this);
	}
}
