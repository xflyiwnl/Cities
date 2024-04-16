package me.xflyiwnl.cities.object.rank;

public enum PermissionNode {

    CITY_BANK_WITHDRAW(PermissionType.CITY, "Снимать с баланса банка"),
    CITY_LAND_CLAIM(PermissionType.CITY, "Приватить территорию"),
    CITY_LAND_UNCLAIM(PermissionType.CITY, "Расприватить территорию"),
    CITY_BROADCAST(PermissionType.CITY, "Сделать объявление"),
    CITY_KICK(PermissionType.CITY, "Выгнать игрока"),
    CITY_INVITE(PermissionType.CITY, "Пригласить игрока"),
    CITY_SET_BOARD(PermissionType.CITY, "Установить табличку"),
    CITY_SET_NAME(PermissionType.CITY, "Установить название города");

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
