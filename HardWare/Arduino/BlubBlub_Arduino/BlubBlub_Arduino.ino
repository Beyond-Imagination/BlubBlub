
#include <OneWire.h>
#include <DallasTemperature.h>
#include <LiquidCrystal_I2C.h>

LiquidCrystal_I2C lcd(0x3F, 16, 2);

OneWire ourWire(3);
DallasTemperature sensors(&ourWire);


void setup() {
  Serial.begin(9600);

lcd.init();
lcd.backlight();

sensors.begin();
}


int lightVal;
int turbityVol;
int turbityVal;
float voltage;
void loop(){
  
  /////LIGHT VALUE
  lightVal = analogRead(A0);
  lightVal/=10;

  if(lightVal < 6)    lightVal =6;
  else if(lightVal>68)    lightVal = 68;
    
  lightVal = 100-((lightVal-6)*100/62);

  //TURBIDITY VALUE
  turbityVol = analogRead(A1); // read the input on analog pin 0:
  voltage = turbityVol * (5.0 / 1024.0); 
  turbityVal = (-1120.4)*voltage*voltage+(5742.3)*voltage-4352.9;
  if(turbityVal<20)   turbityVal = 0;
  else if(turbityVal<40)  turbityVal = 1;
  else  turbityVal = 2;  

  //TEMPERATURE VALUE
  sensors.requestTemperatures(); // Send the command to get temperatures
  

  lcd.setCursor(0, 0);
  lcd.print("CDS=");
  
  lcd.print(lightVal);
  lcd.print("  ");
  lcd.print("TUR:");
  if(turbityVal == 0) lcd.print("GREAT");
  else if(turbityVal == 1) lcd.print("GOOD");
  else  lcd.print("BAD");
  lcd.setCursor(0, 1);
  lcd.print("TEMP=");
  lcd.print(sensors.getTempCByIndex(0));

  Serial.print("CDS=");
  Serial.println(lightVal);
  Serial.print("TUR=");
  Serial.println(voltage);
  Serial.print("TEMP=");
  Serial.println(sensors.getTempCByIndex(0));
  

}
 
