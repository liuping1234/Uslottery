package com.uslotter.util;

import java.io.File;
import java.util.Comparator;


/**
 *实现排序接口,按时间先后排序 
 */
public class Sort implements Comparator<File> {

	@Override
	public int compare(File lhs, File rhs) {
		long diff = lhs.lastModified() - rhs.lastModified();
		if (diff > 0)
			return -1;// 倒序正序控制
		else if (diff == 0)
			return 0;
		else
			return 1;// 倒序正序控制
	}

	public boolean equals(Object obj) {
		return true;
	}

}
