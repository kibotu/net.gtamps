package net.gtamps.android.core.input.listener;

import java.util.Vector;

import net.gtamps.android.core.input.event.InputEventListener;
import net.gtamps.android.game.content.scenes.World;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Vector3;
import net.gtamps.shared.serializer.communication.SendableType;

public class CameraListener implements InputEventListener{
	World world;
	public CameraListener(World world){
		this.world = world; 
	}
	 
	@Override
	public void onSendableRetrieve(SendableType sendableType) {
		Logger.d(this, "Received: "+sendableType.toString());
		// TODO Auto-generated method stub
		Vector3 temp3 = Vector3.createNew();
//         Utils.log(TAG, "is dragging");
         Vector3 viewportSize = world.getActiveCamera().getViewportSize();
         Vector3 pos = Vector3.createNew(0,0,0);;
         if(sendableType.equals(SendableType.ACTION_ACCELERATE)){
        	 pos = Vector3.createNew(0,1,0);	 
         }
         if(sendableType.equals(SendableType.ACTION_DECELERATE)){
        	 pos = Vector3.createNew(0,-1,0);	 
         }
         if(sendableType.equals(SendableType.ACTION_LEFT)){
        	 pos = Vector3.createNew(1,0,0);	 
         }
         if(sendableType.equals(SendableType.ACTION_RIGHT)){
        	 pos = Vector3.createNew(-1,0,0);	 
         }
          
        		 //inputEngine.getPointerPosition();
         
         Vector3 temp = pos.sub(viewportSize).mulInPlace(1).addInPlace(viewportSize);
         //hud.getCursor().setPosition(temp);
         //world.getActiveView().getObject3d().getPosition().addInPlace(temp);
         world.getActiveCamera().move(temp);

         //float angle = Vector3.XAXIS.angleInBetween(pos) - 90;
         //world.getActiveView().getObject3d().setRotation(0, 0, angle);
         //hud.getRing().setRotation(0, 0, angle);

         Vector3 temp2 = Vector3.createNew(temp);
         temp2.normalize();
         temp2.invert();
         temp2.mulInPlace(40);

         Vector3 camPos = world.getActiveCamera().getPosition();
         temp3.set(temp2.x, temp2.y, camPos.z).addInPlace(world.getActiveView().getObject3d().getPosition());

         // send driving impulses
         //fireImpulse(angle, temp);

//         temp.recycle();
         world.getActiveCamera().setPosition(temp3);
     
     world.getActiveCamera().setTarget(world.getActiveView().getObject3d().getPosition());
	}
}
