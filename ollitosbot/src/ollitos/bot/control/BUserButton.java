package ollitos.bot.control;


public class BUserButton {
    public static enum State{
        pressed, released
    }

    private State _state;
    private String _name;

    public BUserButton(String name){
        _name = name;
    }

    public String name(){
        return _name;
    }

    public void setState( State state ){
        _state = state;
    }

    public State state(){
        return _state;
    }

    public boolean pressed(){
        return _state == State.pressed;
    }


}
