package de.commsmp.smp.util;

import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public enum DeathCause {

    PLAYER("$killed wurde von $killer begrüßt", "$killed hat sein Leben für $killer gegeben",
            "$killed wollte nicht mehr mit $killer Leben", "$killer hat $killed seine Klinge gezeigt"),
    NONE("$killed wurde nie gefunden", "$killed ist verschwunden"),
    DOWNING("$killed vergaß zu atmen, während er unter Wasser war"),
    FALLING("$killed sah das tiefe Loch nicht", "$killed ist runtergefallen", "$killed hat die Lücke nicht gesehen",
            "$killed's Füße wurden gebrochen"),
    FALLING_BLOCK("$killed vergaß, dass Sand herunterfallen kann"),
    SUFFOCATION("$killed wollte ein Fossil sein", "$killed hat sich begraben"),
    FIRE("$killed sprang in einen Schornstein und kam nicht mehr heraus"),
    BURNING("$killed spielte gerne mit Feuer"),
    LAVA("$killed versuchte, in scharfer Sauce zu schwimmen", "Die Lava wollte mit $killed kuscheln"),
    DRYOUT("$killed vergaß, wie man Luft atmet"),
    DRAGON_BREATH("$killed wurde von einem Drachen angespuckt"),
    BLOCK_EXPLOSION("$killed sah die Landmine nicht"),
    CACTUS("$killed wollte einen Kaktus umarmen"),
    CRAMMING("$killed hatte zu viele Freunde"),
    FLY_INTO_WALL("Während $killed flog, war er nicht aufmerksam", "$killed war zu schnell und die Wand zu hart"),
    HOT_FLOOR("$killed mochte den heißen und leuchtenden Block im Nether"),
    LIGHTNING("$killed wollte sein Handy aufladen"),
    MAGIC("$killed schaute einer Hexe in die Augen"),
    MELTING("$killed verwandelte sich in Wasserdampf"),
    POISON("$killed wählte die falsche Flasche"),
    STARVATION("$killed war schlecht im Kochen"),
    SUICIDE("$killed hasste diese Welt"),
    PROJECTILE_ARROW("Der Apfel auf $killed's Kopf wurde verfehlt"),
    PROJECTILE_FIREBALL("$killed war in der Hölle berühmt"),
    PROJECTILE_SNOWBALL("$killed aß zu viel Schnee"),
    PROJECTILE_EGG("$killed verlor gegen ein Huhn"),
    PROJECTILE_ENDER_PEARL("$killed wurde in Quanten gespalten"),
    PROJECTILE_LLAMA_SPIT("$killed wird von Lamas gehasst"),
    PROJECTILE_DRAGON_FIREBALL("$killed war im End berühmt"),
    PROJECTILE_SHULKER_BULLET("$killed wollte fliegen"),
    PROJECTILE_WITHER_SKULL("$killed spielte Fangen mit einem Wither"),
    PROJECTILE_SPECTRAL_ARROW("$killed hatten Pech, weil der Apfel auf dem Hut verfehlt wurde"),
    PROJECTILE_TRIDENT("$killed hatte einen Streit mit Neptun"),
    THORNS("$killed liebte Dornen"),
    VOID("$killed wollte die Welt von unten sehen", "$killed hat sein Inventar aufgegeben"),
    WITHER("$killed hatte zu viel Kontakt mit dunklen Skeletten"),
    CUSTOM("$killed, dies wird ignoriert"),
    ENTITY_ATTACK("$killed wurde von $entity erschlagen", "Der letzte Schlag von $entity hat $killed umgebracht"),
    ENTITY_SWEEP_ATTACK("$killed wurde von $entity in einem Schwung getroffen"),
    ENTITY_EXPLOSION("$killed machte einen großen Sprung durch $entity", "$entity hat $killed zerbombt");

    private final String[] fallback;

    DeathCause(String... fallback) {
        this.fallback = fallback;
    }

    public String[] messages() {
        return fallback;
    }

    public String message(int index) {
        return fallback[index];
    }

    public static DeathCause fromBukkitCause(DamageCause cause) {
        if (cause == null) {
            return NONE;
        }
        switch (cause) {
            case CONTACT:
                return CACTUS;
            case FALL:
                return FALLING;
            case FIRE_TICK:
                return BURNING;
            case PROJECTILE:
                return PROJECTILE_ARROW;
            default:
                try {
                    return DeathCause.valueOf(cause.name());
                } catch (IllegalArgumentException ignore) {
                    return NONE;
                }
        }
    }

    public static DeathCause fromProjectileType(EntityType type) {
        return switch (type) {
            case ARROW -> PROJECTILE_ARROW;
            case SPECTRAL_ARROW -> PROJECTILE_SPECTRAL_ARROW;
            case FIREBALL, SMALL_FIREBALL -> PROJECTILE_FIREBALL;
            case DRAGON_FIREBALL -> PROJECTILE_DRAGON_FIREBALL;
            case SNOWBALL -> PROJECTILE_SNOWBALL;
            case EGG -> PROJECTILE_EGG;
            case ENDER_PEARL -> PROJECTILE_ENDER_PEARL;
            case LLAMA_SPIT -> PROJECTILE_LLAMA_SPIT;
            case SHULKER_BULLET -> PROJECTILE_SHULKER_BULLET;
            case WITHER_SKULL -> PROJECTILE_WITHER_SKULL;
            case TRIDENT -> PROJECTILE_TRIDENT;
            default -> NONE;
        };
    }

}
