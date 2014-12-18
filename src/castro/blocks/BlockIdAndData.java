/* cWorldBuilder
 * Copyright (C) 2013 Norbert Kawinski (norbert.kawinski@gmail.com)
 */

package castro.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class BlockIdAndData extends CBlock
{
	public int id;
	byte data;
	
	public BlockIdAndData(Location loc, int id, byte data)
	{
		super(loc);
		this.id = id;
		this.data = data;
	}
	
	@Override
	public void execute(Block block)
	{
		block.setTypeIdAndData(id, data, true);
	}
}
