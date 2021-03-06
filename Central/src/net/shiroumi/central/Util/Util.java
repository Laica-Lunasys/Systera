package net.shiroumi.central.Util;

import static net.shiroumi.central.Util.i18n.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import net.shiroumi.central.CentralCore;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/** @author squarep */
public class Util {
	public static String maskedStringReplace(String par1Src,
	        String[][] par2Masks) {
		String var1 = par1Src;
		if (par2Masks != null) {
			for (int i = 0; i < par2Masks.length; ++i) {
				var1 = var1.replace(par2Masks[i][0], par2Masks[i][1]);
			}
		}
		var1 = var1.replaceAll("&([0-9a-fk-or])", "\u00A7$1");
		return var1;
	}

	public static Player findPlayer(String par1Player) {
		return findPlayer(par1Player, null);
	}

	public static Player findPlayer(String par1Player, CommandSender par2Sender) {
		Player p = Bukkit.getServer().getPlayer(par1Player);
		if (p == null) {
			if (par2Sender != null) {
				Message(par2Sender, _("playernotfound"), new String[][] { {
				        "%player", par1Player } });
			}
			return null;
		}
		return p;
	}

	public static boolean hasPerm(String par1Perm, CommandSender par2Player) {
		return hasPerm(par1Perm, par2Player, true);
	}

	public static boolean hasPerm(String par1Perm, CommandSender par2Player,
	        boolean isShowMessage) {
		if (!(par2Player.hasPermission(par1Perm)) && !(par2Player.isOp())) {
			Message(par2Player, _("msgPrefix") + _("errorperm"),
			        new String[][] { { "%perm", par1Perm } });
			return false;
		}
		return true;
	}

	public static String getIp(Player p) {
		return p.getAddress().getAddress().getHostAddress();
	}

	public static void copyFileFromJar(File targetFile, String sourceFilePath,
	        boolean isBinary) {
		if (!targetFile.getParentFile().exists()) targetFile.getParentFile().mkdirs();
		JarFile jar = null;
		ZipEntry entry = null;
		InputStream is = null;
		BufferedReader br = null;
		OutputStream os = null;
		PrintWriter pw = null;
		try {
			jar = new JarFile(CentralCore.getInstance().getPluginJarFile());
			entry = jar.getEntry(sourceFilePath);
			is = jar.getInputStream(entry);
			if (isBinary) {
				os = new FileOutputStream(targetFile);
				byte[] buf = new byte[(int) entry.getSize()];
				is.read(buf);
				os.write(buf);
			} else {
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				pw = new PrintWriter(new BufferedWriter(new FileWriter(
				        targetFile)));
				String buf;
				while ((buf = br.readLine()) != null) {
					pw.println(buf);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try { if (jar!= null)jar.close(); } catch (IOException e) { }
			try { if (is != null) is.close(); } catch (IOException e) { }
			try { if (br != null) br.close(); } catch (IOException e) { }
			try { if (os != null) os.close(); } catch (IOException e) { }
			      if (pw != null) pw.close();
		}
	}

	public static void Message(CommandSender par1Sender, String par2Messages,
	        String[][] par3Maskes) {
		par1Sender.sendMessage(maskedStringReplace(_("msgPrefix")
		        + par2Messages, par3Maskes));
	}

	public static void broadcastMessage(String par1Messages,
	        String[][] par2Maskes) {
		Bukkit.getServer().broadcastMessage(
		        maskedStringReplace(_("msgPrefix") + par1Messages, par2Maskes));
	}

	public static String MD5Sum(byte[] par1Data) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			StringBuilder sb = new StringBuilder();
			for (byte t : md5.digest(par1Data)) {
				sb.append(Integer.toHexString(t & 0xFF));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String MD5Sum(String par1Data) {
		return MD5Sum(par1Data.getBytes());
	}

	public static String MD5Sum(FileInputStream par1Stream) {
		FileChannel ic = null;
		ByteBuffer bb = null;
		try {
			ic = par1Stream.getChannel();
			bb = ByteBuffer.allocate((int) ic.size());
			ic.read(bb);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (ic != null) ic.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return MD5Sum(bb.array());
	}

	public static void callEvent(Event par1Event) {
		Bukkit.getPluginManager().callEvent(par1Event);
	}
}
