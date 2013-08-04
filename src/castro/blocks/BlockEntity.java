package castro.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;

import castro.builder.CWorldBuilder;

import com.sk89q.worldedit.LocalEntity;
import com.sk89q.worldedit.Vector;

public class BlockEntity extends CBlock
{
	LocalEntity entity;
	Vector pos;
	
	public BlockEntity(LocalEntity entity, Vector pos)
	{
		super(new Location(CWorldBuilder.commandWorld, pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), -1);
		this.entity = entity;
		this.pos = pos;
	}
	
	@Override
	public void execute(Block block)
	{
		entity.spawn(entity.getPosition().setPosition(pos));
	}
}
