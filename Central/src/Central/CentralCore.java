package Central;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

public class CentralCore extends JavaPlugin {
	public static CentralCore Instance;
	public CentralCore(){
		Instance = this;
	}
	@Override
	public void onEnable(){
		
	}
	@Override
	public void onDisable(){
		
	}
	public static CentralCore getInstance(){
		return Instance;
	}
	public File getPluginJarFile(){
		return this.getFile();
	}
	
	public static String getLang(){
		return "en_us"; //cfg.getString("Lang");
	}
}
