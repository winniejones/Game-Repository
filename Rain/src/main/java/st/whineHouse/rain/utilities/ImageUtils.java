package whineHouse.rain.utilities;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferByte;

import static st.whineHouse.rain.utilities.MathUtils.*;

public class ImageUtils {
	
	private ImageUtils(){
		
	}
	/**
	 * Returns a NEW image depending on amount its
	 * @param original
	 * @param amount
	 * @return
	 */
	//changing the image in button
	public static BufferedImage changeBrightness(BufferedImage original, int amount){
		BufferedImage result = new BufferedImage(original.getWidth(),original.getHeight(), BufferedImage.TYPE_INT_ARGB);
		byte[] pixels = ((DataBufferByte)original.getRaster().getDataBuffer()).getData();
		int[] resultPixels = ((DataBufferInt)result.getRaster().getDataBuffer()).getData();
		
		int offset=0;
		for(int imY=0; imY< original.getHeight();imY++){
			for(int imX=0; imX< original.getWidth();imX++){
				
				int a = Byte.toUnsignedInt(pixels[offset++]);
				int b = Byte.toUnsignedInt(pixels[offset++]);
				int g = Byte.toUnsignedInt(pixels[offset++]);
				int r = Byte.toUnsignedInt(pixels[offset++]);
				
				
				
				r = clamp(r+amount,0,255);
				g = clamp(g+amount,0,255);
				b = clamp(b+amount,0,255);
				
				resultPixels[imX + imY * result.getWidth()] = a << 24 |r << 16 | g << 8 | b;
			}
		}
		return result;
	}
}
