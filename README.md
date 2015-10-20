# Assignment 1 for Introduction to Service Design and Engineering -course of UNITN

### What this is about?
This assignment is about XML, XSD, XPATH and marhalling and unmarhalling XML and JSON using JAXB.

### Folder and file structure
The root folder has the XML and XSD files used in the program as well as the ivy.xml and build.xml. The src-package consist of the java files for the program.

### What do each file do?
The ivy.xml and build.xml files are for installing all the dependencies and running the program. HealthProfileReader java file in src-package takes care of reading information from the XML using XPATH and printing all the required information for the assignment. The transforms folder in src-package has all the java files for marshalling the XML and JSON and unmarshalling XML by using JAXB.

### How to run the program?
The program uses ant build-tool to run the program. To execute the program, please run in the terminal:

  ant execute.evaluation
  



