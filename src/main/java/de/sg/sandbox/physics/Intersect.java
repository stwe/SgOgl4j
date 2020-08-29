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

    public static boolean circleIntersectsAabb(Circle circle, Aabb aabb) {
        var closestPoint = closestPointOnAabb(circle.center, aabb);

        //System.out.println("x: " + closestPoint.x + ", y: " + closestPoint.y);

        var v = new Vector2f(closestPoint).sub(circle.center);
        var dot = v.dot(v);

        return dot <= circle.radius * circle.radius;
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
