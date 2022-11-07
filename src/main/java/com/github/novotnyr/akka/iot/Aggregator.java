package com.github.novotnyr.akka.iot;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;

public class Aggregator extends AbstractBehavior<Aggregator.Command> {
    public static final ServiceKey<Aggregator.Command> AGGREGATOR
            = ServiceKey.create(Aggregator.Command.class, "Aggregator");

    private Aggregator(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(context -> {
            context.getSystem()
                   .receptionist()
                   .tell(Receptionist.register(AGGREGATOR, context.getSelf()));
            return new Aggregator(context);
        });
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(RecordTemperature.class, this::recordTemperature)
                .build();
    }

    private Behavior<Command> recordTemperature(RecordTemperature command) {
        return Behaviors.same();
    }

    public interface Command {
    }

    public interface Event {
    }

    public record RecordTemperature(double temperature, ActorRef<Sensor.Command> sensor) implements Command {}
}
