package com.joshuahugh.cent2;

/**
 * Created by joshuahugh on 06/03/15.
 */
public class ToiletItem {
    private String name;
    private int shots;
    private int id;

    public ToiletItem(String name, int shots, int id) {
        this.name = name;
        this.shots = shots;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getShots() {
        return shots;
    }

    public int getId() {
        return id;
    }
}
