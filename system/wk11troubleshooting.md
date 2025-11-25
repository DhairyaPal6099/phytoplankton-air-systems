### Problems
- We have not fully completed our project presentation slides, so we are not ready to present yet.
- The “immediate notification” feature in the app is not working properly. Periodic Notifications are working fine, but when the sensor values reach a certain threshold, it is not sending an immediate or an urgent notification in the application.
- The Raspberry Pi has not been tested inside the 3D-printed enclosure yet.
- We still need to test the entire setup with water and the sensors inside the enclosure to confirm that the readings are consistent and accurate.
### Solutions / Timeline / Current Status
#### 1. Finish Presentation
##### Approach:
Complete all slides (problem statement, system design - including screenshots of the application, database logic, sensors, testing plan, live app demo).
##### Timeline:
By the upcoming class (26th November 2025)
##### Current Status:
Slides are partially complete; attaching pictures and final formatting still needed.

#### 2. Fix Immediate Notifications in App
##### Approach:
- Re-check the Firebase triggers or local logic inside the app.
- Add debug logs to confirm if threshold detection is working.
- Test with temporary hardcoded values to verify notifications fire instantly.
##### Timeline:
2–3 days.
##### Current Status:
General idea of the notifications works well, but we think its better to have an immediate notification if light goes to a certain threshold where its less than the actual required amount, or the same with turbidity (for water) – where Still under debugging.

#### 3. Test Raspberry Pi Inside 3D Model Enclosure
##### Approach:
Place the Pi and sensors (screwed) into the 3D-printed enclosure and check:
- Fit and cable routing
- Temperature inside the case
- Sensor stability
##### Timeline:
1 day after printing and setup.
##### Current Status:
Enclosure is printed but Pi has not been tested inside yet.

#### 4. Water + Sensor Testing in Enclosure
##### Approach:
Fill the container with water and test all sensors (CO₂, light, turbidity, proximity) while running the Pi inside the enclosure.
##### Timeline:
After enclosure testing, likely end of week.
##### Current Status:
Not started yet.


