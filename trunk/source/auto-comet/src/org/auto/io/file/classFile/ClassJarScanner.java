package org.auto.io.file.classFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.auto.io.AntPathMatcher;
import org.auto.io.PathMatcher;
import org.auto.util.ClassUtils;

/**
 * jar文件class扫描
 *
 * @author huxh
 * */
public class ClassJarScanner implements ClassScanner {
    /**
     * 默认扫描路径下的所有文件
     * */
    private String pattern = "**/*";

    private JarFile jarFile;

    private List<ClassHandler> classHandlers = new LinkedList<ClassHandler>();

    private PathMatcher pathMatcher = new AntPathMatcher();

    public ClassJarScanner(JarFile jarFile) {
        this.jarFile = jarFile;
    }

    public void scan() {
        try {
            this.scanJarFile();
        } catch (IOException e) {
            throw new RuntimeException("读取压缩文件[" + jarFile.getName() + "]时出错!",
                    e);
        }
    }

    /**
     * 扫描jar文件
     * */
    protected void scanJarFile() throws IOException {
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryPath = entry.getName();
            if (matchPattern(entryPath)) {
                InputStream inputStream = jarFile.getInputStream(entry);
                try {
                    Class<?> clazz = getClassFromInputStream(inputStream);
                    this.handle(clazz);
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException ioe) {
                        // ignore
                    }
                }
            }
        }
    }

    /**
     *
     * 处理扫描结果
     */
    private void handle(Class<?> clazz) {
        List<ClassHandler> classHandlers = this.getHandlers();
        for (ClassHandler classHandler : classHandlers) {
            classHandler.handle(clazz);
        }
    }

    /**
     * 从资源读取类
     * */
    private Class<?> getClassFromInputStream(InputStream inputStream)
            throws IOException {
        return ClassUtils.getClassFromInputStream(inputStream);
    }

    /**
     * 匹配路径
     * */
    protected boolean matchPattern(String path) {
        if (null == pattern)
            return true;
        return getPathMatcher().match(this.pattern, path);
    }

    public String getPattern() {
        return pattern;
    }

    /**
     * 设置匹配模式
     * */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public PathMatcher getPathMatcher() {
        return pathMatcher;
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    public List<ClassHandler> getHandlers() {
        return classHandlers;
    }

    public void setHandlers(List<ClassHandler> classHandlers) {
        this.classHandlers = classHandlers;
    }

    public void addHandler(ClassHandler classHandler) {
        this.classHandlers.add(classHandler);
    }

}
