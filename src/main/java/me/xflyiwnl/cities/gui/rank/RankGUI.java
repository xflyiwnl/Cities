package me.xflyiwnl.cities.gui.rank;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.*;
import me.xflyiwnl.cities.object.ask.Ask;
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

public class RankGUI extends ColorfulProvider<PaginatedGui> {

    private FileConfiguration yaml;
    private Government government;
    private Citizen citizen;

    public RankGUI(Player player, Government government) {
        super(player);
        this.government = government;

        citizen = Cities.getInstance().getCitizen(player);
        yaml = Cities.getInstance().getFileManager().get("gui/rank/rank.yml").yaml();
    }

    @Override
    public void init() {

        items();
        ranks();

        show();

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
                                } else if (action.equalsIgnoreCase("[create]")) {
                                    getPlayer().closeInventory();

                                    new Ask(
                                            citizen,
                                            Translator.of("ask.ask-messages.rank-create"),
                                            ask -> {
                                                String title = ask.getMessage().getValue();

                                                String[] split = ask.getMessage().getValue().split(" ");
                                                if (split.length > 1) {
                                                    Translator.send(citizen)
                                                            .path("rank.title-format")
                                                            .run();
                                                    return;
                                                }

                                                if (Cities.getInstance().getRank(government, title) != null) {
                                                    Translator.send(citizen)
                                                            .path("rank.rank-exists")
                                                            .run();
                                                    return;
                                                }

                                                Rank rank = new Rank(
                                                        government, title
                                                );
                                                rank.create(true);

                                                Translator.send(citizen)
                                                        .path("rank.create-rank")
                                                        .replace("rank", rank.getTitle())
                                                        .run();

                                            },
                                            () -> {}
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

    public void ranks() {

        for (Rank rank : Cities.getInstance().getRanks(government)) {

            String path = "rank-item.";

            String name = yaml.getString(path + "display-name")
                    .replace("%name%", rank.getTitle());

            List<String> lore = yaml.getStringList(path + "lore");
            lore.replaceAll(word -> word.replace("%name%", rank.getTitle()));

            GuiItem rankItem = Cities.getInstance().getColorfulGUI()
                    .item()
                    .material(Material.RED_BANNER)
                    .name(name)
                    .lore(lore)
                    .action(event -> {

                        if (event.getClick().isRightClick()) {

                            Translator.send(citizen)
                                        .path("rank.rank-remove")
                                        .replace("rank", rank.getTitle())
                                        .run();

                            if (government instanceof City) {
                                for (Citizen cit : ((City) government).getCitizens()) {
                                    if (cit.getRank() != null && cit.getRank().equals(rank)) {
                                        cit.setRank(null);
                                    }
                                }
                            }

                            if (government instanceof Country) {
                                for (City city : ((Country) government).getCities()) {
                                    for (Citizen cit : city.getCitizens()) {
                                        if (cit.getRank() != null && cit.getRank().equals(rank)) {
                                            cit.setRank(null);
                                        }
                                    }
                                }
                            }

                            rank.remove();

                            RankGUI.showGUI(getPlayer(), government);

                        } else if (event.getClick().isLeftClick()) {
                            RankEditorGUI.showGUI(getPlayer(), rank, government);
                        }

                    })
                    .build();
            getGui().addItem(rankItem);

        }

    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setResult(Event.Result.DENY);
    }

    public static void showGUI(Player player, Government government) {
        FileConfiguration yaml = Cities.getInstance().getFileManager().get("gui/rank/rank.yml").yaml();
        DynamicGuiBuilder builder = Cities.getInstance().getColorfulGUI()
                .paginated()
                .holder(new RankGUI(player, government))
                .title(
                        yaml.getString("gui.title")
                                .replace("%government%", government.getName())
                )
                .rows(5);
        if (yaml.get("gui.mask") != null) {
            builder.mask(yaml.getStringList("gui.mask"));
        }
        builder.build();
    }

}
