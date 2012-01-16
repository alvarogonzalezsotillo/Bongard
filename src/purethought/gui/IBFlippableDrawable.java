package purethought.gui;

public interface IBFlippableDrawable extends IBDrawableContainer{
	BFlippableContainer flippableContainer();
	void setFlippableContainer( BFlippableContainer c );
	void showed();
	void hided();
}
