package me.xflyiwnl.cities.object.switches;

import me.xflyiwnl.cities.object.Government;

import java.util.ArrayList;
import java.util.List;

public class Switch implements SwitchHandler {

    private List<SwitchNode> nodes = new ArrayList<>();

    public Switch() {
    }

    public Switch(List<SwitchNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public List<SwitchNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<SwitchNode> nodes) {
        this.nodes = nodes;
    }

}
