import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Acentuator {

	public static void main(String[] args) throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		OutputStreamWriter osw = new OutputStreamWriter(System.out, "UTF8");
		
		char tilderara = '\u0301';
		
		int c = isr.read();
		while( c != -1 ){
			osw.append((char)c);
			if( Character.isLetterOrDigit(c) ){
				osw.append(tilderara);
			}
			osw.flush();
			c = isr.read();
		}
		
	}
}
