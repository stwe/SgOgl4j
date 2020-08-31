package de.sg.ogl.ecs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class HealthComponent {
    public HealthComponent() {}
}

class InputComponent {
    public InputComponent() {}
}

class MeshComponent {
    public MeshComponent() {}
}

class VelocityComponent {
    public VelocityComponent() {}
}

class ManagerTest {

    private static Manager manager;

    @BeforeEach
    void setUp() throws Exception {
        // components
        ArrayList<Class<?>> componentTypes = new ArrayList<>();
        componentTypes.add(HealthComponent.class);
        componentTypes.add(InputComponent.class);
        componentTypes.add(MeshComponent.class);
        componentTypes.add(VelocityComponent.class);

        // signatures
        Signature healthInputSig = new Signature(HealthComponent.class, InputComponent.class);
        Signature meshVelocitySig = new Signature(MeshComponent.class, VelocityComponent.class);

        var signatures = new HashMap<String, Signature>();
        signatures.put("healthInputSignature", healthInputSig);
        signatures.put("meshVelocitySignature", meshVelocitySig);

        // manager
        manager = new Manager(new Settings(componentTypes, signatures));
    }

    @AfterEach
    void tearDown() {
        manager.printState(false);
    }

    @Test
    void createEntity() throws Exception {
        var e0 = manager.createEntity();
        var e1 = manager.createEntity();
        var e2 = manager.createEntity();
        var e3 = manager.createEntity();
        var e4 = manager.createEntity();
        var e5 = manager.createEntity();

        assertThat(e0, is(0));
        assertThat(e1, is(1));
        assertThat(e2, is(2));
        assertThat(e3, is(3));
        assertThat(e4, is(4));
        assertThat(e5, is(5));
    }

    @Test
    void isEntityAlive() throws Exception {
        var e0 = manager.createEntity();
        var e1 = manager.createEntity();
        var e2 = manager.createEntity();
        var e3 = manager.createEntity();
        var e4 = manager.createEntity();
        var e5 = manager.createEntity();

        assertThat(manager.isEntityAlive(e0), is(true));
        assertThat(manager.isEntityAlive(e1), is(true));
        assertThat(manager.isEntityAlive(e2), is(true));
        assertThat(manager.isEntityAlive(e3), is(true));
        assertThat(manager.isEntityAlive(e4), is(true));
        assertThat(manager.isEntityAlive(e5), is(true));
    }

    @Test
    void killEntity() throws Exception {
        var e0 = manager.createEntity();
        var e1 = manager.createEntity();
        var e2 = manager.createEntity();
        var e3 = manager.createEntity();
        var e4 = manager.createEntity();
        var e5 = manager.createEntity();

        assertThat(manager.isEntityAlive(e0), is(true));
        assertThat(manager.isEntityAlive(e1), is(true));
        assertThat(manager.isEntityAlive(e2), is(true));
        assertThat(manager.isEntityAlive(e3), is(true));
        assertThat(manager.isEntityAlive(e4), is(true));
        assertThat(manager.isEntityAlive(e5), is(true));

        manager.killEntity(e1);
        manager.killEntity(e3);
        manager.killEntity(e5);

        assertThat(manager.isEntityAlive(e1), is(false));
        assertThat(manager.isEntityAlive(e3), is(false));
        assertThat(manager.isEntityAlive(e5), is(false));
    }

    @Test
    void update() throws Exception {
        var e0 = manager.createEntity();
        var e1 = manager.createEntity();
        var e2 = manager.createEntity();
        var e3 = manager.createEntity();
        var e4 = manager.createEntity();
        var e5 = manager.createEntity();

        manager.killEntity(e1);
        manager.killEntity(e3);
        manager.killEntity(e5);

        assertThat(manager.getEntityCount(), is(0));

        manager.update();

        assertThat(manager.getEntityCount(), is(3));
    }

    @Test
    void getEntityCount() throws Exception {
        var e0 = manager.createEntity();
        var e1 = manager.createEntity();
        var e2 = manager.createEntity();
        var e3 = manager.createEntity();
        var e4 = manager.createEntity();
        var e5 = manager.createEntity();

        manager.killEntity(e1);
        manager.killEntity(e3);
        manager.killEntity(e5);

        manager.update();

        assertThat(manager.getEntityCount(), is(3));
    }

