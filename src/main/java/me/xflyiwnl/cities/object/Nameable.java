package me.xflyiwnl.cities.object;

public interface Nameable {

    String getName();

    default String gotFormattedName() {
        return getName().replace("_", " ");
    }

}
