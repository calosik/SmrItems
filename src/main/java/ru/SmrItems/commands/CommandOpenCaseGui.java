package ru.SmrItems.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import ru.SmrItems.SmMain;

public class CommandOpenCaseGui extends CommandBase {
    @Override
    public String getCommandName() {
        return "opencasegui";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/opencasegui - Открывает GUI кейсов";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            player.openGui(SmMain.INSTANCE, 0, player.worldObj, 0, 0, 0);
        }
    }
}
