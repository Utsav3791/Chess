package chess;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import javax.swing.JComponent;

// Pad is a class that represents a chess board. 
public class Pad
extends Widget {
    private final HashMap<String, Handler> eventMap = new HashMap();
    private static final String[] eventNames = new String[]{"press", "release", "click", "enter", "exit", "drag", "move"};
    private Dimension minimumSize;
    private Dimension preferredSize;
    private Dimension maximumSize;

    protected Pad() {
        this.me = new PadComponent();
        for (String event : eventNames) {
            this.eventMap.put(event, null);
        }
    }
    // setSize() sets the size of the Pad.
    protected void setSize(int width, int height) {
        this.me.setSize(width, height);
    }
    //setPreferredSize() sets the preferred size of the Pad.
    protected void setPreferredSize(int width, int height) {
        this.preferredSize = new Dimension(width, height);
        this.me.invalidate();
    }
    //setMinimumSize() sets the minimum size of the Pad.
    protected void setMinimumSize(int width, int height) {
        this.minimumSize = new Dimension(width, height);
        this.me.invalidate();
    }
    //setMaximumSize() sets the maximum size of the Pad.
    protected void setMaximumSize(int width, int height) {
        this.maximumSize = new Dimension(width, height);
        this.me.invalidate();
    }
    //getWidth() returns the width of the Pad.
    public int getWidth() {
        return this.me.getWidth();
    }
    //getHeight() returns the height of the Pad.
    public int getHeight() {
        return this.me.getHeight();
    }
    //repaint() repaints the Pad.
    public void repaint() {
        this.me.repaint();
    }
    
    //setMouseHandler() sets the handler for a mouse event.
    protected void setMouseHandler(String event, Object receiver, String funcName) {
        if (!this.eventMap.containsKey(event)) {
            throw new IllegalArgumentException("Unknown event: " + event);
        }
        this.eventMap.put(event, new Handler(funcName, receiver));
        if (event.equals("drag") || event.equals("move")) {
            if (this.me.getMouseMotionListeners().length == 0) {
                this.me.addMouseMotionListener((MouseMotionListener)((Object)this.me));
            }
        } else if (this.me.getMouseListeners().length == 0) {
            //addMouseListener() adds a MouseListener to the Pad.
            this.me.addMouseListener((MouseListener)((Object)this.me));
        }
    }

    protected void setMouseHandler(String event, String funcName) {
        this.setMouseHandler(event, this, funcName);
    }

    protected void paintComponent(Graphics2D g) {
    }

    private void handle(String kind, MouseEvent e) {
        Handler h = this.eventMap.get(kind);
        if (h == null) {
            return;
        }
        try {
            h.func.invoke(h.receiver, e);
        }
        catch (IllegalAccessException excp) {
            System.err.printf("not allowed to call %s.%n", h.func.getName());
        }
        catch (InvocationTargetException excp) {
            System.err.printf("call to %s caused exception: %s.%n", h.func.getName(), excp.getCause());
        }
    }

    private static class Handler {
        Method func;
        Object receiver;

        Handler(String funcName, Object receiver) {
            this.receiver = receiver;
            if (funcName == null) {
                this.func = null;
            } else {
                try {
                    this.func = receiver.getClass().getDeclaredMethod(funcName, MouseEvent.class);
                    this.func.setAccessible(true);
                }
                catch (NoSuchMethodException e) {
                    throw new IllegalArgumentException("Method not found or wrong arguments: " + funcName);
                }
            }
        }
    }

    //PadComponent is a class that represents a Pad.
    private class PadComponent
    extends JComponent
    implements MouseListener,
    MouseMotionListener {
        private PadComponent() {
        }

        //paintComponent() paints the Pad.
        public void paintComponent(Graphics g) {
            Pad.this.paintComponent((Graphics2D)g);
        }

        //mouseClicked() handles a mouse click.
        public void mouseClicked(MouseEvent e) {
            Pad.this.handle("click", e);
        }

        //mouseReleased() handles a mouse release.
        public void mouseReleased(MouseEvent e) {
            Pad.this.handle("release", e);
        }

        //mousePressed() handles a mouse press.
        public void mousePressed(MouseEvent e) {
            Pad.this.handle("press", e);
        }

        //mouseEntered() handles a mouse enter.
        public void mouseEntered(MouseEvent e) {
            Pad.this.handle("enter", e);
        }

        //mouseExited() handles a mouse exit.
        public void mouseExited(MouseEvent e) {
            Pad.this.handle("exit", e);
        }

        //mouseDragged() handles a mouse drag.
        public void mouseDragged(MouseEvent e) {
            Pad.this.handle("drag", e);
        }

        //mouseMoved() handles a mouse move.
        public void mouseMoved(MouseEvent e) {
            Pad.this.handle("move", e);
        }
        //getPreferredSize() returns the preferred size of the Pad.
        public Dimension getPreferredSize() {
            if (Pad.this.preferredSize == null) {
                return super.getPreferredSize();
            }
            return Pad.this.preferredSize;
        }
        //getMinimumSize() returns the minimum size of the Pad.
        public Dimension getMinimumSize() {
            if (Pad.this.minimumSize == null) {
                return super.getMinimumSize();
            }
            return Pad.this.minimumSize;
        }
        //getMaximumSize() returns the maximum size of the Pad.
        public Dimension getMaximumSize() {
            if (Pad.this.maximumSize == null) {
                return super.getMaximumSize();
            }
            return Pad.this.maximumSize;
        }
    }
}

