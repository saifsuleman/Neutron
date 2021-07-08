package net.saifs.neutron.command.subcommands;

import net.saifs.neutron.command.CommandHandler;
import net.saifs.neutron.command.NeutronCommand;
import org.bukkit.command.CommandSender;

public class HelpCommand extends NeutronCommand {
    public HelpCommand(CommandHandler commandHandler) {
        super(commandHandler, "help");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        sender.sendMessage("§3Neutron Plugin Help Page:");
        for (NeutronCommand command : this.getCommandHandler().getCommands()) {
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
