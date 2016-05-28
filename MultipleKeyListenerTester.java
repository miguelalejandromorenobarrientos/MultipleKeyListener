package mkl;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 * Test MultipleKeyListener using three JTextField.
 * Use TAB or mouse to change focus 
 * @author Miguel Alejandro Moreno Barrientos
 */
@SuppressWarnings("serial")
public class MultipleKeyListenerTester extends JFrame
{
	public MultipleKeyListenerTester()
	{
		super( "Test MultipleKeyListener" );
		setLayout( new GridLayout( 3, 1 ) );
		
		JTextField tf1 = new JTextField();
		add( tf1 );
		JTextField tf2 = new JTextField();
		add( tf2 );
		JTextField tf3 = new JTextField();
		add( tf3 );

		new MyListener().setSource( tf1 );
		// equivalent to
		//MyListener listener = new MyListener();
		//tf1.addKeyListener( listener );
		//tf1.addFocusListener( listener );
		new MyListener().setSource( tf2 );
		new MyListener().setSource( tf3 );
		
		setPreferredSize( new Dimension( 400, 150 ) );
		setResizable( false );
		pack();
		setLocationRelativeTo( null );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
	
	private class MyListener extends MultipleKeyListener
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			super.keyPressed(e);
			if ( e.getSource() instanceof JTextComponent )
				( ( JTextComponent ) e.getSource() ).setText( toString() );
			( (Component) e.getSource() ).repaint();
		}
		@Override
		public void keyReleased(KeyEvent e)
		{
			super.keyReleased(e);
			if ( e.getSource() instanceof JTextComponent )
				( ( JTextComponent ) e.getSource() ).setText( toString() );
		}
		@Override
		public void keyTyped(KeyEvent e)
		{
			e.consume();  // avoid extra typed chars in JTextComponent
		}
		@Override
		public void focusLost(FocusEvent e)
		{
			super.focusLost(e);
			if ( e.getSource() instanceof JTextComponent )
				( ( JTextComponent ) e.getSource() ).setText( toString() );
		}
		@Override
		public void focusGained(FocusEvent e) {
			super.focusGained(e);
			if ( e.getSource() instanceof JTextComponent )
				( ( JTextComponent ) e.getSource() ).setText( toString() );
		}
	}
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater( 
					() -> new MultipleKeyListenerTester().setVisible( true ) );
	}
}
