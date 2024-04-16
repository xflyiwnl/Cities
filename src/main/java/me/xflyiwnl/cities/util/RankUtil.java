package me.xflyiwnl.cities.util;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.rank.PermissionNode;
import me.xflyiwnl.cities.object.rank.PermissionType;
import me.xflyiwnl.cities.object.rank.Rank;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class RankUtil {

    public static void loadDefaultRanks() {
        Cities.getInstance().setDefaultRanks(defaultRanks());
    }

    public static Map<UUID, Rank> defaultRanks() {
        Map<UUID, Rank> ranks = new HashMap<>();
        File file = new File(Cities.getInstance().getDataFolder(), "rank");

        if (!file.exists())
            return ranks;

        for (File rankFile : file.listFiles()) {
            if (!rankFile.isFile())
                continue;

            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(rankFile);

            String name = yaml.getString("name");
            PermissionType type = PermissionType.valueOf(yaml.getString("type").toUpperCase());
            List<PermissionNode> nodes = yaml.getStringList("nodes").stream()
                    .map(s -> PermissionNode.valueOf(s.toUpperCase()))
                    .toList();

            Rank rank = new Rank(
                    name, type, nodes
            );
            ranks.put(rank.getUniqueId(), rank);

        }

        return ranks;
    }

}
