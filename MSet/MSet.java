import java.util.Collection;
import java.util.Iterator;

/**
        An MSet is an unordered collection that allows duplicates. This class should be
        a direct, custom, array-based implementation of the Collection interface, created from
        "first prinicples," i.e., you may not use any other material (e.g., ArrayList) from the
        Java Collections Framework.
*/

public class MSet extends Object implements Collection {

        private Object[][] elements;
        private Object[] partition;
        private int numberOfElements;
        private final int sizeOfPartition = 1024;
        private final int numberOfPartitions = ( int ) Math.ceil( ( double ) Integer.MAX_VALUE / sizeOfPartition );

        /** Constructs an MSet with no elements. */
        public MSet () {
            elements = new Object[ numberOfPartitions ][];
            partition = new Object[ sizeOfPartition ];
            numberOfElements = 0;
            elements[ getRow( numberOfElements + 1 ) ] = partition;
        }

        /** Constructs an MSet from the given collection. */
        public MSet ( Collection c ) {
            elements = new Object[ numberOfPartitions ][];
            partition = new Object[ sizeOfPartition ];
            numberOfElements = 0;
            elements[ getRow( numberOfElements + 1 ) ] = partition;
            this.addAll( c );
        }

        /** Ensures that this collection contains the specified element.
            Returns true if this collection changed IN ANY WAY as a result of the call. */
        public boolean add ( Object o ) {
            boolean bagChanged = true;
            /*
                If the object is null, throws a NullPointerException because this collection does not
                support null elements.
            */
            if ( o == null ) {
                throw new NullPointerException();
            }
            /*
                If the current number of elements in the collection is greater than or equal to
                Integer.MAX_VALUE, throws an IllegalStateException because another element
                cannot be added at this time due to insertion restrictions.
            */
            else if ( numberOfElements >= Integer.MAX_VALUE ) {
                throw new IllegalStateException();
            }
            else {
                /*
                    Adds a new partition to the next row of the elements table if the current row of the
                    table is full.
                */
                if ( getRow( numberOfElements + 1 ) > getRow( numberOfElements ) ) {
                    partition = new Object[ sizeOfPartition ];
                    elements[ getRow( numberOfElements + 1 ) ] = partition;
                }

                numberOfElements += 1;
                elements[ getRow( numberOfElements ) ][ getColumn( numberOfElements ) ] = o;
            }

            return bagChanged;
        }

        /** Adds all of the elements in the specified collection to this collection. */
        public boolean addAll ( Collection c ) {
            boolean bagChanged = true;

            if ( c == null ) {
                throw new NullPointerException();
            }
            else {
                /*
                    Adds each element of the specified collection to this MSet.
                */
                for ( Object o : c ) {
                    this.add( o );
                }
            }

            return bagChanged;
        }

        /** Removes all of the elements from this collection. */
        public void clear () {
            elements = new Object[ numberOfPartitions ][];
            partition = new Object[ sizeOfPartition ];
            numberOfElements = 0;
            elements[ getRow( numberOfElements + 1 ) ] = partition;
        }

        /** Returns true if this collection contains the specified element. */
        public boolean contains ( Object o ) {
            boolean bagContainsElement = false;
            /*
                If the object is null, throws a NullPointerException because this collection does not
                support null elements.
            */
            if ( o == null ) {
                throw new NullPointerException();
            }
            else {
                /*
                    Checks each element in the MSet for a match until either a match is found or all of
                    the elements in the MSet have been checked.
                */
                for ( int i = 0; i < numberOfElements; i++ ) {

                    if ( elements[ getRow( i + 1 ) ][ getColumn( i + 1 ) ].equals( o ) ) {
                        bagContainsElement = true;
                        break;
                    }
                }
            }

            return bagContainsElement;
        }

        /** Returns true if this collection contains all of the elements in the specified collection. */
        public boolean containsAll ( Collection c ) {
            boolean bagContainsElements = true;

            if ( c == null ) {
                throw new NullPointerException();
            }
            else {
                /*
                    Checks if each element in the specified collection is contained within this MSet.
                */
                for ( Object o : c ) {
                    /*
                        If an element from the specified collection is found to not be contained within this
                        MSet, return false.
                    */
                    if ( !this.contains( o ) ) {
                        bagContainsElements = false;
                        break;
                    }
                }
            }

            return bagContainsElements;
        }


