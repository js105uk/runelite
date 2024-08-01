package bot.model.bot;

import net.runelite.api.Client;
import net.runelite.api.CollisionData;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.coords.Direction;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.devtools.MovementFlag;

import java.util.*;

public class PathFinder {
	private final Client client;

	public PathFinder(Client client) {
		this.client = client;
	}

	public List<WorldPoint> find(WorldPoint destination) {
		WorldPoint start        = client.getLocalPlayer().getWorldLocation();
		Set<WorldPoint> openSet = new HashSet<>();
		openSet.add(start);
		Map<WorldPoint, WorldPoint> cameFrom    = new HashMap<>();
		Map<WorldPoint, Integer> gScore         = new HashMap<>();
		gScore.put(start, 0);
		Map<WorldPoint, Integer> fScore = new HashMap<>();
		fScore.put(start, start.distanceTo2D(destination));
		Scene scene = client.getLocalPlayer().getWorldView().getScene();
		Tile[][][] tiles = scene.getTiles();
		CollisionData collisionData = Objects.requireNonNull(client.getLocalPlayer().getWorldView().getCollisionMaps())[start.getPlane()];
		while(!openSet.isEmpty()) {
			WorldPoint current = getWorldPointWithLowestFScore(openSet, fScore);
			if(current.equals(destination)) {
				return reconstructPath(cameFrom, current);
			}
			openSet.remove(current);
			for(WorldPoint neighbour : getNeighbours(current, scene, tiles, collisionData)) {
				int tentativeGScore = gScore.get(current) + current.distanceTo2D(neighbour);
				if(!gScore.containsKey(neighbour) || tentativeGScore < gScore.get(neighbour)) {
					cameFrom.put(neighbour, current);
					gScore.put(neighbour, tentativeGScore);
					fScore.put(neighbour, tentativeGScore + neighbour.distanceTo2D(destination));
					openSet.add(neighbour);
				}
			}
		}
		return null;
	}

	private List<WorldPoint> getNeighbours(WorldPoint worldPoint, Scene scene, Tile[][][] tiles, CollisionData collisionData) {
		List<WorldPoint> result = new ArrayList<>();
		int x       = worldPoint.getX();
		int y       = worldPoint.getY();
		int plane   = worldPoint.getPlane();
		Map<Direction, WorldPoint> neighbourMap = new HashMap<>();
		neighbourMap.put(Direction.NORTH,   new WorldPoint(x, y+1, plane));
		neighbourMap.put(Direction.EAST,    new WorldPoint(x+1, y, plane));
		neighbourMap.put(Direction.SOUTH,   new WorldPoint(x, y-1, plane));
		neighbourMap.put(Direction.WEST,    new WorldPoint(x-1, y, plane));
		for(Map.Entry<Direction, WorldPoint> entry : neighbourMap.entrySet()) {
			LocalPoint localPoint = LocalPoint.fromWorld(scene, entry.getValue().getX(), entry.getValue().getY());
			if(localPoint == null) continue;
			int sceneX                      = localPoint.getSceneX();
			int sceneY                      = localPoint.getSceneY();
			Set<MovementFlag> movementFlags = MovementFlag.getSetFlags(collisionData.getFlags()[sceneX][sceneY]);
			if(isValidNeighbour(entry.getKey(), movementFlags)) {
				result.add(entry.getValue());
			}
		}
		return result;
	}

	private boolean isValidNeighbour(Direction direction, Set<MovementFlag> movementFlags) {
		if(movementFlags.isEmpty()) return true;
		if(movementFlags.contains(MovementFlag.BLOCK_MOVEMENT_FULL)) return false;
		switch (direction) {
			case NORTH:
				return !movementFlags.contains(MovementFlag.BLOCK_MOVEMENT_SOUTH);
			case SOUTH:
				return !movementFlags.contains(MovementFlag.BLOCK_MOVEMENT_NORTH);
			case EAST:
				return !movementFlags.contains(MovementFlag.BLOCK_MOVEMENT_WEST);
			case WEST:
				return !movementFlags.contains(MovementFlag.BLOCK_MOVEMENT_EAST);
		}
		return false;
	}

	private WorldPoint getWorldPointWithLowestFScore(Set<WorldPoint> openSet, Map<WorldPoint, Integer> fScore) {
		WorldPoint result = null;
		int lowestFScore = Integer.MAX_VALUE;
		for(WorldPoint worldPoint : openSet) {
			if(!fScore.containsKey(worldPoint)) continue;
			int score = fScore.get(worldPoint);
			if(score < lowestFScore) {
				result = worldPoint;
				lowestFScore = score;
			}
		}
		return result;
	}

	private List<WorldPoint> reconstructPath(Map<WorldPoint, WorldPoint> cameFrom, WorldPoint current) {
		List<WorldPoint> result = new ArrayList<>();
		result.add(current);
		while(cameFrom.containsKey(current)) {
			current = cameFrom.get(current);
			result.add(0, current);
		}
		return result;
	}
}
