package com.emarte.regurgitator.core;

import java.util.ServiceLoader;

public class YmlPackageLookup {
	private static ServiceLoader<YmlPackageMap> PACKAGE_MAPS = ServiceLoader.load(YmlPackageMap.class);

	public static String getPackageForType(String type) throws RegurgitatorException {
		for(YmlPackageMap set: PACKAGE_MAPS) {
			String pakkage = set.getPackageForKind(type);

			if(pakkage != null) {
				return pakkage;
			}
		}

		throw new RegurgitatorException("Package not found for type: " + type);
	}
}
