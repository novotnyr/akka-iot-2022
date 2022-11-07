package com.github.novotnyr.akka.iot;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;

import java.time.Duration;
import java.util.Set;

public class Sensor extends AbstractBehavior<Sensor.Command> {
    private ActorRef<Aggregator.Command> aggregator;

    private Sensor(ActorContext<Command> context) {
        super(context);

        var receptionistMessageAdapter = context.messageAdapter(
                Receptionist.Listing.class, this::adaptReceptionistListing);

        Receptionist.Command message = Receptionist.subscribe(Aggregator.AGGREGATOR, receptionistMessageAdapter);
        context.getSystem()
               .receptionist()
               .tell(message);

        this.aggregator = getContext().getSystem().deadLetters();
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(context -> {
            return Behaviors.withTimers(timers -> {
                timers.startTimerWithFixedDelay(new TriggerMeasurement(), Duration.ofSeconds(2));
                return new Sensor(context);
            });
        });
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(TriggerMeasurement.class, this::onTriggerMeasurement)
                .onMessage(SetAggregator.class, this::onSetAggregator)
                .build();
    }

    private Behavior<Command> onTriggerMeasurement(TriggerMeasurement command) {
        double temperature = (Math.random() * 60) - 30;
        getContext().getLog().info("Measured temperature: {}", temperature);

        this.aggregator.tell(new Aggregator.RecordTemperature(temperature, getContext().getSelf()));

        return Behaviors.same();
    }


    private Behavior<Command> onSetAggregator(SetAggregator command) {
        this.aggregator = command.aggregator();
        getContext().getLog().error("Reassigned aggregator {}", this.aggregator);
        return Behaviors.same();
    }

    private Command adaptReceptionistListing(Receptionist.Listing event) {
        Set<ActorRef<Aggregator.Command>> serviceInstances = event.getServiceInstances(Aggregator.AGGREGATOR);
        if (serviceInstances.size() != 1) {
            getContext().getLog().error("Incorrect number of Aggregators. Found: {}", serviceInstances.size());
            return new SetAggregator(getContext().getSystem().deadLetters());
        }
        ActorRef<Aggregator.Command> aggregator = serviceInstances.iterator().next();
        return new SetAggregator(aggregator);
    }

    public interface Command {
    }

    public record TriggerMeasurement() implements Command {}

    public record SetAggregator(ActorRef<Aggregator.Command> aggregator) implements Command {}

    public interface Event {
    }
}
