import java.util.Random;

class Dice {
    private Random random = new Random();

    public int roll(int sides) {
        return random.nextInt(sides) + 1;
    }
}

class Creature {
    private int attack;
    private int defense;
    private int health;
    private int minDamage;
    private int maxDamage;

    public Creature(int attack, int defense, int health, int minDamage, int maxDamage) {
        if (attack < 1 || attack > 30 || defense < 1 || defense > 30 || health < 0 || minDamage < 1 || maxDamage < minDamage) {
            throw new IllegalArgumentException("Некорректные параметры существа");
        }

        this.attack = attack;
        this.defense = defense;
        this.health = health;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Урон не может быть отрицательным");
        }

        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }

    public void heal() {
        int maxHealAmount = (int) (0.3 * (30 - health));
        int healAmount = Math.min(maxHealAmount, 4);
        health += healAmount;
    }

    public int attack(Creature target, Dice dice) {
        int modifier = attack - target.defense + 1;
        modifier = Math.max(modifier, 1); // Не меньше 1

        int successCount = 0;
        for (int i = 0; i < modifier; i++) {
            if (dice.roll(6) >= 5) {
                successCount++;
            }
        }

        if (successCount > 0) {
            int damage = dice.roll(maxDamage - minDamage + 1) + minDamage;
            target.takeDamage(damage);
            return damage;
        } else {
            return 0; // Неудачная атака
        }
    }
}

class Player extends Creature {
    public Player(int attack, int defense, int health, int minDamage, int maxDamage) {
        super(attack, defense, health, minDamage, maxDamage);
    }
}

class Monster extends Creature {
    public Monster(int attack, int defense, int health, int minDamage, int maxDamage) {
        super(attack, defense, health, minDamage, maxDamage);
    }
}

public class Game {
    public static void main(String[] args) {
        Dice dice = new Dice();
        Player player = new Player(20, 15, 100, 5, 10);
        Monster monster = new Monster(18, 10, 80, 8, 15);

        System.out.println("Игрок атакует монстра!");
        int damage = player.attack(monster, dice);
        if (damage > 0) {
            System.out.println("Игрок наносит " + damage + " урона монстру.");
        } else {
            System.out.println("Игрок не попадает по монстру.");
        }

        System.out.println("Здоровье монстра: " + monster.getHealth());

        System.out.println("Монстр атакует игрока!");
        damage = monster.attack(player, dice);
        if (damage > 0) {
            System.out.println("Монстр наносит " + damage + " урона игроку.");
        } else {
            System.out.println("Монстр не попадает по игроку.");
        }

        System.out.println("Здоровье игрока: " + player.getHealth());
    }
}