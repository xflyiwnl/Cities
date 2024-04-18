package me.xflyiwnl.cities.gui.city;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.gui.BaseGUI;
import me.xflyiwnl.cities.object.citizen.Citizen;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.util.Settinger;
import me.xflyiwnl.cities.util.Translator;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.object.event.click.ClickStaticItemEvent;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class CitizensGUI extends BaseGUI {

    public static String path = "gui/city/citizens.yml";

    private Citizen citizen;
    private City city;

    private String search;
    private boolean online = false;

    public CitizensGUI(Player player, Citizen citizen, City city, String search, boolean online) {
        super(player, path);
        this.citizen = citizen;
        this.city = city;
        this.search = search;
        this.online = online;
    }

    @Override
    public void init() {
        super.init();

        citizens();
    }

    public void citizens() {
        List<Citizen> citizens = city.getCitizens().values()
                .stream().toList();
        
        if (citizens.isEmpty()) return;

        int sum = 0;
        for (Citizen guiCitizen : citizens) {

            if (online && !guiCitizen.isOnline())
                continue;

            if (search != null && !guiCitizen.getName().startsWith(search))
                continue;

            sum++;
            String path = "citizen-item.";

            String name = getYaml().getString(path + "name");
            List<String> lore = getYaml().getStringList(path + "lore");

            StaticItem citizenItem = getApi().staticItem()
                    .amount(sum)
                    .material(Material.PLAYER_HEAD)
                    .skull(guiCitizen.getPlayer())
                    .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS)
                    .name(applyPlaceholders(name, guiCitizen))
                    .lore(lore.stream().map(s -> applyPlaceholders(s, guiCitizen))
                            .collect(Collectors.toList()))
                    .build();

            getGui().addItem(citizenItem);

        }

    }

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Settinger.ofString("time.format"));

    public String applyPlaceholders(String text, Citizen guiCitizen) {
        return text
                .replace("%city%", city.getName())
                .replace("%name%", guiCitizen.getName())
                .replace("%rank%", "Делаем")
                .replace("%joined%", formatter.format(guiCitizen.getJoinedCity()));
    }

    @Override
    public void handleAction(ClickStaticItemEvent event, List<String> actions) {
        super.handleAction(event, actions);

        actions.forEach(s -> {
            if (s.equalsIgnoreCase("[search]")) {
                handleSearch();
            }
        });

    }

    public void handleSearch() {

        getPlayer().closeInventory();

        CitiesAPI.getInstance().createAsk(
                citizen,
                Translator.of("ask.ask-messages.search-citizen"),
                ask -> {
                    CitizensGUI.openGUI(getPlayer(), citizen, city, ask.getMessage(), online);
                },
                () -> {
                    CitizensGUI.openGUI(getPlayer(), citizen, city, null, online);
                });

    }

    public static void openGUI(Player player, Citizen citizen, City city, String search, boolean online) {
        YamlConfiguration yaml = Cities.getInstance().getFileManager().get(path).yaml();
        ColorfulGUI api = Cities.getInstance().getGuiApi();

        api.paginated()
                .mask(yaml.getStringList("gui.mask"))
                .title(yaml.getString("gui.title")
                        .replace("%city%", city.getName()))
                .rows(yaml.getInt("gui.rows"))
                .holder(new CitizensGUI(player, citizen, city, search, online))
                .build();
    }

}
