package gtamapedit.file;

import java.util.HashMap;

import gtamapedit.conf.Configuration;
import gtamapedit.tileManager.TileImageHolder;

public class OBJBuilder {
	private enum Direction {
		NORTH, EAST, SOUTH, WEST
	}

	private static boolean hasNeighbor(Direction d, MapFileTileElement[][] mapData, int x, int y) {
		if (d.equals(Direction.NORTH)) {
			if (y > 0 && mapData[x][y - 1].getFloors() >= mapData[x][y].getFloors()) {
				return true;
			}
		}
		if (d.equals(Direction.EAST)) {
			if (x < mapData.length - 2 && mapData[x + 1][y].getFloors() >= mapData[x][y].getFloors()) {
				return true;
			}
		}
		if (d.equals(Direction.SOUTH)) {
			if (y < mapData[0].length - 2 && mapData[x][y + 1].getFloors() >= mapData[x][y].getFloors()) {
				return true;
			}
		}
		if (d.equals(Direction.WEST)) {
			if (x > 0 && mapData[x - 1][y].getFloors() >= mapData[x][y].getFloors()) {
				return true;
			}
		}
		return false;
	}

	public static String buildObjFromMap(MapFileTileElement[][] mapData, HashMap<String, Coordinate> UVMapper,
			float textureSpacing) {

		StringBuilder start = new StringBuilder();

		StringBuilder v = new StringBuilder();
		StringBuilder vn = new StringBuilder();
		StringBuilder vt = new StringBuilder();
		StringBuilder f = new StringBuilder();
		int halfts = Configuration.tileSize / 2;
		int ts = Configuration.tileSize / 2;
		int currentIndex = 0;
		int vi = 0; // vertexindex
		int vti = 0; // textureindex

		// west
		vn.append("vn -1.000000 0.000000 0.000000\n");
		// east
		vn.append("vn 1.000000 0.000000 0.000000\n");
		// up
		vn.append("vn 0.000000 0.000000 -1.000000\n");
		// south
		vn.append("vn -0.000000 1.000000 -0.000000\n");
		// north
		vn.append("vn 0.000000 -1.000000 0.000000\n");

		start.append("mtllib grid_mtl.mtl\n");
		start.append("usemtl Material_grid.png\n");

		for (int x = 0; x < mapData.length; x++) {
			for (int y = 0; y < mapData[0].length; y++) {

				// floor
				int fl = mapData[x][y].getFloors();
				String texName = mapData[x][y].getTextureTop();

				if (texName != null && !texName.isEmpty()) {
					v.append("v " + (ts * x) + " " + (ts * y) + " " + (ts * fl) + "\n");
					v.append("v " + (ts * x) + " " + (ts + ts * y) + " " + (ts * fl) + "\n");
					v.append("v " + (ts + ts * x) + " " + (ts + ts * y) + " " + (ts * fl) + "\n");
					v.append("v " + (ts + ts * x) + " " + (ts * y) + " " + (ts * fl) + "\n");

					boolean textureWasSet = false;
					if (mapData[x][y].getRotation() == 180) {
						vt.append("vt " + (UVMapper.get(texName).x + textureSpacing) + " " + (UVMapper.get(texName).y)
								+ "\n");

						vt.append("vt " + (UVMapper.get(texName).x + textureSpacing) + " "
								+ (UVMapper.get(texName).y + textureSpacing) + "\n");
						vt.append("vt " + (UVMapper.get(texName).x) + " " + (UVMapper.get(texName).y + textureSpacing)
								+ "\n");

						vt.append("vt " + (UVMapper.get(texName).x) + " " + (UVMapper.get(texName).y) + "\n");
						textureWasSet = true;
					}
					if (mapData[x][y].getRotation() == 90) {
						vt.append("vt " + (UVMapper.get(texName).x) + " " + (UVMapper.get(texName).y) + "\n");
						vt.append("vt " + (UVMapper.get(texName).x + textureSpacing) + " " + (UVMapper.get(texName).y)
								+ "\n");
						vt.append("vt " + (UVMapper.get(texName).x + textureSpacing) + " "
								+ (UVMapper.get(texName).y + textureSpacing) + "\n");
						vt.append("vt " + (UVMapper.get(texName).x) + " " + (UVMapper.get(texName).y + textureSpacing)
								+ "\n");
						textureWasSet = true;
					}
					if (mapData[x][y].getRotation() == 0) {
						vt.append("vt " + (UVMapper.get(texName).x) + " " + (UVMapper.get(texName).y + textureSpacing)
								+ "\n");
						vt.append("vt " + (UVMapper.get(texName).x) + " " + (UVMapper.get(texName).y) + "\n");
						vt.append("vt " + (UVMapper.get(texName).x + textureSpacing) + " " + (UVMapper.get(texName).y)
								+ "\n");
						vt.append("vt " + (UVMapper.get(texName).x + textureSpacing) + " "
								+ (UVMapper.get(texName).y + textureSpacing) + "\n");
						textureWasSet = true;
					}
					if (mapData[x][y].getRotation() == 270) {
						vt.append("vt " + (UVMapper.get(texName).x + textureSpacing) + " "
								+ (UVMapper.get(texName).y + textureSpacing) + "\n");

						vt.append("vt " + (UVMapper.get(texName).x) + " " + (UVMapper.get(texName).y + textureSpacing)
								+ "\n");
						vt.append("vt " + (UVMapper.get(texName).x) + " " + (UVMapper.get(texName).y) + "\n");

						vt.append("vt " + (UVMapper.get(texName).x + textureSpacing) + " " + (UVMapper.get(texName).y)
								+ "\n");
						textureWasSet = true;
					}

					if (textureWasSet) {
						f.append("f " + (vi + 1) + "/" + (vti + 1) + "/3 " + (vi + 2) + "/" + (vti + 2) + "/3 "
								+ (vi + 3) + "/" + (vti + 3) + "/3" + "\n");
						f.append("f " + (vi + 1) + "/" + (vti + 1) + "/3 " + (vi + 3) + "/" + (vti + 3) + "/3 "
								+ (vi + 4) + "/" + (vti + 4) + "/3" + "\n");
						vti += 4;
					} else {
						f.append("f " + (vi + 1) + "/1/3 " + (vi + 2) + "/1/3 " + (vi + 3) + "/1/3" + "\n");
						f.append("f " + (vi + 1) + "/1/3 " + (vi + 3) + "/1/3 " + (vi + 4) + "/1/3" + "\n");
					}
					vi += 4;

					for (fl = 0; fl < mapData[x][y].getFloors(); fl++) {
						// DrawWalls
						// TODO remove unnecessary
						if (!hasNeighbor(Direction.SOUTH, mapData, x, y)) {
							// southwall
							String texNameSouth = mapData[x][y].getTextureSouth();
							boolean southTexWasSet = false;
							if (texNameSouth != null && !texNameSouth.isEmpty()) {
								vt.append("vt " + (UVMapper.get(texNameSouth).x) + " "
										+ (UVMapper.get(texNameSouth).y + textureSpacing) + "\n");
								vt.append("vt " + (UVMapper.get(texNameSouth).x) + " " + (UVMapper.get(texNameSouth).y)
										+ "\n");
								vt.append("vt " + (UVMapper.get(texNameSouth).x + textureSpacing) + " "
										+ (UVMapper.get(texNameSouth).y) + "\n");
								vt.append("vt " + (UVMapper.get(texNameSouth).x + textureSpacing) + " "
										+ (UVMapper.get(texNameSouth).y + textureSpacing) + "\n");
								southTexWasSet = true;
							}

							v.append("v " + (ts * x) + " " + (ts + ts * y) + " " + (ts + ts * fl) + "\n");
							v.append("v " + (ts * x) + " " + (ts + ts * y) + " " + (ts * fl) + "\n");
							v.append("v " + (ts + ts * x) + " " + (ts + ts * y) + " " + (ts * fl) + "\n");
							v.append("v " + (ts + ts * x) + " " + (ts + ts * y) + " " + (ts + ts * fl) + "\n");

							if (southTexWasSet) {
								f.append("f " + (vi + 1) + "/" + (vti + 1) + "/4 " + (vi + 2) + "/" + (vti + 2) + "/4 "
										+ (vi + 3) + "/" + (vti + 3) + "/4" + "\n");
								f.append("f " + (vi + 1) + "/" + (vti + 1) + "/4 " + (vi + 3) + "/" + (vti + 3) + "/4 "
										+ (vi + 4) + "/" + (vti + 4) + "/4" + "\n");
								vti += 4;
							} else {
								f.append("f " + (vi + 1) + "/1/4 " + (vi + 2) + "/1/4 " + (vi + 3) + "/1/4" + "\n");
								f.append("f " + (vi + 1) + "/1/4 " + (vi + 3) + "/1/4 " + (vi + 4) + "/1/4" + "\n");
							}
							vi += 4;
						}
						// eastwall
						if (!hasNeighbor(Direction.EAST, mapData, x, y)) {
							String texNameEast = mapData[x][y].getTextureEast();
							boolean eastTexWasSet = false;
							if (texNameEast != null && !texNameEast.isEmpty()) {
								vt.append("vt " + (UVMapper.get(texNameEast).x) + " "
										+ (UVMapper.get(texNameEast).y + textureSpacing) + "\n");
								vt.append("vt " + (UVMapper.get(texNameEast).x) + " " + (UVMapper.get(texNameEast).y)
										+ "\n");
								vt.append("vt " + (UVMapper.get(texNameEast).x + textureSpacing) + " "
										+ (UVMapper.get(texNameEast).y) + "\n");
								vt.append("vt " + (UVMapper.get(texNameEast).x + textureSpacing) + " "
										+ (UVMapper.get(texNameEast).y + textureSpacing) + "\n");
								eastTexWasSet = true;
							}

							v.append("v " + (ts + ts * x) + " " + (ts + ts * y) + " " + (ts + ts * fl) + "\n");
							v.append("v " + (ts + ts * x) + " " + (ts + ts * y) + " " + (ts * fl) + "\n");
							v.append("v " + (ts + ts * x) + " " + (ts * y) + " " + (ts * fl) + "\n");
							v.append("v " + (ts + ts * x) + " " + (ts * y) + " " + (ts + ts * fl) + "\n");

							if (eastTexWasSet) {
								f.append("f " + (vi + 1) + "/" + (vti + 1) + "/2 " + (vi + 2) + "/" + (vti + 2) + "/2 "
										+ (vi + 3) + "/" + (vti + 3) + "/2" + "\n");
								f.append("f " + (vi + 1) + "/" + (vti + 1) + "/2 " + (vi + 3) + "/" + (vti + 3) + "/2 "
										+ (vi + 4) + "/" + (vti + 4) + "/2" + "\n");
								vti += 4;
							} else {
								f.append("f " + (vi + 1) + "/1/2 " + (vi + 2) + "/1/2 " + (vi + 3) + "/1/2" + "\n");
								f.append("f " + (vi + 1) + "/1/2 " + (vi + 3) + "/1/2 " + (vi + 4) + "/1/2" + "\n");
							}
							vi += 4;
						}
						// northwall
						if (!hasNeighbor(Direction.NORTH, mapData, x, y)) {
							String texNameNorth = mapData[x][y].getTextureNorth();
							boolean northTexWasSet = false;
							if (texNameNorth != null && !texNameNorth.isEmpty()) {
								vt.append("vt " + (UVMapper.get(texNameNorth).x) + " "
										+ (UVMapper.get(texNameNorth).y + textureSpacing) + "\n");
								vt.append("vt " + (UVMapper.get(texNameNorth).x) + " " + (UVMapper.get(texNameNorth).y)
										+ "\n");
								vt.append("vt " + (UVMapper.get(texNameNorth).x + textureSpacing) + " "
										+ (UVMapper.get(texNameNorth).y) + "\n");
								vt.append("vt " + (UVMapper.get(texNameNorth).x + textureSpacing) + " "
										+ (UVMapper.get(texNameNorth).y + textureSpacing) + "\n");
								northTexWasSet = true;
							}

							v.append("v " + (ts * x) + " " + (ts * y) + " " + (ts + ts * fl) + "\n");
							v.append("v " + (ts * x) + " " + (ts * y) + " " + (ts * fl) + "\n");
							v.append("v " + (ts + ts * x) + " " + (ts * y) + " " + (ts * fl) + "\n");
							v.append("v " + (ts + ts * x) + " " + (ts * y) + " " + (ts + ts * fl) + "\n");

							if (northTexWasSet) {
								f.append("f " + (vi + 1) + "/" + (vti + 1) + "/5 " + (vi + 2) + "/" + (vti + 2) + "/5 "
										+ (vi + 3) + "/" + (vti + 3) + "/5" + "\n");
								f.append("f " + (vi + 1) + "/" + (vti + 1) + "/5 " + (vi + 3) + "/" + (vti + 3) + "/5 "
										+ (vi + 4) + "/" + (vti + 4) + "/5" + "\n");
								vti += 4;
							} else {
								f.append("f " + (vi + 1) + "/1/5 " + (vi + 2) + "/1/5 " + (vi + 3) + "/1/5" + "\n");
								f.append("f " + (vi + 1) + "/1/5 " + (vi + 3) + "/1/5 " + (vi + 4) + "/1/5" + "\n");
							}
							vi += 4;
						}
						// westwall
						if (!hasNeighbor(Direction.WEST, mapData, x, y)) {
							String texNameWest = mapData[x][y].getTextureWest();
							boolean westTexWasSet = false;
							if (texNameWest != null && !texNameWest.isEmpty()) {
								vt.append("vt " + (UVMapper.get(texNameWest).x) + " "
										+ (UVMapper.get(texNameWest).y + textureSpacing) + "\n");
								vt.append("vt " + (UVMapper.get(texNameWest).x) + " " + (UVMapper.get(texNameWest).y)
										+ "\n");
								vt.append("vt " + (UVMapper.get(texNameWest).x + textureSpacing) + " "
										+ (UVMapper.get(texNameWest).y) + "\n");
								vt.append("vt " + (UVMapper.get(texNameWest).x + textureSpacing) + " "
										+ (UVMapper.get(texNameWest).y + textureSpacing) + "\n");
								westTexWasSet = true;
							}
							v.append("v " + (ts * x) + " " + (ts + ts * y) + " " + (ts + ts * fl) + "\n");
							v.append("v " + (ts * x) + " " + (ts + ts * y) + " " + (ts * fl) + "\n");
							v.append("v " + (ts * x) + " " + (ts * y) + " " + (ts * fl) + "\n");
							v.append("v " + (ts * x) + " " + (ts * y) + " " + (ts + ts * fl) + "\n");

							if (westTexWasSet) {
								f.append("f " + (vi + 1) + "/" + (vti + 1) + "/1 " + (vi + 2) + "/" + (vti + 2) + "/1 "
										+ (vi + 3) + "/" + (vti + 3) + "/1" + "\n");
								f.append("f " + (vi + 1) + "/" + (vti + 1) + "/1 " + (vi + 3) + "/" + (vti + 3) + "/1 "
										+ (vi + 4) + "/" + (vti + 4) + "/1" + "\n");
								vti += 4;
							} else {
								f.append("f " + (vi + 1) + "/1/1 " + (vi + 2) + "/1/1 " + (vi + 3) + "/1/1" + "\n");
								f.append("f " + (vi + 1) + "/1/1 " + (vi + 3) + "/1/1 " + (vi + 4) + "/1/1" + "\n");
							}
							vi += 4;
						}
					}
				}
			}
		}
		return start.toString() + v.toString() + vn.toString() + vt.toString() + f.toString();

	}

	/**
	 * v -1.000000 1.000000 1.000000 v -1.000000 1.000000 -1.000000 v -1.000000
	 * -1.000000 -1.000000 v 1.000000 -0.999999 1.000000 v 1.000000 1.000001
	 * 0.999999 v 1.000000 1.000000 -1.000000 v 1.000000 -1.000000 -1.000000 vn
	 * -1.000000 0.000000 0.000000 vn 1.000000 0.000000 0.000000 vn 0.000000
	 * 0.000000 1.000000 vn -0.000000 1.000000 -0.000000 vn -0.000000 -0.000000
	 * -1.000000 vn 0.000000 -1.000000 0.000000 s off f 1//1 2//1 3//1 f 1//1
	 * 3//1 4//1
	 * 
	 * f 5//2 8//2 7//2 f 5//2 7//2 6//2 f 1//3 5//3 6//3 f 1//3 6//3 2//3 f
	 * 2//4 6//4 7//4 f 2//4 7//4 3//4 f 3//5 7//5 8//5 f 3//5 8//5 4//5 f 5//6
	 * 1//6 4//6 f 5//6 4//6 8//6
	 **/

}
