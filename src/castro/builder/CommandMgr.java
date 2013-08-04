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
			
			if(arg.equals("-d")) player.omitAir = player.omitLog = player.omitPerm = false;
			if(arg.equals("-a")) player.omitAir = true;
			
			if(sender.hasPermission("aliquam.mod"))
			{
				if(arg.equals("-l")) player.omitLog  = true;
				if(arg.equals("-p")) player.omitPerm = true;
			}
		}
		
		plugin.sendMessage(sender, "Twoje obecne flagi to:");
		plugin.sendMessage(sender, "-a = " + player.omitAir);
		plugin.sendMessage(sender, "-l = " + player.omitLog);
		plugin.sendMessage(sender, "-p = " + player.omitPerm);
		
		return true;
	}
}
