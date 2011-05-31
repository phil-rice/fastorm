package org.fastorm.dataGenerator;

import java.util.Map;

import org.fastorm.utilities.collections.Files;
import org.fastorm.utilities.maps.Maps;
import org.fastorm.xmlItemStrategies.SimpleStringItemStrategy;
import org.fastorm.xmlToMap.AllowElementLikeMeUnrecognisedStrategy;
import org.fastorm.xmlToMap.IStrategyNode;
import org.fastorm.xmlToMap.XmlToMapParser;
import org.fastorm.xmlToMap.XmlToMapStrategy;
import org.springframework.core.io.ClassPathResource;

public class DataGeneratorTest {

	public static void main(String[] args) {
		Map<String, IStrategyNode<String, Object>> idToStrategy = Maps.makeMap(//
				"tableName", new SimpleStringItemStrategy(),//
				"name", new SimpleStringItemStrategy(),//
				"value", new SimpleStringItemStrategy(),//
				"type", new SimpleStringItemStrategy()//
				);
		XmlToMapStrategy<String, Object> xmlToMapStrategy = new XmlToMapStrategy<String, Object>(idToStrategy, new AllowElementLikeMeUnrecognisedStrategy<String, Object>());
		XmlToMapParser<String, Object> parser = new XmlToMapParser<String, Object>(xmlToMapStrategy);
		String xml = Files.getText(new ClassPathResource("extraData.xml"));
		System.out.println(parser.parse(xml));
	}
}
