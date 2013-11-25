import java.util.*;

/**
    An object of this class represents a binary tree. 
    Nodes may be decorated (labeled) with arbitrary objects (the default is null).
*/
public class BinaryTree implements java.util.Collection {
    private Node root;
    private int numberOfNodes;

    /** 
        Constructs an empty binary tree. 
    */
    public  BinaryTree () {
        this.root = null;
        this.numberOfNodes = 0;
    }
   
    /**
        Returns a string that represents this binary tree. 
	    (Some possible formats will be discussed in class.)
    */
    public String toString() {
        return convertTree( this.root );
    }
   
    /**
        Recursively converts a binary tree into a string via its nodes.
    */
    private String convertTree ( Node n ) {
        String datum;
    
        if ( n == null ) {
            return "()";
        }
        else {
            
            if ( n.getDatum() == null ) {
                datum = "";
            }
            else {
                datum = n.getDatum().toString();
            }
            
            return "( " + datum + " " + convertTree( n.getLeftChild() ) + " " + convertTree( n.getRightChild() ) + " )"; 
        }
    }
    
    /**
        Adds a node, decorated with obj, to this binary tree.
        The new node becomes the root of the tree; the former tree becomes the left subtree 
        of the root; the right subtree of the root will be empty.
    */
    public boolean add ( Object obj ) {
        Node n = new Node();
        n.setDatum( obj );
        n.setLeftChild( this.root );
        
        if ( !this.isEmpty() ) {
            this.root.setParent( n );
        }
        
        this.root = n;
        n = null;
        this.numberOfNodes += 1;
        return true;        
    }
   
    /**
        Adds a node, decorated with obj, to this binary tree.
        The new node becomes the root of the tree; the left subtree of the root will be empty; 
        the former tree becomes the right subtree of the root.
    */
    public boolean alternateAdd ( Object obj ) {
        Node n = new Node();
        n.setDatum( obj );
        n.setRightChild( this.root );
        
        if ( !this.isEmpty() ) {
            this.root.setParent( n );
        }
        
        this.root = n;
        n = null;
        this.numberOfNodes += 1;
        return true;
    }
   
    /**
        Static factory for constructing new binary trees.
        The root of the new tree will be decorated with obj; leftSubtree and rightSubtree, respectively, 
        will become the left and right subtrees of the root.
    */
    public static BinaryTree newFromRootAndTwoTrees ( Object obj, BinaryTree leftSubtree, BinaryTree rightSubtree ) {
        BinaryTree tree = new BinaryTree();
        tree.setRoot( new Node() );
        tree.getRoot().setDatum( obj );
        tree.increaseSize( 1 );
        
        if ( leftSubtree != null ) {
            leftSubtree.getRoot().setParent( tree.getRoot() );
            tree.getRoot().setLeftChild( leftSubtree.getRoot() );
            tree.increaseSize( leftSubtree.size() );
        }
        else {
            tree.getRoot().setLeftChild( null );
        }
        
        if ( rightSubtree != null ) {
            rightSubtree.getRoot().setParent( tree.getRoot() );
            tree.getRoot().setRightChild( rightSubtree.getRoot() );
            tree.increaseSize( rightSubtree.size() );
        }
        else {
            tree.getRoot().setRightChild( null );
        }
        
        return tree;
    }
   
    /** 
        Returns a hashcode for this binary tree. 
    */
    public int hashCode () {
        int code = 47;
        Iterator preorderTraversal = this.iterator();
        Iterator inorderTraversal = this.inorderIterator();
        Object preorderDatum;
        Object inorderDatum;
        
        while ( preorderTraversal.hasNext() && inorderTraversal.hasNext() ) {
            preorderDatum = preorderTraversal.next();
            inorderDatum = inorderTraversal.next();
            
            if ( preorderDatum == null ) {
                code = 29 * code;
            }
            else {
                code = 29 * code + preorderDatum.hashCode();
            }
            
            if ( inorderDatum == null ) {
                code = 29 * code;
            }
            else {
                code = 29 * code + inorderDatum.hashCode();
            }
        }
        
        return code;
    }

