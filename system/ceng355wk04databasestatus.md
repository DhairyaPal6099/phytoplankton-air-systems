# CENG 355 Week 04 Database Status
## Weekly checks for every student:
- /1 Present in person in a safe manner for the duration of the class: glasses, no food/drink, etc.
- /1 Hardware and any other project equipment present.
## This week
- Mobile update was due before class, Winter 2024 during class was fine since that week I was teaching through a mask.
- Status update [wk04database.md](wk04database.md) by student C on behalf of the group due the night before next class. Discuss what data will be stored.   
   - /0.5 Mobile Status Report Document [wk04database.md](wk04database.md) Style and Format
   - /0.5 Recent and current progress
   - /0.5 Problems and hyperlinks such as those to uploaded documents
   - /0.5 Financial update
- /1 Work on enclosure in Adobe Illustrator, .ai file and corresponding .pdf file to be uploaded the night before next class.
- Work on KiCad PCB updates for groups that are using additional connections such as SPI. Some groups have multiple sensors at one I2C address, consider what address changes could be done to resolve this. I noticed that in Derek Malloy's textbook he says that "Hexadecimal addresses 0x03 to 0x77 are displayed by default. Using -a will display the full range 00x00 to 0x7F." Note also that while Qwiic I2C bus peripherals with unique addresses can be daisy chained:
>The Qwiic MLX90393 has onboard I2C pull up resistors; if multiple sensors are connected to the bus with the pull-up resistors enabled, the parallel equivalent resistance will create too strong of a pull-up for the bus to operate correctly. As a general rule of thumb, disable all but one pair of pull-up resistors if multiple devices are connected to the bus. If you need to disconnect the pull up resistors they can be removed by cutting the traces on the corresponding jumpers highlighted below. [^1]
[^1]: Qwiic Magnetometer (MLX90393) Hookup Guide. SparkFun Electronics, Accessed December 2023. Available at: https://learn.sparkfun.com/tutorials/qwiic-magnetometer-mlx90393-hookup-guide
>There a single set of jumpers on the MAX30101 side (non Qwiic connector side) of this product. This triple jumper labeled I2C connects pull-up resistors to the I²C data lines. If you're daisy chaining many I²C devices together, you may need to consider cutting these traces.[^2]
[^2]: SparkFun Pulse Oximeter and Heart Rate Monitor Hookup Guide. SparkFun Electronics, Accessed December 2023. Available at: https://learn.sparkfun.com/tutorials/sparkfun-pulse-oximeter-and-heart-rate-monitor-hookup-guide
## Next week
- Hardware Connectivty Status update.
  - Are you able to connect to Humber Wi-Fi reliably?
  - Have you been able to set up pyrebase to connect to Firebase with your pi?
## Showcase
- For Winter 2025: Wednesday April 23rd in the BCTI.