        /** Compares the specified object with this collection for equality. Overrides Object.equals(). */
        public boolean equals ( Object o ) {

            if ( o == null ) {
                return false;
            }
            else {
                return ( this.hashCode() == o.hashCode() );
            }
        }

        /** Returns a hash code value for this collection. May override Object.hashCode(). */
        public int hashCode () {
            int hash = 11;
            int multiplier = 7;

            for ( int i = 0; i < numberOfElements; i++ ) {
                hash = multiplier * ( hash + elements[ getRow( i + 1 ) ][ getColumn( i + 1 ) ].hashCode() );
            }

            return hash;
        }

        /** Returns true if this collection contains no elements. */
        public boolean isEmpty () {
            return ( numberOfElements == 0 );
        }

        /** Returns an iterator over the UNIQUE elements in this collection.
                There are no guarantees concerning the order in which the elements are returned. */
        public Iterator iterator () {
            return new MSetIterator( this );
        }

        /** [REVISED] Removes all instances of the specified element from this collection. */
        public boolean remove ( Object o ) {
            boolean bagChanged = this.contains( o );

            while ( this.reduce( o ) ) {
            }

            return bagChanged;
        }

        /** Removes all of this collection's elements that are also contained in the specified collection.
                After this call returns, this collection will contain no elements in common with the specified collection. */
        public boolean removeAll ( Collection c ) {
            boolean bagChanged = false;

            if ( c == null ) {
                throw new NullPointerException();
            }
            else {
                /*
                    Checks if any of the elements in the specified collection are contained within this MSet.
                    If so, this MSet will be changed.
                */
                for ( Object o : c ) {

                    if ( this.contains( o ) ) {
                        bagChanged = true;
                        break;
                    }
                }
                /*
                    Removes all copies of an object from this MSet for each object that is contained within the specified collection.
                */
                for ( Object o : c ) {
                    this.remove( o );
                }
            }

            return bagChanged;
        }

        /** Retains only the elements in this collection that are contained in the specified collection.
                 In other words, removes from this collection all of its elements that are not contained in the
                 specified collection. */
        public boolean retainAll ( Collection c ) {
            boolean bagChanged = false;

            if ( c == null ) {
                throw new NullPointerException();
            }
            else {
                /*
                    Removes all copies of objects that are contained within this MSet, but not the specified collection.
                */
                for ( Object o : this ) {
                    /*
                        If an object from this MSet is not contained within the specified collection, all copies
                        of the object are removed from this MSet.
                    */
                    if ( !c.contains( o ) ) {
                        this.remove( o );
                        bagChanged = true;
                    }
                }
            }

            return bagChanged;
        }

        /** Returns the number of elements in this collection, including duplicates.  */
        public int size () {
            return numberOfElements;
        }

        /** Returns the number of UNIQUE elements in this collection (i.e., not including duplicates). */
        public int unique () {
            int numberOfUniqueElements = numberOfElements;
            /*
                Checks each element in the table to see if another element can be found further
                down the table that matches it.  The total number of unique elements in the table
                is initialized to the total number of elements in the table.  However, if such a
                match is found, that means that there is one less unique element in the
                table and the total number of unique elements in the table is adjusted accordingly.
            */
            for ( int i = 0; i < numberOfElements; i++ ) {

                for ( int j = ( i + 1 ); j < numberOfElements; j++ ) {

                    if ( elements[ getRow( i + 1 ) ][ getColumn( i + 1 ) ].equals( elements[ getRow( j + 1 ) ][ getColumn( j + 1 ) ] ) ) {
                        numberOfUniqueElements -= 1;
                        break;
                    }
                }
            }

            return numberOfUniqueElements;
        }

