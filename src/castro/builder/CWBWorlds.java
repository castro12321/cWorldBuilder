/* cWorldBuilder
 * Copyright (C) 2013 Norbert Kawinski (norbert.kawinski@gmail.com)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
	public static void loadChunk(World world, int x, int z)
	{
	    if(CWorldBuilder.commandBorder.isOutsideLimit(x, z))
	        throw new IndexOutOfBoundsException("This chunk is outside world border");
		world.loadChunk(x, z); // Try to load the chunk
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