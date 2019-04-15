package xyz.fabiano.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static xyz.fabiano.collections.RangedRedBlackNode.NULL_VALUE;

public class RangedRedBlackTree<T extends Comparable<T>> implements Iterable<RangedRedBlackNode<T>> {

    private RangedRedBlackNode<T> root = NULL_VALUE;

    public RangedRedBlackTree() {
        root.setLeft(NULL_VALUE);
        root.setRight(NULL_VALUE);
        root.setParent(NULL_VALUE);
    }

    @Override
    public Iterator<RangedRedBlackNode<T>> iterator() {
        return everyNodeOrdered().iterator();
    }

    public Collection<RangedRedBlackNode<T>> everyNodeOrdered() {
        List<RangedRedBlackNode<T>> ranges = new ArrayList<>();
        RangedRedBlackNode<T> minNode = treeMinimum(root);

        ranges.add(minNode);
        ranges.addAll(getGreaterThan(minNode));
        return ranges;
    }

    private void leftRotate(RangedRedBlackNode<T> x) {
        RangedRedBlackNode<T> y;
        y = x.getRight();
        x.setRight(y.getLeft());

        if (!isNullValue(y.getLeft())) y.getLeft().setParent(x);
        y.setParent(x.getParent());

        if (isNullValue(x.getParent())) root = y;
        else if (x.getParent().getLeft() == x) x.getParent().setLeft(y);
        else x.getParent().setRight(y);

        y.setLeft(x);
        x.setParent(y);
    }

    private void rightRotate(RangedRedBlackNode<T> y) {
        RangedRedBlackNode<T> x = y.getLeft();
        y.setLeft(x.getRight());

        if (!isNullValue(x.getRight())) x.getRight().setParent(y);

        x.setParent(y.getParent());

        if (isNullValue(y.getParent())) root = x;
        else if (y.getParent().getRight() == y) y.getParent().setRight(x);
        else y.getParent().setLeft(x);

        x.setRight(y);
        y.setParent(x);
    }

    public void insert(ClosedRange<T> key) {
        insert(new RangedRedBlackNode<>(key));
    }

    private void insert(RangedRedBlackNode<T> node) {
        RangedRedBlackNode<T> temp = NULL_VALUE;
        RangedRedBlackNode<T> parent = root;

        while (!isNullValue(parent)) {
            temp = parent;

            if (node.getKey().compareTo(parent.getKey()) < 0) parent = parent.getLeft();
            else parent = parent.getRight();
        }

        node.setParent(temp);
        if (isNullValue(temp)) {
            root = node;
        } else if (node.getKey().compareTo(temp.getKey()) < 0) {
            temp.setLeft(node);
        } else {
            temp.setRight(node);
        }

        node.setLeft(NULL_VALUE);
        node.setRight(NULL_VALUE);
        node.setRed();

        insertFixup(node);
        updateMaxEndValue(node);
    }

    private void updateMaxEndValue(RangedRedBlackNode<T> node) {
        T childMaxValue = node.getMaxEndValueOfChilds();
        if (childMaxValue != null && node.getMaxEndValue().compareTo(childMaxValue) < 0) {
            node.setMaxEndValue(childMaxValue);
            if (isNullValue(node.getParent())) {
                updateMaxEndValue(node.getParent());
            }
        }
    }

    private void insertFixup(RangedRedBlackNode<T> z) {
        RangedRedBlackNode<T> y = NULL_VALUE;
        while (z.getParent().isRed()) {

            if (z.getParent() == z.getParent().getParent().getLeft()) {
                y = z.getParent().getParent().getRight();
                if (y.isRed()) {
                    z.getParent().setBlack();
                    y.setBlack();
                    z.getParent().getParent().setRed();
                    z = z.getParent().getParent();
                } else if (z == z.getParent().getRight()) {
                    z = z.getParent();
                    leftRotate(z);
                } else {
                    z.getParent().setBlack();
                    z.getParent().getParent().setRed();
                    rightRotate(z.getParent().getParent());
                }
            } else {
                y = z.getParent().getParent().getLeft();
                if (y.isRed()) {
                    z.getParent().setBlack();
                    y.setBlack();
                    z.getParent().getParent().setRed();
                    z = z.getParent().getParent();
                } else if (z == z.getParent().getLeft()) {
                    z = z.getParent();
                    rightRotate(z);
                } else {
                    z.getParent().setBlack();
                    z.getParent().getParent().setRed();
                    leftRotate(z.getParent().getParent());
                }
            }
        }
        root.setBlack();
    }

    public RangedRedBlackNode<T> treeMinimum(RangedRedBlackNode<T> node) {
        while (!isNullValue(node.getLeft()))
            node = node.getLeft();
        return node;
    }

    public RangedRedBlackNode<T> treeSuccessor(RangedRedBlackNode<T> x) {
        if (!isNullValue(x.getLeft())) return treeMinimum(x.getRight());

        RangedRedBlackNode<T> y = x.getParent();

        while (!isNullValue(y) && x == y.getRight()) {
            x = y;
            y = y.getParent();
        }
        return y;
    }