    /** 
        Throws an UnsupportedOperationException(). 
    */
    public boolean addAll ( java.util.Collection c ) { // overrides Collection
        throw new UnsupportedOperationException();
    }
	
    /** 
        Re-initializes this to an empty binary tree. 
    */	
    public void clear () {
        Iterator it = this.postorderIterator();
        
        while ( it.hasNext() ) {
            it.next();
            it.remove();
        }
    }

    /**
        Returns true iff this binary tree contains (at least) one example
        of obj.
    */
    public boolean contains ( Object obj ) {
        boolean isContained = false;
        
        for ( Object o: this ) {
        
            if ( o == null ) {
            
                if ( o == obj ) {
                    isContained = true;
                    break;
                }
            }
            else {
                
                if ( o.equals( obj ) ) {
                    isContained = true;
                    break;
                }
            }
        }
        
        return isContained; 
    } 

    /**
        Returns true iff this binary tree contains at least one example of each DIFFERENT
        object in Collection c. Note that c may contain several examples of some 
        (same) object, but this binary tree is only required to contain one such example.
    */
    public boolean containsAll ( java.util.Collection c ) {
        boolean areContained = true;
        
        if ( c == null ) {
            throw new NullPointerException();
        }
        else {
        
            for ( Object o: c ) {
            
                if ( !this.contains( o ) ) {
                    areContained = false;
                    break;
                }
            }
        }
        
        return areContained;
    }

    /**
        Returns true iff this binary tree is equivalent, with respect to both structure and content, 
	    as Object obj.
    */
    public boolean equals ( Object obj ) { // overrides Collection
        boolean isEqual = true;
        Object preorderDatumOne;
        Object preorderDatumTwo;
        Object inorderDatumOne;
        Object inorderDatumTwo;
        
        if ( obj == null ) {
            isEqual = false;
        }
        else if ( this.getClass() != obj.getClass() ) {
            isEqual = false;
        }
        else {
            BinaryTree tree = ( BinaryTree ) obj;
            
            if ( this.size() != tree.size() ) {
                isEqual = false;
            }
            else {
                Iterator preorderTraversalOne = this.iterator();
                Iterator preorderTraversalTwo = tree.iterator();
                Iterator inorderTraversalOne = this.inorderIterator();
                Iterator inorderTraversalTwo = tree.inorderIterator();
                
                while ( preorderTraversalOne.hasNext() && preorderTraversalTwo.hasNext() && inorderTraversalOne.hasNext() && inorderTraversalTwo.hasNext() && isEqual ) {
                    preorderDatumOne = preorderTraversalOne.next();
                    preorderDatumTwo = preorderTraversalTwo.next();
                    inorderDatumOne = inorderTraversalOne.next();
                    inorderDatumTwo = inorderTraversalTwo.next();
                    
                    if ( ( preorderDatumOne == null ) || ( preorderDatumTwo == null ) ) { 
                    
                        if ( preorderDatumOne != preorderDatumTwo ) {
                            isEqual = false;
                        }
                    }
                    else {
                    
                        if ( !preorderDatumOne.equals( preorderDatumTwo ) ) {
                            isEqual = false;
                        }
                    }
                    
                    if ( ( inorderDatumOne == null ) || ( inorderDatumTwo == null ) ) {
                        
                        if ( inorderDatumOne != inorderDatumTwo ) {
                            isEqual = false;
                        }
                    }
                    else {
                    
                        if ( !inorderDatumOne.equals( inorderDatumTwo ) ) {
                            isEqual = false;
                        }
                    }
                }
            }
        }
        
        return isEqual;  
    }
   
    /** 
        Returns true iff this binary tree is empty. 
    */
    public boolean isEmpty () {
        return ( ( this.root == null ) && ( this.numberOfNodes == 0 ) );
    }
	
    /** 
        Returns a preorder iterator for this binary tree. All bets are off if the tree changes during the traversal. 
    */
    public java.util.Iterator iterator () {
        return new PreorderIterator( this );
    }
	
    /** 
        Returns an inorder iterator for this binary tree. All bets are off if the tree changes during the traversal. 
    */
    public java.util.Iterator inorderIterator () {
        return new InorderIterator( this );
    }

