=====================================================
		Readme for Fund-Management App
=====================================================

Used Technology:
	1. Java
		verions: 7 and 8
	2. Spring-boot
		version: 2.3.1.RELEASE
	3. Neo4j-graphdb
		version: 4.3.7

Sonar Qube Report:
	https://sonarcloud.io/summary/overall?id=fund-management
	
Code Location:
	Repository: https://tools.publicis.sapient.com/bitbucket/projects/JATL/repos/assignment-ravi/browse/fund-management?at=refs%2Fheads%2Fdev
	Branch: dev
	
Postman Collection:
	https://tools.publicis.sapient.com/bitbucket/projects/JATL/repos/assignment-ravi/browse/fund-management/src/main/resources/templates/Fund-Management.postman_collection.json?at=refs%2Fheads%2Fdev

Architacture Overview:
	
	Application contains three layer:
		1. DOA layer
		2. Service layer
		3. Contraoller layer
	
	Principle used:
		1. Restfull service
		2. Solid Principle
		3. Design Pattern
			3.1 Singlton (default with spring autowiring)
			3.2 Factory
			3.3 Interface Driven
		
	Test Coverage:
		Unit Test done for DOA and Service layer with 100% coverage.
		For Contraoller layer use Postman testing.
		

	