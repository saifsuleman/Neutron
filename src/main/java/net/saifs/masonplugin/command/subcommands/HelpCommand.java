package net.saifs.masonplugin.command.subcommands;

import net.saifs.masonplugin.command.CommandHandler;
import net.saifs.masonplugin.command.MasonCommand;
import org.bukkit.command.CommandSender;

public class HelpCommand extends MasonCommand {
    public HelpCommand(CommandHandler commandHandler) {
        super(commandHandler, "help");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        sender.sendMessage("§3Mason Plugin Help Page:");
        for (MasonCommand command : this.getCommandHandler().getCommands()) {
            String usage = command.getUsage();
            String description = command.getDescription();
            sender.sendMessage("§3/" + this.getCommandHandler().getID() + " "
                    + usage + "§7 - §3" + description);
        }
    }

    @Override
    public String getDescription() {
        return "Displays all the plugin's sub-commands!";
    }
}
