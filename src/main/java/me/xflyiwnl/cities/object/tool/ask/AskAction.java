package me.xflyiwnl.cities.object.tool.ask;

public interface AskAction<T extends AskMessage> {

    void execute(T ask);

}
