String message = "";

void setup() {
  Serial.begin(9600);
}

void loop() {
    // while (Serial.available() == 0) {}
    message = Serial.readString();
    // str.trim();
    // if (str == "/get") {}
    Serial.print("A");
    delay(20);
}
