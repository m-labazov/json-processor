package com.maestro.xml.json.resolver;

import java.util.List;

import com.maestro.xml.IFieldResolver;

@SuppressWarnings("rawtypes")
public class CompositeFieldResolver implements IFieldResolver {

	private List<IFieldResolver> resolvers;
	
	public CompositeFieldResolver(List<IFieldResolver> resolvers) {
		this.resolvers = resolvers;
	}
	
	@Override
	public Object convert(Class beanClass, Object value) {
		Object result = null;
		
		for (IFieldResolver resolver : resolvers) {
			result = resolver.convert(beanClass, value);
			
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

}
