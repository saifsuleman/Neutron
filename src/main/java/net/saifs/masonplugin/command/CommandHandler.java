package net.saifs.masonplugin.command;

import net.saifs.masonplugin.MasonPlugin;
import net.saifs.masonplugin.command.subcommands.EconomyCommands;
import net.saifs.masonplugin.command.subcommands.HelpCommand;
import org.bukkit.command.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {
    private final String id;
    private final List<MasonCommand> commands;
    private MasonPlugin plugin;

    public CommandHandler(String id, MasonPlugin plugin) {
        this.id = id;
        this.plugin = plugin;
        this.commands = new ArrayList<>();
        PluginCommand command = plugin.getCommand("mason");
        if (command == null) {
            throw new IllegalArgumentException("Command: " + id + " is not registed in the plugin.yml!");
        }
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    public List<MasonCommand> getCommands() {
        return this.commands;
    }

    public void registerCommand(MasonCommand command) {
        this.commands.add(command);
    }

    public void registerCommands() {
        registerCommand(new HelpCommand(this));
        registerCommand(new EconomyCommands(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cCorrect Usage: /" + label + " <sub-command>. " +
                    "Please type '/" + label + " help' for a list of valid sub-commands!");
            return true;
        }

        MasonCommand command = this.getCommand(args[0]);
        if (command == null) {
            sender.sendMessage("§cThat is not a valid subcommand! " +
                    "Please type '/" + label + " help' for a list of valid sub-commands!");
            return true;
        }
        if (command.getNode() != null && !sender.hasPermission(command.getNode())) {
            sender.sendMessage("§cYou do not have permission to run that command!");
            return true;
        }
        command.onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
        return true;
    }

    public MasonCommand getCommand(String label) {
        for (MasonCommand command : commands) {
            if (command.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(label))) return command;
        }
        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> aliases = new ArrayList<>();
        for (MasonCommand masonCommand : this.commands)
            aliases.addAll(masonCommand.getAliases());
        if (args.length == 1) return aliases;

        MasonCommand masonCommand = getCommand(args[0]);
        if (masonCommand != null) {
            return masonCommand.onTabComplete(Arrays.copyOfRange(args, 1, args.length));
        }
        return null;
    }

    public MasonPlugin getPlugin() {
        return plugin;
    }

    public String getID() {
        return this.id;
    }
}
