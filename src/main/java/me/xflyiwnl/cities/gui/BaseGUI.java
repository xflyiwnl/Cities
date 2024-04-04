package me.xflyiwnl.cities.gui;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.builder.item.StaticItemBuilder;
import me.xflyiwnl.colorfulgui.object.PaginatedGui;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.object.event.click.ClickStaticItemEvent;
import me.xflyiwnl.colorfulgui.provider.ColorfulProvider;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseGUI extends ColorfulProvider<PaginatedGui> {

    private YamlConfiguration yaml;
    private ColorfulGUI api = Cities.getInstance().getGuiApi();

    public BaseGUI(Player player, String path) {
        super(player);
        this.yaml = Cities.getInstance().getFileManager().get(path).yaml();;
    }

    public BaseGUI(Player player, int updateTime, String path) {
        super(player, updateTime);
        this.yaml = Cities.getInstance().getFileManager().get(path).yaml();;
    }

    @Override
    public void init() {
        if (!yaml.isConfigurationSection("items")) {
            return;
        }

        for (String section : yaml.getConfigurationSection("items").getKeys(false)) {
            String path = "items." + section;

            if (yaml.get(path + ".type") == null) {
                defaultItem(path);
            } else {
                typeItem(path, yaml.getString(path + ".type"));
            }

        }

    }

    public void typeItem(String path, String type) {

    }

    public void defaultItem(String path) {

        List<String> action = yaml.get(path + ".action") != null ? yaml.getStringList(path + ".action") : new ArrayList<>();

        StaticItemBuilder builder = api.staticItem()
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS)
                .action(event -> {
                    handleAction(event, action);
                });

        String vpath = path + ".material";
        if (yaml.contains(vpath))
            builder.material(Material.valueOf(yaml.getString(vpath).toUpperCase()));

        vpath = path + ".amount";
        if (yaml.contains(vpath))
            builder.amount(yaml.getInt(vpath));

        vpath = path + ".name";
        if (yaml.contains(vpath))
            builder.name(yaml.getString(vpath));

        vpath = path + ".lore";
        if (yaml.contains(vpath))
            builder.lore(yaml.getStringList(vpath));

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

    public void handleAction(ClickStaticItemEvent event, List<String> actions) {
        actions.forEach(s -> {
            if (s.equalsIgnoreCase("[previous]")) {
                getGui().previous();
            } else if (s.equalsIgnoreCase("[next]")) {
                getGui().next();
            } else if (s.equalsIgnoreCase("[close]")) {
                getPlayer().closeInventory();
            }
        });
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setResult(Event.Result.DENY);
    }

    public ColorfulGUI getApi() {
        return api;
    }

    public YamlConfiguration getYaml() {
        return yaml;
    }

}
