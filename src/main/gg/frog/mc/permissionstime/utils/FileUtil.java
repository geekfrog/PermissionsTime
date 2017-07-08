package gg.frog.mc.permissionstime.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtil {
    
    public interface FindFilesDo {
        boolean isProcess(String fileName);
        void process(String fileName, InputStream is);
    }
    
    public static void findFilesFromJar(FindFilesDo ffd, Class<?> jarClazz){
        JarFile jarFile = null;
        try {
            String jarFilePath = jarClazz.getProtectionDomain().getCodeSource().getLocation().getFile();
            jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry e = entries.nextElement();
                if(ffd.isProcess(e.getName())){
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
}
