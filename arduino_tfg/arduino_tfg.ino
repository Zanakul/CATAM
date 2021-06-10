#include <SoftwareSerial.h>
#include <Servo.h>
#include <DHT.h>
#include <DHT_U.h>
/*

  (\_/)
  (º.º)
  (")(")

*/



Servo servo;
SoftwareSerial bt(10, 11);
DHT dht(25, DHT22);
int motor = 6;    //Used PWM PIN
int ccw = 7;
int pos = 0;
const int trigPin = 40;
const int echoPin = 38;
int temp, hum;
const int trigPinb = 36;
const int echoPinb = 34;

long duration;
int distance;
long durationb;
int distanceb;

char res[30];
int ang = 0, spd = 0, dire = 0, sensor = 0;

bool firstmsg = true;



void setup() {
  pinMode(24, OUTPUT);
  digitalWrite(24, 0);
  pinMode(motor, OUTPUT);
  digitalWrite(motor, 1);
  analogWrite(motor, 255);
  pinMode(ccw, OUTPUT);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(trigPinb, OUTPUT);
  pinMode(echoPinb, INPUT);

  Serial.begin(9600);
  bt.begin(9600);
  dht.begin();

  digitalWrite(ccw, 0);
  TCCR4B = TCCR4B & B11111000 | B00000010; // for PWM frequency of 4000 Hz
  servo.attach(12);
  servo.write(75);
  digitalWrite(24, 1);





}


void loop() {
  int i, j;
  int flag = 1;
  ang = 0;
  spd = 0;
  dire = 0;
  bool endrx;
  boolean msgok;
  char aux1[10];
  char aux2[10];
  char aux3[10];
  char aux4[10];

  endrx = false;
  msgok = false;
  i = 0;

  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(echoPin, HIGH);
  // Calculating the distance
  distance = duration / 29 / 2;
  //distance = duration * 0.034 / 2;

  digitalWrite(trigPinb, LOW);
  delayMicroseconds(2);
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPinb, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPinb, LOW);
  // Reads the echoPin, returns the sound wave travel time in microseconds
  durationb = pulseIn(echoPinb, HIGH);
  // Calculating the distance
  distanceb = durationb * 0.034 / 2;





  while (!endrx) {

    while (!bt.available());

    res[i] = bt.read();
    //Serial.print(res[i]);
    if (res[i] == '_') {
      endrx = true;
      if (firstmsg) {
        msgok = false;
        firstmsg = false; //First msg is deleted to clean the buffer;
      }
      else
        msgok = true;
    }
    i++;
    //Serial.println(i);

  }
  i = 0;
  j = 0;
  if (msgok) {
    for (int k = 0; k < 20; k++)


      while (res[i] != ';') {
        aux1[j] = res[i];
        i++;
        j++;
      }

    i++;                          //';' is avoided
    aux1[j] = 0;
    ang = atoi(aux1);
    j = 0;
    while (res[i] != '#') {
      aux2[j] = res[i];
      i++;
      j++;
    }
    i++;
    aux2[j] = 0;
    spd = atoi(aux2);
    j = 0;
    while (res[i] != ':') {
      aux3[j] = res[i];
      i++;
      j++;
    }

    aux3[j] = 0;
    dire = atoi(aux3);

    i++;
    j = 0;
    while (res[i] != '_') {
      aux4[j] = res[i];
      i++;
      j++;
    }

    aux4[j] = 0;
    sensor = atoi(aux4);


    if (distance <= 20 && dire == 1) {
      servo.write(75);
      analogWrite(motor, 255);


    }
    else if (distanceb <= 20 && dire == 0) {
      servo.write(75);
      analogWrite(motor, 255);


    } else {
      servo.write(ang);
      analogWrite(motor, spd);
      digitalWrite(ccw, dire);

    }
    if (sensor == 1) {

      float t = dht.readTemperature();
      Serial.println(t);
      hum = dht.readHumidity();
      temp = t * 5;
      Serial.println(temp);
      bt.write(temp);
      bt.write(hum);
    }


    msgok = false;
  }


}
