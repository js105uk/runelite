package bot.model.bot;

import bot.Common;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.NPC;

import java.awt.*;
import java.awt.event.MouseEvent;

@Getter
@Setter
public class Bot {
	private Input input;
	private Walker walker;

	public boolean interact(NPC npc) {
		// Check if NPC is null
		if(npc == null) {
			// Return false
			return false;
		}

		// Check if the shape is null
		Shape shape = npc.getConvexHull();
		if(shape == null) {
			// Return false
			return false;
		}

		// Get a coordinate to click on within the shape
		Rectangle bounds = shape.getBounds();
		Point randomPoint = null;
		do {
			int randomX = Common.generateRandomNumber(bounds.x, bounds.x + bounds.width);
			int randomY = Common.generateRandomNumber(bounds.y, bounds.y + bounds.height);
			randomPoint = new Point(randomX, randomY);
		} while (!shape.contains(randomPoint));

		// Click on the coordinate
		input.getMouse().click(randomPoint.x, randomPoint.y, MouseEvent.BUTTON1);

		// Return whether the NPC's shape is not null AND it contains the mouse cursor,
		// i.e. did the bot click within the target area?
		shape = npc.getConvexHull();
		return shape != null && shape.contains(input.getMouse().getPosition());
	}
}