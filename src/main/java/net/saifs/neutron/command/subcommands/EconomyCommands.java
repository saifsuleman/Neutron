package net.saifs.neutron.command.subcommands;

import net.saifs.neutron.Neutron;
import net.saifs.neutron.command.CommandHandler;
import net.saifs.neutron.command.NeutronCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EconomyCommands extends NeutronCommand {
    private final List<NeutronCommand> subcommands = new ArrayList<>();

    public EconomyCommands(CommandHandler commandHandler) {
        super(commandHandler, "economy", "eco");

        subcommands.add(new GiveCommand(commandHandler, "give"));
        subcommands.add(new RemoveCommand(commandHandler, "remove"));
        subcommands.add(new SetCommand(commandHandler, "set"));
        subcommands.add(new BalanceCommand(commandHandler, "balance"));
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage("§3Economy Sub-Commands:");
        for (NeutronCommand command : this.subcommands) {
            String usage = command.getUsage();
            String description = command.getDescription();
            sender.sendMessage("§3/" + this.getCommandHandler().getID() + " economy "
                    + usage + "§7 - §3" + description);
        }
    }

    private NeutronCommand getCommand(String s) {
        for (NeutronCommand neutronCommand : this.subcommands) {
            boolean match = neutronCommand.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(s));
            if (match) return neutronCommand;
        }
        return null;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendHelpMessage(sender);
            return;
        }
        NeutronCommand command = getCommand(args[0]);
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

    @Override
    public List<String> onTabComplete(String[] args) {
        if (args.length == 1) {
            List<String> aliases = new ArrayList<>();
            for (NeutronCommand neutronCommand : this.subcommands)
                aliases.addAll(neutronCommand.getAliases());
            return aliases;
        }

        if (args.length > 1) {
            NeutronCommand command = getCommand(args[0]);
            if (command != null)
                return command.onTabComplete(Arrays.copyOfRange(args, 1, args.length));
        }

        return null;
    }
}

class GiveCommand extends NeutronCommand {
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

        Neutron plugin = this.getCommandHandler().getPlugin();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getEconomy().giveMoney(target, amount);
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                sender.sendMessage("§7Successfully given §a$" + amount + "§7 to §a" + target.getName());
            });
        });
    }

    @Override
    public String getDescription() {
        return "Admin command for giving people money";
    }

    public String getNode() {
        return "neutron.economy.give";
    }

    @Override
    public String getUsage() {
        return "give <player> <amount>";
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return args.length == 2 ? new ArrayList<>() : null;
    }
}

class RemoveCommand extends NeutronCommand {
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

        Neutron plugin = this.getCommandHandler().getPlugin();
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
        return "neutron.economy.remove";
    }

    @Override
    public String getUsage() {
        return "remove <player> <amount>";
    }
}

class SetCommand extends NeutronCommand {
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

        Neutron plugin = this.getCommandHandler().getPlugin();
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
        return "neutron.economy.set";
    }

    @Override
    public String getUsage() {
        return "set <player> <amount>";
    }
}

class BalanceCommand extends NeutronCommand {
    public BalanceCommand(CommandHandler commandHandler, String... aliases) {
        super(commandHandler, aliases);
        registerCommandAlias("balance");
    }

    private void sendBalance(CommandSender sender, OfflinePlayer player, double balance) {
        String name = player.getName();
        if (player instanceof Player) {
            name = ((Player) player).getDisplayName();
        }

        sender.sendMessage("§3" + name + "§3's balance: $" + balance);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        String usage = "§cUsage: /" + getCommandHandler().getID() + " eco " + getUsage();

        if (args.length == 0 && !(sender instanceof Player)) {
            sender.sendMessage(usage);
            return;
        }

        Neutron plugin = getCommandHandler().getPlugin();


        final Player player = args.length == 0 ? (Player) sender : Bukkit.getPlayer(args[0]);
        OfflinePlayer[] players = Bukkit.getOfflinePlayers();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            OfflinePlayer target = player;
            if (target == null) {
                target = Arrays.stream(players).filter(p -> args[0].equalsIgnoreCase(p.getName())).findAny().orElse(null);
                if (target == null) {
                    Bukkit.getScheduler().runTask(plugin, () -> sender.sendMessage("§cError: Unable to find that player!"));
                    return;
                }
            }
            double balance = plugin.getEconomy().getPlayerBalance(target);

            // lambdas require final variables :)
            final OfflinePlayer targetPlayer = target;
            Bukkit.getScheduler().runTask(plugin, () -> sendBalance(sender, targetPlayer, balance));
        });
    }

    @Override
    public String getUsage() {
        return "balance <player>";
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return args.length == 1 ? null : new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Command used to check people's balance!";
    }
}