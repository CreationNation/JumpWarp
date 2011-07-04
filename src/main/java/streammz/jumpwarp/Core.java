package streammz.jumpwarp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Core extends JavaPlugin {
	public static PermissionHandler permissionHandler;
	public List<WarpLocation> warplocs;
	
	public void onDisable() {
		DataLoader.save(this);
	}

	public void onEnable() {
		setupPermissions();
		warplocs = new ArrayList<WarpLocation>();
		
		final Core plug = this;
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
			public void run() {
				DataLoader.load(plug);
			}
		});
		//DataLoader.load(plug);
		
		PluginDescriptionFile pdfFile = getDescription();
	    System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String cmd = command.getName().toLowerCase();
		Player p = (Player)sender;
		if (cmd.equals("jump")) {
			if (args.length < 1) { return false; }
			if (args[0].equalsIgnoreCase("create") && args.length == 2 && hasPermission(p, "jumpwarp.create")) {
				WarpLocation l = this.getWarpLocation(args[1]);
				if (l != null) {
					warplocs.remove(l);
					p.sendMessage("Old warp removed.");
				}
				this.warplocs.add(new WarpLocation(args[1],p.getLocation()));
				p.sendMessage("Warp created at your location.");
				
			} else if (args[0].equalsIgnoreCase("remove") && args.length == 2 && hasPermission(p, "jumpwarp.create")) {
				WarpLocation l = this.getWarpLocation(args[1]);
				if (l != null) {
					warplocs.remove(l);
					p.sendMessage(l.name + " removed.");
				} else {
					p.sendMessage("This warp doesn't exist.");
				}
			} else if (args[0].equalsIgnoreCase("list") && args.length == 1 && hasPermission(p, "jumpwarp.create")) {
				String s = "";
				for (int i=0; i<warplocs.size(); i++) {
					s = s + warplocs.get(i).name + " ";
				}
				p.sendMessage(s);
			} else {
				WarpLocation l = this.getWarpLocation(args[0]);
				if (l == null) { p.sendMessage("This jump doesn't exist."); return true; }
				if (!hasPermission(p, "jumpwarp.tp." + l.name) && !hasPermission(p, "jumpwarp.create")) { p.sendMessage("You can't jump to that location"); return true; }
				p.teleport(l.loc);
				p.sendMessage("You jumped to " + l.name);
			}
		}
		return true;
	}
	
	
	private void setupPermissions() {
	    Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
	    if (permissionHandler == null) {
	    	if (permissionsPlugin != null) {
	    		permissionHandler = ((Permissions) permissionsPlugin).getHandler();
         	} else {
	     	}
	     }
	}
	public static boolean hasPermission(Player p, String node) {
		if (p.isOp()) { return true; }
		if (!Bukkit.getServer().getPluginManager().isPluginEnabled("Permissions")) { return false; }
		if (permissionHandler.has(p, node)) { return true; }
		return false;
	}
	public WarpLocation getWarpLocation(String name) {
		for (int i=0; i<warplocs.size(); i++) {
			if (warplocs.get(i).name.equalsIgnoreCase(name)) return warplocs.get(i);
		}
		return null;
	}
	
}