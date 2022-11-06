package com.github.novotnyr.akka.iot;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Aggregator extends AbstractBehavior<Aggregator.Command> {
    private Aggregator(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(Aggregator::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder().build();
    }

    public interface Command {
    }

    public interface Event {
    }
}
