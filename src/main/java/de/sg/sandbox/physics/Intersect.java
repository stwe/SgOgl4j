package de.sg.sandbox.physics;

import org.joml.Vector2f;

public class Intersect {

    public static boolean aabbIntersectsAabb(Aabb one, Aabb two) {
        if (one.max.x < two.min.x || one.min.x > two.max.x) {
            return false;
        }

        if (one.max.y < two.min.y || one.min.y > two.max.y) {
            return false;
        }

        return true;
    }

    public static Collision circleIntersectsAabb(Circle circle, Aabb aabb) {
        var closestPoint = closestPointOnAabb(circle.center, aabb);

        var v = new Vector2f(closestPoint).sub(circle.center);
        var diff = new Vector2f(v);
        var dot = v.dot(v);

        if (dot <= circle.radius * circle.radius) {
            return new Collision(
                    true,
                    vectorDirection(diff),
                    diff
            );
        }

        return new Collision(
                false,
                Collision.Direction.UP,
                new Vector2f(0.0f)
        );
    }

    private static Collision.Direction vectorDirection(Vector2f target) {
        Vector2f[] compass = new Vector2f[]{
                new Vector2f(0.0f, 1.0f),
                new Vector2f(1.0f, 0.0f),
                new Vector2f(0.0f, -1.0f),
                new Vector2f(-1.0f, 0.0f)
        };

        var max = 0.0f;
        var best = -1;

        for (int i = 0; i < 4; i++) {
            var normalTarget = new Vector2f(target).normalize();
            var dot = new Vector2f(normalTarget).dot(compass[i]);
            if (dot > max) {
                max = dot;
                best = i;
            }
        }

        return Collision.Direction.fromInt(best);
    }

    public static Vector2f closestPointOnAabb(Vector2f givenPoint, Aabb aabb) {
        var result = new Vector2f();

        var x = givenPoint.x;
        var y = givenPoint.y;

        if (x < aabb.min.x) {
            x = aabb.min.x;
        }

        if (x > aabb.max.x) {
            x = aabb.max.x;
        }

        if (y < aabb.min.y) {
            y = aabb.min.y;
        }

        if (y > aabb.max.y) {
            y = aabb.max.y;
        }

        result.x = x;
        result.y = y;

        return result;
    }

    private Intersect() {}
}
