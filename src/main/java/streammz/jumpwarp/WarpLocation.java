package streammz.jumpwarp;

import org.bukkit.Location;

public class WarpLocation {
	public String name;
	public Location loc;
	
	public WarpLocation(String name, Location loc) {
		this.name = name.toLowerCase();
		this.loc = loc;
	}
	
	public String toString() {
		return name + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch() + ":" + loc.getWorld().getName();
	}
}
