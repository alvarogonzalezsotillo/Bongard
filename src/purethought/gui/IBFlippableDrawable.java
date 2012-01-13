package purethought.gui;

public interface IBFlippableDrawable extends IBTopDrawable{
	BFlippableContainer flippableContainer();
	void setFlippableContainer( BFlippableContainer c );
	void showed();
	void hided();
}
