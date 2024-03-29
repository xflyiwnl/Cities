package me.xflyiwnl.cities.dynmap;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.land.Land;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.*;

import java.util.*;

public class DynmapDrawer {

    private final int TOWN_BLOCK_SIZE = 16;
    private Plugin dynmap;
    private MarkerSet set;
    private DynmapAPI api;
    private MarkerAPI markerapi;
    private Map<String, PolyLineMarker> lines = new HashMap<>();
    private Map<String, AreaMarker> resareas = new HashMap<>();
    private Map<String, Marker> resmark = new HashMap<>();
    private boolean isHomeSetted;

    enum Direction { XPLUS, ZPLUS, XMINUS, ZMINUS };

    public void enable() throws Exception {
        PluginManager pm = Cities.getInstance().getServer().getPluginManager();
        dynmap = pm.getPlugin("dynmap");
        api = (DynmapAPI)dynmap;
        markerapi = api.getMarkerAPI();
        if(markerapi == null) {
            throw new Exception("Cannot find Dynmap marker API.");
        }

        set = markerapi.getMarkerSet("Cities");
        if(set == null)
            set = markerapi.createMarkerSet("Cities", "Cities", null, false);
        else
            set.setMarkerSetLabel("Cities");
        if(set == null) {
            return;
        }

        updateCities();

    }

    public void updateCities() {
        Map<String, PolyLineMarker> newline = new HashMap<>();
        Map<String,AreaMarker> newmap = new HashMap<>();
        Map<String,Marker> newmark = new HashMap<>();

        List<City> cities = Cities.getInstance().getCities();
        for(City city : cities) {
            isHomeSetted = false;
            handleTown(city, newmap, newmark, newline, null);
        }

        /* Now, review old map - anything left is gone */
        for(AreaMarker oldm : resareas.values()) {
            oldm.deleteMarker();
        }
        for(PolyLineMarker oldplm : lines.values()) {
            oldplm.deleteMarker();
        }
        for(Marker oldm : resmark.values()) {
            oldm.deleteMarker();
        }
        /* And replace with new map */
        lines = newline;
        resareas = newmap;
        resmark = newmark;

    }

    public void disable() {
        if(set != null) {
            set.deleteMarkerSet();
            set = null;
        }
        resareas.clear();
        lines.clear();
    }

    private int floodFillTarget(TileFlags src, TileFlags dest, int x, int y) {
        int cnt = 0;
        ArrayDeque<int[]> stack = new ArrayDeque<int[]>();
        stack.push(new int[] { x, y });

        while(stack.isEmpty() == false) {
            int[] nxt = stack.pop();
            x = nxt[0];
            y = nxt[1];
            if(src.getFlag(x, y)) { /* Set in src */
                src.setFlag(x, y, false);   /* Clear source */
                dest.setFlag(x, y, true);   /* Set in destination */
                cnt++;
                if(src.getFlag(x+1, y))
                    stack.push(new int[] { x+1, y });
                if(src.getFlag(x-1, y))
                    stack.push(new int[] { x-1, y });
                if(src.getFlag(x, y+1))
                    stack.push(new int[] { x, y+1 });
                if(src.getFlag(x, y-1))
                    stack.push(new int[] { x, y-1 });
            }
        }
        return cnt;
    }

