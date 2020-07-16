package spatial.nodes;

import spatial.exceptions.UnimplementedMethodException;
import spatial.kdpoint.KDPoint;
import spatial.knnutils.BoundedPriorityQueue;
import spatial.knnutils.NNData;

import java.util.Collection;

/**
 * <p>{@link KDTreeNode} is an abstraction over nodes of a KD-Tree. It is used extensively by
 * {@link spatial.trees.KDTree} to implement its functionality.</p>
 *
 * <p><b>YOU ***** MUST ***** IMPLEMENT THIS CLASS!</b></p>
 *
 * @author  ---- Jemimah E.P. Salvacion -----
 *
 * @see spatial.trees.KDTree
 */
public class KDTreeNode {


    /* *************************************************************************** */
    /* ************* WE PROVIDE THESE FIELDS TO GET YOU STARTED.  **************** */
    /* ************************************************************************** */
    private KDPoint p;
    private int height;
    private KDTreeNode left, right;

    /* *************************************************************************************** */
    /* *************  PLACE ANY OTHER PRIVATE FIELDS AND YOUR PRIVATE METHODS HERE: ************ */
    /* ************************************************************************************* */
    private boolean searchAux(KDTreeNode curr, KDPoint targ, int currDim, int dims) {
    	if (curr != null) {
    		// Base case to end recursive function
        	if (targ.equals(curr.p))
            	return true;
        	if (currDim == dims)
        		currDim = 0;
            // Element goes to right tree
            if (targ.coords[currDim] > curr.p.coords[currDim]) {
            	return searchAux(curr.right, targ, ++currDim, dims);
            }
            // Element goes to the left tree
            if (targ.coords[currDim] < curr.p.coords[currDim]) {
            	return searchAux(curr.left, targ, ++currDim, dims);
            }
    	}	
        return false;
    }
    
    private KDTreeNode insertAux(KDTreeNode curr, KDPoint newElt, int currDim, int dims) {
    	if (curr != null) {
    		if (currDim == dims)
    			currDim = 0;
            // Element goes to right tree
            if (newElt.coords[currDim] >= curr.p.coords[currDim]) {
            	curr.right = insertAux(curr.right, newElt, ++currDim, dims);
        		// Determine height calculation
            	if (curr.left == null) {
            		curr.height = curr.right.height + 1;
            	} else {
        			if (curr.left.height == curr.right.height || curr.left.height < curr.right.height)
        				curr.height = curr.right.height + 1;
        			else
        				curr.height = curr.left.height + 1;
        		}
            	return curr;
            }
            // Element goes to the left tree
            if (newElt.coords[currDim] < curr.p.coords[currDim]) {
            	curr.left = insertAux(curr.left, newElt, ++currDim, dims);
            	// Determine height calculation
            	if (curr.right == null) {
            		curr.height = curr.left.height + 1;
            	} else {
        			if (curr.left.height == curr.right.height || curr.left.height > curr.right.height)
        				curr.height = curr.left.height + 1;
        			else
        				curr.height = curr.right.height + 1;
        		}
            	return curr;
            }
    	} else { 
    		curr = new KDTreeNode(newElt);
    		curr.height = 0;   		
    	}
    	return curr;
    }
   

    /* *********************************************************************** */
    /* ***************  IMPLEMENT THE FOLLOWING PUBLIC METHODS:  ************ */
    /* *********************************************************************** */


    /**
     * 1-arg constructor. Stores the provided {@link KDPoint} inside the freshly created node.
     * @param p The {@link KDPoint} to store inside this. Just a reminder: {@link KDPoint}s are
     *          <b>mutable!!!</b>.
     */
    public KDTreeNode(KDPoint p){
        this.p = new KDPoint(p);
        height = 0;
        left = null;
        right = null;
    }

    /**
     * <p>Inserts the provided {@link KDPoint} in the tree rooted at this. To select which subtree to recurse to,
     * the KD-Tree acts as a Binary Search Tree on currDim; it will examine the value of the provided {@link KDPoint}
     * at currDim and determine whether it is larger than or equal to the contained {@link KDPoint}'s relevant dimension
     * value. If so, we recurse right, like a regular BST, otherwise left.</p>
     * @param currDim The current dimension to consider
     * @param dims The total number of dimensions that the space considers.
     * @param pIn The {@link KDPoint} to insert into the node.
     * @see #delete(KDPoint, int, int)
     */
    public  void insert(KDPoint pIn, int currDim, int dims){
    	insertAux(this,pIn,currDim,dims);
    }

