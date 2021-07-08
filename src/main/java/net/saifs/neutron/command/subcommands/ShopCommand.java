package net.saifs.neutron.command.subcommands;

import net.saifs.neutron.command.CommandHandler;
import net.saifs.neutron.command.NeutronCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand extends NeutronCommand {
    public ShopCommand(CommandHandler commandHandler) {
        super(commandHandler, "shop");
        registerCommandAlias("shop");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cYou must be a player to do that!");
            return;
        }

        Player player = (Player) sender;
        player.sendMessage("§aOpening...");
        getCommandHandler().getPlugin().getShopManager().openShopGUI(player);
    }

    @Override
    public String getDescription() {
        return "Opens the shop!";
    }

}
