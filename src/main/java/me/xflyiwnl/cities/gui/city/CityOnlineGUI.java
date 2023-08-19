package me.xflyiwnl.cities.gui.city;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.City;
import me.xflyiwnl.colorfulgui.builder.inventory.DynamicGuiBuilder;
import me.xflyiwnl.colorfulgui.object.GuiItem;
import me.xflyiwnl.colorfulgui.object.PaginatedGui;
import me.xflyiwnl.colorfulgui.provider.ColorfulProvider;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class CityOnlineGUI extends ColorfulProvider<PaginatedGui> {

    private City city;
    private FileConfiguration yaml;

    public CityOnlineGUI(Player player, City city) {
        super(player);
        this.city = city;

        yaml = Cities.getInstance().getFileManager().get("gui/city/online-city.yml").yaml();
    }

    @Override
    public void init() {

        items();
        online();

        show();
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setResult(Event.Result.DENY);
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

            GuiItem guiItem = Cities.getInstance().getColorfulGUI()
                    .item()
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

    public void online() {

        for (Citizen citizen : city.getCitizens()) {

            if (!citizen.isOnline()) {
                continue;
            }

            String path = "online-item.";

            String name = yaml.getString(path + "display-name")
                    .replace("%city%", city.getName())
                    .replace("%name%", citizen.getName())
                    .replace("%rank%", "Президент")
                    .replace("%joined%", citizen.getJoinedCity());

            List<String> lore = yaml.getStringList(path + "lore");
            lore.replaceAll(word -> word.replace("%city%", city.getName())
                    .replace("%name%", citizen.getName())
                    .replace("%rank%", "Президент")
                    .replace("%joined%", citizen.getJoinedCity()));

            GuiItem onlineItem = Cities.getInstance().getColorfulGUI()
                    .item()
                    .material(Material.PLAYER_HEAD)
                    .name(name)
                    .lore(lore)
                    .skull(citizen.getPlayer())
                    .build();
            getGui().addItem(onlineItem);

        }

    }

    public static void showGUI(Player player, City city) {
        FileConfiguration yaml = Cities.getInstance().getFileManager().get("gui/city/online-city.yml").yaml();
        DynamicGuiBuilder builder = Cities.getInstance().getColorfulGUI()
                .paginated()
                .holder(new CityOnlineGUI(player, city))
                .title(
                        yaml.getString("gui.title")
                                .replace("%city%", city.getName())
                )
                .rows(5);
        if (yaml.get("gui.mask") != null) {
            builder.mask(yaml.getStringList("gui.mask"));
        }
        builder.build();
    }

}
