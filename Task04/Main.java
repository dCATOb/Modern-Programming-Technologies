import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Композиция: Car создаёт и содержит Engine; передать двигатель извне нельзя.
        Car car = new Car();
        car.start();

        // Агрегация: команда ссылается на созданных отдельно игроков. Игроки могут существовать и после команды.
        Player player = new Player("Саша");
        Team team = new Team();
        team.add(player);
        System.out.println("Участник команды: " + team.members().get(0).name());
    }
}

class Engine {
    void start() { System.out.println("Двигатель запущен."); }
}

class Car {
    private final Engine engine = new Engine();
    void start() { engine.start(); }
}

class Player {
    private final String name;
    Player(String name) { this.name = name; }
    String name() { return name; }
}

class Team {
    private final List<Player> members = new ArrayList<>();
    void add(Player player) { members.add(player); }
    List<Player> members() { return List.copyOf(members); }
}
