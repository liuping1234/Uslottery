package com.uslotter.util;

import java.io.File;
import java.util.Comparator;


/**
 *ʵ������ӿ�,��ʱ���Ⱥ����� 
 */
public class Sort implements Comparator<File> {

	@Override
	public int compare(File lhs, File rhs) {
		long diff = lhs.lastModified() - rhs.lastModified();
		if (diff > 0)
			return -1;// �����������
		else if (diff == 0)
			return 0;
		else
			return 1;// �����������
	}

	public boolean equals(Object obj) {
		return true;
	}

}
