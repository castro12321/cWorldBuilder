package castro.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;

import castro.builder.CWorldBuilder;
import castro.connector.CConnector;

public abstract class CBlock
{
	public Location loc;
	public int id;
	
	public CBlock(Location loc, int id)
	{
		this.loc = loc;
		this.id = id;
	}
	
	
	public abstract void execute(Block block);
	
	
	public boolean canBuild(Block block)
	{
		return CConnector.worldguard.canBuild(CWorldBuilder.executePlayer, block);
	}
	
	
	public Block getBlock()
	{
		return loc.getWorld().getBlockAt(loc);
	}
	
	
	public int getId()
	{
		return id;
	}
}
