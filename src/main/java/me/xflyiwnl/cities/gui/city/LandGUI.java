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

public class LandGUI extends BaseGUI {

    public static String path = "gui/city/lands.yml";

    private Citizen citizen;
    private City city;

    public LandGUI(Player player, Citizen citizen, City city) {
        super(player, path);
        this.citizen = citizen;
        this.city = city;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void handleAction(ClickStaticItemEvent event, List<String> actions) {
        super.handleAction(event, actions);

        actions.forEach(s -> {
            if (s.equalsIgnoreCase("[exit]")) {
                ScreenGUI.openGUI(getPlayer(), citizen, citizen.getCity());
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
                .holder(new LandGUI(player, citizen, city))
                .build();
    }

}
