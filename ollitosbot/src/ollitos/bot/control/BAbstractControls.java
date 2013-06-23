package ollitos.bot.control;


import java.util.HashMap;
import java.util.Map;

public class BAbstractControls implements IBPhysicsControl{

    private Map<String, BUserButton> _buttons = new HashMap<String,BUserButton>();

    private void addButton(BUserButton b){
        _buttons.put( b.name(), b );
    }

    private void init(){
        addButton( new BUserButton("east") );
        addButton( new BUserButton("west") );
        addButton( new BUserButton("north") );
        addButton( new BUserButton("south") );
        addButton( new BUserButton("forward") );
        addButton( new BUserButton("left") );
        addButton( new BUserButton("right") );
        addButton( new BUserButton("jump") );
    }

    protected BUserButton button(String s){
        return _buttons.get(s);
    }

    public boolean buttonPressed(String s){
        return button(s).pressed();
    }
}
