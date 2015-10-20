package transforms;

import transforms.generated.*;

import javax.xml.bind.*;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;

import java.io.*;
import java.util.List;

/**
 * Class for unmarshalling XML
 * @author Toomas Kallioja
 *
 */
public class JAXBUnMarshaller {
	
	/**
	 * Unmarshall given xml document to java objects
	 * @param xmlDocument
	 */
	public void unMarshall(File xmlDocument) {
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance("transforms.generated");
			
			// Create new unmarhshaller
			Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
			
			// Create new schemafactory, get the schema file and assign it to unmarhshaller
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");
			Schema schema = schemaFactory.newSchema(new File(
					"people.xsd"));
			unMarshaller.setSchema(schema);
			
			// Create new custom event and assign it to unmarshaller
			CustomValidationEventHandler validationEventHandler = new CustomValidationEventHandler();
			unMarshaller.setEventHandler(validationEventHandler);

			// Create people element by unmarshalling xml
			JAXBElement<PeopleType> peopleElement = (JAXBElement<PeopleType>) unMarshaller
					.unmarshal(xmlDocument);

			// Create new peopletype based on people element
			PeopleType people = peopleElement.getValue();

			// Make person list from people
			List<PersonType> personList = people.getPerson();
			
			// Loop through person list and print the values
			for (int i = 0; i < personList.size(); i++) {
				// Get the person and its healthprofile
				PersonType person = (PersonType) personList.get(i);
				HealthProfileType healthProfile = person.getHealthprofile();
				
				// Print person attributes including healthprofile 
				System.out.println("");
				System.out.println("Person id: " + person.getId());
				System.out.println("Name: " + person.getFirstname() + " " + person.getLastname());
				System.out.println("Birthdate: " + person.getBirthdate());
				System.out.println("Healthprofile (last update " + healthProfile.getLastupdate() + "):");
				System.out.println("Weight: " + healthProfile.getWeight());
				System.out.println("Height: " + healthProfile.getHeight());
				System.out.println("BMI: " + healthProfile.getBmi());
			}
		} catch (JAXBException e) {
			System.out.println(e.toString());
		} catch (SAXException e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * When program is run, create new unmarshaller and unmarshall the given xml document
	 * @param argv
	 */
	public static void main(String[] argv) {
		File xmlDocument = new File("people.xml");
		JAXBUnMarshaller jaxbUnmarshaller = new JAXBUnMarshaller();
		jaxbUnmarshaller.unMarshall(xmlDocument);

	}

	/**
	 * Custom event handler for unmarshaller
	 * @author Toomas Kallioja
	 *
	 */
	class CustomValidationEventHandler implements ValidationEventHandler {
		public boolean handleEvent(ValidationEvent event) {
			if (event.getSeverity() == ValidationEvent.WARNING) {
				return true;
			}
			if ((event.getSeverity() == ValidationEvent.ERROR)
					|| (event.getSeverity() == ValidationEvent.FATAL_ERROR)) {

				System.out.println("Validation Error:" + event.getMessage());

				ValidationEventLocator locator = event.getLocator();
				System.out.println("at line number:" + locator.getLineNumber());
				System.out.println("Unmarshalling Terminated");
				return false;
			}
			return true;
		}

	}
}
