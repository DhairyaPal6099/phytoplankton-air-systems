# Phytoplankton Air Systems
Title Page (1st odd page not numbered, X.0 sections begin on odd pages, otherwise double sided and numbered)  
## Declaration of Joint Authorship   
[^1]

I, Dhairya Pal, confirm that the portion of this Technology Report attributed to me represents my own work and is expressed in my own words. I contributed to both the mobile application and the hardware development, including sensor integration, PCB assembly, wiring, and the physical enclosure. I also contributed with testing, debugging, and the integration of the software with the hardware to ensure the system operated reliably. Any material taken from other authors - including ideas, equations, figures, text, tables, or code - has been properly acknowledged and referenced.

I, Dharmik Shah, confirm that the portion of this Technology Report attributed to me represents my own work and is expressed in my own words. I majorly contributed to the mobile application and cloud integration using Firebase, including real-time data syncing, database structure design, and communication between the hardware and the application. In addition, I assisted with hardware setup and troubleshooting to support seamless integration of the system. Any material taken from other authors has been properly acknowledged and referenced.

I, Julian Aldrich Imperial, confirm that the portion of this Technology Report attributed to me represents my own work and is expressed in my own words. I contributed to the mobile application and assisted with hardware assembly, sensor testing, and system verification. I also helped document the hardware procedures and supported the integration of the sensors with the Raspberry Pi during testing. Any material taken from other authors has been properly acknowledged and referenced.

I, Sanskriti Mansotra, confirm that the portion of this Technology Report attributed to me represents my own work and is expressed in my own words. I contributed to the mobile application and background research and the preparation of written documentation, supporting the writing and review process to ensure clarity and completeness in the final report. Any material taken from other authors has been properly acknowledged and referenced.

[^1]: Technology Report Guidelines. OACETT, Revised September 2022. Available at: https://www.oacett.org/getmedia/5ad707d7-f472-4b24-a7fe-f34e270b0c41/2022_TR_Guidelines_-_Updated_Version_-_Sept_2022.pdf
## Proposal/Project Specifications   
[Link to proposal](wk01proposal.md).   
## Executive Summary   
A smart and eco-friendly air purification system that uses phytoplankton to naturally convert CO₂ into oxygen. The system connects with an Android app for real-time monitoring, automated alerts, and sustainability-focused feedback.    

## Table of Contents

