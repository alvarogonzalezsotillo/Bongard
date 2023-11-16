package ollitos.bot.map;


public interface IBMapReader {
	IBMap readMap();
	BRoom readRoom(String id);
}
