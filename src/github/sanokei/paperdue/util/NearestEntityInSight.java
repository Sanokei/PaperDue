package github.sanokei.paperdue.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;

/*
 * Code I did not write. No idea how it works.
 * I only wronte the method for {armor stands} any entitiy
 */

public class NearestEntityInSight {
	public static int getATTACK_REACH(){
		final int ATTACK_REACH = 4; 
		return ATTACK_REACH;
	}
	public static Entity nearestEntityInSight(Player player, Class<Entity> entity) {
		Location observerPos = player.getEyeLocation();
		Vector3D observerDir = new Vector3D(observerPos.getDirection());
		
		Vector3D observerStart = new Vector3D(observerPos);
		Vector3D observerEnd = observerStart.add(observerDir.multiply(getATTACK_REACH()));
	
		Entity nearestTarget = null;
		
		for (Entity target : player.getWorld().getEntitiesByClass(entity)) {
			// Bounding box of the given player
			Vector3D targetPos = new Vector3D(target.getLocation());
			Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
			Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);
	
			if (hasIntersection(observerStart, observerEnd, minimum, maximum)) {
				if (nearestTarget == null || nearestTarget.getLocation().distanceSquared(observerPos) > target.getLocation().distanceSquared(observerPos)) {
					nearestTarget = target;
				}
			}
		}
		return nearestTarget;
		
	}
	public static Player nearestPlayerInSight(Player player){
		Location observerPos = player.getEyeLocation();
		Vector3D observerDir = new Vector3D(observerPos.getDirection());
	
		Vector3D observerStart = new Vector3D(observerPos);
		Vector3D observerEnd = observerStart.add(observerDir.multiply(getATTACK_REACH()));
	
		Player nearestTarget = null;
	
		// Get nearby entities
		for (Player target : player.getWorld().getPlayers()) {
			// Bounding box of the given player
			Vector3D targetPos = new Vector3D(target.getLocation());
			Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
			Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);
	
			if (target != player && hasIntersection(observerStart, observerEnd, minimum, maximum)) {
				if (nearestTarget == null || nearestTarget.getLocation().distanceSquared(observerPos) > target.getLocation().distanceSquared(observerPos)) {
					nearestTarget = target;
				}
			}
		}
		return nearestTarget;
	}
	
	//Source:
	// [url]http://www.gamedev.net/topic/338987-aabb---line-segment-intersection-test/[/url]
	private static boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
	    final double epsilon = 0.0001f;
	
	    Vector3D d = p2.subtract(p1).multiply(0.5);
	    Vector3D e = max.subtract(min).multiply(0.5);
	    Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
	    Vector3D ad = d.abs();
	
	    if (Math.abs(c.x) > e.x + ad.x)
	        return false;
	    if (Math.abs(c.y) > e.y + ad.y)
	        return false;
	    if (Math.abs(c.z) > e.z + ad.z)
	        return false;
	
	    if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon)
	        return false;
	    if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon)
	        return false;
	    if (Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + epsilon)
	        return false;
	
	    return true;
	}
}
