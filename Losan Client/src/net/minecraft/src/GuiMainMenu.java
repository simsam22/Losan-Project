package net.minecraft.src;

import java.awt.Desktop;
import java.awt.Menu;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;

import Losan.Gui.GuiButtonTeamSpeak;
import Losan.Gui.GuiDownloadTexturePack;
import Losan.Utils.FontColors;


public class GuiMainMenu extends GuiScreen
{
	private int taille;
	private boolean download = true;
	
	private int anim = -2130706433;
	private boolean color;
	
    /** The RNG used by the Main Menu Screen. */
    private static final Random rand = new Random();

    /** Counts the number of screen updates. */
    private float updateCounter = 0.0F;

    /** The splash message. */
    public String splashText = "missingno";
    private GuiButton field_73973_d;
    
    public int strpos; //ajout
    public String[] announces; //ajout
    public int announceCount; //ajout

    /** Timer used to rotate the panorama, increases every tick. */
    private int panoramaTimer = 0;
    
    private GuiButton multi;
    private GuiButton solo;
    private GuiButton site;
    private GuiButton texture;
    private GuiButton option;
    private GuiButton quit;
    private GuiButton ts;
    private int X = 0;
    private boolean menu = true;
    private boolean slide = false;
    

    /**
     * Texture allocated for the current viewport of the main menu's panorama background.
     */
    private int viewportTexture;
    
