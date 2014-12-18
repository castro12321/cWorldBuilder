/* cWorldBuilder
 * Copyright (C) 2013 Norbert Kawinski (norbert.kawinski@gmail.com)

 */

package castro.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class BlockId extends CBlock
{
	public int id;
	
	public BlockId(Location loc, int id)
	{
		super(loc);
		this.id = id;
	}
	
	@SuppressWarnings("deprecation")
    @Override
	public void execute(Block block)
	{
		block.setTypeId(id, true);
	}
}
