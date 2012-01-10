package purethought.awt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import purethought.gui.IBRaster;
import purethought.gui.IBRectangle;
import purethought.problem.BCardExtractor;
import purethought.problem.BProblem;
import purethought.problem.BProblemLocator;
import purethought.util.BException;


public class AWTCardExtractor extends BCardExtractor{

	/**
	 * 
	 */
	@Override
	protected IBRaster getTestImage(BProblemLocator test) {
		return readImage( test.getImpl(File.class) );
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
		
		return new AWTRaster(ret);
	}
	
	private static IBRaster readImage(File f){
		BufferedImage image;
		try {
			image = ImageIO.read(f);
			return new AWTRaster(image);
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
		File imageFile = new File( "./2bpp-png/p004.png");

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

}
