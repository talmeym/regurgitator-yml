package com.emarte.regurgitator.core;

import static com.emarte.regurgitator.core.StringUtil.dashesToCamelCase;
import static com.emarte.regurgitator.core.YmlPackageLookup.getPackageForType;

public class YmlLoaderUtil<TYPE extends Loader> extends LoaderUtil<TYPE> {
	public TYPE deriveLoader(Yaml yaml) throws RegurgitatorException {
		return buildFromClass(deriveClass(yaml));
	}

	static String deriveClass(Yaml yaml) throws RegurgitatorException {
		String type = yaml.getType();
		return deriveClass(getPackageForType(type), dashesToCamelCase(type));
	}

	static String deriveClass(String packageName, String className) throws RegurgitatorException {
		if (packageName == null) {
			throw new RegurgitatorException("Package not found for class: " + className);
		}

		return packageName + "." + className + "YmlLoader";
	}
}
