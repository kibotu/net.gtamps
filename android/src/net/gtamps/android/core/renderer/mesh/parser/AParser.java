package net.gtamps.android.core.renderer.mesh.parser;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import net.gtamps.android.core.Registry;
import net.gtamps.shared.math.Color4;
import net.gtamps.android.core.renderer.mesh.Uv;
import net.gtamps.android.core.utils.Utils;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.math.Vector3;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Abstract parser class with basic parsing functionality.
 * 
 * @author dennis.ippel
 *
 */
public abstract class AParser implements IParser {

    public static final String TAG = AParser.class.getSimpleName();

	protected String resourceID;
	protected String packageID;
	protected String currentMaterialKey;
	protected ArrayList<ParseObjectData> parseObjects;
	protected ParseObjectData co;
	protected boolean firstObject;
	protected TextureAtlas textureAtlas;
	protected ArrayList<Vector3> vertices;
	protected ArrayList<Uv> texCoords;
	protected ArrayList<Vector3> normals;
	protected boolean generateMipMap;
	protected HashMap<String, Material> materialMap;
	
	public AParser() {
		vertices = new ArrayList<Vector3>();
		texCoords = new ArrayList<Uv>();
		normals = new ArrayList<Vector3>();
		parseObjects = new ArrayList<ParseObjectData>();
		textureAtlas = new TextureAtlas();
		firstObject = true;
		materialMap = new HashMap<String, Material>();
	}
	
	public AParser(String resourceID, Boolean generateMipMap) {
		this();
		this.resourceID = resourceID;
		if (resourceID.indexOf(":") > -1) this.packageID = resourceID.split(":")[0];
		this.generateMipMap = generateMipMap;
	}
	
	protected void cleanup() {
		parseObjects.clear();
		textureAtlas.cleanup();
		vertices.clear();
		texCoords.clear();
		normals.clear();
	}
	
//	/**
//	 * Override this in the concrete parser if applicable
//	 */
//	public AnimationObject3d getParsedAnimationObject() {
//		return null;
//	}

	protected String readString(InputStream stream) throws IOException {
		String result = new String();
		byte inByte;
		while ((inByte = (byte) stream.read()) != 0)
			result += (char) inByte;
		return result;
	}

	protected int readInt(InputStream stream) throws IOException {
		return stream.read() | (stream.read() << 8) | (stream.read() << 16) | (stream.read() << 24);
	}

	protected int readShort(InputStream stream) throws IOException {
		return (stream.read() | (stream.read() << 8));
	}

	protected float readFloat(InputStream stream) throws IOException {
		return Float.intBitsToFloat(readInt(stream));
	}

	/**
	 * Contains texture information. UV offsets and scaling is stored here.
	 * This is used with texture atlases.
	 * 
	 * @author dennis.ippel
	 *
	 */
	protected class BitmapAsset	{
		/**
		 * The texture bitmap
		 */
		public Bitmap bitmap;
		/**
		 * The texture identifier
		 */
		public String key;
		/**
		 * Resource ID
		 */
		public String resourceID;
		/**
		 * U-coordinate offset
		 */
		public float uOffset;
		/**
		 * V-coordinate offset
		 */
		public float vOffset;
		/**
		 * U-coordinate scaling value
		 */
		public float uScale;
		/**
		 * V-coordinate scaling value
		 */
		public float vScale;
		public boolean useForAtlasDimensions;
		
		/**
		 * Creates a new BitmapAsset object
		 * @param key
		 */
		public BitmapAsset(String key, String resourceID){
			this.key = key;
			this.resourceID = resourceID;
			useForAtlasDimensions = false;
		}
	}
	
	/**
	 * When a model contains per-face textures a texture atlas is created. This
	 * combines multiple textures into one and re-calculates the UV coordinates.
	 * 
	 * @author dennis.ippel
	 * 
	 */
	protected class TextureAtlas {
		/**
		 * The texture bitmaps that should be combined into one.
		 */
		private ArrayList<BitmapAsset> bitmaps;
		/**
		 * The texture atlas bitmap
		 */
		private Bitmap atlas;

		/**
		 * Creates a new texture atlas instance.
		 */
		public TextureAtlas() {
			bitmaps = new ArrayList<BitmapAsset>();
		}
		private String atlasId;

		/**
		 * Adds a bitmap to the atlas
		 * 
		 */
		public void addBitmapAsset(BitmapAsset ba) {
			BitmapAsset existingBA = getBitmapAssetByResourceID(ba.resourceID);

			if(existingBA == null) {
				int bmResourceID = Registry.getContext().getResources().getIdentifier(ba.resourceID, null, null);
				if(bmResourceID == 0) {
					Logger.i(this, "Texture not found: " + ba.resourceID);
					return;
				}
				Logger.i(this, "Adding texture " + ba.resourceID);
				Bitmap b = Utils.makeBitmapFromResourceId(bmResourceID);
				ba.useForAtlasDimensions = true;
				ba.bitmap = b;
			}
			else {
				ba.bitmap = existingBA.bitmap;
			}

			bitmaps.add(ba);
		}
		
