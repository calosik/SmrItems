package ru.SmrItems.common.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.OrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import ru.SmrItems.Registry;
import ru.SmrItems.common.tileentity.TileWorldAnchor;

public class ChunkLoadingHandler implements OrderedLoadingCallback {
   public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) {
      List<Ticket> validTickets = new ArrayList();
      Iterator var5 = tickets.iterator();

      while(var5.hasNext()) {
         Ticket ticket = (Ticket)var5.next();
         int coreX = ticket.getModData().getInteger("coreX");
         int coreY = ticket.getModData().getInteger("coreY");
         int coreZ = ticket.getModData().getInteger("coreZ");
         Block blockChunkLoader = world.getBlock(coreX, coreY, coreZ);
         if (blockChunkLoader == Registry.Blocks.blockWorldAnchor) {
            validTickets.add(ticket);
         }
      }

      return validTickets;
   }

   public void ticketsLoaded(List<Ticket> tickets, World world) {
      Iterator var3 = tickets.iterator();

      while(var3.hasNext()) {
         Ticket ticket = (Ticket)var3.next();
         int coreX = ticket.getModData().getInteger("coreX");
         int coreY = ticket.getModData().getInteger("coreY");
         int coreZ = ticket.getModData().getInteger("coreZ");
         TileWorldAnchor te = (TileWorldAnchor)world.getTileEntity(coreX, coreY, coreZ);
         te.forceChunkLoadingCallback(ticket);
      }

   }
}
