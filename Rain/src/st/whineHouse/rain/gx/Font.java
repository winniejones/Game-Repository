package st.whineHouse.rain.gx;

/**
 * Font-klass
 * Egen skapad fontklass med egna sprite för varje bokstav som ritas upp på spelfönstret.
 * @author Winston Jones
 *
 */
public class Font {
	   
	   private static SpriteSheet font = new SpriteSheet("/fonts/arial.png", 16);
	   private static Sprite[] characters = Sprite.split(font);
	   
	   private static String charIndex = 	"ABCDEFGHIJKLM" + //
	   										"NOPQRSTUVWXYZ" + //
	   										"abcdefghijklm" + //
	   										"nopqrstuvwxyz" + //
	   										"0123456789., " + //
	   										"'\":;-!?�%)(/+";
	   
	   public Font() {
	      
	   }
	   
	   public void render(int x, int y,String text, Screen screen) {
		   render(x,y,0,0, text,screen);
	   }
	   
	   public void render(int x, int y, int color, String text, Screen screen) {
		   render(x,y,0,color, text,screen);
	   }
	   
	   public void render(int x, int y, int spacing, int color, String text, Screen screen) {
		   int xOffset = 0;
		   int line =0;
		   for(int i = 0; i < text.length(); i++){
			   int yOffset = 0;
			   xOffset += 16+spacing;														//fixar till att char �kar med r�tt spacing mellan bokst�verna.
			   char currentChar = text.charAt(i); 											//kollar vilken plats char �r p� i inmatnings str�ngen.
			   if(currentChar == 'g' || currentChar == 'j' || currentChar == 'y' || currentChar == ',' || currentChar == ';'
				   || currentChar == 'p'|| currentChar == 'q') yOffset = 3;
			   if(currentChar == '\n'){ 													//tar \n och s�tter offset till noll s� den b�rjar om vart i xleg charen ska st� och �kar line f�r varje \n.
				   xOffset = 0;
				   line++;
			   }
			   int index = charIndex.indexOf(currentChar);									//kollar plats p� char och l�gger in i index f�r att sedan anv�nda n�r man renderar bokstaven i spelet.
			   if(index == -1) continue;													//f�r att krachsa n�r bokstav inte finns s� ignorerar den bara.
			   screen.renderTextCharacter(x + xOffset, y + line*20 + yOffset, characters[index], color , false);			   
		   }
	   }

}
