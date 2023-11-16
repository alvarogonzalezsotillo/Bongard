package ollitos.bot.map;

public interface IBMap {
	IBMapReader mapReader();
	BRoom initialRoom();
	BRoom room(String id);
}
