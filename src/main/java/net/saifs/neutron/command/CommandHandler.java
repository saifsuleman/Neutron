package net.saifs.neutron.command;

import net.saifs.neutron.Neutron;
import net.saifs.neutron.command.subcommands.EconomyCommands;
import net.saifs.neutron.command.subcommands.HelpCommand;
import net.saifs.neutron.command.subcommands.ShopCommand;
import org.bukkit.command.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {
    private final String id;
    private final List<NeutronCommand> commands;
    private Neutron plugin;

    public CommandHandler(String id, Neutron plugin) {
        this.id = id;
        this.plugin = plugin;
        this.commands = new ArrayList<>();
        PluginCommand command = plugin.getCommand("neutron");
        if (command == null) {
            throw new IllegalArgumentException("Command: " + id + " is not registed in the plugin.yml!");
        }
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    public List<NeutronCommand> getCommands() {
        return this.commands;
    }

    public void registerCommand(NeutronCommand command) {
        this.commands.add(command);
    }

    public void registerCommands() {
        registerCommand(new HelpCommand(this));
        registerCommand(new EconomyCommands(this));
        registerCommand(new ShopCommand(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            args = new String[]{"help"};
        }

        NeutronCommand command = this.getCommand(args[0]);
        if (command == null)
            command = this.getCommand("help");

        if (command.getNode() != null && !sender.hasPermission(command.getNode())) {
            sender.sendMessage("§cYou do not have permission to run that command!");
            return true;
        }
        command.onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
        return true;
    }

    public NeutronCommand getCommand(String label) {
        for (NeutronCommand command : commands) {
            if (command.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(label))) return command;
        }
        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> aliases = new ArrayList<>();
        for (NeutronCommand neutronCommand : this.commands)
            aliases.addAll(neutronCommand.getAliases());
        if (args.length == 1) return aliases;

        NeutronCommand neutronCommand = getCommand(args[0]);
        if (neutronCommand != null) {
            return neutronCommand.onTabComplete(Arrays.copyOfRange(args, 1, args.length));
        }
        return null;
    }

    public Neutron getPlugin() {
        return plugin;
    }

    public String getID() {
        return this.id;
    }
}
