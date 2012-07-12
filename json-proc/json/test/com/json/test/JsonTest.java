package com.json.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import com.json.test.entity.*;
import com.maestro.xml.*;
import com.maestro.xml.json.*;
import com.maestro.xml.json.resolver.*;

public class JsonTest {

	JSONProcessor processor;
	
	@Before
	public void init() {
		processor = getProcessor();
	}
	
	private JSONProcessor getProcessor() {
		List<IBeanResolver> beanResolvers = new ArrayList<IBeanResolver>();
		beanResolvers.add(new PolymorphismBeanResolver());
		beanResolvers.add(new SimpleBeanResolver());
		IBeanResolver beanResolver = new CompositeBeanResolver(beanResolvers);
		
		List<IFieldResolver> fieldResolvers = new ArrayList<IFieldResolver>();
		fieldResolvers.add(new PrimitiveFieldResolver());
		fieldResolvers.add(new SimpleDateFieldResolver("dd.MMM.yyyy"));
		fieldResolvers.add(new EnumFieldResolver());
		IFieldResolver fieldRslover = new CompositeFieldResolver(fieldResolvers);
		
		IBeanAdapter adapter = XBeanAdapter.getInstance();
		IBeanProcessor beanProcessor = new CustomBeanProcessor(beanResolver, adapter, fieldRslover);
		
		JSONProcessor processor = new JSONProcessor(beanProcessor);
		return processor;
	}
	
	@Test
	public void primitiveAttributeTest() {
		byte b = 1;
		Byte by = 2;
		double d = 3.;
		Double doub = 4.;
		float f = 5;
		Float flo = 6f;
		int i = 7;
		Integer in = 8;
		long l = 9;
		Long lon = 10l;
		short s = 11;
		Short sho = 12;
		String text = "someText";
		
		JPrimitiveEntity entity = new JPrimitiveEntity();
		entity.setB(b);
		entity.setBy(by);
		entity.setD(d);
		entity.setDoub(doub);
		entity.setF(f);
		entity.setFlo(flo);
		entity.setI(i);
		entity.setIn(in);
		entity.setL(l);
		entity.setLon(lon);
		entity.setS(s);
		entity.setSho(sho);
		entity.setText(text);
		
		String json = processor.processBean(entity);
		
		JPrimitiveEntity result = processor.processJson(JPrimitiveEntity.class, json);
		
		assertEquals(entity.getB(), result.getB());
		assertEquals(entity.getBy(), result.getBy());
		assertTrue(entity.getD() == result.getD());
		assertEquals(entity.getDoub(), result.getDoub());
		assertTrue(entity.getF() == result.getF());
		assertEquals(entity.getFlo(), result.getFlo());
		assertEquals(entity.getI(), result.getI());
		assertEquals(entity.getIn(), result.getIn());
		assertEquals(entity.getL(), result.getL());
		assertEquals(entity.getLon(), result.getLon());
		assertEquals(entity.getS(), result.getS());
		assertEquals(entity.getSho(), result.getSho());
		assertEquals(entity.getText(), result.getText());
	}
	
	@Test
	public void beanAttributeTest() {
		JPerson person = new JPerson();
		person.setFirstName("Anjiii");
		person.setLastName("O'Callahan");
		
		JCar car = new JCar();
		car.setModel("HUMMER");
		car.setName("PUSSY CAR");
		car.setYear(2013);
		person.setCar(car);
		
		String json = processor.processBean(person);
		
		JPerson result = processor.processJson(JPerson.class, json);
		
		assertEquals(person.getFirstName(), result.getFirstName());
		assertEquals(person.getLastName(), result.getLastName());
		
		assertNotNull(result.getCar());
		
		JCar carResult = result.getCar();
		assertEquals(car.getModel(), carResult.getModel());
		assertEquals(car.getName(), carResult.getName());
		assertEquals(car.getYear(), carResult.getYear());
		
	}
	
