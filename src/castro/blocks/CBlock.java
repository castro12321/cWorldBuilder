/* cWorldBuilder
 * Copyright (C) 2013 Norbert Kawinski (norbert.kawinski@gmail.com)

 */

package castro.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;

public abstract class CBlock
{
	public Location loc;
	
	public CBlock(Location loc)
	{
		this.loc = loc;
	}
	
	
	public abstract void execute(Block block);
	
	
	public Block getBlock()
	{
		return loc.getWorld().getBlockAt(loc);
	}
}
