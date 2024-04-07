package me.xflyiwnl.cities.gui.rank;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.gui.BaseGUI;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Government;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.country.Country;
import me.xflyiwnl.cities.object.rank.PermissionNode;
import me.xflyiwnl.cities.object.rank.PermissionType;
import me.xflyiwnl.cities.object.rank.Rank;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.builder.inventory.DynamicGuiBuilder;
import me.xflyiwnl.colorfulgui.object.GuiItem;
import me.xflyiwnl.colorfulgui.object.PaginatedGui;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.object.event.click.ClickStaticItemEvent;
import me.xflyiwnl.colorfulgui.provider.ColorfulProvider;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RankEditorGUI extends BaseGUI {

    public static String path = "gui/rank/rank-edit.yml";

    private final Rank rank;
    private final Government government;
    private final Citizen citizen;

    public RankEditorGUI(Player player, Rank rank, Government government) {
        super(player, path);
        this.rank = rank;
        this.government = government;

        citizen = CitiesAPI.getInstance().getCitizen(player);
    }

    @Override
    public void init() {
        super.init();

        ranks();
    }

    public void ranks() {

        List<PermissionNode> nodes = new ArrayList<PermissionNode>();

        PermissionType type = government instanceof City
                ? PermissionType.CITY
                : PermissionType.COUNTRY;
        for (PermissionNode node : PermissionNode.values()) {
            if (node.getType() == type) {
                nodes.add(node);
            }
        }

        for (PermissionNode node : nodes) {
            String path = "node-item.";

            String name = getYaml().getString(path + "name");
            List<String> lore = getYaml().getStringList(path + "lore");

            StaticItem nodeItem = getApi().staticItem()
                    .material(rank.hasPermission(node) ? Material.LIME_WOOL : Material.RED_WOOL)
                    .name(applyPlaceholder(name, rank, node))
                    .lore(lore.stream()
                            .map(s -> applyPlaceholder(s, rank, node))
                            .collect(Collectors.toList()))
                    .action(event -> {
                        handleClick(event, node);

                        event.getCurrentItem().builder()
                                .material(rank.hasPermission(node) ? Material.LIME_WOOL : Material.RED_WOOL)
                                .name(applyPlaceholder(name, rank, node))
                                .lore(lore.stream()
                                        .map(s -> applyPlaceholder(s, rank, node))
                                        .collect(Collectors.toList()))
                                .build();
                        getGui().updateItem(event.getCurrentItem());
                    })
                    .build();
            getGui().addItem(nodeItem);
        }

    }

    public String applyPlaceholder(String text, Rank rank, PermissionNode node) {
        return text
                .replace("%name%", node.toString().toLowerCase())
                .replace("%title%", rank.getTitle())
                .replace("%status%", rank.hasPermission(node) ? "&aВключен" : "&cВыключен")
                .replace("%lore%", node.getLore());
    }

    public void handleClick(ClickStaticItemEvent event, PermissionNode node) {
        if (rank.getNodes().contains(node)) {
            rank.getNodes().remove(node);
        } else {
            rank.getNodes().add(node);
        }

        rank.save();
    }

    @Override
    public void handleAction(ClickStaticItemEvent event, List<String> actions) {
        super.handleAction(event, actions);

        for (String action : actions) {
            if (action.equalsIgnoreCase("[next]")) {
                getGui().next();
            } else if (action.equalsIgnoreCase("[previous]")) {
                getGui().previous();
            } else if (action.equalsIgnoreCase("[leave]")) {
                RankGUI.openGUI(getPlayer(), government);
            }
        }
    }

    public static void openGUI(Player player, Rank rank, Government government) {
        YamlConfiguration yaml = Cities.getInstance().getFileManager().get(path).yaml();
        ColorfulGUI api = Cities.getInstance().getGuiApi();

        api.paginated()
                .mask(yaml.getStringList("gui.mask"))
                .title(
                        yaml.getString("gui.title")
                                .replace("%government%", government.getName())
                                .replaceAll("%rank%", rank.getTitle())
                )
                .rows(yaml.getInt("gui.rows"))
                .holder(new RankEditorGUI(player, rank, government))
                .build();
    }

}