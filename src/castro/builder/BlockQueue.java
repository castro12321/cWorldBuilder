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

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import castro.blocks.BlockData;
import castro.blocks.BlockId;
import castro.blocks.BlockIdAndData;
import castro.blocks.CBlock;
import castro.connector.CConnector;

public class BlockQueue
{
	public Queue<CBlock> queue = new ArrayDeque<CBlock>(50000);
	
	public final Player player;
	public final boolean omitPerm;
	
	public BlockQueue(Player player)
	{
		this.player = player;
		omitPerm = player.hasPermission("aliquam.admin");
	}
	
	public void execute()
	{
		long start = System.currentTimeMillis();
		
		while(System.currentTimeMillis() - start < 15)
		{
			if(!execute100())
				return;
		}
	}
	
	private boolean execute100()
	{
		try
		{
			for(int i = 0; i < 100; ++i)
			{
				CBlock block = queue.remove();
				Block b = block.getBlock();
				
				try
				{
				    CWBWorlds.loadChunk(b);
				}
				catch(IndexOutOfBoundsException e)
				{
				    queue.clear();
				    return false;
				}
				
				if(!omitPerm)
					if(!canBuild(b))
						continue;
				
				block.execute(b);
			}
		}
		catch (NoSuchElementException e)
		{
            return false;
        }
		return true;
	}
	
	
	public boolean isEmpty()
	{
		return queue.isEmpty();
	}
	
	
	public boolean canBuild(Block block)
	{
		return CConnector.worldguard.canBuild(player, block);
	}
	
	
	public void addBlock(byte data, Location loc)
	{
		queue.add(new BlockData(loc, data));
	}
	public void addBlock(int id, Location loc)
	{
		queue.add(new BlockId(loc, id));
	}
	public void addBlock(int id, byte data, Location loc)
	{
		queue.add(new BlockIdAndData(loc, id, data));
	}
	public void addBlock(CBlock block)
	{
		queue.add(block);
	}
}
