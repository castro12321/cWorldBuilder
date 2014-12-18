/* cWorldBuilder
 * Copyright (C) 2013 Norbert Kawinski (norbert.kawinski@gmail.com)

 */

package castro.builder;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class CWBWorlds
{
	public static void loadChunk(Block block)
	{
		loadChunk(block.getWorld(), block.getX() >> 4, block.getZ() >> 4);
	}
	
	public static void loadChunk(World world, int chunkX, int chunkZ)
	{
	    if(CWorldBuilder.commandBorder.isOutsideLimit(chunkX, chunkZ))
	        throw new IndexOutOfBoundsException("This chunk is outside world border");
		world.loadChunk(chunkX, chunkZ); // Try to load the chunk
	}
	
	public static Block getBlockAt(Location location)
	{
	    if(CWorldBuilder.commandBorder.isOutsideLimit(location.getBlockX() >> 4, location.getBlockZ() >> 4))
	        throw new IndexOutOfBoundsException("This chunk is outside world border");
	    return location.getBlock();
	}
	
	public static Block getBlockAt(World world, int x, int y, int z)
	{
	    if(CWorldBuilder.commandBorder.isOutsideLimit(x >> 4, z >> 4))
            throw new IndexOutOfBoundsException("This chunk is outside world border");
        return world.getBlockAt(x, y, z);
	}
}