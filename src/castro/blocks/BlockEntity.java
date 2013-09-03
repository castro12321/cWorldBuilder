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
