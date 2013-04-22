package ollitos.bot.map;

import ollitos.bot.geom.BDirection;

public class BTestRoomReader extends BRoomReader{
	
	
	private String[][] _data;


	public BTestRoomReader( IBMap map, String[][] data ){
		super(map);
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


	public static String[][][] ROOMS = { BALLTEST, DOORTEST, SMALLROOM, CONVEYORBELTS, UNIDIMENSIONAL,  BIGROOM };
		
		
	public BRoom createRoomFromData(String[][] data ){	
		
		addLayers(data);
		
		return room();
	}

	public void addLayers(String[][] layers) {
		for (int i = 0; i < layers.length; i++) {
			String[] layer = layers[i];
			addLayer(i, layer);
		}
	}
	
	public BRoom createRoom_code(){		
		BRoom room = room();
		
		int cols = 9;
		int rows = 9;
		
		for( int x = 0 ; x < cols ; x++ ){
			for( int y = 0 ; y < rows ; y++ ){
				BMapItem f = BItemType.floor.createItem(room);
				traslateBasicBlocks(f,cols-x-1,rows-y-1,0);
			}
		}
		
		{
			BMapItem block1 = BItemType.floor.createItem(room);
			traslateBasicBlocks(block1,1,1,1);
			BMapItem block2 = BItemType.floor.createItem(room);
			traslateBasicBlocks(block2,1,1,2);
		}
		{
			BMapItem block1 = BItemType.floor.createItem(room);
			traslateBasicBlocks(block1,4,2,1);
			BMapItem block2 = BItemType.floor.createItem(room);
			traslateBasicBlocks(block2,4,2,2);
		}
		{
			BMapItem block1 = BItemType.floor.createItem(room);
			traslateBasicBlocks(block1,rows-2,cols-2,1);
			BMapItem block2 = BItemType.floor.createItem(room);
			traslateBasicBlocks(block2,rows-2,cols-2,2);
		}

		
		
		for( int x = 0 ; x < cols ; x++ ){
			for( int y = 0 ; y < rows ; y++ ){
				if( x == 0 || x == cols-1 || y == 0 || y == rows-1 ){
					BMapItem f = BItemType.floor.createItem(room);
					traslateBasicBlocks(f,cols-x-1,rows-y-1,1);
				}
			}
		}
		
		BMapItem boot1 = BItemType.centinel_clockwise.createItem(room);
		traslateBasicBlocks(boot1,1,2,1);
		
		BMapItem boot2 = BItemType.centinel_counterclockwise.createItem(room);
		traslateBasicBlocks(boot2,3,4,1);
		boot2.rotateTo(BDirection.north);


		
		return room;
	}
	
	
	@Override
	protected BRoom populateRoom(){
		return createRoomFromData(_data);
	}

}
