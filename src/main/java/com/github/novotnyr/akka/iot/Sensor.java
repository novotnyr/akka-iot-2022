package com.github.novotnyr.akka.iot;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Sensor extends AbstractBehavior<Sensor.Command> {
    private Sensor(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(Sensor::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(TriggerMeasurement.class, this::onTriggerMeasurement)
                .build();
    }

    private Behavior<Command> onTriggerMeasurement(TriggerMeasurement command) {
        double temperature = (Math.random() * 60) - 30;
        getContext().getLog().info("Measured temperature: {}", temperature);
        return Behaviors.same();
    }

    public interface Command {
    }

    public record TriggerMeasurement() implements Command {}

    public interface Event {
    }
}
