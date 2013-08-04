package castro.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class BlockId extends CBlock
{
	public BlockId(Location loc, int id)
	{
		super(loc, id);
	}
	
	@Override
	public void execute(Block block)
	{
		block.setTypeId(id, false);
	}
}
