package org.auto.io.scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.auto.io.processor.ClassProcessor;
import org.auto.io.processor.FileProcessor;
import org.auto.util.ClassUtils;

/**
 * 类文件扫描器
 *
 * @author huxh
 * */
public class ClassFileScanner implements ClassScanner {

	private DefaultFileScanner fileScanner;
	private List<ClassProcessor> classProcessors = new LinkedList<ClassProcessor>();

	/**
	 * @param file
	 *            要扫描的目录或者文件
	 * */
	public ClassFileScanner(File file) {
		this.fileScanner = new DefaultFileScanner(file);
		fileScanner.setPattern(DEFAULT_CLASS_RESOURCE_PATTERN);

		fileScanner.addProcessor(new FileProcessor() {
			public void process(File file) {
				Class<?> clazz;
				try {
					clazz = getClassFromFile(file);
				} catch (IOException e) {
					throw new RuntimeException("读取文件 [" + file.getName()
							+ "] 时出错", e);
				}
				for (ClassProcessor classProcessor : getProcessors()) {
					classProcessor.process(clazz);
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

	public List<ClassProcessor> getProcessors() {
		return classProcessors;
	}

	public void setProcessors(List<ClassProcessor> classProcessors) {
		this.classProcessors = classProcessors;
	}

	public void addProcessor(ClassProcessor classProcessor) {
		this.classProcessors.add(classProcessor);
	}

}
