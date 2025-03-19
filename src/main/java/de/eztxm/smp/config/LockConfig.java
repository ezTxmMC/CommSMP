package de.eztxm.smp.config;

import de.eztxm.ezlib.config.annotation.JsonClassConfig;
import de.eztxm.ezlib.config.annotation.JsonClassElement;
import de.eztxm.smp.config.types.LockObject;

import java.util.ArrayList;
import java.util.UUID;

@JsonClassConfig
public class LockConfig {

    @JsonClassElement
    private UUID uniqueId;
    @JsonClassElement
    private ArrayList<LockObject> lockObjects;

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setLockObjects(ArrayList<LockObject> lockObjects) {
        this.lockObjects = lockObjects;
    }

    public void addLockObject(LockObject lockObject) {
        this.lockObjects.add(lockObject);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public ArrayList<LockObject> getLockObjects() {
        return lockObjects;
    }
}
