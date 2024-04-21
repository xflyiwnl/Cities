package me.xflyiwnl.cities.gui.switches;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.gui.BaseGUI;
import me.xflyiwnl.cities.gui.city.CitizensGUI;
import me.xflyiwnl.cities.gui.city.CityBankGUI;
import me.xflyiwnl.cities.gui.city.ScreenGUI;
import me.xflyiwnl.cities.gui.rank.RankGUI;
import me.xflyiwnl.cities.object.Government;
import me.xflyiwnl.cities.object.citizen.Citizen;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.switches.SwitchNode;
import me.xflyiwnl.cities.object.switches.SwitchType;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.builder.item.StaticItemBuilder;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.object.action.click.ClickStaticAction;
import me.xflyiwnl.colorfulgui.object.event.click.ClickStaticItemEvent;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SwitchGUI extends BaseGUI {

    public static String path = "gui/switch/switch.yml";

    private Citizen citizen;
    private Government government;

    private List<SwitchNode> nodes;

    public SwitchGUI(Player player, Citizen citizen, Government government) {
        super(player, path);
        this.citizen = citizen;
        this.government = government;

        if (government instanceof City) {
            nodes = SwitchNode.getNodes(SwitchType.CITY);
        } else {
            nodes = SwitchNode.getNodes(SwitchType.COUNTRY);
        }
    }

    @Override
    public void init() {
        super.init();
    }

    private Material offMaterial = Material.valueOf(getYaml().getString("off-material").toUpperCase());

    @Override
    public void typeItem(String path, String type) {
        super.typeItem(path, type);

        SwitchNode node = SwitchNode.getNode(type);
        if (node == null)
            return;

        YamlConfiguration yaml = getYaml();

        StaticItemBuilder builder = getApi().staticItem()
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS);

        String vpath = path + ".material";
        Material material;
        if (yaml.contains(vpath)) {
            material = Material.valueOf(yaml.getString(vpath).toUpperCase());
        } else {
            material = offMaterial;
        }

        vpath = path + ".amount";
        if (yaml.contains(vpath))
            builder.amount(yaml.getInt(vpath));

        vpath = path + ".name";
        String name;
        if (yaml.contains(vpath)) {
            name = yaml.getString(vpath);
        } else {
            name = "";
        }

        vpath = path + ".lore";
        List<String> lore = new ArrayList<>();
        if (yaml.contains(vpath)) {
            lore.addAll(yaml.getStringList(vpath));
        }

        builder.material(government.getSwitches().hasNode(node)
                ? material
                : offMaterial)
                .name(applySwitchPlaceholders(name, node))
                .lore(lore.stream()
                .map(s -> applySwitchPlaceholders(s, node))
                .collect(Collectors.toList()))
                .action(event -> {

                    if (government.getSwitches().hasNode(node)) {
                        government.getSwitches().removeNode(node);
                    } else {
                        government.getSwitches().addNode(node);
                    }

                    event.getCurrentItem()
                            .builder()
                            .material(government.getSwitches().hasNode(node)
                                    ? material
                                    : offMaterial)
                            .name(applySwitchPlaceholders(name, node))
                            .lore(lore.stream()
                                    .map(s -> applySwitchPlaceholders(s, node))
                                    .collect(Collectors.toList()))
                            .build();

                    getGui().updateItem(event.getCurrentItem());
                    getGui().render();
                });

        StaticItem staticItem = builder.build();

        vpath = path + ".mask";
        if (yaml.contains(vpath))
            getGui().addMask(yaml.getString(vpath), staticItem);

        vpath = path + ".slot";
        if (yaml.contains(vpath))
            getGui().setItem(yaml.getInt(vpath), staticItem);

        vpath = path + ".slots";
        if (yaml.contains(vpath))
            yaml.getIntegerList(vpath).forEach(integer -> {
                getGui().setItem(integer, staticItem);
            });

    }

//    public String applySwitchPlaceholders(String text, SwitchNode node) {
//        return text
//                .replace("%lore%", node.getLore())
//                .replace("%status%", nodes.contains(node)
//                ? getYaml().getString("status.on")
//                : getYaml().getString("status.off"));
//    }

    public String applySwitchPlaceholders(String text, SwitchNode node) {
        return text
                .replace("%lore%", node.getLore())
                .replace("%status%", government.getSwitches().hasNode(node)
                        ? getYaml().getString("status.on-placeholder")
                        : getYaml().getString("status.off-placeholder"));
    }

    @Override
    public void handleAction(ClickStaticItemEvent event, List<String> actions) {
        super.handleAction(event, actions);

        actions.forEach(action -> {
            if (action.equalsIgnoreCase("[exit]")) {
                if (government instanceof City city) {
                    ScreenGUI.openGUI(getPlayer(), citizen, city);
                }
            }
        });
    }

    public static void openGUI(Player player, Citizen citizen, City city) {
        YamlConfiguration yaml = Cities.getInstance().getFileManager().get(path).yaml();
        ColorfulGUI api = Cities.getInstance().getGuiApi();

        api.paginated()
                .mask(yaml.getStringList("gui.mask"))
                .title(yaml.getString("gui.title")
                        .replace("%city%", city.getName()))
                .rows(yaml.getInt("gui.rows"))
                .holder(new SwitchGUI(player, citizen, city))
                .build();
    }

}
