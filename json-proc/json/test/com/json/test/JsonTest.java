package com.json.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.json.test.entity.EnumEntity;
import com.json.test.entity.JArrayEntity;
import com.json.test.entity.JCar;
import com.json.test.entity.JDateEntity;
import com.json.test.entity.JDepartment;
import com.json.test.entity.JDetailPerson;
import com.json.test.entity.JEnumEntity;
import com.json.test.entity.JPerson;
import com.json.test.entity.JPrimitiveEntity;
import com.json.test.entity.JStringEntity;
import com.maestro.xml.IBeanProcessor;
import com.maestro.xml.JsonException;
import com.maestro.xml.json.CustomBeanProcessor;

public class JsonTest {

	IBeanProcessor processor;
	
	@Before
	public void init() {
		processor = getProcessor();
	}
	
	private IBeanProcessor getProcessor() {
		IBeanProcessor beanProcessor = new CustomBeanProcessor();
		return beanProcessor;
	}
	
	@Test
	public void primitiveAttributeTest() throws JsonException {
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
		
		String json = processor.serialize(entity);
		
		JPrimitiveEntity result = processor.deserialize(JPrimitiveEntity.class, json);
		
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
	public void beanAttributeTest() throws JsonException {
		JPerson person = new JPerson();
		person.setFirstName("Anjiii");
		person.setLastName("O'Callahan");
		
		JCar car = new JCar();
		car.setModel("HUMMER");
		car.setName("PUSSY CAR");
		car.setYear(2013);
		person.setCar(car);
		
		String json = processor.serialize(person);
		
		JPerson result = processor.deserialize(JPerson.class, json);
		
		assertEquals(person.getFirstName(), result.getFirstName());
		assertEquals(person.getLastName(), result.getLastName());
		
		assertNotNull(result.getCar());
		
		JCar carResult = result.getCar();
		assertEquals(car.getModel(), carResult.getModel());
		assertEquals(car.getName(), carResult.getName());
		assertEquals(car.getYear(), carResult.getYear());
		
	}
	
	@Test
	public void collectionAttributeTest() throws JsonException {
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
		
		String json = processor.serialize(depart);
		
		JDepartment result = processor.deserialize(JDepartment.class, json);
		
		assertEquals(depart.getName(), result.getName());
		
		assertNotNull(result.getPersonList());
		
		ArrayList<JPerson> resultPersons = result.getPersonList();
		assertEquals(depart.getPersonList().size(), resultPersons.size());
		
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
	public void polymorphismTest() throws JsonException {
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
		
		String json = processor.serialize(depart);
		
		JDepartment resultDep = processor.deserialize(JDepartment.class, json);
		
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
	public void dateEntityProcessingTest() throws JsonException {
		String jsonResult = "{\"monthDate\":\"01.Jan.1970\"}";
		String jsonToParse = "{\"monthDate\":\"01.Jan.1970\", \"simpleDate\":\"01.01.1970\", \"timeDate\":\"01.01.1970 00:00\"}";
		Date defaultDate = new Date(-7200000);
		JDateEntity entity = new JDateEntity();
		entity.setMonthDate(defaultDate);
		
		String json = processor.serialize(entity);
		assertEquals(jsonResult, json);
		
		JDateEntity result = processor.deserialize(JDateEntity.class, jsonToParse);
		assertEquals(defaultDate, result.getMonthDate());
		assertEquals(defaultDate, result.getSimpleDate());
		assertEquals(defaultDate, result.getTimeDate());
	}
	
	@Test
	public void enumTest() throws JsonException {
		JEnumEntity entity = new JEnumEntity();
		entity.setEnt1(EnumEntity.VAL1);
		entity.setEnt2(EnumEntity.VAL2);
		entity.setEnt3(EnumEntity.VAL3);
		
		String json = processor.serialize(entity);
		
		JEnumEntity result = processor.deserialize(JEnumEntity.class, json);
		
		assertEquals(entity.getEnt1(), result.getEnt1());
		assertEquals(entity.getEnt2(), result.getEnt2());
		assertEquals(entity.getEnt3(), result.getEnt3());
	}
	
	@Test
	public void arrayAttributeTest() throws JsonException {
		JDepartment depart = new JDepartment();
		depart.setName("JSON development department");
		
		JPerson[] persArray = new JPerson[2];		
		JPerson pers1 = new JPerson();
		pers1.setFirstName("Max");
		pers1.setLastName("Avatar");
		
		JCar car1 = new JCar();
		car1.setModel("Nimbula");
		pers1.setCar(car1);
		
		JPerson pers2 = new JPerson();
		pers2.setFirstName("Avici");
		persArray[0] = pers1;
		persArray[1] = pers2;
		
		depart.setPersonArray(persArray);
		
		String json = processor.serialize(depart);
		
		JDepartment result = processor.deserialize(JDepartment.class, json);
		
		JPerson[] resultArray = result.getPersonArray();
		assertEquals(2, resultArray.length);
		
		JPerson res_pers1 = resultArray[0];
		JPerson res_pers2 = resultArray[1];
		
		assertEquals(pers1.getFirstName(), res_pers1.getFirstName());
		assertEquals(pers1.getLastName(), res_pers1.getLastName());
		assertNotNull(res_pers1.getCar());
		
		JCar resCar1 = res_pers1.getCar();
		assertEquals(car1.getModel(), resCar1.getModel());
		
		assertEquals(pers2.getFirstName(), res_pers2.getFirstName());
	}
	
	@Test
	public void textElementTest() throws JsonException {
		JArrayEntity entity = new JArrayEntity();
		JStringEntity stringEntity = new JStringEntity("qqq", "www");
		entity.setEntity(stringEntity);
		
		JStringEntity[] array = new JStringEntity[3];
		JStringEntity stringEntity2 = new JStringEntity("aaa", "sss");
		JStringEntity stringEntity3 = new JStringEntity("fff", "ddd");
		JStringEntity stringEntity4 = new JStringEntity("ggg", "hhh");
		array[0] = stringEntity2;
		array[1] = stringEntity3;
		array[2] = stringEntity4;
		entity.setArray(array);
		
		List<JStringEntity> collection = new ArrayList<JStringEntity>();
		JStringEntity stringEntity5 = new JStringEntity("zzz", "xxx");
		JStringEntity stringEntity6 = new JStringEntity("ccc", "vvv");
		JStringEntity stringEntity7 = new JStringEntity("bbb", "nnn");
		collection.add(stringEntity5);
		collection.add(stringEntity6);
		collection.add(stringEntity7);
		entity.setCollection(collection);
		
		String json = processor.serialize(entity);
		
		JArrayEntity result = processor.deserialize(JArrayEntity.class, json);
		
		List<JStringEntity> resultCollection = result.getCollection();
		assertNotNull(resultCollection);
		assertEquals(3, resultCollection.size());
		assertEquals(stringEntity5.getStr2(), resultCollection.get(0).getStr2());
		assertEquals(stringEntity6.getStr2(), resultCollection.get(1).getStr2());
		assertEquals(stringEntity7.getStr2(), resultCollection.get(2).getStr2());
		
		JStringEntity[] resultArray = result.getArray();
		assertNotNull(resultArray);
		assertEquals(3, resultArray.length);
		assertEquals(stringEntity2.getStr2(), resultArray[0].getStr2());
		assertEquals(stringEntity3.getStr2(), resultArray[1].getStr2());
		assertEquals(stringEntity4.getStr2(), resultArray[2].getStr2());
		
		JStringEntity resultEntity = result.getEntity();
		assertNotNull(resultEntity);
		assertEquals(stringEntity.getStr2(), resultEntity.getStr2());
		
		
	}
}
