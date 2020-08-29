package de.sg.sandbox.physics;

import org.joml.Vector2f;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class IntersectTest {

    @Test
    void aabbIntersectsAabb() {
        var aabb0 = new Aabb();
        aabb0.min = new Vector2f(0.0f, 1.0f);
        aabb0.max = new Vector2f(1.0f, 0.0f);

        var aabb1 = new Aabb();
        aabb1.min = new Vector2f(1.0f, 2.0f);
        aabb1.max = new Vector2f(2.0f, 1.0f);

        assertThat(Intersect.aabbIntersectsAabb(aabb0, aabb1), is(false));
    }

    @Test
    void circleIntersectsAabb() {
        var aabb = new Aabb();
        aabb.min = new Vector2f(0.0f, 0.0f);
        aabb.max = new Vector2f(1.0f, 1.0f);

        var circle = new Circle();
        circle.center = new Vector2f(0.5f, 1.5f);
        circle.radius = 0.5f;

        var res = Intersect.circleIntersectsAabb(circle, aabb);

        assertThat(res, is(true));
    }

    @Test
    void closestPointOnAabb() {
    }
}
