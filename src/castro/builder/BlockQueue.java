package castro.builder;

import java.util.ArrayDeque;
import java.util.Queue;

import org.bukkit.entity.Player;

import castro.blocks.CBlock;

public class BlockQueue
{
	//public Queue<CBlock> queue = new LinkedList<>();
	public Queue<CBlock> queue = new ArrayDeque<CBlock>(50000);
	
	Player player;
	boolean omitAir  = false;
	boolean omitLog  = false;
	boolean omitPerm = false;
	
	public BlockQueue(Player player)
	{
		this.player = player;
		
		CWBPlayer cplayer = CWorldBuilder.players.get(player.getName());
		if(cplayer != null)
		{
			omitAir  = cplayer.omitAir;
			omitLog  = cplayer.omitLog;
			omitPerm = cplayer.omitPerm;
		}
	}
}
