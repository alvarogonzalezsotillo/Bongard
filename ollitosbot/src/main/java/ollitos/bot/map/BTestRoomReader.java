package ollitos.bot.map;


public class BTestRoomReader{
	
	
	private String[][] _data;


	public BTestRoomReader( String[][] data ){
		_data = data;
	}
	

	public static String[][] BIGROOM = {
			{
				"fl fl fl fl fl fl fl fl",	
				"fl fl fl fl fl fl fl fl",	
				"fl fl fl fl fl fl fl fl",	
				"fl fl fl fl fl fl fl fl",	
				"fl fl fl fl fl fl fl fl",	
				"fl fl fl fl fl fl fl fl",	
				"fl fl fl fl fl fl fl fl",	
				"fl fl fl fl fl fl fl fl",
			},
			{
				"fl fl fl fl fl fl fl fl",
				"pu .  he  .   .  .   .  pu",
				".  .  .   .   .  cws .  .",
				".  .  .   .   .  .   .  cas",
				"bx .  ccn .   .  cww .  .",
				".  .  .   cce .  .   .  .",
				".  .  .   .   bx .   .  .",
				"pu .  cae .   .  .   .  pu",
			},
			{
				"fl fl fl fl fl fl fl fl",
				".  .  .   .  .  .   .  .",
				".  .  bx  .  .  .   .  .",
				".  .  .   .  .  bx  .  .",
				".  .  .   .  .  .   .  ba",
				".  .  .   .  .  .   .  .",
				".  bx .   .  .  .   .  .",
				".  .  .   ba .  .   .  .",
			},
			{
				"fl fl fl fl fl fl fl fl",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
			},
			{
				"fl fl fl fl fl fl fl fl",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
			},
			{
				"fl fl fl fl fl fl fl fl",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
			},
			{
				"fl fl fl fl fl fl fl fl",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  bk  .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
			},
		};
		
	public static String[][] SMALLROOM = {
			{
				"fl fl fl fl",
				"fl fl fl fl",
				"fl fl fl fl",
				"fl fl fl fl ",
			},
			{
				"cw .  .  . ",
				".  cc .  bx",
				".  .  .  db ",
				"bx .  db  . ",
			},
		};

	public static String[][] CONVEYORBELTS = {
				{	
					"fl fl fl",
					"fl fl fl",
					"fl fl fl",
					"fl fl db",
				},
				
				{
					".   .   . ",
					".   .   . ",
					".   .   . ",
					"bee bee . "
				},
				{
					"bes .   .",
					"bes .   . ",
					"bes .   . ",
					".   .   . "
				},
				{
					".   bew bew",
					".   .   . ",
					".   .   . ",
					".   .   . "
				},
				{
					".   .   . ",
					".   .   . ",
					".   .   . ",
					".   .   . "
				},
				{
					".   .   bx ",
					".   .   . ",
					".   .   . ",
					".   .   . "
				},
				{
					".   .   bx ",
					".   .   . ",
					".   .   . ",
					".   .   . "
				},
				{
					".   .   bx ",
					".   .   . ",
					".   .   . ",
					".   .   . "
				},
				{
					".   .   . ",
					".   .   . ",
					".   .   . ",
					".   .   . "
				},
				{
					".   .   bx ",
					".   .   . ",
					".   .   . ",
					".   .   . "
				},
				{
					".   .   bx",
					".   .   . ",
					".   .   . ",
					".   .   . "
				},
			};

		
		
	public static String[][] UNIDIMENSIONAL = {
			{
				"be"
			},
			{
				"."
			},
			{
				"."
			},
			{
				"."
			},
			{
				"."
			},
			{
				"."
			},
			{
				"."
			},
			{
				"bx"
			},
			{
				"."
			},
			{
				"bx"
			},
			{
				"."
			},
			{
				"bx"
			},
		};
			
		public static String[][] BALLTEST = {
			{
				"fl fl fl fl fl fl fl fl",
				"fl fl fl fl fl fl fl fl",
				"fl fl fl fl fl fl fl fl",
				"fl fl fl fl fl fl fl fl",
				"fl fl fl fl fl fl fl fl",
				"fl fl fl fl fl fl fl fl",
				"fl fl fl fl fl fl fl fl",
				"fl fl fl fl fl fl bee fl",
			},
			{
				"pu  .   .  .  .  .  .  pu",
				".   .   .  .  .  .  .  .",
				".   he  .  .  .  .  .  bk",
				".   ba   .  .  .  .  .  .",
				".   ba    bk  .  ba  .  .",
				".   ba   .  .  .  .  .  .",
				".   ba   .  .  .  .  .  .",
				"pu  .   .  bk  .  .  .  pu",
			},
			
		};
		
		public static String[][] DOORTEST = {
			{
				"fl fl fl fl fl fl fl fl",
				"fl fl fl fl fl fl fl fl",
				"fl fl fl fl fl fl fl fl",
				"fl fl fl fl fl fl fl fl",
				"fl fl fl fl fl fl fl fl",
				"fl fl fl fl fl fl fl fl",
				"fl fl fl fl fl fl fl fl",
				"fl fl fl fl fl fl fl fl",
			},
			{
				".   don .   don .   .   don .",
				".   .   he  .   .   .   .   .",
				".   .   .   don .   .   .   .",
				".   .   dow .   doe .   .   doe",
				"dow .   .   dos .   .   .   .",
				".   .   .   .   .   .   .   .",
				".   .   .   .   .   .   .   .",
				".   dos .   dos .   .   dos .",
			},
		};

}
