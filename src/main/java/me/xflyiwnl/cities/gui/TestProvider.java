package me.xflyiwnl.cities.gui;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.colorfulgui.object.Gui;
import me.xflyiwnl.colorfulgui.object.GuiItem;
import me.xflyiwnl.colorfulgui.provider.ColorfulProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.Arrays;

public class TestProvider extends ColorfulProvider<Gui> {

    public TestProvider(Player player) {
        super(player, 1);
    }

    @Override
    public void init() {

        GuiItem borderItem = Cities.getInstance().getColorfulGUI().item()
                .material(Material.BLACK_STAINED_GLASS_PANE)
                .name("Барьер")
                .lore(Arrays.asList(
                        "1",
                        "2",
                        "3"
                ))
                .amount(1)
                .action(event -> {
                    getPlayer().sendMessage("ты кликнул по барьеру");
                })
                .build();
        getGui().getMask().addItem("B", borderItem);

        GuiItem simpleItem = Cities.getInstance().getColorfulGUI().item()
                .material(Material.GRASS_BLOCK)
                .name("Просто предмет")
                .lore(Arrays.asList(
                        "1",
                        "2",
                        "3"
                ))
                .amount(1)
                .action(event -> {
                    getPlayer().sendMessage("ты кликнул по простому предмету");
                })
                .build();
        getGui().getMask().addItem("S", simpleItem);

        show();
    }

    @Override
    public void update() {
        getPlayer().sendMessage("update event");
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        getPlayer().sendMessage("click event");
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        getPlayer().sendMessage("open event");
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        getPlayer().sendMessage("close event");
    }

    @Override
    public void onDrag(InventoryDragEvent event) {
        getPlayer().sendMessage("drag event");
    }

    public static void showGUI(Player player) {
        Cities.getInstance().getColorfulGUI().gui()
                .holder(new TestProvider(player))
                .title("Меню")
                .rows(5)
                .mask(Arrays.asList(
                                "BBBBBBBBB",
                                "BOOOOOOOB",
                                "BOOOSOOOB",
                                "BOOOOOOOB",
                                "BBBBBBBBB"
                        )
                )
                .build();
    }

}
