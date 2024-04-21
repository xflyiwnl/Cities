package me.xflyiwnl.cities.gui.city;

import com.google.gson.internal.bind.JsonTreeReader;
import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.gui.BaseGUI;
import me.xflyiwnl.cities.gui.rank.RankGUI;
import me.xflyiwnl.cities.gui.switches.SwitchGUI;
import me.xflyiwnl.cities.object.citizen.Citizen;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.util.Settinger;
import me.xflyiwnl.cities.util.Translator;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.builder.item.StaticItemBuilder;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.object.action.click.ClickStaticAction;
import me.xflyiwnl.colorfulgui.object.event.click.ClickStaticItemEvent;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScreenGUI extends BaseGUI {

    public static String path = "gui/city/info-screen.yml";

    private Citizen citizen;
    private City city;

    public ScreenGUI(Player player, Citizen citizen, City city) {
        super(player, path);
        this.citizen = citizen;
        this.city = city;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void typeItem(String path, String type) {
        super.typeItem(path, type);

        switch (type.toLowerCase()) {
            case "city" -> {
                String name = "";
                List<String> lore = new ArrayList<>();

                if (getYaml().contains(path + ".name"))
                    name = getYaml().getString(path + ".name");

                if (getYaml().contains(path + ".lore"))
                    lore = getYaml().getStringList(path + ".lore");

                createItem(path, applyCityPlaceholders(name), lore.stream()
                        .map(this::applyCityPlaceholders)
                        .collect(Collectors.toList()), clickStaticItemEvent -> {});
            }
            case "bank" -> {
                String name = "";
                List<String> lore = new ArrayList<>();

                if (getYaml().contains(path + ".name"))
                    name = getYaml().getString(path + ".name");

                if (getYaml().contains(path + ".lore"))
                    lore = getYaml().getStringList(path + ".lore");

                createItem(path, applyBankPlaceholders(name), lore.stream()
                        .map(this::applyBankPlaceholders)
                        .collect(Collectors.toList()), clickStaticItemEvent -> {
                    CityBankGUI.openGUI(getPlayer(), citizen, city);
                });
            }
            case "rank" -> {
                String name = "";
                List<String> lore = new ArrayList<>();

                if (getYaml().contains(path + ".name"))
                    name = getYaml().getString(path + ".name");

                if (getYaml().contains(path + ".lore"))
                    lore = getYaml().getStringList(path + ".lore");

                createItem(path, applyRankPlaceholders(name), lore.stream()
                        .map(this::applyRankPlaceholders)
                        .collect(Collectors.toList()), clickStaticItemEvent -> {
                    RankGUI.openGUI(getPlayer(), city);
                });
            }

            case "citizens" -> {
                String name = "";
                List<String> lore = new ArrayList<>();

                if (getYaml().contains(path + ".name"))
                    name = getYaml().getString(path + ".name");

                if (getYaml().contains(path + ".lore"))
                    lore = getYaml().getStringList(path + ".lore");

                createItem(path, applyCitizensPlaceholders(name), lore.stream()
                        .map(this::applyCitizensPlaceholders)
                        .collect(Collectors.toList()), clickStaticItemEvent -> {
                    CitizensGUI.openGUI(getPlayer(), citizen, city, null, false);
                });
            }

            case "switch" -> {
                String name = "";
                List<String> lore = new ArrayList<>();

                if (getYaml().contains(path + ".name"))
                    name = getYaml().getString(path + ".name");

                if (getYaml().contains(path + ".lore"))
                    lore = getYaml().getStringList(path + ".lore");

                createItem(path, name, lore, clickStaticItemEvent -> {
                    SwitchGUI.openGUI(getPlayer(), citizen, city);
                });
            }

            case "permission" -> {
                String name = "";
                List<String> lore = new ArrayList<>();

                if (getYaml().contains(path + ".name"))
                    name = getYaml().getString(path + ".name");

                if (getYaml().contains(path + ".lore"))
                    lore = getYaml().getStringList(path + ".lore");

                createItem(path, name, lore, clickStaticItemEvent -> {
                    getPlayer().sendMessage("Скоро!");
                });
            }

        }

    }

    public String applyCitizensPlaceholders(String text) {
        return text
                .replace("%citizens%", String.valueOf(city.getCitizens().size()));
    }

    public String applyRankPlaceholders(String text) {
        return text
                .replace("%ranks%", String.valueOf(city.getRanks().size()));
    }

    public String applyBankPlaceholders(String text) {
        return text
                .replace("%current%", String.valueOf(city.getBank().current()))
                .replace("%upkeep%", String.valueOf(city.getUpkeep()));
    }

    public String applyCityPlaceholders(String text) {
        return text
                .replace("%name%", city.getName())
                .replace("%board%", city.getBoard())
                .replace("%mayor%", city.getMayor().getName())
                .replace("%citizens%", String.valueOf(city.getCitizens().size()))
                .replace("%lands%", String.valueOf(city.getLands().size()));
    }

    public void createItem(String path, String name, List<String> lore, ClickStaticAction action) {
        YamlConfiguration yaml = getYaml();

        StaticItemBuilder builder = getApi().staticItem()
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS)
                .action(action);

        String vpath = path + ".material";
        if (yaml.contains(vpath))
            builder.material(Material.valueOf(yaml.getString(vpath).toUpperCase()));

        vpath = path + ".amount";
        if (yaml.contains(vpath))
            builder.amount(yaml.getInt(vpath));

        builder.name(name);
        builder.lore(lore);

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

    public static void openGUI(Player player, Citizen citizen, City city) {
        YamlConfiguration yaml = Cities.getInstance().getFileManager().get(path).yaml();
        ColorfulGUI api = Cities.getInstance().getGuiApi();

        api.paginated()
                .mask(yaml.getStringList("gui.mask"))
                .title(yaml.getString("gui.title")
                        .replace("%city%", city.getName()))
                .rows(yaml.getInt("gui.rows"))
                .holder(new ScreenGUI(player, citizen, city))
                .build();
    }

}