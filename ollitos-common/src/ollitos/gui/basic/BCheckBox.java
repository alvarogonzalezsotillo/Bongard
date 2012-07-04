package ollitos.gui.basic;

import java.util.ArrayList;
import java.util.List;


public class BCheckBox extends BButton{

	public interface StateListener{
		void stateChanged(BCheckBox b);
	}

	private List<StateListener> _stateListeners = new ArrayList<StateListener>();

	public void addStateListener( StateListener l ){
		if( !_stateListeners.contains(l) ){
			_stateListeners.add(l);
		}
	}
	
	public void removeStateListener( StateListener l ){
		_stateListeners.remove(l);
	}
	
	private StateListener[] listeners(){
		return (StateListener[]) _stateListeners.toArray(new StateListener[_stateListeners.size()]);
	}

	protected void stateChanged(){
		platform().logger().log( this, "stateChanged" );
		if( _stateListeners != null ){
			for (StateListener l : listeners() ) {
				l.stateChanged(this);
			}
		}
	}
	
	
	private static ClickedListener _clickListener = new ClickedListener() {
		@Override
		public void clicked(BButton b) {
			BCheckBox c = (BCheckBox) b;
			int i = c.state();
			i = (i+1)%c.possibleStates();
			c.setState(i);
		}
	};
	
	private IBDrawable[] _stateDrawables;
	private int _state;
	
	public BCheckBox(IBDrawable ... stateDrawables) {
		super(stateDrawables[0]);
		_stateDrawables = stateDrawables;
		addClickedListener(_clickListener);
		setState( 0 );
	}
	
	public int state(){
		return _state;
	}
	
	public void setState(int state){
		int i = state;
		if( _stateDrawables.length < i ){
			throw new IllegalArgumentException( "No drawable for state:" + state );
		}
		_state = state;
		setDrawable( _stateDrawables[i] );
		stateChanged();
		platform().game().screen().refresh();
	}
	
	public int possibleStates(){
		return _stateDrawables.length;
	}
}