    /** 
        Returns a postorder iterator for this binary tree. All bets are off if the tree changes during the traversal. 
    */
    public java.util.Iterator postorderIterator () {
        return new PostorderIterator( this );
    }	

    /**
        Removes a matching object from this binary tree, and returns true,  
        provided that the matching object is at a leaf; if there is no matching leaf,
        then the method returns false.
    */
    public boolean remove ( Object obj ) {
        int initialSize = this.size();
        boolean removed = false;
        Iterator preorderTraversal = this.iterator();
        Object o;

        while ( preorderTraversal.hasNext() && !removed ) {
            o = preorderTraversal.next();
            
            if ( o == null ) {
                
                if ( o == obj ) {
                    preorderTraversal.remove();
                    removed = ( initialSize > this.size() );
                }
            }
            else {
                
                if ( o.equals( obj ) ) {
                    preorderTraversal.remove();
                    removed = ( initialSize > this.size() );
                }
            }
        }
        
        return removed;
    }

    /** 
        Throws an UnsupportedOperationException.
    */
    public boolean removeAll (  java.util.Collection c ) { // overrides Collection
        throw new UnsupportedOperationException();
    }
	
    /** 
        Throws an UnsupportedOperationException(). 
    */
    public boolean retainAll (  java.util.Collection c ) { // overrides Collection
        throw new UnsupportedOperationException();
    }
    
    /**
        Sets the root of this binary tree.
    */
    void setRoot( Node n ) {
        this.root = n;
    }
	
    /**
        Returns the root of this binary tree.
    */
    Node getRoot () {
        return this.root;
    }
    
