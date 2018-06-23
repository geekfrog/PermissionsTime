package gg.frog.mc.base.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtil {

	public interface FindFilesDo {
		boolean isProcess(String fileName);

		void process(String fileName, InputStream is);
	}

	public static void findFilesFromJar(FindFilesDo ffd, Class<?> jarClazz) {
		JarFile jarFile = null;
		try {
			String jarFilePath = jarClazz.getProtectionDomain().getCodeSource().getLocation().getFile();
			jarFilePath = URLDecoder.decode(jarFilePath, Charset.defaultCharset().name());
			jarFile = new JarFile(jarFilePath);
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry e = entries.nextElement();
				if (ffd.isProcess(e.getName())) {
					InputStream is = jarFile.getInputStream(e);
					ffd.process(e.getName(), is);
					try {
						is.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jarFile != null) {
				try {
					jarFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void writeOnFile(String fileName, String content) {
		FileWriter fw;
		try {
			File f = new File(fileName);
			fw = new FileWriter(f, true);
			fw.write(content + "\r\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