		public BitmapAsset getBitmapAssetByResourceID(String resourceID) {
			int numBitmaps = bitmaps.size();
			
			for(int i=0; i<numBitmaps; i++)	{
				if(bitmaps.get(i).resourceID.equals(resourceID))
					return bitmaps.get(i);
			}
			
			return null;
		}

		/**
		 * Generates a new texture atlas
		 */
		public void generate() {
			Collections.sort(bitmaps, new BitmapHeightComparer());

			if(bitmaps.size() == 0) return;
			
			BitmapAsset largestBitmap = bitmaps.get(0);
			int totalWidth = 0;
			int numBitmaps = bitmaps.size();
			int uOffset = 0;
			int vOffset = 0;

			for (int i = 0; i < numBitmaps; i++) {
				if(bitmaps.get(i).useForAtlasDimensions)
					totalWidth += bitmaps.get(i).bitmap.getWidth();
			}

			atlas = Bitmap.createBitmap(totalWidth, largestBitmap.bitmap.getHeight(), Config.ARGB_8888);

			for (int i = 0; i < numBitmaps; i++) {
				BitmapAsset ba = bitmaps.get(i);
				BitmapAsset existingBA = getBitmapAssetByResourceID(ba.resourceID);				
				
				if(ba.useForAtlasDimensions) {
					Bitmap b = ba.bitmap;
					int w = b.getWidth();
					int h = b.getHeight();
					int[] pixels = new int[w * h];
					
					b.getPixels(pixels, 0, w, 0, 0, w, h);
					atlas.setPixels(pixels, 0, w, uOffset, vOffset, w, h);
					
					ba.uOffset = (float) uOffset / totalWidth;
					ba.vOffset = 0;
					ba.uScale = (float) w / (float) totalWidth;
					ba.vScale = (float) h / (float) largestBitmap.bitmap.getHeight();
					
					uOffset += w;
					b.recycle();
				}
				else {
					ba.uOffset = existingBA.uOffset;
					ba.vOffset = existingBA.vOffset;
					ba.uScale = existingBA.uScale;
					ba.vScale = existingBA.vScale;
				}
			}

//            saveAtlas("/data/screenshot.png",Bitmap.CompressFormat.PNG, 100);
			setId(Registry.getTextureLibrary().getNewAtlasId());
		}

        private void saveAtlas(String filepath, Bitmap.CompressFormat format, int quality) {
            FileOutputStream fos;
			try {
				fos = new FileOutputStream(filepath);
				atlas.compress(format, quality, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				Logger.e(this,""+e.getMessage());
			} catch (IOException e) {
				Logger.e(this, ""+e.getMessage());
			}
        }

		/**
		 * Returns the generated texture atlas bitmap
		 * 
		 * @return
		 */
		public Bitmap getBitmap() {
			return atlas;
		}

		/**
		 * Indicates whether bitmaps have been added to the atlas.
		 * 
		 * @return
		 */
		public boolean hasBitmaps() {
			return bitmaps.size() > 0;
		}

		/**
		 * Compares the height of two BitmapAsset objects.
		 * 
		 * @author dennis.ippel
		 * 
		 */
		private class BitmapHeightComparer implements Comparator<BitmapAsset> {
			public int compare(BitmapAsset b1, BitmapAsset b2) {
				int height1 = b1.bitmap.getHeight();
				int height2 = b2.bitmap.getHeight();

				if (height1 < height2) {
					return 1;
				} else if (height1 == height2) {
					return 0;
				} else {
					return -1;
				}
			}
		}
		
		/**
		 * Returns a bitmap asset with a specified name.
		 * 
		 * @param materialKey
		 * @return
		 */
		public BitmapAsset getBitmapAssetByName(String materialKey) {
			int numBitmaps = bitmaps.size();

			for (int i = 0; i < numBitmaps; i++) {
				if (bitmaps.get(i).key.equals(materialKey))
					return bitmaps.get(i);
			}

			return null;
		}
		
		public void cleanup()
		{
			int numBitmaps = bitmaps.size();

			for (int i = 0; i < numBitmaps; i++) {
				bitmaps.get(i).bitmap.recycle();
			}
			
			if(atlas != null) atlas.recycle();
			bitmaps.clear();
			vertices.clear();
			texCoords.clear();
			normals.clear();
		}

		public void setId(String newAtlasId) {
			atlasId = newAtlasId;			
		}

		public String getId() {
			return atlasId;
		}
	}

	protected class Material {
		public String name;
		public String diffuseTextureMap;
		public Color4 diffuseColor;

		public Material(String name) {
			this.name = name;
		}
	}
}
