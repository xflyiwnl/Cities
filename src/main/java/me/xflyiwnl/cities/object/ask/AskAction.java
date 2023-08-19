package me.xflyiwnl.cities.object.ask;

public interface AskAction<T extends AskMessage> {

    void execute(T ask);

}
