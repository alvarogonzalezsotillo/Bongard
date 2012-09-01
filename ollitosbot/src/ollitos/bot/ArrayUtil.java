package ollitos.bot;

public class ArrayUtil {
	public static <T> int arraySearch(T[] directions, T direction) {
		for (int i = 0; i < directions.length; i++) {
			if(directions[i]==direction){
				return i;
			}
		}
		return -1;
	}
}
