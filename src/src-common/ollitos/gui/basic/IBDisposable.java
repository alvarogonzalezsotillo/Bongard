package ollitos.gui.basic;

import ollitos.platform.BFactory;

public interface IBDisposable {
	
	public static class Util{
		public static void disposeLater( final IBDisposable d ){
			BFactory.instance().game().animator().post( new Runnable(){
				@Override
				public void run() {
					d.dispose();
				}
			});
		}
	}
	
	public void setUp();
	public void dispose();
	public boolean disposed();
}