    public void remove(RangedRedBlackNode<T> node) {
        RangedRedBlackNode<T> temp;
        RangedRedBlackNode<T> anotherTemp;

        if (isNullValue(node.getLeft()) || isNullValue(node.getRight())) temp = node;
        else temp = treeSuccessor(node);

        if (!isNullValue(temp.getLeft())) anotherTemp = temp.getLeft();
        else anotherTemp = temp.getRight();

        anotherTemp.setParent(temp.getParent());

        if (isNullValue(temp.getParent())) root = anotherTemp;
        else if (!isNullValue(temp.getParent().getLeft()) && temp.getParent().getLeft() == temp) temp.getParent().setLeft(anotherTemp);
        else if (!isNullValue(temp.getParent().getRight()) && temp.getParent().getRight() == temp) temp.getParent().setRight(anotherTemp);

        if (temp != node) node.setKey(temp.getKey());
        if (temp.isBlack()) removeFixup(anotherTemp);
    }

    private void removeFixup(RangedRedBlackNode<T> node) {
        RangedRedBlackNode<T> tmp;

        while (node != root && node.isBlack()) {
            if (node == node.getParent().getLeft()) {
                tmp = node.getParent().getRight();
                if (tmp.isRed()) {
                    tmp.setBlack();
                    node.getParent().setRed();
                    leftRotate(node.getParent());
                    tmp = node.getParent().getRight();
                }
                if (tmp.getLeft().isBlack() &&
                        tmp.getRight().isBlack()) {
                    tmp.setRed();
                    node = node.getParent();
                } else {
                    if (tmp.getRight().isBlack()) {
                        tmp.getLeft().setBlack();
                        tmp.setRed();
                        rightRotate(tmp);
                        tmp = node.getParent().getRight();
                    }
                    tmp.setColor(node.getParent().getColor());
                    node.getParent().setBlack();
                    tmp.getRight().setBlack();
                    leftRotate(node.getParent());
                    node = root;
                }
            } else {
                tmp = node.getParent().getLeft();
                if (tmp.isRed()) {
                    tmp.setBlack();
                    node.getParent().setRed();
                    rightRotate(node.getParent());
                    tmp = node.getParent().getLeft();
                }

                if (tmp.getRight().isBlack() &&
                        tmp.getLeft().isBlack()) {
                    tmp.setRed();
                    node = node.getParent();
                } else {
                    if (tmp.getLeft().isBlack()) {
                        tmp.getRight().setBlack();
                        tmp.setRed();
                        leftRotate(tmp);
                        tmp = node.getParent().getLeft();
                    }
                    tmp.setColor(node.getParent().getColor());
                    node.getParent().setBlack();
                    tmp.getLeft().setBlack();
                    rightRotate(node.getParent());
                    node = root;
                }
            }
        }
        node.setBlack();
    }

    public void minus(ClosedRange<T> range) {
        RangedRedBlackNode<T> intersection = findFirstIntersection(range);
        if (intersection == null) return;

        Collection<ClosedRange<T>> newRanges = intersection.getKey().minus(range);
        this.remove(intersection);
        newRanges.forEach(this::insert);
        this.minus(range);
    }

    public RangedRedBlackNode<T> findFirstIntersection(ClosedRange<T> range) {
        RangedRedBlackNode<T> current = root;

        while (!isNullValue(current)) {
            if (current.getMaxEndValue().compareTo(range.getStart()) <= 0) break;
            else if (current.getKey().overlaps(range)) return current;
            else if (current.getLeft().getMaxEndValue().compareTo(range.getStart()) > 0) current = current.getLeft();
            else current = current.getLeft();
        }

        return null;
    }

    /**
     * Returns sorted list of nodes greater than the given node.
     *
     * @param node RangedRedBlackNode to search for
     * @return List of RangedRedBlackNode greater than the given node.
     */
    public List<RangedRedBlackNode<T>> getGreaterThan(RangedRedBlackNode<T> node) {
        return getGreaterThan(root, node.getKey());
    }

    private List<RangedRedBlackNode<T>> getGreaterThan(RangedRedBlackNode<T> node, ClosedRange<T> key) {
        List<RangedRedBlackNode<T>> list = new ArrayList<>();
        if (isNullValue(node)) {
            return list;
        } else if (node.getKey().compareTo(key) > 0) {
            list.addAll(getGreaterThan(node.getLeft(), key));
            list.add(node);
            list.addAll(getGreaterThan(node.getRight(), key));
        } else {
            list.addAll(getGreaterThan(node.getRight(), key));
        }
        return list;
    }

    private boolean isNullValue(RangedRedBlackNode node) {
        return RangedRedBlackNode.isNullValue(node);
    }

    public void printOrderedNodes() {
        for (RangedRedBlackNode<T> range : this) {
            System.out.println(range.getKey());
        }
    }
}