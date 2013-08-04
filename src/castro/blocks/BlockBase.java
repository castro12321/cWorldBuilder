package castro.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.SignBlock;

public class BlockBase extends CBlock
{
	BaseBlock baseBlock;
	
	public BlockBase(Location loc, BaseBlock baseBlock)
	{
		super(loc, -1);
		this.baseBlock = baseBlock;
	}
	
	@Override
	public void execute(Block block)
	{
		block.setTypeIdAndData(baseBlock.getId(), (byte)baseBlock.getData(), false);
		block = getBlock();
		
		if (baseBlock instanceof SignBlock)
		{
			String[] text = ((SignBlock)baseBlock).getText();
			BlockState state = block.getState();
			if (state == null || !(state instanceof Sign)) return;
			Sign sign = (Sign)state;
			sign.setLine(0, text[0]);
			sign.setLine(1, text[1]);
			sign.setLine(2, text[2]);
			sign.setLine(3, text[3]);
			sign.update();
			return;
		}
		//if(block instanceof Furnace) ... Moze kiedys to zrobie :D
	}
}