    private void handleTown(City city, Map<String, AreaMarker> newmap, Map<String, Marker> newmark, Map<String, PolyLineMarker> newline, Land land) {
        String name = city.getName();
        double[] x;
        double[] z;
        int poly_index = 0;

        Collection<Land> lands = Cities.getInstance().getCityLands(city);
        if(lands.isEmpty()) return;

        LinkedList<Land> nodevals = new LinkedList<>();
        TileFlags curblks = new TileFlags();

        if(land == null) {
            for(Land land1 : lands) {
                curblks.setFlag((int) land1.getCord2().getX(), (int) land1.getCord2().getZ(), true);
                nodevals.addLast(land1);
            }
        } else {
            Land reg = Cities.getInstance().getLand(land.getCord2());
            curblks.setFlag((int) reg.getCord2().getX(), (int) reg.getCord2().getZ(), true);
            nodevals.addLast(reg);
        }


        while(nodevals != null) {
            LinkedList<Land> ournodes = null;
            LinkedList<Land> newlist = null;
            TileFlags ourblks = null;
            int minx = Integer.MAX_VALUE;
            int minz = Integer.MAX_VALUE;
            for(Land node : nodevals) {
                int nodex = (int) node.getCord2().getX();
                int nodez = (int) node.getCord2().getZ();

                if((ourblks == null) && curblks.getFlag(nodex, nodez)) {
                    ourblks = new TileFlags();
                    ournodes = new LinkedList<Land>();
                    floodFillTarget(curblks, ourblks, nodex, nodez);
                    ournodes.add(node);
                    minx = nodex; minz = nodez;
                }
                else if((ourblks != null) && ourblks.getFlag(nodex, nodez)) {
                    ournodes.add(node);
                    if(nodex < minx) {
                        minx = nodex; minz = nodez;
                    }
                    else if((nodex == minx) && (nodez < minz)) {
                        minz = nodez;
                    }
                }
                else {
                    if(newlist == null) newlist = new LinkedList<Land>();
                    newlist.add(node);
                }
            }
            nodevals = newlist;
            if(ourblks != null) {
                int init_x = minx;
                int init_z = minz;
                int cur_x = minx;
                int cur_z = minz;
                Direction dir = Direction.XPLUS;
                ArrayList<int[]> linelist = new ArrayList<int[]>();
                linelist.add(new int[] { init_x, init_z } );
                while((cur_x != init_x) || (cur_z != init_z) || (dir != Direction.ZMINUS)) {
                    switch(dir) {
                        case XPLUS:
                            if(!ourblks.getFlag(cur_x+1, cur_z)) {
                                linelist.add(new int[] { cur_x+1, cur_z });
                                dir = Direction.ZPLUS;
                            }
                            else if(!ourblks.getFlag(cur_x+1, cur_z-1)) {
                                cur_x++;
                            }
                            else {
                                linelist.add(new int[] { cur_x+1, cur_z });
                                dir = Direction.ZMINUS;
                                cur_x++; cur_z--;
                            }
                            break;
                        case ZPLUS:
                            if(!ourblks.getFlag(cur_x, cur_z+1)) {
                                linelist.add(new int[] { cur_x+1, cur_z+1 });
                                dir = Direction.XMINUS;
                            }
                            else if(!ourblks.getFlag(cur_x+1, cur_z+1)) {
                                cur_z++;
                            }
                            else {
                                linelist.add(new int[] { cur_x+1, cur_z+1 });
                                dir = Direction.XPLUS;
                                cur_x++; cur_z++;
                            }
                            break;
                        case XMINUS:
                            if(!ourblks.getFlag(cur_x-1, cur_z)) {
                                linelist.add(new int[] { cur_x, cur_z+1 });
                                dir = Direction.ZMINUS;
                            }
                            else if(!ourblks.getFlag(cur_x-1, cur_z+1)) {
                                cur_x--;
                            }
                            else {
                                linelist.add(new int[] { cur_x, cur_z+1 });
                                dir = Direction.ZPLUS;
                                cur_x--; cur_z++;
                            }
                            break;
                        case ZMINUS: /* Segment in Z- direction */
                            if(!ourblks.getFlag(cur_x, cur_z-1)) {
                                linelist.add(new int[] { cur_x, cur_z });
                                dir = Direction.XPLUS;
                            }
                            else if(!ourblks.getFlag(cur_x-1, cur_z-1)) {
                                cur_z--;
                            }
                            else {
                                linelist.add(new int[] { cur_x, cur_z });
                                dir = Direction.XMINUS;
                                cur_x--; cur_z--;
                            }
                            break;
                    }
                }

                String polyid = city.getUniqueId() + "__" + poly_index;
                if(land != null) {
                    polyid = String.valueOf(land.hashCode());
                }

                int sz = linelist.size();
                x = new double[sz];
                z = new double[sz];
                for(int i = 0; i < sz; i++) {
                    int[] line = linelist.get(i);
                    x[i] = (double)line[0] * (double)TOWN_BLOCK_SIZE;
                    z[i] = (double)line[1] * (double)TOWN_BLOCK_SIZE;
                }


                AreaMarker m = resareas.remove(polyid);
                if(m == null) {
                    m = set.createAreaMarker(polyid, name, false, "world", x, z, false);
                    if(m == null) {
                        return;
                    }
                }
                else {
                    m.setCornerLocations(x, z);
                    m.setLabel(name);
                }

                m.setLineStyle(3, 1D, 0x000000);
                m.setFillStyle(0.7, 0x46CAB1);

                newmap.put(polyid, m);
                poly_index++;

            }
        }
    }

}