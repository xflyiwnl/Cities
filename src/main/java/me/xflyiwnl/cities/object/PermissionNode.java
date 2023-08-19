package me.xflyiwnl.cities.object;

public enum PermissionNode {

    CITY_BANK_DEPOSIT(PermissionType.CITY, "Пополнить баланс банка"),
    CITY_BANK_WITHDRAW(PermissionType.CITY, "Снимать с баланса банка"),
    CITY_LAND_CLAIM(PermissionType.CITY, "Приватить территорию"),
    CITY_LAND_UNCLAIM(PermissionType.CITY, "Расприватить территорию"),
    CITY_RANK(PermissionType.CITY, "Смотреть ранги города"),
    CITY_ONLINE(PermissionType.CITY, "Смотреть онлайн игроков"),
    CITY_BROADCAST(PermissionType.CITY, "Сделать объявление"),
    CITY_KICK(PermissionType.CITY, "Выгнать игрока"),
    CITY_INVITE(PermissionType.CITY, "Пригласить игрока"),
    CITY_SET(PermissionType.CITY, "Тут крч хз");

    private PermissionType type;
    private String lore;

    PermissionNode(PermissionType type, String lore) {
        this.type = type;
        this.lore = lore;
    }

    public PermissionType getType() {
        return type;
    }

    public String getLore() {
        return lore;
    }

}
