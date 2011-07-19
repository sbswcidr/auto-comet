package org.auto.comet.example.chat.test;

import java.lang.annotation.Annotation;

import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;

public class Test2 {
	public static void main(String[] args) throws Exception {

		AnnotationConfiguration annotationConfiguration = new AnnotationConfiguration();
		annotationConfiguration.configure("/hibernate.cfg.xml");
		Class c = Student.class;
		Annotation[] annotations = c.getAnnotations();
		for (Annotation annotation : annotations) {
			System.out.println(annotation);
		}
		// Class clazz =
		// Class.forName("org.auto.comet.example.chat.test.Student");
		Class clazz2 = Test2.class.getClassLoader().loadClass(
				"org.auto.comet.example.chat.test.Student");

		// Session session = annotationConfiguration.buildSessionFactory()
		// .openSession();

		// Student stu = new Student();
	}
}
