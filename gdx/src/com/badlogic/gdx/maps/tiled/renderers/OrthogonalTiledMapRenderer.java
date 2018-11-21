/*******************************************************************************
 * Copyright 2013 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.maps.tiled.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import static com.badlogic.gdx.graphics.g2d.Batch.C1;
import static com.badlogic.gdx.graphics.g2d.Batch.C2;
import static com.badlogic.gdx.graphics.g2d.Batch.C3;
import static com.badlogic.gdx.graphics.g2d.Batch.C4;
import static com.badlogic.gdx.graphics.g2d.Batch.U1;
import static com.badlogic.gdx.graphics.g2d.Batch.U2;
import static com.badlogic.gdx.graphics.g2d.Batch.U3;
import static com.badlogic.gdx.graphics.g2d.Batch.U4;
import static com.badlogic.gdx.graphics.g2d.Batch.V1;
import static com.badlogic.gdx.graphics.g2d.Batch.V2;
import static com.badlogic.gdx.graphics.g2d.Batch.V3;
import static com.badlogic.gdx.graphics.g2d.Batch.V4;
import static com.badlogic.gdx.graphics.g2d.Batch.X1;
import static com.badlogic.gdx.graphics.g2d.Batch.X2;
import static com.badlogic.gdx.graphics.g2d.Batch.X3;
import static com.badlogic.gdx.graphics.g2d.Batch.X4;
import static com.badlogic.gdx.graphics.g2d.Batch.Y1;
import static com.badlogic.gdx.graphics.g2d.Batch.Y2;
import static com.badlogic.gdx.graphics.g2d.Batch.Y3;
import static com.badlogic.gdx.graphics.g2d.Batch.Y4;

public class OrthogonalTiledMapRenderer extends BatchTiledMapRenderer {

	public OrthogonalTiledMapRenderer (TiledMap map) {
		super(map);
	}

	public OrthogonalTiledMapRenderer (TiledMap map, Batch batch) {
		super(map, batch);
	}

	public OrthogonalTiledMapRenderer (TiledMap map, float unitScale) {
		super(map, unitScale);
	}

	public OrthogonalTiledMapRenderer (TiledMap map, float unitScale, Batch batch) {
		super(map, unitScale, batch);
	}

	@Override
	public void renderTileLayer (TiledMapTileLayer layer) {
		final float color = getColorFromLayer(layer);

		final int layerWidth = layer.getWidth();
		final int layerHeight = layer.getHeight();

		final float layerTileWidth = layer.getTileWidth() * unitScale;
		final float layerTileHeight = layer.getTileHeight() * unitScale;

		final float layerOffsetX = layer.getRenderOffsetX() * unitScale;
		// offset in tiled is y down, so we flip it
		final float layerOffsetY = -layer.getRenderOffsetY() * unitScale;

		final int col1 = Math.max(0, (int)((viewBounds.x - layerOffsetX) / layerTileWidth));
		final int col2 = Math.min(layerWidth,
			(int)((viewBounds.x + viewBounds.width + layerTileWidth - layerOffsetX) / layerTileWidth));

		final int row1 = Math.max(0, (int)((viewBounds.y - layerOffsetY) / layerTileHeight));
		final int row2 = Math.min(layerHeight,
			(int)((viewBounds.y + viewBounds.height + layerTileHeight - layerOffsetY) / layerTileHeight));

		float y = row2 * layerTileHeight + layerOffsetY;
		float xStart = col1 * layerTileWidth + layerOffsetX;

		for (int row = row2; row >= row1; row--) {
			float x = xStart;
			for (int col = col1; col < col2; col++) {
				final TiledMapTileLayer.Cell cell = layer.getCell(col, row);
				if (cell == null) {
					x += layerTileWidth;
					continue;
				}

				renderCell(cell , x, y, color);

				x += layerTileWidth;
			}

			y -= layerTileHeight;
		}
	}

	private float getColorFromLayer(TiledMapTileLayer layer) {
        final Color batchColor = batch.getColor();
        return Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());
    }

	public void renderCell(Cell cell, float x, float y, TiledMapTileLayer layer) {
        renderCell(cell, x, y, getColorFromLayer(layer));
	}

	private void renderCell(Cell cell, float x, float y, float color) {
        final TiledMapTile tile = cell.getTile();

        if (tile != null) {
            final boolean flipX = cell.getFlipHorizontally();
            final boolean flipY = cell.getFlipVertically();
            final int rotations = cell.getRotation();

            TextureRegion region = tile.getTextureRegion();

            float x1 = x + tile.getOffsetX() * unitScale;
            float y1 = y + tile.getOffsetY() * unitScale;
            float x2 = x1 + region.getRegionWidth() * unitScale;
            float y2 = y1 + region.getRegionHeight() * unitScale;

            float u1 = region.getU();
            float v1 = region.getV2();
            float u2 = region.getU2();
            float v2 = region.getV();

            vertices[X1] = x1;
            vertices[Y1] = y1;
            vertices[C1] = color;
            vertices[U1] = u1;
            vertices[V1] = v1;

            vertices[X2] = x1;
            vertices[Y2] = y2;
            vertices[C2] = color;
            vertices[U2] = u1;
            vertices[V2] = v2;

            vertices[X3] = x2;
            vertices[Y3] = y2;
            vertices[C3] = color;
            vertices[U3] = u2;
            vertices[V3] = v2;

            vertices[X4] = x2;
            vertices[Y4] = y1;
            vertices[C4] = color;
            vertices[U4] = u2;
            vertices[V4] = v1;

            if (flipX) {
                float temp = vertices[U1];
                vertices[U1] = vertices[U3];
                vertices[U3] = temp;
                temp = vertices[U2];
                vertices[U2] = vertices[U4];
                vertices[U4] = temp;
            }
            if (flipY) {
                float temp = vertices[V1];
                vertices[V1] = vertices[V3];
                vertices[V3] = temp;
                temp = vertices[V2];
                vertices[V2] = vertices[V4];
                vertices[V4] = temp;
            }
            if (rotations != 0) {
                switch (rotations) {
                    case Cell.ROTATE_90: {
                        float tempV = vertices[V1];
                        vertices[V1] = vertices[V2];
                        vertices[V2] = vertices[V3];
                        vertices[V3] = vertices[V4];
                        vertices[V4] = tempV;

                        float tempU = vertices[U1];
                        vertices[U1] = vertices[U2];
                        vertices[U2] = vertices[U3];
                        vertices[U3] = vertices[U4];
                        vertices[U4] = tempU;
                        break;
                    }
                    case Cell.ROTATE_180: {
                        float tempU = vertices[U1];
                        vertices[U1] = vertices[U3];
                        vertices[U3] = tempU;
                        tempU = vertices[U2];
                        vertices[U2] = vertices[U4];
                        vertices[U4] = tempU;
                        float tempV = vertices[V1];
                        vertices[V1] = vertices[V3];
                        vertices[V3] = tempV;
                        tempV = vertices[V2];
                        vertices[V2] = vertices[V4];
                        vertices[V4] = tempV;
                        break;
                    }
                    case Cell.ROTATE_270: {
                        float tempV = vertices[V1];
                        vertices[V1] = vertices[V4];
                        vertices[V4] = vertices[V3];
                        vertices[V3] = vertices[V2];
                        vertices[V2] = tempV;

                        float tempU = vertices[U1];
                        vertices[U1] = vertices[U4];
                        vertices[U4] = vertices[U3];
                        vertices[U3] = vertices[U2];
                        vertices[U2] = tempU;
                        break;
                    }
                }
            }
            batch.draw(region.getTexture(), vertices, 0, NUM_VERTICES);
        }
    }

    public void renderCellRotated(Cell cell, float x, float y, TiledMapTileLayer layer, double rotation, float origin_x, float origin_y) {
        final TiledMapTile tile = cell.getTile();

        if (tile != null) {
            final boolean flipX = cell.getFlipHorizontally();
            final boolean flipY = cell.getFlipVertically();

            // adjust rotation to the cell's rotation in case idiots rotate something being arbitrarily rotated
            rotation += cell.getRotation() * (Math.PI / 2);

            TextureRegion region = tile.getTextureRegion();

            final float color = getColorFromLayer(layer);

            // bottom left and top right corner points relative to origin
            float width = region.getRegionWidth() * unitScale;
            float height = region.getRegionHeight() * unitScale;

            final float worldOriginX = x + tile.getOffsetX() * unitScale + origin_x;
            final float worldOriginY = y + tile.getOffsetY() * unitScale + origin_y;
            float fx = -origin_x;
            float fy = -origin_y;
            float fx2 = width - origin_x;
            float fy2 = height - origin_y;

            // construct corner points, start from top left and go counter clockwise
            final double p1x = fx;
            final double p1y = fy;
            final double p2x = fx;
            final double p2y = fy2;
            final double p3x = fx2;
            final double p3y = fy2;
            final double p4x = fx2;
            final double p4y = fy;
            double x1 = p1x;
            double y1 = p1y;
            double x2 = p2x;
            double y2 = p2y;
            double x3 = p3x;
            double y3 = p3y;
            double x4 = p4x;
            double y4 = p4y;

            // rotate
            if (rotation != 0) {
                final double cos = Math.cos(rotation);
                final double sin = Math.sin(rotation);

                x1 = cos * p1x - sin * p1y;
                y1 = sin * p1x + cos * p1y;

                x2 = cos * p2x - sin * p2y;
                y2 = sin * p2x + cos * p2y;

                x3 = cos * p3x - sin * p3y;
                y3 = sin * p3x + cos * p3y;

                x4 = x1 + (x3 - x2);
                y4 = y3 - (y2 - y1);
            }

            // translate over
            x1 += worldOriginX;
            y1 += worldOriginY;
            x2 += worldOriginX;
            y2 += worldOriginY;
            x3 += worldOriginX;
            y3 += worldOriginY;
            x4 += worldOriginX;
            y4 += worldOriginY;


            float u1 = region.getU();
            float v1 = region.getV2();
            float u2 = region.getU2();
            float v2 = region.getV();

            vertices[X1] = (float) x1;
            vertices[Y1] = (float) y1;
            vertices[C1] = color;
            vertices[U1] = u1;
            vertices[V1] = v1;

            vertices[X2] = (float) x2;
            vertices[Y2] = (float) y2;
            vertices[C2] = color;
            vertices[U2] = u1;
            vertices[V2] = v2;

            vertices[X3] = (float) x3;
            vertices[Y3] = (float) y3;
            vertices[C3] = color;
            vertices[U3] = u2;
            vertices[V3] = v2;

            vertices[X4] = (float) x4;
            vertices[Y4] = (float) y4;
            vertices[C4] = color;
            vertices[U4] = u2;
            vertices[V4] = v1;

            if (flipX) {
                float temp = vertices[U1];
                vertices[U1] = vertices[U3];
                vertices[U3] = temp;
                temp = vertices[U2];
                vertices[U2] = vertices[U4];
                vertices[U4] = temp;
            }
            if (flipY) {
                float temp = vertices[V1];
                vertices[V1] = vertices[V3];
                vertices[V3] = temp;
                temp = vertices[V2];
                vertices[V2] = vertices[V4];
                vertices[V4] = temp;
            }

            batch.draw(region.getTexture(), vertices, 0, NUM_VERTICES);
        }
    }
}
