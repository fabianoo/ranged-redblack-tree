package xyz.fabiano.collections;

public class RangedRedBlackNode<T extends Comparable> {

    private static final boolean BLACK = false;
    private static final boolean RED = true;

    static final RangedRedBlackNode NULL_VALUE = new RangedRedBlackNode<>();

    private HalfClosedRange<T> key;
    private T maxEndValue;

    private RangedRedBlackNode<T> parent;
    private RangedRedBlackNode<T> left;
    private RangedRedBlackNode<T> right;
    private boolean color = BLACK;

    RangedRedBlackNode() {
        parent = null;
        left = null;
        right = null;
    }

    RangedRedBlackNode(HalfClosedRange<T> key) {
        this();
        this.key = key;
        this.maxEndValue = key.getEnd();
    }

    public HalfClosedRange<T> getKey() {
        return key;
    }

    public void setKey(HalfClosedRange<T> key) {
        this.key = key;
    }

    public RangedRedBlackNode<T> getParent() {
        return parent;
    }

    public void setParent(RangedRedBlackNode<T> parent) {
        this.parent = parent;
    }

    public RangedRedBlackNode<T> getLeft() {
        return left;
    }

    public void setLeft(RangedRedBlackNode<T> left) {
        this.left = left;
    }

    public RangedRedBlackNode<T> getRight() {
        return right;
    }

    public void setRight(RangedRedBlackNode<T> right) {
        this.right = right;
    }

    public boolean isRed() {
        return color == RED;
    }

    public boolean isBlack() {
        return color == BLACK;
    }

    public void setBlack() {
        this.color = BLACK;
    }

    public void setRed() {
        this.color = RED;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public boolean getColor() {
        return color;
    }

    public T getMaxEndValue() {
        return maxEndValue;
    }

    public void setMaxEndValue(T maxEndValue) {
        this.maxEndValue = maxEndValue;
    }

    public T getMaxEndValueOfChilds() {
        if (!isNullValue(left) && !isNullValue(right)) {
            if (left.maxEndValue.compareTo(right.maxEndValue) > 0) {
                return left.maxEndValue;
            } else {
                return right.maxEndValue;
            }
        } else {
            if (!isNullValue(left)) return left.maxEndValue;
            else return right.maxEndValue;
        }

    }

    public static boolean isNullValue(RangedRedBlackNode node) {
        return node == NULL_VALUE || node == null;
    }

    @Override
    public String toString() {
        return "RangedRedBlackNode{key=" + key + ", maxEndValue=" + maxEndValue + '}';
    }
}