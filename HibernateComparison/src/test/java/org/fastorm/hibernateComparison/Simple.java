package org.fastorm.hibernateComparison;


import java.io.IOException;

import org.fastorm.utilities.Files;
import org.fastorm.utilities.Sets;
import org.springframework.core.io.ClassPathResource;

public class Simple {

	public static void main(String[] args) throws IOException {
		System.out.println("Hello World");
		Sets.makeSet(1);
		System.out.println(Files.getText(new ClassPathResource( "org/fastorm/sample/MySqlDataSource.xml")));
	} 
}
