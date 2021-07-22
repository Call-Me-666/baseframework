package org.example.baseframework;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.example.common.interfaces.MyInterface;
import org.reflections.Reflections;

public class Application {


    static String path = "D://common-1.0-SNAPSHOT.jar";
    public static void main(String[] args) {
        List<String> paths = loadJar(path);
        loadClass(path);
    }

    /**
     * 根据路径加载Jar包
     * @param jarPath Jar包路径
     */
    public static List<String> loadJar(String jarPath){
        List<String> paths = new ArrayList<String>();
        getJarPath(jarPath,paths);
        return paths;
    }

    /**
     * 根据路径获取此路径下的所有jar包名称，以及其文件夹下的jar包
     * @param basePath
     * @param paths
     */
    public static void getJarPath(String basePath, List<String> paths){
        File directory = new File(basePath);
        // 不存在直接返回
        if(!directory.exists())
            return;
        // 如果路径是文件夹
        if(directory.isDirectory()){
            File[] files = directory.listFiles();
            for (File file:files) {
                // 判断file是否是jar包
                // 如果是jar包直接加加到集合中
                if(file.isFile()&&file.getPath().endsWith(".jar")){
                    paths.add(file.getPath());
                }else{      // 不是jar包就递归
                    getJarPath(file.getPath(),paths);
                }
            }
        }else{
            if(directory.getPath().endsWith(".jar")){
                paths.add(basePath);
            }
        }
    }


    public static void loadClass(String jarPath){
        jarPath = String.format("file://%s",jarPath);
        URL url = null;
        try {
            url = new URL(jarPath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if(url!=null){
            URLClassLoader loader = new URLClassLoader(new URL[]{url},Thread.currentThread().getContextClassLoader());
            Reflections reflections = new Reflections("org.example.common",loader);
            Set<Class<? extends MyInterface>> subTypesOfMyInterface = reflections.getSubTypesOf(MyInterface.class);
            int i = 0;
            for (Class<? extends MyInterface> interf:subTypesOfMyInterface) {
                try {
//                    interf.get
                    MyInterface inter = interf.getConstructor().newInstance();
                    System.out.println(inter.getName());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}