package streammz.jumpwarp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.bukkit.Location;
import org.bukkit.World;

public class DataLoader {
	private static File file = new File("plugins/jumpwarp/data.txt");
	private static File folder = new File("plugins/jumpwarp/");
	
	public static void load(Core plugin) {
		if (!checkFile(false)) return;
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String s;
			while ((s = in.readLine()) != null) {
				try {
					String[] split = s.split(":");
					String name = split[0];
					double x = Double.parseDouble(split[1]);
					double y = Double.parseDouble(split[2]);
					double z = Double.parseDouble(split[3]);
					float a = Float.parseFloat(split[4]);
					float b = Float.parseFloat(split[5]);
					World w = plugin.getServer().getWorld(split[6]);
					if (w == null) continue;
					
					plugin.warplocs.add(new WarpLocation(name, new Location(w, x, y, z, a, b)));
				} catch (Exception e) { continue; }
			}
			in.close();
		} catch (Exception e) { return; }
	}
	public static void save(Core plugin) {
		if (!checkFile(true)) return;
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			for (int i=0; i<plugin.warplocs.size(); i++) {
				WarpLocation l = plugin.warplocs.get(i);
				out.write(l.toString());
				out.newLine();
			}
			out.close();
		} catch (Exception e) { return; }
	}
	
	
	private static boolean checkFile(boolean create) {
		if (!file.exists()) {
			if (create) {
				try {
					folder.mkdirs();
					file.createNewFile();
				} catch (Exception e) { return false; }
			} else return false;
		}
		return true;
	}
}
