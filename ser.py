import serial
import time
import argparse

# parse command-line arguments
parser = argparse.ArgumentParser(description='Controlling Cutting Mechanism')
parser.add_argument('--dir', type=str, default='D', help='Movement of the cutting mechanism. Options are D (downward) and U (upwards).')

# Global serial var
ser = None

def sendData(data):
    data += "\r\n"
    ser.write(data.encode())
     
def slice_fruit():
    data = 'DOWN'
    sendData(data)
    
def reset_mechanism():
    data = 'UP'
    sendData(data)
    
def main():
    args = parser.parse_args()
    global ser
    
    try:
        ser = serial.Serial('/dev/ttyUSB0', 9600)
    except:
        print("Please check the port: error with USB connection.")
    
    # Move downward
    if args.dir == 'D':
        print("Slicing fruit")
        t_end = time.time() + 0.01
        while time.time() < t_end:
            slice_fruit()
    
    # Move upwards
    if args.dir == 'U':
        print("Resetting")
        t_end = time.time() + 0.01
        while time.time() < t_end:
            reset_mechanism()

if __name__=='__main__':
    main()