        /** Returns an array containing all of the UNIQUE elements in this collection. */
        public Object[] toArray () {
            int numberOfUniqueElements = this.unique();
            Object[] uniqueElements = new Object[ numberOfUniqueElements ];
            boolean isUniqueElement = true;
            int position = 0;
            /*
                Searches for a unique element in the table.  If a unique element is found, it is
                placed in the array.
            */
            for ( int i = 0; i < numberOfElements; i++ ) {

                for ( int j = ( i + 1 ); j < numberOfElements; j++ ) {

                    if ( elements[ getRow( i + 1 ) ][ getColumn( i + 1 ) ].equals( elements[ getRow( j + 1 ) ][ getColumn( j + 1 ) ] ) ) {
                        isUniqueElement = false;
                        break;
                    }
                }
                /*
                    Places a unique element in the array.
                */
                if ( isUniqueElement ) {
                        uniqueElements[ position ] =  elements[ getRow( i + 1 ) ][ getColumn( i + 1 ) ];
                        position += 1;
                }
                else {
                    isUniqueElement = true;
                }
            }

            return uniqueElements;
        }

        /** Throws an UnsupportedOperationException. */
        public Object[] toArray ( Object[] a  ) {
            throw new UnsupportedOperationException();
        }


        /** [ADDED] Decrements the number of copies of o in this MSet. Returns true iff this MSet changed
                as a result of the operation. */
        public boolean reduce ( Object o ) {
            boolean bagChanged = false;
            /*
                If this MSet contains the specified object, the number of copies of the object is reduced
                by one.
            */
            if ( this.contains( o ) ) {
                /*
                    Finds the location of the first copy of the object in the table and removes it by
                    shifting the subsequent elements in the table to the left by one.
                */
                for ( int i = 0; i < numberOfElements; i++ ) {
                    /*
                        Finds the location of the first copy of the object in the table.
                    */
                    if ( elements[ getRow( i + 1 ) ][ getColumn( i + 1 ) ].equals( o ) ) {
                        /*
                            Shifts each subsequent element in the table to the left by one.
                        */
                        for ( int j = ( i + 1 ); j < numberOfElements; j++ ) {
                            elements[ getRow( j ) ][ getColumn( j ) ] = elements[ getRow( j + 1 ) ][ getColumn( j + 1 ) ];
                        }
                        /*
                            The original location of the last element in the table is changed to null.
                        */
                        elements[ getRow( numberOfElements ) ][ getColumn( numberOfElements ) ] = null;
                        break;
                    }
                }
                /*
                    Removes the last row of the table if there no longer are any elements in it.
                */
                if ( ( getRow( numberOfElements - 1 ) < getRow( numberOfElements ) ) && ( getRow( numberOfElements - 1 ) > 0 ) ) {
                    elements[ getRow( numberOfElements ) ] = null;
                }

                numberOfElements -= 1;
                bagChanged = true;
            }

            return bagChanged;
        }

        /*
            Returns the row of an element in the elements table.
        */
        private int getRow ( int n ) {
            return ( ( n - 1 ) / sizeOfPartition );
        }

        /*
            Returns the column of an element in the elements table.
        */
        private int getColumn ( int n ) {
            return ( ( n - 1 ) % sizeOfPartition );
        }


/* The following override methods inherited from Object: */

        /** Throws an UnsupportedOperationException. Overrides Object.clone(). */
        protected Object clone() {
            throw new UnsupportedOperationException();
        }

        /** Throws an UnsupportedOperationException. Overrides Object.finalize(). */
        protected void finalize () {
            throw new UnsupportedOperationException();
        }

        /** Returns a stringy representation of this MSet. Overrides Object.toString(). */
        public String toString () {
            String s = "";

            for ( int i = 0; i < numberOfElements; i++ ) {

                if ( i > 0 ) {
                    s += " ";
                }

                s += elements[ getRow( i + 1 ) ][ getColumn( i + 1 ) ].toString();

                if ( i < ( numberOfElements - 1 ) ) {
                    s += ",";
                }
            }

            return s;
        }
}

/*
    An Iterator over the unique elements of an MSet.
*/
class MSetIterator implements Iterator {

    private Object[] uniqueElements;
    private int numberOfUniqueElements;
    private int index;

    public MSetIterator ( MSet bag ) {
        uniqueElements = bag.toArray();
        numberOfUniqueElements = bag.unique();
        index = 0;
    }

    public boolean hasNext () {
        return ( index < numberOfUniqueElements );
    }

    public Object next () {
        Object o = uniqueElements[ index ];
        index += 1;
        return o;
    }

    public void remove () {
        throw new UnsupportedOperationException();
    }
}
