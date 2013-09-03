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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import castro.blocks.BlockData;
import castro.blocks.BlockId;
import castro.blocks.BlockIdAndData;
import castro.blocks.CBlock;
import castro.connector.CConnector;

// TODO: maybe later? caching? check performance
/*
class NewBlockQueue
{
	private class NewBlockQueuePart
	{
		// 100 blocks coordinates
		// [x1, y1, z1,
		//  x2, y2, z2,
		//  x3, y3, z3,
		//  ... ]
		public int[] partX = new int[300];
	}
	
	List<NewBlockQueuePart> parts = new List<>();
}
*/


class SimpleQueue
{
	CBlock block;
	Queue<Location> locations = new LinkedList<>();;
	
	
	public SimpleQueue(CBlock pattern)
	{
		this.block = pattern;
	}
	
	
	public boolean offer(Location location)
	{
		return locations.offer(location);
	}
	
	
	public Location poll()
	{
		return locations.poll();
	}
}


public class BlockQueue
{
	// TODO: while //move or //paste need to use LinkedHashMap
	public HashMap<CBlock, SimpleQueue> groupedQueue = new HashMap<>();
	
	public Player	player;
	public boolean	omitLog  = false;
	public boolean	omitPerm = false;
	
	
	public BlockQueue(Player player)
	{
		CWBPlayer cplayer = CWorldBuilder.players.get(player.getName());
		
		if(cplayer != null)
			init(player, cplayer.omitLog, cplayer.omitPerm);
		init(player, false, false);
	}
	
	
	public BlockQueue(Player player, World world, boolean omitLog, boolean omitPerm)
	{
		init(player, omitLog, omitPerm);
	}
	
	
	private void init(Player player, boolean omitLog, boolean omitPerm)
	{
		this.player		= player;
		this.omitLog	= omitLog;
		this.omitPerm	= omitPerm;
	}
	
	
	private static BlockId blockId = new BlockId(0);
	public void addBlock(int id, Location location)
	{
		blockId.id = id;
		addBlock(blockId.getCloned(), location);
	}
	
	private static BlockData blockData = new BlockData((byte)0);
	public void addBlock(byte data, Location location)
	{
		blockData.data = data;
		addBlock(blockData.getCloned(), location);
	}
	
	private static BlockIdAndData blockIdData = new BlockIdAndData(0, (byte)0);
	public void addBlock(int id, byte data, Location location)
	{
		blockIdData.id = id;
		blockIdData.data = data;
		addBlock(blockIdData.getCloned(), location);
	}
	
	public void addBlock(CBlock block, Location location)
	{		
		SimpleQueue simpleQueue = groupedQueue.get(block);
		if(simpleQueue == null)
		{
			CWorldBuilder.get().log("origin: " + block.getClass().getCanonicalName());
			simpleQueue = new SimpleQueue(block);
			groupedQueue.put(block, simpleQueue);
		}
		simpleQueue.offer(location);
	}
	
	
	public void run()
	{	
		long start = System.currentTimeMillis();
		
		CWBWorlds.loadChunksForWorld(player.getWorld().getName());
		Iterator<SimpleQueue> iterator = groupedQueue.values().iterator();
		
		if(!iterator.hasNext())
			return;
		SimpleQueue queue = iterator.next();
		
		while(System.currentTimeMillis() - start < 15)
			if(!run_impl(queue))
			{
				iterator.remove();
				
				if(iterator.hasNext())
					queue = iterator.next();
				else
					return;
			}
	}
	
	
	private boolean run_impl(SimpleQueue queue)
	{
		CWorldBuilder.get().log("CP1 " + queue.locations.size());
		CBlock pattern = queue.block;
		CWorldBuilder.get().log("CP2 " + pattern);
		
		for(int i = 0; i < 100; ++i)
		{
			CWorldBuilder.get().log("CP3 " + i);
			Location location = queue.poll();
			
			if(location == null)
				return false;
			
			if(!CWBWorlds.loadChunk(location))
				continue;
			
			if(!omitPerm)
				if(!canBuild(location))
					continue;
			
			pattern.execute(location);
		}
		
		return true;
	}
	
	
	public boolean canBuild(Location location)
	{
		return CConnector.worldguard.canBuild(player, location);
	}
	
	
	public boolean isEmpty()
	{
		return groupedQueue.isEmpty();
	}
	
	
	public int size()
	{
		int size = 0;
		
		Iterator<SimpleQueue> it = groupedQueue.values().iterator();
		while(it.hasNext())
			size += it.next().locations.size();
		
		return size;
	}
}
