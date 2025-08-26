#include <WiFi.h>
#include <Firebase_ESP_Client.h>
#include "addons/TokenHelper.h"
#include "time.h"

#define WIFI_SSID "Galaxy network"
#define WIFI_PASSWORD "raveen00789"
#define API_KEY "AIzaSyALJsTJb_Iz3wVhGk4EcaziYs8BYXCCZeI"
#define DATABASE_URL "https://disasteralertsystem-78a0c-default-rtdb.asia-southeast1.firebasedatabase.app/"
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;
#define WATER_SENSOR_PIN 32  // Pin for analog input
bool signupOK = false;
unsigned long dataMillis = 0; 
String city = "";

const char* ntpServer = "pool.ntp.org";
const long gmtOffset_sec = 5.5 * 3600;    
const int   daylightOffset_sec = 0;
String getFormattedTime() {
  struct tm timeinfo;
  if (!getLocalTime(&timeinfo)) {
    Serial.println("Failed to obtain time");
    return "N/A";
  }
  char buffer[20];
  strftime(buffer, sizeof(buffer), "%Y-%m-%d %H:%M:%S", &timeinfo);
  return String(buffer);
}
void setup() {
  Serial.begin(9600);
  pinMode(WATER_SENSOR_PIN, INPUT);
  #if WATER_SENSOR_PIN == 32
    Serial.println("WATER_SENSOR_PIN is correctly defined as 32");
    city = "Gampaha";
  #else
    Serial.println("Other location");
  #endif 
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(300);
  }
  configTime(gmtOffset_sec, daylightOffset_sec, ntpServer);
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();
  config.api_key = API_KEY;
  config.database_url = DATABASE_URL; 
  // config.host = DATABASE_URL;
  // config.signer.tokens.legacy_token = API_KEY;
    config.token_status_callback = tokenStatusCallback;
  if (Firebase.signUp(&config, &auth,"",""))  {
    Serial.println("signup OK");
    signupOK = true;
  } else {
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
}

void loop() {
  int sensorValue = analogRead(WATER_SENSOR_PIN);
  float waterLevelPercentage = (sensorValue / 4095.0) * 100;
  Serial.print("Sensor Value: ");
  Serial.print(sensorValue);
  Serial.print("  Water Level Percentage: ");
  Serial.println(waterLevelPercentage);
  if (Firebase.ready() && (millis() - dataMillis > 5000 || dataMillis == 0)){
    dataMillis = millis();
if (Firebase.RTDB.pushInt(&fbdo, "Sensors/waterLevelSensor/levelData", 0)) {
    String newKey = fbdo.pushName(); 
    Firebase.RTDB.setFloat(&fbdo, "Sensors/waterLevelSensor/levelData/" + newKey + "/WaterLevelPercentage", waterLevelPercentage);
    String timeStamp = getFormattedTime();
    Firebase.RTDB.setString(&fbdo, "Sensors/waterLevelSensor/levelData/" + newKey + "/time",timeStamp);
    Firebase.RTDB.setString(&fbdo, "Sensors/waterLevelSensor/levelData/" + newKey + "/location",city);
    Serial.println("Data saved");
}
  }else{
    Serial.println("DB is not ready");
  }
  delay(1000);
}