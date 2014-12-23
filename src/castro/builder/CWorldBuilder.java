/* cWorldBuilder
 * Copyright (C) 2013 Norbert Kawinski (norbert.kawinski@gmail.com)

 */

package castro.builder;

import java.util.ArrayDeque;
import java.util.Queue;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import castro.base.plugin.CPlugin;
import castro.base.plugin.CPluginSettings;
import castro.blocks.CBlock;
import castro.cBorder.Border;
import castro.cBorder.BorderMgr;

import com.sk89q.worldedit.Vector;


public class CWorldBuilder extends CPlugin implements Runnable
{
	private static CWorldBuilder instance;
	private static CommandMgr    commandMgr;
	
	public static Player commandPlayer;
	public static World  commandWorld;
	public static Border commandBorder;
	
	private static Queue<BlockQueue>  queues = new ArrayDeque<BlockQueue>(); // Queues for small operations (less than 100k blocks)
	private static Queue<BlockQueue> lqueues = new ArrayDeque<BlockQueue>(); // Queues for large operations (>100k blocks)
	public static BlockQueue lastQueue; // for adding
	
	
	private static boolean eq(Player p1, Player p2) { return p1.getName().equals(p2.getName()); }
	private static void sendMsg(Player player) { get().sendMessage(player, "&cMozesz miec tylko jedna aktywna kolejke. Wpisz &a/cwb stop &caby zatrzymac kolejke."); }
	public static boolean addQueue(Player p, boolean message)
	{
		// flush in case we have another queue.
		// It's safe to flush anybody's queue, because commands/interact-events are handled synchronously
		flush(); 
		
		// Check if player is already in queue
		for(BlockQueue q :  queues) if(eq(q.player, p)) { if(message) sendMsg(p); return false; }
		for(BlockQueue q : lqueues) if(eq(q.player, p)) { if(message) sendMsg(p); return false; }
		
		commandPlayer = p;
		commandWorld  = p.getWorld();
		commandBorder = BorderMgr.getNewBorder(commandWorld);
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
			if(lastQueue.queue.size() > 100000)
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
		return settings;
	}
	
	@Override
	protected void init()
	{
		commandMgr = new CommandMgr();
		getServer().getScheduler().scheduleSyncRepeatingTask(this, this, 1, 1); // schedule run to run every tick
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return commandMgr.onCommand(sender, cmd, args);
	}
	
	public static CWorldBuilder get()
	{
		return instance;
	}
}