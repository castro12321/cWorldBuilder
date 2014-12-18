/* cWorldBuilder
 * Copyright (C) 2013 Norbert Kawinski (norbert.kawinski@gmail.com)

 */

package castro.builder;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import castro.base.GenericCommandMgr;

public class CommandMgr implements GenericCommandMgr
{
	private CWorldBuilder plugin = CWorldBuilder.get();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String[] args)
	{
		if(!command.getName().equals("cwb"))
			return true;
		
		for(String arg : args)
		{
			if(arg.equals("stop"))
				if(sender instanceof Player)
				{
					CWorldBuilder.remove((Player)sender);
					plugin.sendMessage(sender, "Zatrzymales swoja kolejke.");
					return true;
				}
		}
		
		return true;
	}
}