    @Test
    void addComponent() throws Exception {
        // create entity
        var e0 = manager.createEntity();

        // add two components
        var healthComponentOpt = manager.addComponent(e0, HealthComponent.class);
        assertThat(healthComponentOpt.isPresent(), is(true));

        var healthComponent = healthComponentOpt.get();
        assertThat(healthComponent, isA(HealthComponent.class));

        var inputComponentOpt0 = manager.addComponent(e0, InputComponent.class);
        assertThat(inputComponentOpt0.isPresent(), is(true));

        var inputComponent = inputComponentOpt0.get();
        assertThat(inputComponent, isA(InputComponent.class));

        // try adding the InputComponent again
        var inputComponentOpt1 = manager.addComponent(e0, InputComponent.class);
        assertThat(inputComponentOpt1.isPresent(), is(false));

        // kill the entity and try to add a component
        manager.killEntity(e0);
        var inputComponentOpt2 = manager.addComponent(e0, InputComponent.class);
        assertThat(inputComponentOpt2.isPresent(), is(false));
    }

    @Test
    void hasComponent() throws Exception {
        // create entity
        var e0 = manager.createEntity();

        // add HealthComponent
        var healthComponentOpt = manager.addComponent(e0, HealthComponent.class);
        assertThat(healthComponentOpt.isPresent(), is(true));

        // hasComponent should return true
        assertThat(manager.hasComponent(e0, HealthComponent.class), is(true));

        // kill the entity
        manager.killEntity(e0);

        // hasComponent should return false
        assertThat(manager.hasComponent(e0, HealthComponent.class), is(false));
    }

    @Test
    void deleteComponent() throws Exception {
        // create entity
        var e0 = manager.createEntity();

        // add HealthComponent
        var healthComponentOpt = manager.addComponent(e0, HealthComponent.class);
        assertThat(healthComponentOpt.isPresent(), is(true));

        // delete the HealthComponent
        manager.deleteComponent(e0, HealthComponent.class);

        // hasComponent should return false
        assertThat(manager.hasComponent(e0, HealthComponent.class), is(false));

        // kill the entity
        manager.killEntity(e0);

        // delete the HealthComponent again - possible, but with warning message
        manager.deleteComponent(e0, HealthComponent.class);

        // hasComponent should return false
        assertThat(manager.hasComponent(e0, HealthComponent.class), is(false));
    }

    @Test
    void getComponent() throws Exception {
        // create entity
        var e0 = manager.createEntity();

        // add HealthComponent
        manager.addComponent(e0, HealthComponent.class);

        // get HealthComponent
        var healthComponentOpt0= manager.getComponent(e0, HealthComponent.class);
        assertThat(healthComponentOpt0.isPresent(), is(true));

        // get InputComponent - shouldn't be possible
        var inputComponentOpt= manager.getComponent(e0, InputComponent.class);
        assertThat(inputComponentOpt.isPresent(), is(false));

        // kill the entity
        manager.killEntity(e0);

        // get HealthComponent shouldn't be possible now
        var healthComponentOpt1= manager.getComponent(e0, HealthComponent.class);
        assertThat(healthComponentOpt1.isPresent(), is(false));
    }

    @Test
    void getEntities() throws Exception {
        for (int i = 0; i < 10; i++) {
            var e = manager.createEntity();

            manager.addComponent(e, HealthComponent.class);
            manager.addComponent(e, MeshComponent.class);
        }

        for (int i = 0; i < 10; i++) {
            var e = manager.createEntity();

            manager.addComponent(e, HealthComponent.class);
            manager.addComponent(e, InputComponent.class);
        }

        for (int i = 0; i < 10; i++) {
            var e = manager.createEntity();

            manager.addComponent(e, MeshComponent.class);
        }

        manager.update();

        var entitiesMeshComp = manager.getEntities(MeshComponent.class);
        var entitiesInputComp = manager.getEntities(InputComponent.class);
        assertThat(entitiesMeshComp.size(), is(20));
        assertThat(entitiesInputComp.size(), is(10));

        var entitiesMeshVelSig = manager.getEntities("meshVelocitySignature");
        assertThat(entitiesMeshVelSig.size(), is(0));

        var entitiesHealthInSig = manager.getEntities("healthInputSignature");
        assertThat(entitiesHealthInSig.size(), is(10));
    }

    @Test
    void matchesSignature() throws Exception {
        // create entity
        var e0 = manager.createEntity();

        // add components
        manager.addComponent(e0, HealthComponent.class);
        manager.addComponent(e0, MeshComponent.class);
        manager.addComponent(e0, InputComponent.class);

        assertThat(manager.matchesSignature(e0, "healthInputSignature"), is(true));

        // kill the entity
        manager.killEntity(e0);

        // matchesSignature should return false
        assertThat(manager.matchesSignature(e0, "healthInputSignature"), is(false));
    }
}
