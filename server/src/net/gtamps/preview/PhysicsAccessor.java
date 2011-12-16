package net.gtamps.preview;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class PhysicsAccessor {

	private final World world;
	
	public PhysicsAccessor(final World world) {
		if (world == null) {
			throw new IllegalArgumentException("'var' must not be 'null'");
		}
		this.world = world;
	}
	
	
	public Iterator<Body> bodyIterator() {
		return new Iterator<Body>() {
			
			Body current = world.getBodyList();

			@Override
			public boolean hasNext() {
				return current.getNext() != null;
			}

			@Override
			public Body next() {
				if (current == null) {
					throw new NoSuchElementException();
				}
				final Body tmp = current;
				current = current.getNext();
				return tmp;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
		};
	}
	
	

}
