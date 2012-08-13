package com.maestro.json.impl.resolver;

import java.util.ArrayList;
import java.util.List;

import com.maestro.json.IFieldResolver;

@SuppressWarnings("rawtypes")
public class CompositeFieldResolver implements IFieldResolver {

	private List<IFieldResolver> resolvers;
	
	public CompositeFieldResolver() {
		init();
	}
	
	public CompositeFieldResolver(List<IFieldResolver> resolvers) {
		this.resolvers = resolvers;
	}

	private void init() {
		resolvers = new ArrayList<IFieldResolver>();
		resolvers.add(new PrimitiveFieldResolver());
		resolvers.add(new SimpleDateFieldResolver("dd.MMM.yyyy"));
		resolvers.add(new EnumFieldResolver());
	}
	
	public void addFieldResolver(IFieldResolver resolver) {
		resolvers.add(resolver);
	}
	
	@Override
	public Object convertToObject(Class beanClass, Object value) {
		Object result = null;
		
		for (IFieldResolver resolver : resolvers) {
			result = resolver.convertToObject(beanClass, value);
			if (result != null) {
				break;
			}
		}
		return result ;
	}

	@Override
	public boolean isPrimitive(Object value) {
		boolean result = false;
		
		if (value == null) {
			return result;
		}
		
		for (IFieldResolver resolver : resolvers) {
			result |= resolver.isPrimitive(value);
		}
		
		return result;
	}

	@Override
	public String convertFromObject(Object value) {
		String result = null;
		for (IFieldResolver resolver : resolvers) {
			result = resolver.convertFromObject(value);
			if (result != null) {
				break;
			}
		}
		return result ;
	}

}
