/* cWorldBuilder
 * Copyright (C) 2013 Norbert Kawinski (norbert.kawinski@gmail.com)

 */

package castro.blocks;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.NBTBase;
import net.minecraft.server.v1_8_R1.NBTTagByte;
import net.minecraft.server.v1_8_R1.NBTTagByteArray;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.NBTTagDouble;
import net.minecraft.server.v1_8_R1.NBTTagFloat;
import net.minecraft.server.v1_8_R1.NBTTagInt;
import net.minecraft.server.v1_8_R1.NBTTagIntArray;
import net.minecraft.server.v1_8_R1.NBTTagList;
import net.minecraft.server.v1_8_R1.NBTTagLong;
import net.minecraft.server.v1_8_R1.NBTTagShort;
import net.minecraft.server.v1_8_R1.NBTTagString;
import net.minecraft.server.v1_8_R1.TileEntity;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;

import com.sk89q.jnbt.ByteArrayTag;
import com.sk89q.jnbt.ByteTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.DoubleTag;
import com.sk89q.jnbt.EndTag;
import com.sk89q.jnbt.FloatTag;
import com.sk89q.jnbt.IntArrayTag;
import com.sk89q.jnbt.IntTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.LongTag;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.blocks.BaseBlock;

public class BlockBase extends CBlock
{
	BaseBlock baseBlock;
	private static Method nbtCreateTagMethod;
	static
	{
		try
        {
	        nbtCreateTagMethod = NBTBase.class.getDeclaredMethod("createTag", new Class[] { Byte.TYPE });
        }
        catch(NoSuchMethodException | SecurityException e)
        {
	        e.printStackTrace();
        }
	    nbtCreateTagMethod.setAccessible(true);
	}
	
	public BlockBase(Location loc, BaseBlock baseBlock)
	{
		super(loc);
		this.baseBlock = baseBlock;
	}
	
	
	@Override
	public void execute(Block block_unused)
	{
		//Preconditions.checkNotNull(loc);
		//Preconditions.checkNotNull(baseBlock);
		
		loc.getBlock().setTypeIdAndData(baseBlock.getId(), (byte)baseBlock.getData(), true);
		
		CompoundTag nativeTag = baseBlock.getNbtData();
		if(nativeTag != null)
		{
			CraftWorld craftWorld = (CraftWorld) loc.getWorld();
			int x = loc.getBlockX();
			int y = loc.getBlockY();
			int z = loc.getBlockZ();
			
			TileEntity tileEntity = craftWorld.getHandle().getTileEntity(new BlockPosition(x, y, z));
			if(tileEntity != null)
			{
				NBTTagCompound tag = (NBTTagCompound) fromNative(nativeTag);
				tag.set("x", new NBTTagInt(x));
				tag.set("y", new NBTTagInt(y));
				tag.set("z", new NBTTagInt(z));
				readTagIntoTileEntity(tag, tileEntity);
			}
		}
	}
	
	
	private static void readTagIntoTileEntity(NBTTagCompound tag, TileEntity tileEntity)
	{
		tileEntity.a(tag);
	}
	
	
	private NBTBase fromNative(Tag foreign)
	{
		if(foreign == null)
			return null;
		Map.Entry<String, Tag> entry;
		if((foreign instanceof CompoundTag))
		{
			NBTTagCompound tag = new NBTTagCompound();
			for(Iterator<Entry<String, Tag>> localIterator = ((CompoundTag) foreign).getValue().entrySet().iterator(); localIterator.hasNext();)
			{
				entry = (Entry<String, Tag>) localIterator.next();
				tag.set((String) entry.getKey(), fromNative((Tag) entry.getValue()));
			}
			return tag;
		}
		if((foreign instanceof ByteTag))
			return new NBTTagByte(((ByteTag) foreign).getValue().byteValue());
		if((foreign instanceof ByteArrayTag))
			return new NBTTagByteArray(((ByteArrayTag) foreign).getValue());
		if((foreign instanceof DoubleTag))
			return new NBTTagDouble(((DoubleTag) foreign).getValue().doubleValue());
		if((foreign instanceof FloatTag))
			return new NBTTagFloat(((FloatTag) foreign).getValue().floatValue());
		if((foreign instanceof IntTag))
			return new NBTTagInt(((IntTag) foreign).getValue().intValue());
		if((foreign instanceof IntArrayTag))
			return new NBTTagIntArray(((IntArrayTag) foreign).getValue());
		if((foreign instanceof ListTag))
		{
			NBTTagList tag = new NBTTagList();
			ListTag foreignList = (ListTag) foreign;
			for(Tag t : foreignList.getValue())
				tag.add(fromNative(t));
			return tag;
		}
		if((foreign instanceof LongTag))
			return new NBTTagLong(((LongTag) foreign).getValue().longValue());
		if((foreign instanceof ShortTag))
			return new NBTTagShort(((ShortTag) foreign).getValue().shortValue());
		if((foreign instanceof StringTag))
			return new NBTTagString(((StringTag) foreign).getValue());
		if((foreign instanceof EndTag))
		{
			try
			{
				return (NBTBase) nbtCreateTagMethod.invoke(null, new Object[] { Byte.valueOf((byte) 0) });
			}
			catch(Exception e)
			{
				return null;
			}
		}
		throw new IllegalArgumentException("Don't know how to make NMS " + foreign.getClass().getCanonicalName());
	}
}
