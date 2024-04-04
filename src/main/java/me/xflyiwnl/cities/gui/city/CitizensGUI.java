package me.xflyiwnl.cities.gui.city;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.util.Translator;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.colorfulgui.builder.inventory.DynamicGuiBuilder;
import me.xflyiwnl.colorfulgui.object.PaginatedGui;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.provider.ColorfulProvider;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class CitizensGUI extends ColorfulProvider<PaginatedGui> {

    private Citizen citizen;
    private City city;
    private String search;

    private FileConfiguration yaml;

    public CitizensGUI(Player player, City city, String search) {
        super(player);
        this.city = city;
        this.search = search;

        citizen = CitiesAPI.getInstance().getCitizen(player);
        yaml = Cities.getInstance().getFileManager().get("gui/city/citizens.yml").yaml();
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

            StaticItem guiItem = Cities.getInstance().getGuiApi()
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
                                } else if (action.equalsIgnoreCase("[search]")) {
                                    getPlayer().closeInventory();

                                    CitiesAPI.getInstance().createAsk(
                                            citizen,
                                            Translator.of("ask.ask-messages.search-citizen"),
                                            ask -> {
                                                CitizensGUI.showGUI(getPlayer(), city, ask.getMessage());
                                            },
                                            () -> {
                                                CitizensGUI.showGUI(getPlayer(), city, search);
                                            }
                                    );

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

        for (Citizen citizen : city.getCitizens().values()) {

            if (search != null) {
                if (!citizen.getName().startsWith(search)) {
                    continue;
                }
            }

            String path = "citizen-item.";

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

            StaticItem citizenItem = Cities.getInstance().getGuiApi()
                    .staticItem()
                    .material(Material.PLAYER_HEAD)
                    .name(name)
                    .lore(lore)
                    .skull(citizen.getPlayer())
                    .build();
            getGui().addItem(citizenItem);

        }

    }

    public static void showGUI(Player player, City city, String searchOrNull) {
        FileConfiguration yaml = Cities.getInstance().getFileManager().get("gui/city/citizens.yml").yaml();
        DynamicGuiBuilder builder = Cities.getInstance().getGuiApi()
                .paginated()
                .holder(new CitizensGUI(player, city, searchOrNull))
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
