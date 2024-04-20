package me.xflyiwnl.cities.gui.city;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.gui.BaseGUI;
import me.xflyiwnl.cities.object.citizen.Citizen;
import me.xflyiwnl.cities.object.bank.Transaction;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.object.event.click.ClickStaticItemEvent;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BankHistoryGUI extends BaseGUI {

    public static String path = "gui/city/bank-history.yml";

    private Citizen citizen;
    private City city;

    public BankHistoryGUI(Player player, Citizen citizen, City city) {
        super(player, path);
        this.citizen = citizen;
        this.city = city;
    }

    @Override
    public void init() {
        super.init();

        transactions();
    }

    public void transactions() {
        List<Transaction> transactions = city.getBank().transactions();

        if (transactions.isEmpty()) return;

        transactions.sort(Comparator.comparing(Transaction::getDate).reversed());

        for (Transaction transaction : transactions) {
            String path = "transaction-item.";

            switch (transaction.getType()) {
                case SET -> path = path + "set.";
                case WITHDRAW -> path = path + "withdraw.";
                case DEPOSIT -> path = path + "deposit.";
            }

            Material material = Material.valueOf(getYaml().getString(path + "material").toUpperCase());
            String name = getYaml().getString(path + "name");
            List<String> lore = getYaml().getStringList(path + "lore");

            int amount = (int) Math.round(transaction.getAmount() / 10.0);

            StaticItem citizenItem = getApi().staticItem()
                    .amount(amount == 0 ? 1 : amount)
                    .material(material)
                    .name(applyPlaceholders(name, transaction))
                    .lore(lore.stream().map(s -> applyPlaceholders(s, transaction))
                            .collect(Collectors.toList()))
                    .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS)
                    .build();

            getGui().addItem(citizenItem);

        }

    }

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public String applyPlaceholders(String text, Transaction transaction) {
        return text
                .replace("%from%", transaction.getFrom())
                .replace("%reason%", transaction.getReason())
                .replace("%amount%", String.valueOf(transaction.getAmount()))
                .replace("%date%", formatter.format(transaction.getDate()));
    }

    @Override
    public void handleAction(ClickStaticItemEvent event, List<String> actions) {
        super.handleAction(event, actions);

        actions.forEach(action -> {
            if (action.equalsIgnoreCase("[exit]")) {
                CityBankGUI.openGUI(getPlayer(), citizen, city);
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
                .holder(new BankHistoryGUI(player, citizen, city))
                .build();
    }

}
