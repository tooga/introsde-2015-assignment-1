package transforms;

import transforms.generated.*;

import javax.xml.bind.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.math.BigInteger;

/**
 * Class for marshalling to JSON
 * @author toomas
 *
 */
public class JAXBMarshalJSON {
	
	/**
	 * Generate the JSON document. 
	 * Takes the destination JSON file and ObjectMapper object as parameters
	 * @param jsonDocument
	 * @param mapper
	 */
	public void generateJSONDocument(File jsonDocument, ObjectMapper mapper) {
		try {

			transforms.generated.ObjectFactory factory = new transforms.generated.ObjectFactory();
			// Create new people type
			PeopleType people = factory.createPeopleType();
			// Create new person type and set attributes
			PersonType person = factory.createPersonType();
			person.setFirstname("Lucky");
			person.setLastname("Luke");
			person.setBirthdate(stringToXMLGregorianCalendar("1982-12-20T21:00:00.000+02:00"));
			person.setId(BigInteger.valueOf(0002));
			// Create new healthprofile type and set attributes
			HealthProfileType healthProfile = factory.createHealthProfileType();
			healthProfile.setLastupdate(stringToXMLGregorianCalendar("2015-09-20T16:00:00.000+02:00"));
			healthProfile.setWeight(BigInteger.valueOf(80));
			healthProfile.setHeight((float)1.60);
			healthProfile.setBmi((float)30.10);
			// Assign healthprofile the person
			person.setHealthprofile(healthProfile);

			// Make list of persons and add the person to the list
			List<PersonType> personList = people.getPerson();
			personList.add(person);

			// Make new person with healthprofile and add to persons list
			person = factory.createPersonType();
			person.setFirstname("Charlie");
			person.setLastname("Brown");
			person.setBirthdate(stringToXMLGregorianCalendar("1964-06-10T20:00:00.000+02:00"));
			person.setId(BigInteger.valueOf(3));

			healthProfile = factory.createHealthProfileType();
			healthProfile.setLastupdate(stringToXMLGregorianCalendar("2013-05-12T18:00:00.000+02:00"));
			healthProfile.setWeight(BigInteger.valueOf(80));
			healthProfile.setHeight((float)1.75);
			healthProfile.setBmi((float)25.14);
			person.setHealthprofile(healthProfile);

			personList = people.getPerson();
			personList.add(person);

			// Make new person with healthprofile and add to persons list
			person = factory.createPersonType();
			person.setFirstname("Jack");
			person.setLastname("Black");
			person.setBirthdate(stringToXMLGregorianCalendar("1986-05-20T19:30:00.000+02:00"));
			person.setId(BigInteger.valueOf(0004));

			healthProfile = factory.createHealthProfileType();
			healthProfile.setLastupdate(stringToXMLGregorianCalendar("2015-02-01T18:00:00.000+02:00"));
			healthProfile.setWeight(BigInteger.valueOf(95));
			healthProfile.setHeight((float)1.85);
			healthProfile.setBmi((float)28.00);
			person.setHealthprofile(healthProfile);

			personList = people.getPerson();
			personList.add(person);

			// Convert people object to string and print it
			
			String result = mapper.writeValueAsString(people);
	        System.out.println(result);
	        // Write the people object to specified json file
	        mapper.writeValue(jsonDocument, people);

		} catch (IOException e) {
			System.out.println(e.toString());

		}

	}

	/**
	 * Convert string representation of date to type XMLGregorianCalendar
	 * @param stringDate
	 * @return
	 */
	private static XMLGregorianCalendar stringToXMLGregorianCalendar(String stringDate){
        try {
        	return DatatypeFactory.newInstance().newXMLGregorianCalendar(stringDate);
        } catch (DatatypeConfigurationException e) {
            System.out.print(e.getMessage());
            return null;
        }
    }

	/**
	 * When program is run, create new ObjectMapper, set configurations and call JSON generator method
	 * @param argv
	 */
	public static void main(String[] argv){		
		// Jackson Object Mapper 
		ObjectMapper mapper = new ObjectMapper();
		
		// Adding the Jackson Module to process JAXB annotations
        JaxbAnnotationModule module = new JaxbAnnotationModule();
        
		// Configure mapper
		mapper.registerModule(module);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // Name of JSON document
		String jsonDocument = "people1.json";
		// Create new jaxbJSONMarshaller object and generate JSON
		JAXBMarshalJSON jaxbJSONMarshaller = new JAXBMarshalJSON();
		jaxbJSONMarshaller.generateJSONDocument(new File(jsonDocument), mapper);
	}
}
