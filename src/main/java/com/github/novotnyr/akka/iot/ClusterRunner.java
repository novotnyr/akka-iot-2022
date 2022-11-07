package com.github.novotnyr.akka.iot;

import akka.actor.typed.ActorSystem;
import akka.cluster.typed.ClusterSingleton;
import akka.cluster.typed.SingletonActor;

public class ClusterRunner {
    public static void main(String[] args) {
        var system = ActorSystem.create(Sensor.create(), "smarthome");
        system.tell(new Sensor.TriggerMeasurement());

        ClusterSingleton clusterSingleton = ClusterSingleton.get(system);
        clusterSingleton.init(SingletonActor.of(Aggregator.create(), "Aggregator"));
    }
}
