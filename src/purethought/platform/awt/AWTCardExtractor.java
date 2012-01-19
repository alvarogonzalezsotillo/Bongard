package purethought.platform.awt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Formatter;

import javax.imageio.ImageIO;

import purethought.geom.IBRectangle;
import purethought.gui.IBRaster;
import purethought.problem.BCardExtractor;
import purethought.problem.BProblem;
import purethought.problem.BProblemLocator;
import purethought.util.BException;


public class AWTCardExtractor extends BCardExtractor{

	/**
	 * 
	 */
	@Override
	public IBRaster getTestImage(BProblemLocator test) {
		return readImage( test.getImpl(URL.class), Color.white );
	}

	/**
	 * Extract one tile of a Bongard problem
	 * @param r
	 * @param i
	 * @return
	 */
	@Override
	protected IBRaster extract( IBRectangle r, IBRaster i ){
		BufferedImage ret = new BufferedImage((int)r.w(), (int)r.h(), BufferedImage.TYPE_INT_RGB);
		Graphics g = ret.getGraphics();
		Color bgcolor = Color.white;

		int x = (int) -r.x();
		int y = (int) -r.y();
		
		Image img = i.getImpl(Image.class);
		g.drawImage(img, x, y, bgcolor, null);
		g.dispose();
		
		return new AWTRaster(ret);
	}
	
	private static IBRaster readImage(URL f, Color bgColor){
		try {
			BufferedImage image = ImageIO.read(f);
			BufferedImage ret = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics g = ret.getGraphics();
			g.drawImage(image, 0, 0, bgColor, null );
			g.dispose();
			
			return new AWTRaster(ret);
		} catch (IOException ex) {
			throw new BException( "Unable to read:" + f, ex );
		}
	}
	
	private static void writeImage( File f, IBRaster image ) throws IOException{
		String format = "png";
		RenderedImage i = image.getImpl(RenderedImage.class);
		ImageIO.write(i,format,f);
	}
	

	public static void main(String[] args) throws IOException {
		URL imageFile = AWTCardExtractor.class.getResource("/tests/p004.png");

		BProblemLocator test = new BProblemLocator(imageFile);
		
		BProblem problem = extract(test);
		System.out.println( problem );
		
		for( int j= 0 ; j < problem.aImages().length ; j++ ){
			IBRaster card = problem.aImages()[j];
			File f = new File("a"+j+".png");
			writeImage(f, card);
		}
		
		for( int j= 0 ; j < problem.bImages().length ; j++ ){
			IBRaster card = problem.bImages()[j];
			File f = new File("b"+j+".png");
			writeImage(f, card);
		}
	}
	

	@Override
	public BProblemLocator[] allProblems(){
		int count = 280;
		BProblemLocator[] ret = new BProblemLocator[280];
		for( int i = 0 ; i < count ; i++ ){
			Formatter f = new Formatter();
			f.format("/tests/p%03d.png", i+1 );
			URL file = getClass().getResource(f.toString());
			ret[i] = new BProblemLocator(file);
		}
		return ret;
	}


}
