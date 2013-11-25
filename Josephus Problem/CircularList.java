import java.util.Iterator;

public class CircularList implements Iterable {
    private Node first = null;
    private int size;

    /**
        Constructs a CircularList from a specified non-negative integer value n.
            The initial size of this CircularList is equal to n.
            In other words, there will be n Nodes in this CirularList.
            The data field for each Node is specified by the following sequence:  1, 2, ..., n.
            The sequence is carried out in clockwise order such that each Node's next field refers to the next Node in this sequence.
            Furthermore, each Node's previous field refers to the previous Node in the sequence to allow for counter-clockwise traversal.
            Finally, the first field of this CircularList initially refers to the Node in this CirularList that has had it's data field set to 1.
            However, if the size of this CircularList is initialized to 0, then, since it does not contain any Nodes, this CircularList's first field is null.
    */
    public CircularList ( int n ) {
    
        if ( n < 0 ) {
            throw new IllegalArgumentException();
        }
        else {
            this.size = n;
            Node link = null;
        
            for ( int i = 0; i < this.size; i++ ) {
            
                if ( i == 0 ) {
                    this.first = new Node();
                    this.first.setData( i + 1 );
                    link = this.first;
                }
                else {
                    link.setNext( new Node() );
                    link.getNext().setPrevious( link );
                    link = link.getNext();
                    link.setData( i + 1 );
                }
            }
            
            if ( link != null ) {
                link.setNext( this.first );
                first.setPrevious( link );
                link = null;
            }
        }
    }
    
    /**
        Returns the size of this CircularList.
    */
    public int size () {
        return this.size;
    }
    
    /**
        Skips to the previous Node in this CircularList, relative to the Node that is referred to by this CirularList's first field, and returns a reference to it.
            Works as long as there is at least one Node in this CircularList.
            After a skip, this CircularList's first field refers to the Node in this CircularList that was reached by the skip.
    */
    public Node skip () {
        
        if ( this.size() == 0 ) {
            throw new NullPointerException();
        }
        else {
            this.first = this.first.getPrevious();
            return this.first;
        }
    }
    
    /**
        Deletes the Node in this CircularList that is referred to by this CircularList's first field.
            Works as long as there is at least one Node in this CircularList.
            If at least one Node remains in this CircularList after deletion, this CircularList's first field refers to the Node that was referred to by the deleted Node's previous field.
            Otherwise, this CircularList's first field is null.
            Also, the size field of this CircularList is decremented by one.
    */
    public void delete () {
        
        if ( this.size == 0 ) {
            throw new NullPointerException();
        }
        else if ( this.size == 1 ) {
            this.first.setNext( null );
            this.first.setPrevious( null );
            this.first = null;
            this.size = 0;
        }
        else {
            Node link = this.first;
            this.first = link.getPrevious();
            link.getPrevious().setNext( link.getNext() );
            link.getNext().setPrevious( link.getPrevious() );
            link.setNext( null );
            link.setPrevious( null );
            link = null;
            this.size -= 1;
        }
    }
    
    /**
        Returns an Iterator over this CircularList.
    */
    public Iterator iterator () {
        return new CircularListIterator( this );
    }
}

class CircularListIterator implements Iterator {
    private CircularList list;

    /**
        Constructs an Iterator over a specified CircularList.
    */
    public CircularListIterator ( CircularList list ) {
        this.list = list;
    }

    /**
        Returns true if this CircularList contains at least one Node.
    */
    public boolean hasNext () {
        return ( this.list.size() > 0 );
    }
    
    /**
        Returns the data field of the next Node in this CircularList after wrapping it in an Integer Object.
    */
    public Object next () {
        Node first = this.list.skip();
        return new Integer( first.getData() );
    }
    
    /**
        Removes the current Node from this CircularList.
    */
    public void remove () {
        this.list.delete();
    }
}

class Node {
    private Node previous;
    private Node next;
    private int data;

    /**
        Constructs a default Node.  
            The default settings are a previous reference to null, a next reference to null, and a data entry of 0.
    */
    public Node () {
        this.previous = null;
        this.next = null;
        this.data = 0;
    }
    
    /**
        Changes the previous reference field of this Node to refer to a specified Node n.
    */
    public void setPrevious ( Node n ) {
        this.previous = n;
    }
    
    /**
        Changes the next reference field of this Node to refer to a specified Node n.
    */
    public void setNext ( Node n ) {
        this.next = n;
    }
    
    /**
        Changes the data field of this Node to the specified integer value x.
    */
    public void setData ( int x ) {
        this.data = x;
    }
    
    /**
        Returns a reference to a Node via this Node's previous reference field.
    */
    public Node getPrevious () {
        return this.previous;
    }
    
    /**
        Returns a reference to a Node via this Node's next reference field.
    */
    public Node getNext () {
        return this.next;
    }
    
    /**
        Returns an integer value via this Node's data field.
    */
    public int getData () {
        return this.data;
    }
}