package com.github.novotnyr.akka.iot;

import akka.actor.typed.ActorSystem;

public class SensorRunner {
    public static void main(String[] args) {
        var system = ActorSystem.create(Sensor.create(), "system");
        system.tell(new Sensor.TriggerMeasurement());
    }
}
