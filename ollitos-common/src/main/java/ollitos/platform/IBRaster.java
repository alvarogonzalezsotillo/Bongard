package ollitos.platform;




public interface IBRaster extends IBDisposable{
	IBCanvas canvas();
	int w();
	int h();
}