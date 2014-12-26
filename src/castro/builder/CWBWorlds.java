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
		int minX = chunkX * 16;
		int maxX = minX + 16;
		int minZ = chunkZ * 16;
		int maxZ = minZ + 16;
	    if(CWorldBuilder.commandBorder.isOutside(minX, minZ)
	    && CWorldBuilder.commandBorder.isOutside(maxX, maxZ)
	    && CWorldBuilder.commandBorder.isOutside(minX, maxZ)
	    && CWorldBuilder.commandBorder.isOutside(maxX, minZ))
	        throw new IndexOutOfBoundsException("This chunk is outside world border");
		world.loadChunk(chunkX, chunkZ); // Try to load the chunk
	}
	
	public static Block getBlockAt(Location location)
	{
	    if(CWorldBuilder.commandBorder.isOutside(location.getBlockX(), location.getBlockZ()))
	        throw new IndexOutOfBoundsException("This chunk is outside world border");
	    return location.getBlock();
	}
	
	public static Block getBlockAt(World world, int x, int y, int z)
	{
	    if(CWorldBuilder.commandBorder.isOutside(x, z))
            throw new IndexOutOfBoundsException("This chunk is outside world border");
        return world.getBlockAt(x, y, z);
	}
}