    public GuiMainMenu(){
    	this.color = false;
        this.strpos = this.width + 300;
        this.announces = new String[] {"Bon jeu sur Losan.fr !"};
        this.announceCount = 0;
        this.panoramaTimer = 0;
        BufferedReader var1 = null;
        try{
            ArrayList var2 = new ArrayList();
            var1 = new BufferedReader(new InputStreamReader(GuiMainMenu.class.getResourceAsStream("/title/splashes.txt"), Charset.forName("UTF-8")));
            String var3;
            while ((var3 = var1.readLine()) != null){
                var3 = var3.trim();
                if (var3.length() > 0){
                    var2.add(var3);
                }
            }
            do{
                this.splashText = (String)var2.get(rand.nextInt(var2.size()));
            }
            while (this.splashText.hashCode() == 125780783);
        }
        catch (IOException var12){;}
        finally{
            if (var1 != null){
                try{
                    var1.close();
                }
                catch (IOException var11){;}
            }
        }
        this.updateCounter = rand.nextFloat();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen(){
        ++this.panoramaTimer;
        if (this.strpos < 0 - this.mc.fontRenderer.getStringWidth(this.announces[this.announceCount])){
            this.strpos = this.width +10;
            if (this.announceCount >= this.announces.length - 1){
                this.announceCount = -1;
            }
            ++this.announceCount;            
        }      
        this.strpos -= 2;     
        if(this.slide){
        	this.X += 20;
        
        	if(this.X >= 154)
        		this.X = 154;
        }else{
        	this.X -=20;
        	
        	if(this.X <= 0)
        		this.X = 0;
        }        
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame(){
        return false;
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2) {}

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    
    private GuiButton test;
    public void initGui(){
        this.viewportTexture = this.mc.renderEngine.allocateAndSetupTexture(new BufferedImage(256, 256, 2));
        Calendar var1 = Calendar.getInstance();
        var1.setTime(new Date());

        if (var1.get(2) + 1 == 11 && var1.get(5) == 9)
        {
            this.splashText = "Bon Anniversaire, ez!";
        }
        else if (var1.get(2) + 1 == 6 && var1.get(5) == 1)
        {
            this.splashText = "Bon Anniversaire, Notch!";
        }
        else if (var1.get(2) + 1 == 12 && var1.get(5) == 24)
        {
            this.splashText = "Joyeux No�l!";
        }
        else if (var1.get(2) + 1 == 1 && var1.get(5) == 1)
        {
            this.splashText = "Bonne Ann�e!";
        }
        StringTranslate var2 = StringTranslate.getInstance();
        int var4 = this.height / 4 + 48;
    	this.taille = var4;       
        this.updateAnnounces();
        
        drawBackground();
    }
    
    private void updateAnnounces(){
        String var3 = "";
        try{
            URL var4 = new URL("http://losan.fr/download-launcheur/mods/announce.txt");
            String var2;

            for (BufferedReader var5 = new BufferedReader(new InputStreamReader(var4.openStream())); (var2 = var5.readLine()) != null; var3 = var3 + var2 + "\n"){
                ;
            }
        }
        catch (MalformedURLException var8){
            var8.printStackTrace();
        }
        catch (IOException var9){
            var9.printStackTrace();
        }

        this.doSplashes(var3);
    }

    private void doSplashes(String var1){
        var1 = var1.replaceAll("&0", FontColors.BLACK.toString());
        var1 = var1.replaceAll("&1", FontColors.DARK_BLUE.toString());
        var1 = var1.replaceAll("&2", FontColors.DARK_GREEN.toString());
        var1 = var1.replaceAll("&3", FontColors.DARK_AQUA.toString());
        var1 = var1.replaceAll("&4", FontColors.DARK_RED.toString());
        var1 = var1.replaceAll("&5", FontColors.DARK_PURPLE.toString());
        var1 = var1.replaceAll("&6", FontColors.GOLD.toString());
        var1 = var1.replaceAll("&7", FontColors.GRAY.toString());
        var1 = var1.replaceAll("&8", FontColors.DARK_GRAY.toString());
        var1 = var1.replaceAll("&9", FontColors.BLUE.toString());
        var1 = var1.replaceAll("&a", FontColors.GREEN.toString());
        var1 = var1.replaceAll("&b", FontColors.AQUA.toString());
        var1 = var1.replaceAll("&c", FontColors.RED.toString());
        var1 = var1.replaceAll("&d", FontColors.LIGHT_PURPLE.toString());
        var1 = var1.replaceAll("&e", FontColors.YELLOW.toString());
        var1 = var1.replaceAll("&f", FontColors.WHITE.toString());
        this.announces = var1.split("\n");
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton){
        if (par1GuiButton.id == 0){
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (par1GuiButton.id == 6){
            Desktop desktop = null;
            java.net.URI url;
            try{
                url = new java.net.URI("http://losan.fr");

                if (Desktop.isDesktopSupported()){
                    desktop = Desktop.getDesktop();
                    desktop.browse(url);
                }
            }
            catch (Exception ex){
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (par1GuiButton.id == 1){
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if (par1GuiButton.id == 2){
        	ITexturePack IT = null;
        	String var1 = "";
        	try{
        		URL url = new URL("http://dermen-design.fr/downloads/LosanPack_version.txt");

        		String var2;
        		for (BufferedReader var5 = new BufferedReader(new InputStreamReader(url.openStream())); (var2 = var5.readLine()) != null; var1 = var1 + var2){
        			;
        		}

        		this.mc.texturePackList.updateAvaliableTexturePacks();             
        		int size = this.mc.texturePackList.availableTexturePacks().size();              
        		for(int T = 0;T <size; T++){
        			IT = (ITexturePack) (this.mc.texturePackList.availableTexturePacks().get(T));
        			if(IT.getTexturePackFileName().equals("LosanPack.zip")){
        				break;
        			}
        			IT = null;
        		}       



        		String comp ="";
        		if(IT != null){
        			comp = IT.getSecondDescriptionLine();
        			comp = comp.replace(".", "");
        		}

        		var1 = var1.replace(".", "");

        		if(var1.equals("") || comp.equals(""))
        			this.mc.displayGuiScreen(new GuiConnecting(mc, "88.190.210.60", 25565)); 

        		if(IT == null)
        			this.mc.displayGuiScreen(new GuiDownloadTexturePack(this.strpos, this.announces, this.announceCount, this.splashText));
        		else if(IT.getSecondDescriptionLine().equals("") || Integer.parseInt(var1) > Integer.parseInt(comp))
        			this.mc.displayGuiScreen(new GuiDownloadTexturePack(this.strpos, this.announces, this.announceCount, this.splashText));
        		else
        			this.mc.displayGuiScreen(new GuiConnecting(mc, "88.190.210.60", 25565));  
        	}catch(Exception ex){
        		this.mc.displayGuiScreen(new GuiConnecting(mc, "88.190.210.60", 25565));  
        	}	

        }
        if (par1GuiButton.id == 3){
            this.mc.displayGuiScreen(new GuiTexturePacks(this));
        }
        if (par1GuiButton.id == 4){
            this.mc.shutdown();
        }
        if(par1GuiButton.id == 7){   
        	String ipts3 = "losan.fr"; 
        	int portts3 = 9987; 
        	URI uri = URI.create("ts3server://"+ ipts3 +"?port="+ portts3 +"&nickname="+ this.mc.session.username); // On ouvre le teamspeak //ajout
        	try{
        		Desktop.getDesktop().browse(uri);
        	} 
        	catch(IOException e){
        		e.printStackTrace();
        	}
        }
    }

    public void confirmClicked(boolean par1, int par2){
        if (par1 && par2 == 12){
            ISaveFormat var3 = this.mc.getSaveLoader();
            var3.flushCache();
            var3.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        }
    }

    
    private void drawBackground(){
    	
    	this.fontRenderer.setUnicodeFlag(true);
        Tessellator var4 = Tessellator.instance;
        short var5 = 274;
        int var6 = this.width / 2 - var5 / 2;
        byte var7 = 30;
        
        //light edition
        
//    	 GL11.glPushMatrix();
//         GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/title/parchemin.png"));
//     	GL11.glScalef((float)(1280/this.width)+0.5F, (float)(768/this.height)+0.2F, 1.0F);
//         this.drawTexturedModalRect(0, 0, 0, 0, 640, 384);
//         GL11.glPopMatrix();
//         System.out.println(this.width+"   "+this.height);
       this.drawGradientRect(0, 0, this.width, this.height, this.anim, 16777215);
       this.drawGradientRect(0, 10, this.width, this.height-45, 0xfd8d8d8, Integer.MIN_VALUE);
       this.drawRect(0, 0, this.width, this.height, 0xff);
         
         drawHorizontalLine(0, this.width, this.height-47, 0xff000000);
         drawHorizontalLine(0, this.width, this.height-46, 0xff737373);
         
         this.drawRect(0, this.height-45, this.width, this.height, 0xff333333);
         
         GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/title/mclogo.png"));
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

         if ((double)this.updateCounter < 1.0E-4D){
             this.drawTexturedModalRect(var6 + 0, var7 + 0, 0, 0, 99, 44);
             this.drawTexturedModalRect(var6 + 99, var7 + 0, 129, 0, 27, 44);
             this.drawTexturedModalRect(var6 + 99 + 26, var7 + 0, 126, 0, 3, 44);
             this.drawTexturedModalRect(var6 + 99 + 26 + 3, var7 + 0, 99, 0, 26, 44);
             this.drawTexturedModalRect(var6 + 155, var7 + 0, 0, 45, 155, 44);
         }else{
         	GL11.glPushMatrix();
         	GL11.glScalef(0.4F, 0.4F, 0.4F);
             this.drawTexturedModalRect((int)((this.width-122)/0.4F), (int)((this.height-35)/0.4F), 0, 0, 155, 44);
             this.drawTexturedModalRect((int)((this.width-60)/0.4F) , (int)((this.height-35)/0.4F), 0, 45, 155, 44);
             GL11.glPopMatrix();
            
             GL11.glPushMatrix();
             GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/title/blason.png"));
         	GL11.glScalef(0.6F, 0.6F, 0.6F);
             this.drawTexturedModalRect((int)(((this.width/100)*10)/0.6F), (int)(((this.height/100)*15)/0.6F), 0, 0, 256, 256);
             GL11.glPopMatrix();
         }
    }
    
    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3){
    	drawBackground();
    	this.fontRenderer.setUnicodeFlag(true);
        Tessellator var4 = Tessellator.instance;
        short var5 = 274;
        int var6 = this.width / 2 - var5 / 2;
        byte var7 = 30;
        
       

        var4.setColorOpaque_I(16777215);
        GL11.glPushMatrix();
        if(this.width <= 427)
        	GL11.glTranslatef((float)((this.width/100)*28.5F), (float)(this.height-85), 0.0F);
        else
        	GL11.glTranslatef((float)((this.width/100)*21), (float)(this.height-100), 0.0F);
        
        GL11.glRotatef(-0.0F, 0.0F, 0.0F, 1.0F);
        float var8 = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
        var8 = var8 * 100.0F / (float)(this.fontRenderer.getStringWidth(this.splashText)+32);
        GL11.glScalef(var8, var8, var8);
        this.drawCenteredString(this.fontRenderer, this.splashText, 0, 0, 16776960);
        GL11.glPopMatrix();

        this.drawString(this.fontRenderer, "Modd� par:", 10, this.height - 32, 0x716dd6); //ajout
        this.drawString(this.fontRenderer, "\2474Dermenslof \2476&& \2474yoyo1902", 10, this.height - 22, 0x8a0f0f); //ajout
        this.drawString(this.fontRenderer, "\2476Losan.fr \24741.4.5", this.width - this.fontRenderer.getStringWidth("\2476Losan.fr \24741.4.5") - 40, this.height - 15, 0x716dd6); //ajout

        drawRect(0, 0, this.width, this.fontRenderer.FONT_HEIGHT + 4, Integer.MIN_VALUE); //ajout
        drawRect(0, 0, this.width, this.fontRenderer.FONT_HEIGHT + 4, Integer.MIN_VALUE); //ajout

        this.drawString(this.fontRenderer, this.announces[this.announceCount], this.strpos, 2, 16777215); //ajout
        
        onBoutonOver((float)par1, (float)par2);
        
        GL11.glPushMatrix();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/title/menu.png"));
        this.drawRect(this.width+this.X -152, 18, this.width, 191, 0xff838383);
        
        this.drawRect(this.width+this.X -154, 18, this.width, 21, 0xffa6a6a6);
        
        this.drawVerticalLine(this.width+this.X -153, 45, 190, 0xff000000);
        
        this.drawHorizontalLine(this.width+this.X -166, this.width+this.X-154, 46, 0xcc000000);
        this.drawHorizontalLine(this.width+this.X -154, this.width+this.X, 18, 0xff000000);
        this.drawHorizontalLine(this.width+this.X -153, this.width+this.X, 190, 0xff000000);
        this.drawHorizontalLine(this.width+this.X -152, this.width+this.X, 191, 0x99000000);
        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(this.width+this.X -170, 18, 0, 0, 18, 28);
        if(this.slide)
        	drawString(this.fontRenderer, "<",this.width+this.X -162, 27, 0xd8d8d8);
        else
        	this.drawString(this.fontRenderer, ">", this.width+this.X -162, 27, 0xd8d8d8);
        
        GL11.glPopMatrix();
        
        this.controlList.clear();
        
        int taille = this.height / 4 + 48;

    	this.taille = taille;
        this.option = new GuiButton(0, this.width -151 + this.X, 110, 150, 20, "Options");
        this.controlList.add(this.option);
        
        this.solo = new GuiButton(1, this.width -151 + this.X, 44, 150, 20, "Solo");
        this.controlList.add(this.solo);
        
        this.multi = new GuiButton(2, this.width -151 + this.X, 22, 150, 20, "Jouer sur \2476Losan.fr");
        this.controlList.add(this.multi);

        this.texture = new GuiButton(3, this.width -151 + this.X, 88, 150, 20, "Packs de textures");
        this.controlList.add(this.texture);
        
        this.quit = new GuiButton(4, this.width -151 + this.X, 132, 150, 20, "Quitter le jeu");
        this.controlList.add(this.quit);
        
        this.site = new GuiButton(6, this.width -151 + this.X, 66, 150, 20, "Le site");
        this.controlList.add(this.site);

        this.ts = new GuiButtonTeamSpeak(7, this.width - 85 + this.X, 160);
        this.controlList.add(this.ts);
        
        onBoutonOver((float)par1, (float)par2);
        
        super.drawScreen(par1, par2, par3);  
    }
    
	private void onBoutonOver(float x, float y){
		if(x >= this.width / 2 - 100 && x <= this.width/2+100 && y >= 108 && y <= 128){
			this.color = true;
			this.menu = false;
		}else if(x >= this.width+this.X -170 && x <= this.width+this.X -170+15 && y >= 18 && y <= 48){
			this.color = false;
			this.menu = true;
		}else{
			this.color = false;
			this.menu = false;
		}
	}
	
	public void handleMouseInput(){
        int var1;
        int var2;

        if (Mouse.getEventButtonState() && Mouse.getEventButton() == 0){
            var1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
            var2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
            this.mousePressed(var1, var2, 0);
        }
    }
	
	public void mousePressed(int i, int j, int k){
		if(this.menu)
			this.slide = !this.slide;
		
		super.mouseClicked(i, j, k);
    }		
}