	@Test
	public void arrayAttributeTest() {
		JDepartment depart = new JDepartment();
		depart.setName("JSON development department");
		
		JPerson pers1 = new JPerson();
		pers1.setFirstName("Max");
		pers1.setLastName("Avatar");
		
		JCar car1 = new JCar();
		car1.setModel("Nimbula");
		pers1.setCar(car1);
		depart.addPerson(pers1);
		
		JPerson pers2 = new JPerson();
		pers2.setFirstName("Avici");
		depart.addPerson(pers2);
		
		String json = processor.processBean(depart);
		
		JDepartment result = processor.processJson(JDepartment.class, json);
		
		assertEquals(depart.getName(), result.getName());
		
		assertNotNull(result.getPersons());
		
		HashSet<JPerson> resultPersons = result.getPersons();
		assertEquals(depart.getPersons().size(), resultPersons.size());
		
		Iterator<JPerson> iterator = resultPersons.iterator();
		JPerson res_pers1 = iterator.next();
		JPerson res_pers2 = iterator.next();
		
		assertEquals(pers1.getFirstName(), res_pers1.getFirstName());
		assertEquals(pers1.getLastName(), res_pers1.getLastName());
		assertNotNull(res_pers1.getCar());
		
		JCar resCar1 = res_pers1.getCar();
		assertEquals(car1.getModel(), resCar1.getModel());
		
		assertEquals(pers2.getFirstName(), res_pers2.getFirstName());
	}
	
	@Test
	public void polymorphismTest() {
		JDepartment depart = new JDepartment();
		depart.setName("JSON development department");
		
		JDetailPerson person = new JDetailPerson();
		person.setFirstName("Anjiii");
		person.setLastName("O'Callahan");
		person.setSurName("Amandovna");
		person.setYear(1984);
		
		JCar car = new JCar();
		car.setModel("HUMMER");
		car.setName("PUSSY CAR");
		car.setYear(2013);
		person.setCar(car);
		
		depart.setjBoss(person);
		
		String json = processor.processBean(depart);
		
		JDepartment resultDep = processor.processJson(JDepartment.class, json);
		
		assertEquals(depart.getName(), resultDep.getName());
		
		JDetailPerson result = (JDetailPerson) resultDep.getjBoss();
		
		assertEquals(person.getFirstName(), result.getFirstName());
		assertEquals(person.getLastName(), result.getLastName());
		assertEquals(person.getSurName(), result.getSurName());
		assertEquals(person.getYear(), result.getYear());
		
		assertNotNull(result.getCar());
		
		JCar carResult = result.getCar();
		assertEquals(car.getModel(), carResult.getModel());
		assertEquals(car.getName(), carResult.getName());
		assertEquals(car.getYear(), carResult.getYear());
	}
	
	@Test
	public void dateEntityProcessingTest() {
		String jsonResult = "{\"monthDate\":\"01.Jan.1970\"}";
		String jsonToParse = "{\"monthDate\":\"01.Jan.1970\", \"simpleDate\":\"01.01.1970\", \"timeDate\":\"01.01.1970 00:00\"}";
		Date defaultDate = new Date(-7200000);
		JDateEntity entity = new JDateEntity();
		entity.setMonthDate(defaultDate);
		
		String json = processor.processBean(entity);
		assertEquals(jsonResult, json);
		
		JDateEntity result = processor.processJson(JDateEntity.class, jsonToParse);
		assertEquals(defaultDate, result.getMonthDate());
		assertEquals(defaultDate, result.getSimpleDate());
		assertEquals(defaultDate, result.getTimeDate());
	}
	
	@Test
	public void enumTest() {
		JEnumEntity entity = new JEnumEntity();
		entity.setEnt1(EnumEntity.VAL1);
		entity.setEnt2(EnumEntity.VAL2);
		entity.setEnt3(EnumEntity.VAL3);
		
		String json = processor.processBean(entity);
		
		JEnumEntity result = processor.processJson(JEnumEntity.class, json);
		
		assertEquals(entity.getEnt1(), result.getEnt1());
		assertEquals(entity.getEnt2(), result.getEnt2());
		assertEquals(entity.getEnt3(), result.getEnt3());
	}
}