    /**
        Increases the size of this binary tree by incrementing its number of nodes.
    */
    void increaseSize ( int numberOfNodes ) {
        if ( numberOfNodes >= 0 ) {
            this.numberOfNodes += numberOfNodes;
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    
    /**
        Decreases the size of this binary tree by decrementing its number of nodes.
    */
    void decreaseSize ( int numberOfNodes ) {
        if ( numberOfNodes >= 0 ) {
            this.numberOfNodes -= numberOfNodes;
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    
    /**
        Returns the number of nodes in this binary tree.
    */
    public int size () {
        return this.numberOfNodes;
    }

    /** 
        Throws an UnsupportedOperationException().
    */
    public Object[] toArray () {
        throw new UnsupportedOperationException();
    }

    /** 
        Throws an UnsupportedOperationException().
    */
    public Object[] toArray ( Object[] a ) {
        throw new UnsupportedOperationException();
    }
}

/**
    A node of a binary tree.
    Each node contains the following four references:
        one to its datum, one to its parent, one to its left child, and one to its right child.
*/
class Node {
    private Object datum;
    private Node parent;
    private Node leftChild;
    private Node rightChild;
    
    /**
        Constructs a default node.
        The default settings are:
            a datum reference to null, 
            a left child reference to null, 
            and a right child reference to null.
    */
    public Node () {
        this.datum = null;
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
    }
    
    /**
        Changes the datum reference field of this node to refer to a specified Object o.
    */
    public void setDatum ( Object o ) {
        this.datum = o;
    }
    
    /**
        Changes the parent reference field of this node to refer to a specified Node n.
    */
    public void setParent ( Node n ) {
        this.parent = n;
    }
    
    /**
        Changes the left child reference field of this node to refer to a specified Node n.
    */
    public void setLeftChild ( Node n ) {
        this.leftChild = n;
    }
    
    /**
        Changes the right child reference field of this node to refer to a specified Node n.
    */
    public void setRightChild ( Node n ) {
        this.rightChild = n;
    }
    
    /**
        Returns a reference to an object via this node's datum reference field.
    */
    public Object getDatum () {
        return this.datum;
    }
    
    /**
        Returns a reference to a node via this node's parent reference field.
    */
    public Node getParent () {
        return this.parent;
    }
    
    /**
        Returns a reference to a node via this node's left child reference field.
    */
    public Node getLeftChild () {
        return this.leftChild;
    }
    
    /**
        Returns a reference to a node via this node's right child reference field.
    */
    public Node getRightChild () {
        return this.rightChild;
    }
}

/**
    An iterator that traverses a binary tree in preorder.
*/
class PreorderIterator implements java.util.Iterator {
    private BinaryTree tree;
    private Node root;
    private int numberOfNodes;
    private Stack<Node> stack;
    private boolean nextCalled;
    
    /**
        Constructor for an iterator that traverses a binary tree in preorder.
    */
    public PreorderIterator ( BinaryTree tree ) {
        this.tree = tree;
        this.root = null;
        this.numberOfNodes = tree.size();
        this.stack = new Stack<Node>();
        this.nextCalled = false;
    }
    
    /**
        Returns true if this binary tree still has more nodes to be iterated over.
    */
    public boolean hasNext () {
        return ( this.numberOfNodes > 0 );
    }
    
    /**
        Returns the next object, following preorder traversal, in this binary tree.
    */
    public Object next () {
        this.nextCalled = true;
        
        if ( this.numberOfNodes == 0 ) {
            throw new NoSuchElementException();
        }
        else {
        
            if ( this.root == null ) {
                this.root = this.tree.getRoot();
            }
            else {
        
                if ( this.root.getRightChild() != null ) {
                    this.stack.push( this.root );
                }
        
                if ( this.root.getLeftChild() != null ) {
                    this.root = this.root.getLeftChild();
                }
                else if ( !this.stack.empty() ) {
                    this.root = this.stack.pop().getRightChild();
                }
            }
            
            this.numberOfNodes -= 1;
        }
        
        return this.root.getDatum();
    }
    
    /**
        Removes a node from the binary tree if the node is a leaf.
    */
    public void remove () {
        
        if ( !this.nextCalled ) {
            throw new IllegalStateException();
        }
        else {
            this.nextCalled = false;
         
            if ( ( this.root.getLeftChild() == null ) && ( this.root.getRightChild() == null ) ) {
            
                if ( this.tree.size() == 1 ) {
                    this.tree.setRoot( null );
                    this.tree.decreaseSize( 1 );
                }
                else {
                
                    if ( this.root.getParent().getLeftChild() == this.root ) {
                        this.root.getParent().setLeftChild( null );
                    }
                    else if ( this.root.getParent().getRightChild() == this.root ) {
                        this.root.getParent().setRightChild( null );
                    }
                    
                    this.root.setParent( null );
                    this.tree.decreaseSize( 1 );
                }
            }
        }
    }
}

/**
    An iterator that traverses a binary tree in inorder.
*/
class InorderIterator implements java.util.Iterator {
    private BinaryTree tree;
    private Node root;
    private int numberOfNodes;
    private Stack<Node> stack;
    private Node cursor;
    private boolean nextCalled;
    
    /**
        Constructor for an iterator that traverses a binary tree in inorder.
    */
    public InorderIterator ( BinaryTree tree ) {
        this.tree = tree;
        this.root = this.tree.getRoot();
        this.numberOfNodes = tree.size();
        this.stack = new Stack<Node>();
        this.cursor = this.root;
        this.nextCalled = false;
    }
    
    /**
        Returns true if this binary tree still has more nodes to be iterated over.
    */
    public boolean hasNext () {
        return ( this.numberOfNodes > 0 );
    }
    
    /**
        Returns the next object, following inorder traversal, in this binary tree.
    */
    public Object next () {
        this.nextCalled = true;
        
        if ( this.numberOfNodes == 0 ) {
            throw new NoSuchElementException();
        }
        else {
       
                if ( !this.stack.empty() && ( this.stack.peek() == this.cursor ) ) {
                    this.stack.pop();
                    this.root = this.cursor;
                    
                    if ( this.cursor.getRightChild() != null ) {
                        this.cursor = this.cursor.getRightChild();
                    }
                    else if ( !this.stack.empty() ) {
                        this.cursor = this.stack.peek();
                    }
                }    
                else {
                
                    while ( this.cursor.getLeftChild() != null ) {
                        this.stack.push( this.cursor );
                        this.cursor = this.cursor.getLeftChild();                   
                    }
                
                    this.root = this.cursor;
                    
                    if ( this.cursor.getRightChild() != null ) {
                        this.cursor = this.cursor.getRightChild();
                    }
                    else if ( !this.stack.empty() ) {
                        this.cursor = this.stack.peek();
                    }
                }
                        
            this.numberOfNodes -= 1;
        }
        
        return this.root.getDatum();
    }
    
    /**
        Removes a node from the binary tree if the node is a leaf.
    */
    public void remove () {
        
        if ( !this.nextCalled ) {
            throw new IllegalStateException();
        }
        else {
            this.nextCalled = false;
         
            if ( ( this.root.getLeftChild() == null ) && ( this.root.getRightChild() == null ) ) {
            
                if ( this.tree.size() == 1 ) {
                    this.tree.setRoot( null );
                    this.tree.decreaseSize( 1 );
                }
                else {
                
                    if ( this.root.getParent().getLeftChild() == this.root ) {
                        this.root.getParent().setLeftChild( null );
                    }
                    else if ( this.root.getParent().getRightChild() == this.root ) {
                        this.root.getParent().setRightChild( null );
                    }
                    
                    this.root.setParent( null );
                    this.tree.decreaseSize( 1 );
                }
            }
        }
    }
}

/**
    An iterator that traverses a binary tree in postorder.
*/
class PostorderIterator implements java.util.Iterator {
    private BinaryTree tree;
    private Node root;
    private int numberOfNodes;
    private Stack<Node> stack;
    private Stack<String> direction;
    private boolean nextCalled;
    
    /**
        Constructor for an iterator that traverses a binary tree in postorder.
    */
    public PostorderIterator ( BinaryTree tree ) {
        this.tree = tree;
        this.root = this.tree.getRoot();
        this.numberOfNodes = tree.size();
        this.stack = new Stack<Node>();
        this.direction = new Stack<String>();
        this.nextCalled = false;
    }
    
    /**
        Returns true if this binary tree still has more nodes to be iterated over.
    */
    public boolean hasNext () {
        return ( this.numberOfNodes > 0 );
    }
    
    /**
        Returns the next object, following postorder traversal, in this binary tree.
    */
    public Object next () {
        this.nextCalled = true;
        
        if ( this.numberOfNodes == 0 ) {
            throw new NoSuchElementException();
        }
        else {

            if ( !this.stack.empty() && !this.direction.empty() ) {
                this.root = this.stack.peek();
                
                if ( this.direction.peek().equals( "Left" ) && ( this.root.getRightChild() != null ) ) {
                    this.direction.pop();
                    this.direction.push( "Right" );
                    this.root = this.root.getRightChild();
                    
                    while ( this.root.getLeftChild() != null ) {
                        this.stack.push( this.root );
                        this.direction.push( "Left" );
                        this.root = this.root.getLeftChild();
                    }
            
                    while ( this.root.getRightChild() != null ) {
                        this.stack.push( this.root );
                        this.direction.push( "Right" );
                        this.root = this.root.getRightChild();
                    }
                }
                else {
                    this.stack.pop();
                    this.direction.pop();
                }
            }
            else {    
            
                while ( this.root.getLeftChild() != null ) {
                    this.stack.push( this.root );
                    this.direction.push( "Left" );
                    this.root = this.root.getLeftChild();
                }
            
                while ( this.root.getRightChild() != null ) {
                    this.stack.push( this.root );
                    this.direction.push( "Right" );
                    this.root = this.root.getRightChild();
                }
            }
            
            this.numberOfNodes -= 1;
        }
        
        return this.root.getDatum();
    }
    
    /**
        Removes a node from the binary tree if the node is a leaf.
    */
    public void remove () {
        
        if ( !this.nextCalled ) {
            throw new IllegalStateException();
        }
        else {
            this.nextCalled = false;
         
            if ( ( this.root.getLeftChild() == null ) && ( this.root.getRightChild() == null ) ) {
            
                if ( this.tree.size() == 1 ) {
                    this.tree.setRoot( null );
                    this.tree.decreaseSize( 1 );
                }
                else {
                
                    if ( this.root.getParent().getLeftChild() == this.root ) {
                        this.root.getParent().setLeftChild( null );
                    }
                    else if ( this.root.getParent().getRightChild() == this.root ) {
                        this.root.getParent().setRightChild( null );
                    }
                    
                    this.root.setParent( null );
                    this.tree.decreaseSize( 1 );
                }
            }
        }
    }
}