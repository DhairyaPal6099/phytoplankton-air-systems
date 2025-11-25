import qwiic_ens160
import board
import busio
import digitalio
from adafruit_vcnl4040 import VCNL4040
import time
import firebase_admin
from firebase_admin import credentials, db

#Julian new imports
import RPi.GPIO as GPIO
from adafruit_ads1x15.ads1015 import ADS1015
from adafruit_ads1x15.analog_in import AnalogIn

#Sanskriti new imports
import adafruit_veml7700

#Firebase setup
cred = credentials.Certificate("servicekey.json")
firebase_admin.initialize_app(cred, {'databaseURL': 'https://phytoplankton-air-systems-default-rtdb.firebaseio.com/'})
device_ref = db.reference("device_001")

# =========================== CARBON DIOXIDE FUNCTION =============================== #

def convert_ppm_to_kg_co2(ppm, volume_m3):
    mg_per_m3 = ppm * 1.8
    total_mg = mg_per_m3 * volume_m3
    kg = total_mg / 1000000
    return kg

# ==================================================================================== #

def read_sensor():
    print("Starting sensors...")
    
    # CO2
    ens160 = qwiic_ens160.QwiicENS160()
    ens160.begin()
    ens160.set_operating_mode(2)
    
    # Proximity
    i2c = busio.I2C(board.SCL, board.SDA)
    vcnl4040 = VCNL4040(i2c)
    led = digitalio.DigitalInOut(board.D17)
    led.direction = digitalio.Direction.OUTPUT
    MOTION_THRESHOLD = 100
    previous_state_proximity = None
    
    # Turbidity
    ADS_CHANNEL = 2
    LED_PIN = 17
    
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(LED_PIN, GPIO.OUT)

    i2c = busio.I2C(board.SCL, board.SDA)
    ads = ADS1015(i2c)
    channel = AnalogIn(ads, ADS_CHANNEL)

    latest_voltage = 0.0
    
    # Light
    i2c = busio.I2C(board.SCL, board.SDA)
    sensor_light = adafruit_veml7700.VEML7700(i2c)
    
    time.sleep(1)
    
    while True:
        ens160.check_data_status()
        eco2 = ens160.get_eco2()
        co2_kg = convert_ppm_to_kg_co2(eco2, 0.005)
        co2_kg = round(co2_kg, 10)
        
        proximity = vcnl4040.proximity
        current_state_proximity = proximity > MOTION_THRESHOLD
        
        latest_voltage = channel.voltage
        
        lux = float(sensor_light.lux or 0.0)
        als = float(sensor_light.light or 0.0)
        white = float(sensor_light.white or 0.0)

        timestamp = time.strftime("%Y-%m-%d %H:%M:%S")

        update_data = {
            'co2_converted': co2_kg,
            'proximity': current_state_proximity,
            'turbidity': round(latest_voltage, 4),
            'light': round(lux, 2),
            'als_raw': round(als, 0),
            'white_raw': round(white, 0),
            'timestamp': timestamp
        }
            
        previous_state_proximity = current_state_proximity
        
        device_ref.update(update_data)
        
        print(f"CO2_converted: {eco2}, Proximity: {proximity}, Motion state: {current_state_proximity}, Turbidity: {round(latest_voltage, 4)}, Light: {round(lux, 2)} at {timestamp}")
        time.sleep(2)
        
if __name__ == "__main__":
    try:
        read_sensor()
    except KeyboardInterrupt:
        print("Exiting...")
