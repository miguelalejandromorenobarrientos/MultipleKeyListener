package mkl;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;

/**
 * Key listener to handle multiple key events  
 * @author Miguel Alejandro Moreno Barrientos, @2016
 * @version 0.1
 */
public class MultipleKeyListener implements KeyListener, FocusListener
{
	private List<KeyEvent> currentKeys = new LinkedList<>();

	/**
	 * Create new MultipleKeyListener and optionally add one or more sources
	 * @param sources source component/s
	 */
	public MultipleKeyListener( Component... sources )
	{
		setSource( sources );
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		if ( currentKeys
			 .stream()
			 .noneMatch( ke -> ke.getExtendedKeyCode() == e.getExtendedKeyCode()
							   && ke.getKeyLocation() == e.getKeyLocation() ) )
			currentKeys.add( e );
	}
	
	@Override
	public void keyReleased(KeyEvent e) 
	{
		currentKeys
		.stream()
		.filter( ke -> ke.getExtendedKeyCode() == e.getExtendedKeyCode()
					   && ke.getKeyLocation() == e.getKeyLocation() )
		.findFirst()
		.ifPresent( currentKeys::remove );
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void focusLost(FocusEvent e)
	{
		if ( e.getOppositeComponent() != null  )
			for ( KeyListener kl : e.getOppositeComponent().getKeyListeners() )
				if ( kl instanceof MultipleKeyListener && kl != this )
					( (MultipleKeyListener) kl ).currentKeys = 
												new LinkedList<>( currentKeys );
		
		clear();
	}
	
	@Override
	public void focusGained( FocusEvent e ) {}

	/**
	 * Set listener source/s. Equivalent to addKeyListener and addFocusListener
	 * @param sources listener source/s
	 */
	public void setSource( Component... sources )
	{
		for ( Component c : sources )
		{
			c.addKeyListener( this );
			c.addFocusListener( this );
		}
	}
	
	/**
	 * Get array with current non-released key events in chronological order
	 * @return array with current key events
	 */
	public KeyEvent[] getCurrentKeyEvents()
	{
		return currentKeys
			   .stream()
			   .toArray( KeyEvent[]::new );
	}
	
	/**
	 * Get keys pressed count
	 * @return number of pressed keys
	 */
	public int keysPressedCount() { return currentKeys.size(); }
	
	/**
	 * Check if there is any pressed key
	 * @return true if key memory is empty
	 */
	public boolean isEmpty() { return currentKeys.isEmpty(); }
	
	/**
	 * Check if a key given by key code is pressed
	 * @param keyCode key event key code
	 * @return true if is pressed
	 */
	public boolean isPressed( int keyCode )
	{
		return currentKeys
			   .stream()
			   .anyMatch( ke -> ke.getKeyCode() == keyCode );
	}

	/**
	 * Check if a key given by key code and key location is pressed
	 * @param keyCode key event key code
	 * @param keyLocation key event key location
	 * @return true if is pressed
	 */
	public boolean isPressed( int keyCode, int keyLocation )
	{
		return currentKeys
			   .stream()
			   .anyMatch( ke -> ke.getKeyCode() == keyCode
					   			&& ke.getKeyLocation() == keyLocation );
	}
	
	/**
	 * Clear all keys
	 */
	public void clear()
	{
		currentKeys = new LinkedList<>();
	}
	
	/**
	 * Check if Control is pressed
	 * @return true if Control (left or right) is pressed
	 */
	public boolean isControlDown()
	{
		return isPressed( KeyEvent.VK_CONTROL );
	}
	
	/**
	 * Check if Shift is pressed
	 * @return true if Shift (left or right) is pressed
	 */
	public boolean isShiftDown()
	{
		return isPressed( KeyEvent.VK_SHIFT );
	}
	
	/**
	 * Check if Alt is pressed
	 * @return true if Alt is pressed
	 */
	public boolean isAltDown()
	{
		return isPressed( KeyEvent.VK_ALT );
	}
	
	/**
	 * Return a string representation of the pressed keys with location
	 * @return pressed keys as string 
	 */
	public String toStringLocation()
	{
		return String.join( "+", currentKeys
				.stream()
				.map( ke -> KeyEvent.getKeyText( 
						ke.getKeyCode() ) + "(" + ke.getKeyLocation() + ")" )
				.toArray( String[]::new ) );
	}
	
	@Override
	public String toString()
	{
		return String.join( "+", currentKeys
			   .stream()
			   .map( ke -> KeyEvent.getKeyText( ke.getKeyCode() ) )
			   .toArray( String[]::new ) );
	}
}
