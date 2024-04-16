package me.xflyiwnl.cities.gui.rank;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.gui.BaseGUI;
import me.xflyiwnl.cities.gui.city.CitizensGUI;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Government;
import me.xflyiwnl.cities.object.rank.PermissionType;
import me.xflyiwnl.cities.util.Translator;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.country.Country;
import me.xflyiwnl.cities.object.rank.Rank;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.builder.inventory.DynamicGuiBuilder;
import me.xflyiwnl.colorfulgui.object.PaginatedGui;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.object.action.click.ClickStaticAction;
import me.xflyiwnl.colorfulgui.object.event.click.ClickStaticItemEvent;
import me.xflyiwnl.colorfulgui.provider.ColorfulProvider;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;

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
