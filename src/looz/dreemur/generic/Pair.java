/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package looz.dreemur.generic;

/**
 * Store variable of 2 type in a pair
 *
 * @author Loo Zi Kang
 * @param <T> Type of first item in pair
 * @param <K> Type of second item in pair
 */
public class Pair<T, K> {

    private T left;
    private K right;

    public Pair(T left, K right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Get the first item of the pair
     * @return 
     */
    public T getLeft() {
        return left;
    }

    public void setLeft(T left) {
        this.left = left;
    }

    /**
     * Get the 2nd item of the pair
     * @return 
     */
    public K getRight() {
        return right;
    }

    public void setRight(K right) {
        this.right = right;
    }

}
