package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.database.SQLDataSource;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class City extends Government implements CitizenList, Spawnable, Claimable {

    private Citizen mayor;
    private Country country;

    private Location spawn;

    private List<Citizen> citizens = new ArrayList<Citizen>();

    public City() {
        super();
    }

    public City(String name) {
        super(name);
    }

    public City(String name, double bank, Citizen mayor, Country country, Location spawn) {
        super(name);
        setBank(new Bank(this, bank));
        this.mayor = mayor;
        this.country = country;
        this.spawn = spawn;
    }

    public City(String name, UUID uuid, double bank, Citizen mayor, Country country, Location spawn) {
        super(name, uuid);
        setBank(new Bank(this, bank));
        this.mayor = mayor;
        this.country = country;
        this.spawn = spawn;
    }

    public void create(boolean save) {
        Cities.getInstance().getCities().add(this);
        if (save) {
            save();
        }
    }

    @Override
    public void save() {
        SQLDataSource source = (SQLDataSource) Cities.getInstance().getDatabase().getSource();
        source.getCityDAO().save(this);
    }

    @Override
    public void remove() {

        for (Citizen citizen : citizens) {
            citizen.setCity(null);
            citizen.save();
        }

        for (Land land : Cities.getInstance().getCityLands(this)) {
            land.remove();
        }

        Cities.getInstance().getCities().remove(this);

        SQLDataSource source = (SQLDataSource) Cities.getInstance().getDatabase().getSource();
        source.getCityDAO().remove(this);
    }

    public void broadcast(String message, boolean format) {
        for (Citizen citizen : citizens) {
            if (!citizen.isOnline()) {
                continue;
            }
            if (format) {
                Translator.send(citizen)
                        .path("other.broadcast-format")
                        .replace("city", getName())
                        .replace("message", message)
                        .run();
            } else {
                citizen.sendMessage(message);
            }
        }
    }

    @Override
    public void claimLand(Land land) {
        land.setCity(this);
        land.save();
    }

    @Override
    public void unclaimLand(Land land) {
        land.setCity(null);
        land.save();
    }

    public Citizen getMayor() {
        return mayor;
    }

    public void setMayor(Citizen mayor) {
        this.mayor = mayor;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    @Override
    public Bank getBank() {
        return super.getBank();
    }

    @Override
    public void setBank(Bank bank) {
        super.setBank(bank);
    }

    @Override
    public List<Citizen> getCitizens() {
        return citizens;
    }

    public void setCitizens(List<Citizen> citizens) {
        this.citizens = citizens;
    }

    @Override
    public void addCitizen(Citizen citizen) {
        System.out.println(getCitizens().size());

        getCitizens().add(citizen);
        citizen.setCity(this);

        broadcast(Translator.of("city.citizen-added")
                .replace("%city%", this.getName())
                .replace("%citizen%", citizen.getName()), false);

        citizen.save();

        System.out.println(getCitizens().size());
    }

    @Override
    public void leaveCitizen(Citizen citizen) {

        broadcast(Translator.of("city.citizen-leaved")
                .replace("%city%", this.getName())
                .replace("%citizen%", citizen.getName()), false);

        getCitizens().remove(citizen);
        citizen.setCity(null);


        citizen.save();
    }

    @Override
    public void kickCitizen(Citizen citizen1, Citizen citizen2) {

        broadcast(Translator.of("city.citizen-kicked")
                .replace("%city%", this.getName())
                .replace("%citizen1%", citizen2.getName())
                .replace("%citizen2%", citizen1.getName()), false);

        getCitizens().remove(citizen2);
        citizen2.setCity(null);

        citizen2.save();
    }

}
