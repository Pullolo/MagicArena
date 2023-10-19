package net.pullolo.magicarena.players;

import net.pullolo.magicarena.game.Game;
import org.bukkit.entity.Entity;

public class DungeonEntity extends ArenaEntity{

    private boolean hasBossKey = false;
    private boolean hasWitherKey = false;

    public DungeonEntity(Entity entity, int level, Game game, boolean isTester) {
        super(entity, level, game, isTester);
    }

    public boolean hasBossKey() {
        return hasBossKey;
    }

    public DungeonEntity setBossKey(boolean hasBossKey) {
        this.hasBossKey = hasBossKey;
        return this;
    }

    public boolean hasWitherKey() {
        return hasWitherKey;
    }

    public DungeonEntity setWitherKey(boolean hasWitherKey) {
        this.hasWitherKey = hasWitherKey;
        return this;
    }
}
