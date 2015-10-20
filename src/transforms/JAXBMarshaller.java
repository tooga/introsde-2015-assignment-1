package transforms;

import transforms.generated.*;

import javax.xml.bind.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
 * Class for marshalling to XML
 * @author Toomas Kallioja
 *
 */
public class JAXBMarshaller {
	
	/**
	 * Generate the XML document
	 * Takes the destination XML file as parameter
	 * @param xmlDocument
	 */
	public void generateXMLDocument(File xmlDocument) {
		try {
			
			JAXBContext jaxbContext = JAXBContext.newInstance("transforms.generated");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
			transforms.generated.ObjectFactory factory = new transforms.generated.ObjectFactory();
			
			// TODO More people? Check updated assignment
			
			// Create new people type
			PeopleType people = factory.createPeopleType();
			// Create new person type and set attributes
			PersonType person = factory.createPersonType();
			person.setFirstname("Lucky");
			person.setLastname("Luke");
			person.setBirthdate(stringToXMLGregorianCalendar("1982-12-20T21:00:00.000+02:00"));
			person.setId(BigInteger.valueOf(2));
			// Create new healthprofile type and set attributes
			HealthProfileType healthProfile = factory.createHealthProfileType();
			healthProfile.setLastupdate(stringToXMLGregorianCalendar("2015-09-20T16:00:00.000+02:00"));
			healthProfile.setWeight(BigInteger.valueOf(80));
			healthProfile.setHeight((float)1.60);
			healthProfile.setBmi((float)30.10);
			// Assign healthprofile to the person
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
			person.setId(BigInteger.valueOf(4));

			healthProfile = factory.createHealthProfileType();
			healthProfile.setLastupdate(stringToXMLGregorianCalendar("2015-02-01T18:00:00.000+02:00"));
			healthProfile.setWeight(BigInteger.valueOf(95));
			healthProfile.setHeight((float)1.85);
			healthProfile.setBmi((float)28.00);
			person.setHealthprofile(healthProfile);

			personList = people.getPerson();
			personList.add(person);

			// Create people element
			JAXBElement<PeopleType> peopleElement = factory
					.createPeople(people);
			// Marshal people element, print it out and save to defined xml file
			marshaller.marshal(peopleElement,
					System.out);
			marshaller.marshal(peopleElement,
					new FileOutputStream(xmlDocument));

		} catch (IOException e) {
			System.out.println(e.toString());

		} catch (JAXBException e) {
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
	 * When program is run, create new JAXBMarshaller and generate xml-document
	 * @param argv
	 */
	public static void main(String[] argv){
		String xmlDocument = "people1.xml";
		JAXBMarshaller jaxbMarshaller = new JAXBMarshaller();
		jaxbMarshaller.generateXMLDocument(new File(xmlDocument));
	}
}
