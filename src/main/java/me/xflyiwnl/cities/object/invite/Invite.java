package me.xflyiwnl.cities.object.invite;

public interface Invite {

    void init();

    void accept();
    void decline();
    void timeOut();
    void remove();

    InviteType type();
    int getSeconds();
    void setSeconds(int seconds);

}
