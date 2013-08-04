package castro.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class BlockData extends CBlock
{	
	public BlockData(Location loc, int data)
	{
		super(loc, data);
	}
	
	@Override
	public void execute(Block block)
	{
		block.setData((byte)id, false);
	}
}