[Declaration of Joint Authorship](#declaration-of-joint-authorship)   
[Proposal/Project Specifications](#proposalproject-specifications)   
[Executive Summary](#executive-summary)   
[Table of Contents](#table-of-contents)   
[List of Figures](#list-of-figures)   

[1.0 Introduction](#10-introduction)   
[1.1 Background](#11-background)   
[1.2 Project Requirements and Specifications](#12-project-requirements-and-specifications)   
[1.3 Project Schedule](#13-project-schedule)   

[2.0 Hardware Development Platform Report/Build Instructions](#20-hardware-development-platform-reportbuild-instructions)  
[2.1 Dhairya Pal](#21-dhairya-pal)  
[2.2 Sanskriti Mansotra](#22-sanskriti-mansotra)  
[2.3 Dharmik Shah](#23-dharmik-shah)  
[2.4 Julian Aldrich Imperial](#24-julian-aldrich-imperial)  

[3.0 Mobile Application Report](#30-mobile-application-report)  
[3.1 Deliverable 1](#31-deliverable-1)  
[3.2 Deliverable 2](#32-deliverable-2)  
[3.3 Deliverable 3](#33-deliverable-3)  
[3.4 Deliverable 4](#34-deliverable-4)  
[3.5 Deliverable 5](#35-deliverable-5)  

[4.0 Integration](#40-integration)  
[4.1 Enterprise Wireless Connectivity](#41-enterprise-wireless-connectivity)  
[4.2 Database Configuration](#42-database-configuration)  
[4.3 Network and Security Considerations](#43-network-and-security-considerations)  
[4.4 Unit Testing](#44-unit-testing)  
[4.5 Production Testing](#45-production-testing)  
[4.6 Sustainability Considerations](#46-sustainability-considerations)  
[4.7 Challenges/Problems](#47-challengesproblems)  
[4.8 Solutions](#48-solutions)  

[5.0 Results and Discussion](#50-results-and-discussion)

[6.0 Conclusions](#60-conclusions)

[7.0 Appendix](#70-appendix)  
[7.1 Firmware Code](#71-firmware-code)  
[7.2 Mobile Application Code](#72-mobile-application-code)  

[8.0 References](#80-references)  

## List of Figures   
[Figure 1: Gantt Chart](#figure-1-gantt-chart)  

## 1.0 Introduction  
### 1.1 Background 
Air quality has become a significant concern in urban and industrial environments due to rising levels of carbon dioxide (CO₂) and other pollutants. High CO₂ concentrations can affect human health, cognitive performance, and overall well-being. Traditional air purification methods often rely on chemical filters or energy-intensive processes, which can be costly and environmentally unsustainable.

Phytoplankton, a type of microalgae, naturally convert CO₂ into oxygen through photosynthesis. This project leverages this biological process to develop a smart, eco-friendly air purification system. By integrating sensors, a Raspberry Pi, and a mobile application, the system monitors air quality in real-time, automates alerts, and provides sustainability-focused feedback to users. This approach combines biotechnology, electronics, and software to create an innovative solution for cleaner air.

### 1.2 Project Requirements and Specifications   
The Phytoplankton Air System is designed to:
- Monitor CO₂ levels, light intensity, turbidity, and proximity using multiple sensors.
- Process and upload sensor data in real-time to a cloud database using a Raspberry Pi and Firebase.
- Display data and system metrics through a connected Android mobile application.
- Provide automated alerts and notifications when environmental thresholds are reached.
- Track the total CO₂ converted by the phytoplankton and support user achievements to encourage sustainability.
- Operate continuously with minimal maintenance, using a durable enclosure and energy-efficient components.

This combination of hardware and software ensures that the system is both functional and user-friendly, while emphasizing sustainability and environmental impact.

### 1.3 Project Schedule   
The project was executed over a 12-week period from May to July 2025, following an Agile Scrum timeline to ensure iterative development. As illustrated in Figure 1: Gantt Chart, the schedule was organized into four strategic phases:

#### Initiation and System Design:
The project began with the establishment of the software architecture and hardware requirements, specifically sensor selection and PCB schematic design. Concurrently, the team utilized Scrum tools to define user stories and set up the development environment.

#### Frontend Architecture (Sprint 1):
The initial development phase focused on the application's visual structure. The team prioritized the creation of the user interface layouts and navigation systems to establish the "look and feel" before integrating complex logic.

#### Core Feature Integration (Sprint 3):
Focus then shifted to backend functionality. This phase involved connecting the UI to the database, implementing user authentication, and enabling real-time data feedback loops within the application.

#### Refinement and Quality Assurance:
The final phase was dedicated to optimizing the software architecture (MVC implementation) and conducting comprehensive automated testing to ensure system stability prior to final deployment.
    
###### Figure 1: Gantt Chart  
![Gantt Chart](https://github.com/user-attachments/assets/c138356e-6b16-4434-bcb2-14d2db0d9012)
![Gantt Chart 1](https://github.com/user-attachments/assets/340ee00f-1d20-47cf-b968-cf50232dc089)

## 2.0 Hardware Development Platform Report/Build Instructions   
### 2.1 Dhairya Pal
[Hardware report](../hardware/Dhairya%20Pal%20-%20Hardware%20Report%20-%20Build%20Instruction.pdf)   
### 2.2 Sanskriti Mansotra  
[Hardware report](../hardware/Hardware%20Report%20-%20Build%20Instruction%20Template.docx)   
### 2.3 Dharmik Shah  
[Hardware report](../hardware/Dharmik%20Shah%20-%20Hardware%20Report%20-%20Build%20Instruction.pdf)   
### 2.4 Julian Aldrich Imperial  
[Hardware report](../hardware/Julian_Imperial%20Hardware%20Report%20-%20SEN0189.pdf)   
  
## 3.0 Mobile Application Report   
### 3.1 Deliverable 1
[Mobile Deliverable 1](/docs/deliverable1/Algaerithms_Phytopurifier_Group1_Deliverable_1.pdf)   
### 3.2 Deliverable 2      
[Mobile Deliverable 2](/docs/deliverable2/Algaerithms_Phytopurifier_1_2.pdf)   
### 3.3 Deliverable 3      
[Mobile Deliverable 3](/docs/deliverable3/Algaerithms_Phytopurifier_1_3.pdf)   
### 3.4 Deliverable 4      
[Mobile Deliverable 4](/docs/deliverable4/Algaerithms_Phytopurifier_Deliverable4.pdf)   
### 3.4 Deliverable 5      
[Mobile Deliverable 5](/docs/deliverable5/Algaerithms_Phytopurifier_Deliverable5.pdf)   

## 4.0 Integration   
### 4.1 Enterprise Wireless Connectivity   
The system uses Wi-Fi through a Raspberry Pi to send real-time sensor data to the cloud. The connection is set up using the Firebase Software Development Kit (SDK) provided by Firebase, which allows the device to write data directly to a Realtime Database through a Python script. A service key file is used for authentication so that only authorized devices can connect and update the database. Once connected, the Raspberry Pi continuously uploads data from all sensors at regular intervals. Each update includes readings for carbon dioxide, proximity, turbidity, and light intensity, along with a timestamp. THe use of Wi-Fi and cloud connectivity ensures that data is transmitted securely and can be accessed from anywhere through the linked application.

### 4.2 Database Configuration   
The system uses Firebase Realtime Database to store and manage sensor readings. Data is sent from the Raspberry Pi to a dedicated node in the database through the Firebase SDK. Each data entry contains carbon dioxide, proximity, turbidity, and light measurements, along with a timestamp. The system also utilizes the Firestore database from Firebase to store user records. These records have user profile details, and their notifications, and their metrics (e.g. lifetime carbondioxide converted) that are used to assess what achievements should they be given, if any. Firestore and Real-time database are connected through a script resides in the Android application, runs periodically, and adds the real-time carbon dioxide converted value to the lifetime carbon dioxde converted field in Firestore. The database structure is organized in a simple key-value format that supports fast updates and live synchronization. This setup allows the data to be retrieved easily by the connected application without delays or manual refreshes.

### 4.3 Network and Security Considerations   
The system operates on a secure Wi-Fi network and connects to Firebase through the official Firebase SDK, which manages secure communication automatically using encrypted HTTPS requests. User authentication is handled through the FIrebase Authentication service, ensuring that only registered and verified users can access the database. Database rules restrict read and write operations to authorized users and devices. Network stability is tested to confirm consistent data transmission without interruptions or interference. Exception handling is also in place in case of poor network stability, so no data gets corrupted. Since the sensor data trasmission is within every 1-2 seconds, the most simple network instability fix that is implemented is forgetting about the half-read or lost data and continue with the new fetch. Other exception handling is also in place, for example cases where there is no change in the data (due to network instability or other reasons). This setup ensures secure and reliable data flow throughout the system.

### 4.4 Unit Testing   
Each sensor is tested individually to verify proper operation before full system integration. Carbon dioxide, proximity, turbidity, and light sensors are read repeatedly to confirm that the values are consistent and within expected ranges. The data update function is checked by pushing test values to Firebase and confirming that the database reflects the correct readings. The Android application is also tested to ensure it correctly displays the sensor data when updates occue. Unit testing helps identify and resolve issues early, ensuring reliable performance when the system runs continuously.

### 4.5 Production Testing   
The full system is tested under continuous operation to verify stability and reliability. The Raspberry Pi collects data from all sensors and updates the Firebase database at regular intervals. Tests monitor the consistency of readings, database updates, and the synchronization with the connected application. The system is observed for network interruptions, sensor performance, and overall data accuracy during extended operation. Production testing confirms that the system functions reliably in a real-world environment and maintains consistent data flow.

### 4.6 Sustainability Considerations
The main sustainability impact of the system comes from the phytoplankton, which convert carbon dioxide into oxygen, contributing to improved air quality. The enclosure is laser-cut from acrylic, providing a durable housing for the system; while not biodegradable, it is strong and long-lasting. The enclosures for production, if manufactured at a large scale could use biodegradable material. The custom PCB simplifies the connection of all sensors to the Raspberry Pi, reductin wiring complexity and material use compared to a setup with separate modules and breadboards. The system is designed for continuous operation, supporting long-term monitoring and carbon dioxide conversion without excessive maintenance.

### 4.7 Challenges/Problems   
Maintaining a stable Wi-Fi connection can be challenging in areas with weak signal strength. Sensor readings may fluctuate slightly due to environmental vairations, which can affect accuracy. Designing and assembling the custom PCB requires careful attention to ensure proper connections and prevent interference between sensors. Coordinating consistent data formatting between the Raspberry Pi, Firebase database, and the connected application is critical to prevent errors in data display or storage. Finally, integrating multiple sensors with different interfaces on a single system requires careful handling to ensure reliable operation.

### 4.8 Solutions   
Network stability is improved by testing the Wi-Fi connection and placing the Raspberry Pi in areas with reliable signal strength. Sensor calibration is verified through repeated readings and comparision with expected ranges to ensure accuracy. The custom PCB is carefully designed and assembled to provide proper connections and minimize interference. Data consistency is maintained by standardizing the format of all readings before uploading to Firebase, ensuring the connected application displays information correctly. Coordinating the integration of multiple sensors and the Raspberry Pi through careful testing and documentation ensures reliable and continuous operation of the system.

## 5.0 Results and Discussion   
### 5.1 Hardware Results
The hardware performed reliably during testing. All sensors, including CO₂, light, turbidity, and proximity, provided consistent and accurate readings. The Raspberry Pi successfully collected data from all sensors, and the custom PCB and wiring ensured stable connections. Calibration and sensor placement supported optimal performance throughout the testing process.

### 5.2 Mobile Application and Cloud Integration Results
The Android application displayed live sensor data effectively, with Firebase enabling real-time updates. User alerts and notifications were triggered accurately when environmental thresholds were reached, and achievement tracking successfully reflected CO₂ conversion progress. The app provided an intuitive and responsive interface for monitoring the system.

### 5.3 System Integration Results
Integration between hardware and software was seamless. The system operated continuously during testing, with sensor data accurately transmitted, stored in the cloud, and displayed on the application. The combined operation of the Raspberry Pi, sensors, and mobile app confirmed that the system met the intended functional objectives.

### 5.4 Discussion
Overall, the Phytoplankton Air System successfully achieved its project goals. The system effectively monitored environmental parameters in real-time, provided meaningful user feedback, and accurately measured CO₂ levels. The testing confirmed the reliability and efficiency of the system’s hardware, software, and cloud integration. Future development can build on this strong foundation to further enhance user experience and system capabilities.

## 6.0 Conclusions   
The Phytoplankton Air System successfully demonstrated an innovative, eco-friendly approach to air purification. By combining phytoplankton-based CO₂ conversion with real-time sensor monitoring, cloud integration, and a responsive mobile application, the system provided reliable measurements and meaningful feedback to users.

The project confirmed the feasibility of integrating hardware and software components into a cohesive system that operates efficiently and supports sustainability goals. Testing verified that the sensors, Raspberry Pi, and mobile application worked together seamlessly, and that the system could accurately track CO₂ levels and environmental parameters.

Overall, the project met its objectives by delivering a functional, user-friendly, and environmentally focused air purification system. This work provides a strong foundation for future enhancements, including extended operation, advanced analytics, and expanded user engagement features.

## 7.0 Appendix
### 7.1 Firmware Code   
[Link to firmware](../hardware/FinalScript.py).
### 7.2 Mobile Application Code   
[Link to GitHub repository for app](https://github.com/Algaerithms-Inc/PhytoplanktonAirSystems.git)

## 8.0 References   
