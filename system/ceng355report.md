# Phytoplankton Air Systems
Title Page (1st odd page not numbered, X.0 sections begin on odd pages, otherwise double sided and numbered)  
## Declaration of Joint Authorship   
[^1]

I, Dhairya Pal, confirm that this breakdown of authorship represents my
contribution to the work submitted for assessment and my contribution is my own work and
is expressed in my own words. Any uses made within the Technology Report of the works of
any other author, separate to the work group, in any form (ideas, equations, figures, texts,
tables, programs), are properly acknowledged at the point of use. A list of the references
used is included.

I, Dharmik Shah, confirm that this breakdown of authorship represents my
contribution to the work submitted for assessment and my contribution is my own work and
is expressed in my own words. Any uses made within the Technology Report of the works of
any other author, separate to the work group, in any form (ideas, equations, figures, texts,
tables, programs), are properly acknowledged at the point of use. A list of the references
used is included.

I, Julian Aldrich Imperial, confirm that this breakdown of authorship represents my
contribution to the work submitted for assessment and my contribution is my own work and
is expressed in my own words. Any uses made within the Technology Report of the works of
any other author, separate to the work group, in any form (ideas, equations, figures, texts,
tables, programs), are properly acknowledged at the point of use. A list of the references
used is included.

I, Sanskriti Mansotra, confirm that this breakdown of authorship represents my
contribution to the work submitted for assessment and my contribution is my own work and
is expressed in my own words. Any uses made within the Technology Report of the works of
any other author, separate to the work group, in any form (ideas, equations, figures, texts,
tables, programs), are properly acknowledged at the point of use. A list of the references
used is included.

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
### 1.2 Project Requirements and Specifications   
### 1.3 Project Schedule   
    
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
Maintaining a stable Wi=Fi connection can be challenging in areaas with weak signal strength. Sensor readings may fluctuate slightly due to environmental vairations, which can affect accuracy. Designing and assembling the custom PCB requires careful attention to ensure proper connections and prevent interference between sensors. Coordinating consistent data formatting between the Raspberry Pi, Firebase database, and the connected applicatoin is critical to prevent errors in data display or storage. Finally, integrating multiple sensors with different interfaces on a single system requires careful handling to ensure reliable operation.

### 4.8 Solutions   


## 5.0 Results and Discussion   

## 6.0 Conclusions   

## 7.0 Appendix
### 7.1 Firmware Code   
[Link to firmware](hardware/firmware).
### 7.2 Mobile Application Code   
[Link to GitHub repository for app]()

## 8.0 References   
