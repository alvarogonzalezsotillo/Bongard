package ollitos.platform.awt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import ollitos.geom.IBRectangle;
import ollitos.gui.basic.IBRaster;
import ollitos.platform.BResourceLocator;

import bongard.problem.BCardExtractor;
import bongard.problem.BProblem;



public class AWTCardExtractor extends BCardExtractor{


	/**
	 * Extract one tile of a Bongard problem
	 * @param r
	 * @param i
	 * @return
	 */
	@Override
	public IBRaster extract( IBRectangle r, IBRaster i ){
		BufferedImage ret = new BufferedImage((int)r.w(), (int)r.h(), BufferedImage.TYPE_INT_RGB);
		Graphics g = ret.getGraphics();
		Color bgcolor = Color.white;

		int x = (int) -r.x();
		int y = (int) -r.y();
		
		Image img = ((AWTRaster)i).image();
		g.drawImage(img, x, y, bgcolor, null);
		g.dispose();
		
		return new AWTRaster(ret);
	}
	
	
	private static void writeImage( File f, AWTRaster image ) throws IOException{
		String format = "png";
		RenderedImage i = (RenderedImage) image.image();
		ImageIO.write(i,format,f);
	}
	

	public static void main(String[] args) throws IOException {
		URL imageFile = AWTCardExtractor.class.getResource("/images/tests/p004.png");

		BResourceLocator test = new BResourceLocator(imageFile);
		
		BProblem problem = new BProblem(test);
		System.out.println( problem );
		
		for( int j= 0 ; j < problem.aImages().length ; j++ ){
			IBRaster card = problem.aImages()[j];
			File f = new File("a"+j+".png");
			writeImage(f, (AWTRaster) card);
		}
		
		for( int j= 0 ; j < problem.bImages().length ; j++ ){
			IBRaster card = problem.bImages()[j];
			File f = new File("b"+j+".png");
			writeImage(f, (AWTRaster) card);
		}
	}
	

}
