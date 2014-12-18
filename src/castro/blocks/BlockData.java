/* cWorldBuilder
 * Copyright (C) 2013 Norbert Kawinski (norbert.kawinski@gmail.com)

 */

package castro.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class BlockData extends CBlock
{
	byte data;
	
	public BlockData(Location loc, int data)
	{
		super(loc);
		this.data = (byte)data;
	}
	
	@Override
	public void execute(Block block)
	{
		block.setData(data, true);
	}
}
