package ollitos.gui.event;


public interface IBEventConsumer {
	IBEventListener listener();
	
	public static class Util{
		public static IBEventConsumer consumer(Object obj){
			return obj instanceof IBEventConsumer ? (IBEventConsumer)obj : null;
		}
	}
}
