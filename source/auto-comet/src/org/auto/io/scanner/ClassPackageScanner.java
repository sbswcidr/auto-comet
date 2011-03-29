package org.auto.io.scanner;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.auto.io.ResourceUtils;
import org.auto.io.handler.ClassHandler;

/**
 * 包扫描器
 *
 * @author huxh
 * */
public class ClassPackageScanner implements ClassScanner {

    private String basePackage;
    private List<ClassHandler> classProcessors = new LinkedList<ClassHandler>();

    /**
     * @param basePackage
     *            包根
     * */
    public ClassPackageScanner(String basePackage) {
        this.basePackage = basePackage;
    }

    public void scannerPackage() {
        String packageSearchPath = convertClassNameToResourcePath(basePackage);
        scanClassPath(packageSearchPath);
    }

    public void scan() {
        this.scannerPackage();
    }

    /**
     * 将类名转化为资源路径
     * */
    private String convertClassNameToResourcePath(String className) {
        return className.replace('.', '/');
    }

    private Enumeration<URL> gerUrlResources(String location) {
        Enumeration<URL> resourceUrls = null;
        try {
            resourceUrls = getClassLoader().getResources(location);
        } catch (IOException e1) {
            throw new RuntimeException("读取资源 [" + location + "] 时出错!", e1);
        }
        return resourceUrls;
    }

    /**
     * 扫描ClassPath
     * */
    public void scanClassPath(String location) {

        Enumeration<URL> urlEnumeration = gerUrlResources(location);
        while (urlEnumeration.hasMoreElements()) {
            URL url = (URL) urlEnumeration.nextElement();
            if (isJarUrl(url)) {
                try {
                    scanJarUrl(url);
                } catch (IOException e) {
                    throw new RuntimeException("读取资源 [" + url.getPath()
                            + "] 时出错!", e);
                }
            } else {
                scanFileUrl(url);
            }
        }

    }

    protected void scanJarUrl(URL url) throws IOException {
        URLConnection con = url.openConnection();
        JarFile jarFile = null;
        String rootEntryPath = "";
        if (con instanceof JarURLConnection) {
            JarURLConnection jarCon = (JarURLConnection) con;
            jarCon.setUseCaches(false);
            jarFile = jarCon.getJarFile();
            JarEntry jarEntry = jarCon.getJarEntry();
            if (jarEntry != null)
                rootEntryPath = jarEntry.getName();
            scanJarFile(jarFile, rootEntryPath);
        } else {
            try {
                String urlFilePath = url.getPath();
                int separatorIndex = urlFilePath
                        .indexOf(ResourceUtils.JAR_URL_SEPARATOR);
                if (separatorIndex != -1) {
                    String jarFileUrl = urlFilePath
                            .substring(0, separatorIndex);
                    jarFile = getJarFile(jarFileUrl);
                    rootEntryPath = urlFilePath.substring(separatorIndex
                            + ResourceUtils.JAR_URL_SEPARATOR.length());
                    jarFile = getJarFile(jarFileUrl);
                } else {
                    jarFile = new JarFile(urlFilePath);
                }
                scanJarFile(jarFile, rootEntryPath);
            } finally {
                if (null != jarFile)
                    jarFile.close();
            }
        }

    }

    private void scanJarFile(JarFile jarFile, String rootEntryPath) {
        ClassJarScanner classJarScanner = new ClassJarScanner(jarFile);
        String pattern = rootEntryPath + "/" + DEFAULT_CLASS_RESOURCE_PATTERN;
        classJarScanner.setPattern(pattern);
        classJarScanner.setProcessors(this.getProcessors());
        classJarScanner.scan();
    }

    protected void scanFileUrl(URL url) {
        File file = null;
        try {
            file = new File(url.toURI());
            ClassFileScanner classFileScanner = new ClassFileScanner(file);
            classFileScanner.setProcessors(this.getProcessors());
            classFileScanner.scan();
        } catch (URISyntaxException e) {
            throw new RuntimeException("读取文件[" + url.getFile() + "] 时出错!", e);
        }
    }

    protected JarFile getJarFile(String jarFileUrl) throws IOException {
        if (jarFileUrl.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
            try {
                String uriPath = jarFileUrl.replace(" ", "%20");
                URI uri = new URI(uriPath);
                return new JarFile(uri.getSchemeSpecificPart());
            } catch (URISyntaxException ex) {
                return new JarFile(jarFileUrl
                        .substring(ResourceUtils.FILE_URL_PREFIX.length()));
            }
        } else {
            return new JarFile(jarFileUrl);
        }
    }

    protected boolean isJarUrl(URL url) {
        return ResourceUtils.isJarURL(url);
    }

    private ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public List<ClassHandler> getProcessors() {
        return classProcessors;
    }

    public void setProcessors(List<ClassHandler> classProcessors) {
        this.classProcessors = classProcessors;
    }

    public void addProcessor(ClassHandler classProcessor) {
        this.classProcessors.add(classProcessor);
    }
}
