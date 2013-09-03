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

public class BlockIdAndData extends CBlock
{
	public int id;
	public byte data;
	
	public BlockIdAndData(int id, byte data)
	{
		this.id = id;
		this.data = data;
	}
	
	@Override
	public void execute(Location location)
	{
		location.getBlock().setTypeIdAndData(id, data, false);
	}	

	
	@Override
	public int hashCode()
	{
		return getHash(id, data);
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof BlockIdAndData)
			return data == ((BlockIdAndData)o).data
			&& id == ((BlockIdAndData)o).id;
		return false;
	}
	
	
	@Override
	protected CBlock clone()
	{
		return new BlockIdAndData(id, data);
	}
}
