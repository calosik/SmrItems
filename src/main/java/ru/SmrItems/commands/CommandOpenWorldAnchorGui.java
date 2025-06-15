package ru.SmrItems.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import ru.SmrItems.SmMain;

public class CommandOpenWorldAnchorGui extends CommandBase {
    @Override
    public String getCommandName() {
        return "openworldanchorgui";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/openworldanchorgui - Открывает GUI якоря мира";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            player.openGui(SmMain.INSTANCE, 0, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
        }
    }
}
