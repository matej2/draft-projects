import subprocess

# Replace these values with your desired phone number and message
phone_number = "031123456"
message = "Pozdrav!"

# Escape any special characters in the message
message = message.replace('"', '\\"').replace("'", "\\'")

# Use adb to send the SMS command to the device
subprocess.run(["adb", "shell", f"am start -a android.intent.action.SENDTO -d sms:{phone_number} --es sms_body '{message}' --ez exit_on_sent true"])
subprocess.run(["adb", "shell", "input keyevent 22"])
subprocess.run(["adb", "shell", "input keyevent 66"])
