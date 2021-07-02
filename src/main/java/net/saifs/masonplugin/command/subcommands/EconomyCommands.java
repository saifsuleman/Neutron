package net.saifs.masonplugin.command.subcommands;

import net.saifs.masonplugin.MasonPlugin;
import net.saifs.masonplugin.command.CommandHandler;
import net.saifs.masonplugin.command.MasonCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EconomyCommands extends MasonCommand {
    private final Map<String, MasonCommand> subcommands = new HashMap<>();

    public EconomyCommands(CommandHandler commandHandler) {
        super(commandHandler, "economy", "eco");

        this.subcommands.put("set", new SetCommand(commandHandler, "set"));
        this.subcommands.put("give", new GiveCommand(commandHandler, "give"));
        this.subcommands.put("remove", new RemoveCommand(commandHandler, "remove"));
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage("§aEconomy Sub-Commands:");
        for (MasonCommand command : this.subcommands.values()) {
            String usage = command.getUsage();
            String description = command.getDescription();
            sender.sendMessage("§b/" + this.getCommandHandler().getID() + " "
                    + usage + "§7 - §b" + description);
        }
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendHelpMessage(sender);
            return;
        }
        MasonCommand command = this.subcommands.get(args[0]);
        if (command == null) {
            sendHelpMessage(sender);
            return;
        }
        if (command.getNode() != null && !sender.hasPermission(command.getNode())) {
            sender.sendMessage("§cYou do not have permission to run that command!");
            return;
        }
        command.onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public String getDescription() {
        return "Economy based commands!";
    }
}

class GiveCommand extends MasonCommand {
    public GiveCommand(CommandHandler commandHandler, String... aliases) {
        super(commandHandler, aliases);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        String usage = "§cUsage: /" + getCommandHandler().getID() + " eco " + getUsage();

        if (args.length < 2) {
            sender.sendMessage(usage);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cError: §4Unable to find that player!");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException ignored) {
            sender.sendMessage(usage);
            return;
        }

        MasonPlugin plugin = this.getCommandHandler().getPlugin();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getEconomy().giveMoney(target, amount);
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                sender.sendMessage("§7Successfully given §a$" + amount + "§7to §a" + target.getName());
            });
        });
    }

    @Override
    public String getDescription() {
        return "Admin command for giving people money";
    }

    public String getNode() {
        return "mason.economy.give";
    }

    @Override
    public String getUsage() {
        return "give <player> <amount>";
    }
}

class RemoveCommand extends MasonCommand {
    public RemoveCommand(CommandHandler commandHandler, String... aliases) {
        super(commandHandler, aliases);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        String usage = "§cUsage: /" + getCommandHandler().getID() + " eco " + getUsage();

        if (args.length < 2) {
            sender.sendMessage(usage);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cError: §4Unable to find that player!");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException ignored) {
            sender.sendMessage(usage);
            return;
        }

        MasonPlugin plugin = this.getCommandHandler().getPlugin();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getEconomy().removeMoney(target, amount);
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                sender.sendMessage("§7Successfully removed §a$" + amount + "§7from §a" + target.getName());
            });
        });
    }

    @Override
    public String getDescription() {
        return "Admin command for removing people's money";
    }

    public String getNode() {
        return "mason.economy.remove";
    }

    @Override
    public String getUsage() {
        return "remove <player> <amount>";
    }
}

class SetCommand extends MasonCommand {
    public SetCommand(CommandHandler commandHandler, String... aliases) {
        super(commandHandler, aliases);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        String usage = "§cUsage: /" + getCommandHandler().getID() + " eco " + getUsage();

        if (args.length < 2) {
            sender.sendMessage(usage);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cError: §4Unable to find that player!");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException ignored) {
            sender.sendMessage(usage);
            return;
        }

        MasonPlugin plugin = this.getCommandHandler().getPlugin();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getEconomy().setPlayerBalance(target, amount);
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                sender.sendMessage("§7Successfully set §a" + target.getName() + "§7's balance to §a$" + amount);
            });
        });
    }

    @Override
    public String getDescription() {
        return "Admin command for setting people's money";
    }

    public String getNode() {
        return "mason.economy.set";
    }

    @Override
    public String getUsage() {
        return "set <player> <amount>";
    }
}
