## Spúšťanie

Spúšťanie v klastri _Akka Cluster_.

### Prvá inštancia a _seed node_

Potrebujeme nastaviť: 

- *port pre inštanciu*: `-Dakka.remote.artery.canonical.port=10001`
- odkaz na centrálny uzol v klastri (_seed_, niekde aj _leader_, či _master_). Aj jednouzlový klaster potrebuje odkaz na _seed node_.

  Pri spúšťaní nastavíme parametre JVM:

      -Dakka.remote.artery.canonical.port=10001 -Dakka.cluster.seed-nodes.0=akka://smarthome@127.0.0.1:10001 

- `smarthome` je názov z `ActorSystem.create()`
- port v URL adrese sa musí zhodovať s portom centrálneho uzla.

Ak vynecháme port `canonical port`, použije sa `25520`.

Ak vynecháme _seed nodes_, uzol sa musí manuálne pripojiť do klastra, k čomu je hláška:

    Cluster Node [akka://smarthome@127.0.0.1:25520] - No seed-nodes configured, manual cluster join required, see https://doc.akka.io/docs/akka/current/typed/cluster.html#joining 

### Druhá inštancia

Druhá inštancia potrebuje vlastný port, odlišný od portu pre _seed node_, ak bežíme na jednom stroji:

Pri spúšťaní nastavíme parametre JVM:

    -Dakka.remote.artery.canonical.port=10002 -Dakka.cluster.seed-nodes.0=akka://smarthome@127.0.0.1:10001 

Druhá inštancia pobeží na porte 10002.

Odkazujeme sa na _seed node_ na porte 10001.

## Architektúra

![Architektúra](architecture.png)