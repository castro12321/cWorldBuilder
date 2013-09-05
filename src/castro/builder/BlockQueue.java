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

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import castro.blocks.CBlock;
import castro.connector.CConnector;

public class BlockQueue
{
	//public Queue<CBlock> queue = new LinkedList<>();
	public Queue<CBlock> queue = new ArrayDeque<CBlock>(50000);
	
	public Player player;
	public boolean omitPerm = false;
	
	public BlockQueue(Player player)
	{
		this.player = player;
		
		CWBPlayer cplayer = CWorldBuilder.players.get(player.getName());
		if(cplayer != null)
		{
			omitPerm = cplayer.omitPerm;
		}
	}
	
	
	public void execute()
	{
		long start = System.currentTimeMillis();
		
		CWBWorlds.loadChunksForWorld(player.getWorld().getName());
		
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
				
				if(!CWBWorlds.loadChunk(b))
					continue;
				
				if(!omitPerm)
					if(!canBuild(b))
						continue;
				
				block.execute(b);
			}
		} catch(NoSuchElementException e) { return false; }
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
}
