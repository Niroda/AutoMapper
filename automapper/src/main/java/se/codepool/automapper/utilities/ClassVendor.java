package se.codepool.automapper.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

/**
 * A class used to check if given type is user-defined type or JDK
 * @author Ali
 */
class ClassVendor {
	// stores all JDK types from 'java.home' path
	private static Set<String> CS = new HashSet<String>();

	static {
		try {
			File path = new File(System.getProperty("java.home"), "lib/classlist");
			BufferedReader br = new BufferedReader(new FileReader(path));
			String tempPath;
			while (true) {
				tempPath = br.readLine();
				if (tempPath == null) {
					break;
				} else {
					CS.add(tempPath.replace('/', '.'));
				}
			}
			br.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private ClassVendor() {
	}
	/**
	 * Checks if given type is one of the types in 'java.home' path or its package starts with one of the java paths
	 * @param type given type to check
	 * @return true if given type is one of the JDK defined types, otherwise false
	 */
	static boolean isJDKDefinedType(String type) {
		Class<?> clazz;
		try {
			clazz = Class.forName(type);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		}
		Package p = clazz.getPackage();
		if(p == null)
			return true;
		String packagePath = p.getName();
		return CS.contains(packagePath) || 
			   packagePath.startsWith("java") || 
			   packagePath.startsWith("com.sun") || 
			   packagePath.startsWith("sun") || 
			   packagePath.startsWith("oracle") || 
			   packagePath.startsWith("org.xml") || 
			   packagePath.startsWith("com.oracle");
	}
}
