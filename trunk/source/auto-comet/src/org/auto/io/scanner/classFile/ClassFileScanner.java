package org.auto.io.scanner.classFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.auto.io.scanner.FileHandler;
import org.auto.io.scanner.PatternFileScanner;
import org.auto.util.ClassUtils;

/**
 * 类文件扫描器
 *
 * @author huxh
 * */
public class ClassFileScanner implements ClassScanner {

	private PatternFileScanner fileScanner;
	private List<ClassHandler> classHandlers = new LinkedList<ClassHandler>();

	/**
	 * @param file
	 *            要扫描的目录或者文件
	 * */
	public ClassFileScanner(File file) {
		this.fileScanner = new PatternFileScanner(file);
		fileScanner.setPattern(DEFAULT_CLASS_RESOURCE_PATTERN);

		fileScanner.addHandler(new FileHandler() {
			public void handle(File file) {
				Class<?> clazz;
				try {
					clazz = getClassFromFile(file);
				} catch (IOException e) {
					throw new RuntimeException("读取文件 [" + file.getName()
							+ "] 时出错", e);
				}
				for (ClassHandler classHandler : getHandlers()) {
					classHandler.handle(clazz);
				}
			}
		});
	}

	public void scan() {
		this.fileScanner.scan();
	}

	private Class<?> getClassFromFile(File file) throws IOException {
		InputStream inputStream = new FileInputStream(file);
		try {
			return getClassFromInputStream(inputStream);
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

	private Class<?> getClassFromInputStream(InputStream inputStream)
			throws IOException {
		return ClassUtils.getClassFromInputStream(inputStream);
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
