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
import java.util.HashMap;
import java.util.Queue;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import castro.base.plugin.CPlugin;
import castro.base.plugin.CPluginSettings;
import castro.blocks.CBlock;

import com.sk89q.worldedit.Vector;


public class CWorldBuilder extends CPlugin implements Runnable
{
	private static CWorldBuilder instance;
	
	public static Player commandPlayer;
	public static World  commandWorld;
	
	//public static Player executePlayer; // current player while executing run()
	public static HashMap<String, CWBPlayer> players = new HashMap<>();
	
	private static Queue<BlockQueue>  queues = new ArrayDeque<BlockQueue>();
	private static Queue<BlockQueue> lqueues = new ArrayDeque<BlockQueue>();
	public static BlockQueue lastQueue; // for adding
	
	
	private static boolean eq(Player p1, Player p2) { return p1.getName().equals(p2.getName()); }
	private static void sendMsg(Player player) { get().sendMessage(player, "&cMozesz miec tylko jedna aktywna kolejke. Wpisz &a/cwb stop &caby zatrzymac kolejke."); }
	public static boolean addQueue(Player p, boolean message)
	{
		flush(); // In case we have another queue
		
		// Check if player is already in queue
		for(BlockQueue q :  queues) if(eq(q.player, p)) { if(message) sendMsg(p); return false; }
		for(BlockQueue q : lqueues) if(eq(q.player, p)) { if(message) sendMsg(p); return false; }
		
		CWBWorlds.loadChunksForWorld(p.getWorld().getName());
		
		commandPlayer = p;
		commandWorld  = p.getWorld();
		lastQueue = new BlockQueue(p);
		return true;
	}
	
	
	/**/
	public static void addBlock(CBlock b)
	{
		if(lastQueue == null)
			return;
		
		lastQueue.queue.add(b);
	}
	/**/
	
	
	public static void flush()
	{
		// Add last queue to appropriate queues list
		if(lastQueue == null)
			return;
		
		if(lastQueue.queue.size() > 0)
			if(lastQueue.queue.size() > 50000)
				lqueues.add(lastQueue);
			else
				queues.add(lastQueue);
		lastQueue = null;
	}
	
	
	public static void remove(Player p)
	{
		for(BlockQueue q :  queues) if(eq(q.player, p)) {  queues.remove(q); return; }
		for(BlockQueue q : lqueues) if(eq(q.player, p)) { lqueues.remove(q); return; }
	}
	
	
	@Override
	public void run()
	{
		Queue<BlockQueue> currentQueues = getNextQueues();
		if(currentQueues == null)
			return;
		
		BlockQueue queue = currentQueues.peek();
		queue.execute();
		if(queue.isEmpty())
			currentQueues.remove();
	}
	
	
	private Queue<BlockQueue> getNextQueues()
	{
		if(queues.isEmpty())
			if(lqueues.isEmpty())
				return null;
			else
				return lqueues;
		return queues;
	}
	
	
	public static Location getLocation(World world, Vector pt)
	{
		return new Location(world, pt.getBlockX(), pt.getBlockY(), pt.getBlockZ());
	}
	
	
	@Override
	protected CPluginSettings getSettings()
	{
		instance = this;
		
		CPluginSettings settings = new CPluginSettings();
		settings.commandMgr = new CommandMgr();
		return settings;
	}
	
	
	@Override
	protected void init()
	{
		getServer().getScheduler().scheduleSyncRepeatingTask(this, this, 1, 1); // schedule run to run every tick
	}
	
	
	public static CWorldBuilder get()
	{
		return instance;
	}
}