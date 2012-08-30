package com.maestro.json.impl.beaninfo;

import java.util.ArrayList;
import java.util.List;

import com.maestro.json.IBeanInfo;
import com.maestro.json.IBeanInfoContext;

public class DefaultBeanContext implements IBeanInfoContext {

	// TODO possibly needs synchronization
	private List<IBeanInfo> beanInfos;

	public DefaultBeanContext() {
		beanInfos = new ArrayList<IBeanInfo>();
	}
	
	@Override
	public void addBeanInfo(IBeanInfo info) {
		beanInfos.add(info);
	}
	
	@Override
	public boolean containsBeanInfo(Class<?> clazz) {
		boolean result = false;
		IBeanInfo beanInfo = getBeanInfo(clazz);
		if (beanInfo != null) {
			result = true;
		}
		return result;
	}

	@Override
	public List<IBeanInfo> getBeanInfos() {
		return beanInfos;
	}

	public void setBeanInfos(List<IBeanInfo> beanInfos) {
		this.beanInfos = beanInfos;
	}

	@Override
	public IBeanInfo getBeanInfo(Class<?> clazz) {
		IBeanInfo result = null;
		for (IBeanInfo info : beanInfos)  {
			if (clazz.equals(info.getBeanClass())) {
				result = info;
			}
		}
		return result;
	}
	
}
