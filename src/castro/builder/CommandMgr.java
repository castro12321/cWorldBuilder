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

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import castro.base.GenericCommandMgr;

public class CommandMgr implements GenericCommandMgr
{
	CWorldBuilder plugin = CWorldBuilder.get();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String[] args)
	{
		if(!command.getName().equals("cwb"))
			return true;
		
		String name = sender.getName();
		CWBPlayer player = CWorldBuilder.players.get(name);
		
		if(player == null)
		{
			player = new CWBPlayer(name);
			CWorldBuilder.players.put(name, player);
		}
		
		for(String arg : args)
		{
			if(arg.equals("stop"))
				if(sender instanceof Player)
				{
					CWorldBuilder.remove((Player)sender);
					plugin.sendMessage(sender, "Zatrzymales swoja kolejke.");
					return true;
				}
			
			if(arg.equals("-d")) player.omitLog = player.omitPerm = false;
			
			if(sender.hasPermission("aliquam.mod"))
			{	
				if(arg.equals("-l")) player.omitLog  = true;
				if(arg.equals("-p")) player.omitPerm = true;
			}
		}
		
		plugin.sendMessage(sender, "Twoje obecne flagi to:");
		plugin.sendMessage(sender, "-l = " + player.omitLog);
		plugin.sendMessage(sender, "-p = " + player.omitPerm);
		
		return true;
	}
}