    /**
     * <p>Deletes the provided {@link KDPoint} from the tree rooted at this. To select which subtree to recurse to,
     * the KD-Tree acts as a Binary Search Tree on currDim; it will examine the value of the provided {@link KDPoint}
     * at currDim and determine whether it is larger than or equal to the contained {@link KDPoint}'s relevant dimension
     * value. If so, we recurse right, like a regular BST, otherwise left. There exist two special cases of deletion,
     * depending on whether we are deleting a {@link KDPoint} from a node who either:</p>
     *
     * <ul>
     *      <li>Has a NON-null subtree as a right child.</li>
     *      <li>Has a NULL subtree as a right child.</li>
     * </ul>
     *
     * <p>You should consult the class slides, your notes, and the textbook about what you need to do in those two
     * special cases.</p>
     * @param currDim The current dimension to consider.
     * @param dims The total number of dimensions that the space considers.
     * @param pIn The {@link KDPoint} to insert into the node.
     * @see #insert(KDPoint, int, int)
     * @return A reference to this after the deletion takes place.
     */
    public KDTreeNode delete(KDPoint pIn, int currDim, int dims){
        return deleteAux(this, pIn, currDim, dims);
    }
    
    private KDTreeNode deleteAux(KDTreeNode curr, KDPoint toDelete, int currDim, int dims) {
    	if (curr != null) {
    		if (curr.p.equals(toDelete)) {
    			if (curr.left == null && curr.right == null) {
    				curr = null;  				
    			} else if (curr.right != null) {	
    				curr.p = findMin(curr.right, curr.right.p, currDim, dims);
    				curr.right = deleteAux(curr.right, curr.p, currDim, dims);
    			}
    			return curr;
    		}
    		if (currDim == dims)
    			currDim = 0;
    		// Element goes to right tree
            if (toDelete.coords[currDim] > curr.p.coords[currDim]) {
            	curr.right = deleteAux(curr.right, toDelete, ++currDim, dims);
            }
            // Element goes to the left tree
            if (toDelete.coords[currDim] < curr.p.coords[currDim]) {
            	curr.left = deleteAux(curr.left, toDelete, ++currDim, dims);
            }
    	}
    	
    	return curr;
    }
    
    private KDPoint findMin(KDTreeNode curr, KDPoint min, int currDim, int dims) {
    	if (curr != null) {
	    	if (curr.p.coords[currDim] < min.coords[currDim]) {
	    		min = curr.p;
	    	} else {
	    		min = findMin(curr.right, min, currDim, dims);
	    		min = findMin(curr.left, min, currDim, dims);
	    	}
    	}
    	return min;
    }

    /**
     * Searches the subtree rooted at the current node for the provided {@link KDPoint}.
     * @param pIn The {@link KDPoint} to search for.
     * @param currDim The current dimension considered.
     * @param dims The total number of dimensions considered.
     * @return true iff pIn was found in the subtree rooted at this, false otherwise.
     */
    public  boolean search(KDPoint pIn, int currDim, int dims){
    	return searchAux(this, pIn, currDim, dims);
    }

    /**
     * <p>Executes a range query in the given {@link KDTreeNode}. Given an &quot;anchor&quot; {@link KDPoint},
     * all {@link KDPoint}s that have a {@link KDPoint#euclideanDistance(KDPoint) euclideanDistance} of <b>at most</b> range
     * <b>INCLUSIVE</b> from the anchor point <b>except</b> for the anchor itself should be inserted into the {@link Collection}
     * that is passed.</p>
     *
     * <p>Remember: range queries behave <em>greedily</em> as we go down (approaching the anchor as &quot;fast&quot;
     * as our currDim allows and <em>prune subtrees</em> that we <b>don't</b> have to visit as we backtrack. Consult
     * all of our resources if you need a reminder of how these should work.</p>
     *
     * @param anchor The centroid of the hypersphere that the range query implicitly creates.
     * @param results A {@link Collection} that accumulates all the {@link }
     * @param currDim The current dimension examined by the {@link KDTreeNode}.
     * @param dims The total number of dimensions of our {@link KDPoint}s.
     * @param range The <b>INCLUSIVE</b> range from the &quot;anchor&quot; {@link KDPoint}, within which all the
     *              {@link KDPoint}s that satisfy our query will fall. The euclideanDistance metric used} is defined by
     *              {@link KDPoint#euclideanDistance(KDPoint)}.
     */
    public void range(KDPoint anchor, Collection<KDPoint> results,
                      double range, int currDim , int dims){
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }


