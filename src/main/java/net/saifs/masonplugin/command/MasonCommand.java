package net.saifs.masonplugin.command;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public abstract class MasonCommand {
    private List<String> aliases;
    private CommandHandler commandHandler;
    private String id;

    public MasonCommand(CommandHandler commandHandler, String...aliases) {
        if (aliases.length == 0) {
            throw new IllegalArgumentException("sub-commands must have at least one alias!");
        }

        this.commandHandler = commandHandler;
        this.id = aliases[0];
        this.aliases = Arrays.asList(aliases);
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

    protected CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    public abstract String getDescription();

    public String getUsage() {
        return this.id;
    }
}
