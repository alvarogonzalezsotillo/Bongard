package bongard.gui.game;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.container.BSlidableContainer;
import ollitos.gui.menu.*;
import ollitos.platform.BPlatform;
import ollitos.platform.IBGame;
import ollitos.platform.IBScreen;
import ollitos.platform.state.*;
import ollitos.gui.basic.IBTopDrawable;

import java.awt.event.ActionListener;

public class BSlidableBongardGame extends BSlidableContainer implements BState.Stateful, IBMenuHolder, IBTopDrawable{

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

    private IBMenuItem _helpMenuItem = new BMenuItem("Help"){
            @Override
            public void run() {
                IBScreen screen = platform().game().screen();
                BGameHelp d = platform().stateManager().restore(BGameHelp.class);
                screen.setDrawable( d );
            }
        };

    @Override
    public IBMenu menu(){
        BMenu ret = new BMenu();
        ret.addItem( _helpMenuItem);

        return ret;
    }
    
  private IBKeyValueDatabase _db;
	private static final String DATABASE = "BSlidableBongardGame";
	private static final String HELP_SHOWED = "hasHelpBeenShowed";
	
	private IBKeyValueDatabase database(){
		if (_db == null) {
			_db = BPlatform.instance().database( DATABASE );
		}
		return _db;
	}
    
   private IBKeyValueTable table() {
		return database().table(BSlidableBongardGame.class);
	}
    public void setAsTopDrawable(){
      
      byte[] helpShowed = table().getBytes(HELP_SHOWED);
      if( helpShowed == null ){
        table().putBytes( new byte[]{1}, HELP_SHOWED );
        _helpMenuItem.actionListener().run();
      }
    }
    
    public void removedAsTopDrawable(){
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
