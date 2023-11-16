package ollitos.bot.physics;

public class BPhysicalContact implements IBPhysicalContact{

	private IBPhysicalItem[] _items;

	public BPhysicalContact(IBPhysicalItem ... items ){
		_items = items;
	}
	
	@Override
	public IBPhysicalItem[] items() {
		return _items;
	}

}
