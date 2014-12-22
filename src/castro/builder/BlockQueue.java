/* cWorldBuilder
 * Copyright (C) 2013 Norbert Kawinski (norbert.kawinski@gmail.com)

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
import castro.cWorlds.MemberType;
import castro.cWorlds.PlotsMgr;
import castro.connector.CConnector;

public class BlockQueue
{
	public Queue<CBlock> queue = new ArrayDeque<CBlock>(50000);
	
	public final Player player;
	public final boolean omitPerm;
	
	public BlockQueue(Player player)
	{
		this.player = player;
		omitPerm = 
		        player.hasPermission("aliquam.admin")
		     || PlotsMgr.get(player.getWorld()).is(player, MemberType.MEMBER);
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
		    //long cps = System.currentTimeMillis();
		    
			for(int i = 0; i < 100; ++i)
			{
			    CBlock block = queue.remove();
				Block b = block.getBlock();
				/*
				try
				{
				    CWBWorlds.loadChunk(b);
				}
				catch(IndexOutOfBoundsException e)
				{
				    queue.clear();
				    return false;
				}
				*/
				
				if(i == 0)
				    if(!omitPerm)
				        if(!canBuild(b))
				        {
				            queue.clear();
				            return false;
				        }
				block.execute(b);
			}
			
			//long cpe = System.currentTimeMillis();
			//CWorldBuilder.get().log("exec100: " + (cpe-cps) + "ms");
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