    /**
     * <p>Executes a nearest neighbor query, which returns the nearest neighbor, in terms of
     * {@link KDPoint#euclideanDistance(KDPoint)}, from the &quot;anchor&quot; point.</p>
     *
     * <p>Recall that, in the descending phase, a NN query behaves <em>greedily</em>, approaching our
     * &quot;anchor&quot; point as fast as currDim allows. While doing so, it implicitly
     * <b>bounds</b> the acceptable solutions under the current <b>best solution</b>, which is passed as
     * an argument. This approach is known in Computer Science as &quot;branch-and-bound&quot; and it helps us solve an
     * otherwise exponential complexity problem (nearest neighbors) efficiently. Remember that when we want to determine
     * if we need to recurse to a different subtree, it is <b>necessary</b> to compare the euclideanDistance reported by
     * {@link KDPoint#euclideanDistance(KDPoint)} and coordinate differences! Those are comparable with each other because they
     * are the same data type ({@link Double}).</p>
     *
     * @return An object of type {@link NNData}, which exposes the pair (distance_of_NN_from_anchor, NN),
     * where NN is the nearest {@link KDPoint} to the anchor {@link KDPoint} that we found.
     *
     * @param anchor The &quot;ancor&quot; {@link KDPoint}of the nearest neighbor query.
     * @param currDim The current dimension considered.
     * @param dims The total number of dimensions considered.
     * @param n An object of type {@link NNData}, which will define a nearest neighbor as a pair (distance_of_NN_from_anchor, NN),
     *      * where NN is the nearest neighbor found.
     *
     * @see NNData
     * @see #kNearestNeighbors(int, KDPoint, BoundedPriorityQueue, int, int)
     */
    public  NNData<KDPoint> nearestNeighbor(KDPoint anchor, int currDim,
                                            NNData<KDPoint> n, int dims){
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }

    /**
     * <p>Executes a nearest neighbor query, which returns the nearest neighbor, in terms of
     * {@link KDPoint#euclideanDistance(KDPoint)}, from the &quot;anchor&quot; point.</p>
     *
     * <p>Recall that, in the descending phase, a NN query behaves <em>greedily</em>, approaching our
     * &quot;anchor&quot; point as fast as currDim allows. While doing so, it implicitly
     * <b>bounds</b> the acceptable solutions under the current <b>worst solution</b>, which is maintained as the
     * last element of the provided {@link BoundedPriorityQueue}. This is another instance of &quot;branch-and-bound&quot;
     * Remember that when we want to determine if we need to recurse to a different subtree, it is <b>necessary</b>
     * to compare the euclideanDistance reported by* {@link KDPoint#euclideanDistance(KDPoint)} and coordinate differences!
     * Those are comparable with each other because they are the same data type ({@link Double}).</p>
     *
     * <p>The main difference of the implementation of this method and the implementation of
     * {@link #nearestNeighbor(KDPoint, int, NNData, int)} is the necessity of using the class
     * {@link BoundedPriorityQueue} effectively. Consult your various resources
     * to understand how you should be using this class.</p>
     *
     * @param k The total number of neighbors to retrieve. It is better if this quantity is an odd number, to
     *          avoid ties in Binary Classification tasks.
     * @param anchor The &quot;anchor&quot; {@link KDPoint} of the nearest neighbor query.
     * @param currDim The current dimension considered.
     * @param dims The total number of dimensions considered.
     * @param queue A {@link BoundedPriorityQueue} that will maintain at most k nearest neighbors of
     *              the anchor point at all times, sorted by euclideanDistance to the point.
     *
     * @see BoundedPriorityQueue
     */
    public  void kNearestNeighbors(int k, KDPoint anchor, BoundedPriorityQueue<KDPoint> queue, int currDim, int dims){
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }

    /**
     * Returns the height of the subtree rooted at the current node. Recall our definition of height for binary trees:
     * <ol>
     *     <li>A null tree has a height of -1.</li>
     *     <li>A non-null tree has a height equal to max(height(left_subtree), height(right_subtree))+1</li>
     * </ol>
     * @return the height of the subtree rooted at the current node.
     */
    public int height(){
        return height;
    }

    /**
     * A simple getter for the {@link KDPoint} held by the current node. Remember: {@link KDPoint}s ARE
     * MUTABLE, SO WE NEED TO DO DEEP COPIES!!!
     * @return The {@link KDPoint} held inside this.
     */
    public KDPoint getPoint(){
        return p;
    }

    public KDTreeNode getLeft(){
        return left;
    }

    public KDTreeNode getRight(){
        return right;
    }
}
