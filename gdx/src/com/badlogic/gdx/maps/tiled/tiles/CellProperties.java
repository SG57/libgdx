package com.badlogic.gdx.maps.tiled.tiles;

import com.badlogic.gdx.maps.tiled.TileProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;

public interface CellProperties extends TileProperties {
    public boolean isDynamic();

    public boolean isVisible();
    public void setVisible(boolean v);

    public TiledMapTile getTile();
}
