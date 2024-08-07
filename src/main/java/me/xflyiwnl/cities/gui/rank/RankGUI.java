package me.xflyiwnl.cities.gui.rank;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.gui.BaseGUI;
import me.xflyiwnl.cities.gui.city.ScreenGUI;
import me.xflyiwnl.cities.object.citizen.Citizen;
import me.xflyiwnl.cities.object.Government;
import me.xflyiwnl.cities.object.rank.PermissionType;
import me.xflyiwnl.cities.util.Translator;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.country.Country;
import me.xflyiwnl.cities.object.rank.Rank;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.builder.item.StaticItemBuilder;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.object.event.click.ClickStaticItemEvent;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RankGUI extends BaseGUI {

    public static String path = "gui/rank/rank.yml";

    private final Government government;
    private final Citizen citizen;

    public RankGUI(Player player, Government government) {
        super(player, path);
        this.government = government;

        citizen = CitiesAPI.getInstance().getCitizen(player);
    }

    @Override
    public void init() {
        super.init();

        ranks();
    }

    public void ranks() {

        int sum = 0;
        for (Rank rank : government.getRanks().values()) {
            String path = "rank-item.";
            sum++;

            String name = getYaml().getString(path + "name");

            List<String> lore = getYaml().getStringList(path + "lore");

            StaticItem rankItem = getApi().staticItem()
                    .material(Material.RED_BANNER)
                    .amount(sum)
                    .name(applyPlaceholders(name, rank))
                    .lore(lore.stream()
                            .map(s -> applyPlaceholders(s, rank))
                            .collect(Collectors.toList()))
                    .action(event -> {
                        handleClick(event, rank);
                    })
                    .build();
            getGui().addItem(rankItem);

        }

    }

    public void handleClick(ClickStaticItemEvent event, Rank rank) {
        if (event.getClick().isRightClick()) {

            citizen.sendMessage(Translator.of("rank.rank-remove")
                    .replace("%rank%", rank.getTitle()));

            if (government instanceof City city) {
                for (Citizen cit : city.getCitizens().values()) {
                    if (cit.hasRank(rank)) {
                        cit.removeRank(rank);
                    }
                }
            }

            if (government instanceof Country country) {
                for (City city : country.getCities().values()) {
                    for (Citizen cit : city.getCitizens().values()) {
                        if (cit.hasRank(rank)) {
                            cit.removeRank(rank);
                        }
                    }
                }
            }

            rank.remove();

            RankGUI.openGUI(getPlayer(), government);
        } else if (event.getClick().isLeftClick()) {
            RankEditorGUI.openGUI(getPlayer(), rank, government);
        }
    }

    public String applyPlaceholders(String text, Rank rank) {
        return text.replace("%name%", rank.getTitle());
    }

    @Override
    public void handleAction(ClickStaticItemEvent event, List<String> actions) {
        super.handleAction(event, actions);

        for (String action : actions) {
            if (action.equalsIgnoreCase("[next]")) {
                getGui().next();
            } else if (action.equalsIgnoreCase("[previous]")) {
                getGui().previous();
            } else if (action.equalsIgnoreCase("[create]")) {
                handleCreate();
            } else if (action.equalsIgnoreCase("[exit]")) {
                ScreenGUI.openGUI(getPlayer(), citizen, citizen.getCity());
            }
        }

    }

    public void handleCreate() {
        getPlayer().closeInventory();

        CitiesAPI.getInstance().createAsk(
                citizen,
                Translator.of("ask.ask-messages.rank-create"),
                ask -> {
                    String title = ask.getMessage();

                    String[] split = ask.getMessage().split(" ");
                    if (split.length > 1) {
                        citizen.sendMessage(Translator.of("rank.title-format"));
                        return;
                    }

                    if (government.getRankByName(title) != null) {
                        citizen.sendMessage(Translator.of("rank.rank-exists"));
                        return;
                    }

                    Rank rank = new Rank(
                            government, title, (government instanceof City) ? PermissionType.CITY : PermissionType.COUNTRY
                    );
                    rank.create();
                    rank.save();

                    citizen.sendMessage(Translator.of("rank.create-rank")
                            .replace("%rank%", rank.getTitle()));

                    RankGUI.openGUI(getPlayer(), government);
                },
                () -> {}
        );
    }

    @Override
    public void typeItem(String path, String type) {
        super.typeItem(path, type);

        if (type.equalsIgnoreCase("rank")) {
            List<String> action = getYaml().get(path + ".action") != null ? getYaml().getStringList(path + ".action") : new ArrayList<>();

            StaticItemBuilder builder = getApi().staticItem()
                    .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS)
                    .action(event -> {
                        handleAction(event, action);
                    });

            String vpath = path + ".material";
            if (getYaml().contains(vpath))
                builder.material(Material.valueOf(getYaml().getString(vpath).toUpperCase()));

            vpath = path + ".amount";
            if (getYaml().contains(vpath))
                builder.amount(getYaml().getInt(vpath));

            vpath = path + ".name";
            if (getYaml().contains(vpath))
                builder.name(applyRankPlaceholders(getYaml().getString(vpath)));

            vpath = path + ".lore";
            if (getYaml().contains(vpath))
                builder.lore(getYaml().getStringList(vpath).stream()
                        .map(this::applyRankPlaceholders)
                        .collect(Collectors.toList()));

            StaticItem staticItem = builder.build();

            vpath = path + ".mask";
            if (getYaml().contains(vpath))
                getGui().addMask(getYaml().getString(vpath), staticItem);

            vpath = path + ".slot";
            if (getYaml().contains(vpath))
                getGui().setItem(getYaml().getInt(vpath), staticItem);

            vpath = path + ".slots";
            if (getYaml().contains(vpath))
                getYaml().getIntegerList(vpath).forEach(integer -> {
                    getGui().setItem(integer, staticItem);
                });
        }
    }

    public String applyRankPlaceholders(String text) {
        return text.replace("%ranks%", String.valueOf(government.getRanks().size()));
    }

    public static void openGUI(Player player, Government government) {
        YamlConfiguration yaml = Cities.getInstance().getFileManager().get(path).yaml();
        ColorfulGUI api = Cities.getInstance().getGuiApi();

        api.paginated()
                .mask(yaml.getStringList("gui.mask"))
                .title(
                        yaml.getString("gui.title")
                                .replace("%government%", government.getName())
                )
                .rows(yaml.getInt("gui.rows"))
                .holder(new RankGUI(player, government))
                .build();
    }

}
