package ollitos.platform;


public interface IBDisposable {
	
	public static class Util{
		public static void disposeLater( final IBDisposable d ){
			BPlatform.instance().game().animator().post( new Runnable(){
				@Override
				public void run() {
					d.dispose();
				}
			});
		}
		public static void setUpLater( final IBDisposable d ){
			BPlatform.instance().game().animator().post( new Runnable(){
				@Override
				public void run() {
					d.setUp();
				}
			});
		}
	}
	
	public void setUp();
	public void dispose();
	public boolean disposed();
}
