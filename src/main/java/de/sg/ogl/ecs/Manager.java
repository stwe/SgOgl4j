package de.sg.ogl.ecs;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Optional;

public class Manager {

    private static final int DEFAULT_ENTITY_CAPACITY = 100;

    private final Settings settings;

    private final ArrayList<Entity> entities = new ArrayList<>();
    private final ArrayList<ArrayList<Object>> componentPools = new ArrayList<>();

    private int capacity = 0;
    private int size = 0;
    private int sizeNext = 0;

    //private boolean needsAnUpdate = false; // todo

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Manager(Settings settings) throws Exception {
        this.settings = settings;

        growTo(DEFAULT_ENTITY_CAPACITY);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Settings getSettings() {
        return settings;
    }

    //-------------------------------------------------
    // Entities
    //-------------------------------------------------

    public int createEntity() throws Exception {
        growIfNeeded();

        var freeIndex = sizeNext++;

        var entity = entities.get(freeIndex);
        entity.alive = true;
        entity.bitSet.clear();

        return freeIndex;
    }

    public boolean isEntityAlive(int entityId) {
        return getEntity(entityId).alive;
    }

    public void killEntity(int entityId) {
        getEntity(entityId).alive = false;
        getEntity(entityId).bitSet.clear();
    }

    public void update() {
        if (sizeNext == 0) {
            size = 0;

            return;
        }

        size = sizeNext = arrangeAliveEntitiesToLeft();
    }

    public int getEntityCount() {
        return size;
    }

    public void printState(boolean printEntities) {
        System.out.println("size (entity count): " + size);
        System.out.println("sizeNext: " + sizeNext);
        System.out.println("capacity: " + capacity);

        if (printEntities) {
            for (var i = 0; i < sizeNext; i++) {
                if (getEntity(i).alive) {
                    System.out.println("A");
                } else {
                    System.out.println("D");
                }
            }
        }

        System.out.println("");
    }

    private int arrangeAliveEntitiesToLeft() {
        int iD = 0;
        int iA = sizeNext - 1;

        while (true)
        {
            // Find first dead entity from the left.
            for (; true; iD++)
            {
                if (iD > iA) {
                    return iD;
                }

                if (!entities.get(iD).alive) {
                    break;
                }
            }

            // Find first alive entity from the right.
            for (; true; iA--)
            {
                if (entities.get(iA).alive) {
                    break;
                }

                if (iA <= iD) {
                    return iD;
                }
            }

            // We have found two entities that need to be swapped.
            if (entities.get(iA).alive && !entities.get(iD).alive) {
                Collections.swap(entities, iA, iD);
            } else {
                throw new RuntimeException("Invalid entities.");
            }

            iD++; iA--;
        }
    }

    private Entity getEntity(int entityId) {
        return entities.get(entityId);
    }

    //-------------------------------------------------
    // Component
    //-------------------------------------------------

    public <T> Optional<T> addComponent(int entityId, Class<T> componentType) throws Exception {
        if (!isEntityAlive(entityId)) {
            System.out.println("[Warning in addComponent()] The entity " + entityId + " is dead.");
            return Optional.empty();
        }

        if (hasComponent(entityId, componentType)) {
            System.out.println("[Warning in addComponent()] The component already exists for the entity " + entityId + ".");
            return Optional.empty();
        }

        var entity = getEntity(entityId);
        entity.bitSet.set(settings.getComponentId(componentType));

        var pool = componentPools.get(settings.getComponentId(componentType));
        var component = newInstance(componentType);
        pool.set(entityId, component);

        return Optional.of(component);
    }

    public <T> boolean hasComponent(int entityId, Class<T> componentType) {
        if (!isEntityAlive(entityId)) {
            System.out.println("[Warning in hasComponent()] The entity " + entityId + " is dead.");
        }

        return getEntity(entityId).bitSet.get(settings.getComponentId(componentType));
    }

    public <T> void deleteComponent(int entityId, Class<T> componentType) {
        if (!isEntityAlive(entityId)) {
            System.out.println("[Warning in deleteComponent()] The entity " + entityId + " is dead.");
        }

        getEntity(entityId).bitSet.clear(settings.getComponentId(componentType));
    }

    public <T> Optional<T> getComponent(int entityId, Class<T> componentType) {
        if (!isEntityAlive(entityId)) {
            System.out.println("[Warning in getComponent()] The entity " + entityId + " is dead.");
            return Optional.empty();
        }

        if (hasComponent(entityId, componentType)) {
            var component = componentType.cast(componentPools.get(settings.getComponentId(componentType)).get(entityId));
            return Optional.of(component);
        }

        System.out.println("[Warning in getComponent()] The component does not exist for the entity " + entityId + ".");

        return Optional.empty();
    }

    //-------------------------------------------------
    // Iterate
    //-------------------------------------------------

    public boolean matchesSignature(int entityId, String signatureId) {
        if (!isEntityAlive(entityId)) {
            System.out.println("[Warning in matchesSignature()] The entity " + entityId + " is dead.");
        }

        var bitset = new BitSet();
        bitset= (BitSet) getEntity(entityId).bitSet.clone();
        var signatureBitset = settings.getSignature(signatureId).getBitSet();

        bitset.and(signatureBitset);

        return bitset.equals(signatureBitset);
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private static <T> T newInstance(Class<T> type) throws Exception {
        return type.getConstructor().newInstance();
    }

    private void growIfNeeded() throws Exception {
        if (capacity > sizeNext) {
            return;
        }

        growTo((capacity + 16) * 2);
    }

    private void growTo(int newCapacity) throws Exception {
        // resize entities array
        for (var i = capacity; i < newCapacity; i++) {
            var entity = new Entity();
            entity.id = i;
            entity.alive = false;
            entity.bitSet.clear();
            entities.add(i, entity);
        }

        // if necessary, initialize component pools
        if (componentPools.isEmpty()) {
            for (int i = 0; i < settings.getComponentTypes().size(); i++) {
                componentPools.add(new ArrayList<>());
            }
        }

        // resize component pools
        for (int p = 0; p < settings.getComponentTypes().size(); p++) {
            var pool= componentPools.get(p);

            for (var i = capacity; i < newCapacity; i++) {
                pool.add(newInstance(settings.getComponentTypes().get(p)));
            }
        }

        capacity = newCapacity;
    }
}
