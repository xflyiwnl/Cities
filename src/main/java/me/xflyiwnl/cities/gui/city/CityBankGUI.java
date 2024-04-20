package me.xflyiwnl.cities.gui.city;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.gui.BaseGUI;
import me.xflyiwnl.cities.object.bank.Transaction;
import me.xflyiwnl.cities.object.citizen.Citizen;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.util.Settinger;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.builder.item.StaticItemBuilder;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CityBankGUI extends BaseGUI {

    public static String path = "gui/city/bank.yml";

    private Citizen citizen;
    private City city;

    public CityBankGUI(Player player, Citizen citizen, City city) {
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

        switch (type) {
            case "HISTORY" -> {
                YamlConfiguration yaml = getYaml();

                StaticItemBuilder builder = getApi().staticItem()
                        .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS)
                        .action(event -> {
                            BankHistoryGUI.openGUI(getPlayer(), citizen, city);
                        });

                String vpath = path + ".material";
                if (yaml.contains(vpath))
                    builder.material(Material.valueOf(yaml.getString(vpath).toUpperCase()));

                vpath = path + ".amount";
                if (yaml.contains(vpath))
                    builder.amount(yaml.getInt(vpath));

                vpath = path + ".name";
                if (yaml.contains(vpath))
                    builder.name(applyPlaceholders(yaml.getString(vpath)));

                vpath = path + ".lore";
                if (yaml.contains(vpath))
                    builder.lore(yaml.getStringList(vpath).stream()
                            .map(this::applyPlaceholders)
                            .collect(Collectors.toList()));

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
            case "BANK" -> {

                YamlConfiguration yaml = getYaml();
                List<String> action = yaml.get(path + ".action") != null ? yaml.getStringList(path + ".action") : new ArrayList<>();

                StaticItemBuilder builder = getApi().staticItem()
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
                    builder.name(applyPlaceholders(yaml.getString(vpath)));

                vpath = path + ".lore";
                if (yaml.contains(vpath))
                    builder.lore(yaml.getStringList(vpath).stream()
                            .map(this::applyPlaceholders)
                            .collect(Collectors.toList()));

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
        }

    }

    public String applyPlaceholders(String text) {
        return text
                .replace("%current%", String.valueOf(city.getBank().current()))
                .replace("%upkeep%", String.valueOf(city.getUpkeep()))
                .replace("%upkeep-city%", String.valueOf(Settinger.ofDouble("upkeep.city.base")))
                .replace("%upkeep-lands%", String.valueOf(Settinger.ofDouble("upkeep.city.per-land") * city.getLands().size()));
    }

    public static void openGUI(Player player, Citizen citizen, City city) {
        YamlConfiguration yaml = Cities.getInstance().getFileManager().get(path).yaml();
        ColorfulGUI api = Cities.getInstance().getGuiApi();

        api.paginated()
                .mask(yaml.getStringList("gui.mask"))
                .title(yaml.getString("gui.title")
                        .replace("%city%", city.getName()))
                .rows(yaml.getInt("gui.rows"))
                .holder(new CityBankGUI(player, citizen, city))
                .build();
    }

}
