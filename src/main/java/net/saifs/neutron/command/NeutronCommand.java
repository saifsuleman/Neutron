package net.saifs.neutron.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.Arrays;
import java.util.List;

public abstract class NeutronCommand implements CommandExecutor {
    private List<String> aliases;
    private CommandHandler commandHandler;
    private String id;

    public NeutronCommand(CommandHandler commandHandler, String... aliases) {
        if (aliases.length == 0) {
            throw new IllegalArgumentException("sub-commands must have at least one alias!");
        }

        this.commandHandler = commandHandler;
        this.id = aliases[0];
        this.aliases = Arrays.asList(aliases);
    }

    protected void registerCommandAlias(String... aliases) {
        for (String alias : aliases) {
            PluginCommand pluginCommand = getCommandHandler().getPlugin().getCommand(alias);
            if (pluginCommand != null) {
                pluginCommand.setExecutor(this);
            }
        }
    }

    public String getNode() {
        return null;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public List<String> onTabComplete(String[] args) {
        return null;
    }

    public String getID() {
        return this.id;
    }

    public abstract void onCommand(CommandSender sender, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (getNode() != null && !sender.hasPermission(getNode())) {
            sender.sendMessage("§cYou do not have permission to run that command!");
            return true;
        }

        onCommand(sender, args);
        return true;
    }

    protected CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    public abstract String getDescription();

    public String getUsage() {
        return this.id;
    }
}
