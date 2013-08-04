package castro.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class BlockIdAndData extends CBlock
{
	byte data;
	
	public BlockIdAndData(Location loc, int id, byte data)
	{
		super(loc, id);
		this.data = data;
	}
	
	@Override
	public void execute(Block block)
	{
		block.setTypeIdAndData(id, data, false);
	}
}
