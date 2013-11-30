package gtamapedit.preview;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.security.auth.Destroyable;

import processing.core.PApplet;

public class ProcessingPreviewFrame extends Frame{
	ProcessingPreview embed;
	public ProcessingPreviewFrame() {
        super("Embedded PApplet");

        setLayout(new BorderLayout());
        setSize(1024, 768);
        setVisible(true);
        
        
        embed = new ProcessingPreview();
        
        addWindowListener(new WindowAdapter(){
      	  public void windowClosing(WindowEvent we){
      		  embed.stop();
      		  //embed.destroy();
      		  setVisible(false);
      	  }
        });
        addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				embed.mouseWheelMoved(e.getWheelRotation());
			}
		});
        
        add(embed, BorderLayout.CENTER);

        embed.init();
        
    }
}
