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
