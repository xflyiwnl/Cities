package me.xflyiwnl.cities.gui.rank;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Government;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.country.Country;
import me.xflyiwnl.cities.object.rank.PermissionNode;
import me.xflyiwnl.cities.object.rank.PermissionType;
import me.xflyiwnl.cities.object.rank.Rank;
import me.xflyiwnl.colorfulgui.builder.inventory.DynamicGuiBuilder;
import me.xflyiwnl.colorfulgui.object.GuiItem;
import me.xflyiwnl.colorfulgui.object.PaginatedGui;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.provider.ColorfulProvider;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class RankEditorGUI extends ColorfulProvider<PaginatedGui> {

    private FileConfiguration yaml;
    private Rank rank;
    private Government government;
    private Citizen citizen;

    public RankEditorGUI(Player player, Rank rank, Government government) {
        super(player);
        this.rank = rank;
        this.government = government;

        citizen = Cities.getInstance().getCitizen(player);
        yaml = Cities.getInstance().getFileManager().get("gui/rank/rank-edit.yml").yaml();
    }

    @Override
    public void init() {

        items();
        ranks();

        show();

    }

    public void items() {

        if (!yaml.isConfigurationSection("items")) {
            return;
        }

        for (String section : yaml.getConfigurationSection("items").getKeys(false)) {

            String path = "items." + section + ".";

            String name = yaml.getString(path + "display-name");
            List<String> lore = yaml.getStringList(path + "lore");
            int amount = yaml.getInt(path + "amount");
            Material material = Material.valueOf(yaml.getString(path + "material").toUpperCase());
            String mask = yaml.get(path + "mask") == null ? null : yaml.getString(path + "mask");
            List<Integer> slots = yaml.get(path + "slots") == null ? null : yaml.getIntegerList(path + "slots");
            List<String> actions = yaml.get(path + "action") == null ? null : yaml.getStringList(path + "action");

            StaticItem guiItem = Cities.getInstance().getColorfulGUI()
                    .staticItem()
                    .material(material)
                    .name(name)
                    .lore(lore)
                    .amount(amount)
                    .action(event -> {
                        if (actions != null) {
                            for (String action : actions) {
                                if (action.equalsIgnoreCase("[next]")) {
                                    getGui().next();
                                } else if (action.equalsIgnoreCase("[previous]")) {
                                    getGui().previous();
                                } else if (action.equalsIgnoreCase("[leave]")) {
                                    RankGUI.showGUI(getPlayer(), government);
                                }
                            }
                        }
                    })
                    .build();
            if (mask != null) {
                getGui().getMask().addItem(mask, guiItem);
            }
            if (slots != null) {
                slots.forEach(slot -> {
                    getGui().setItem(slot, guiItem);
                });
            }

        }

    }

    public void ranks() {

        List<PermissionNode> nodes = new ArrayList<PermissionNode>();
        for (PermissionNode value : PermissionNode.values()) {
            if (government instanceof City && value.getType() == PermissionType.CITY) {
                nodes.add(value);
            } else if (government instanceof Country && value.getType() == PermissionType.COUNTRY) {
                nodes.add(value);
            }
        }

        for (PermissionNode node : nodes) {

            String path = "node-item.";

            String name = yaml.getString(path + "display-name")
                    .replace("%name%", node.toString().toLowerCase())
                    .replace("%title%", rank.getTitle())
                    .replace("%status%", rank.hasPermission(node) ? "&aВключен" : "&cВыключен");

            List<String> lore = yaml.getStringList(path + "lore");
            lore.replaceAll(word -> word.replace("%title%", rank.getTitle())
                    .replace("%lore%", node.getLore())
                    .replace("%status%", rank.hasPermission(node) ? "&aВключен" : "&cВыключен"));
            Material material = rank.hasPermission(node) ? Material.LIME_WOOL : Material.RED_WOOL;

            GuiItem nodeItem = Cities.getInstance().getColorfulGUI()
                    .staticItem()
                    .material(material)
                    .name(name)
                    .lore(lore)
                    .action(event -> {

                        if (rank.getNodes().contains(node)) {
                            rank.getNodes().remove(node);
                        } else {
                            rank.getNodes().add(node);
                        }

                        rank.save();
                        RankEditorGUI.showGUI(getPlayer(), rank, government);
                    })
                    .build();
            getGui().addItem(nodeItem);

        }

    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setResult(Event.Result.DENY);
    }

    public static void showGUI(Player player, Rank rank, Government government) {
        FileConfiguration yaml = Cities.getInstance().getFileManager().get("gui/rank/rank-edit.yml").yaml();
        DynamicGuiBuilder builder = Cities.getInstance().getColorfulGUI()
                .paginated()
                .holder(new RankEditorGUI(player, rank, government))
                .title(
                        yaml.getString("gui.title")
                                .replace("%government%", government.getName())
                                .replaceAll("%rank%", rank.getTitle())
                )
                .rows(5);
        if (yaml.get("gui.mask") != null) {
            builder.mask(yaml.getStringList("gui.mask"));
        }
        builder.build();
    }

}