package ldr.server.storage.drive;

import java.util.Iterator;
import java.util.NoSuchElementException;

import ldr.client.domen.Embedding;
import ldr.server.util.PeekIterator;
import ldr.server.storage.StorageManager;

public class GravedMergedIterator implements Iterator<Embedding> {
    private final PeekIterator out;
    private final PeekIterator in;
    private Embedding next;

    /**
     * Priority for out, out can have graves.
     */
    GravedMergedIterator(PeekIterator out, PeekIterator in) {
        this.out = out;
        this.in = in;
    }

    @Override
    public boolean hasNext() {
        if (next == null) {
            next = tryGetNext();
        }

        return next != null;
    }

    @Override
    public Embedding next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        var ans = next;
        next = null;
        return ans;
    }

    private Embedding tryGetNext() {
        if (out.hasNext() && in.hasNext() && out.peek().id() == in.peek().id()) {
            // Drop duplicate. Priority for inPeek.
            in.next();
            // If it was grave we just drop it.
            skipGraves(out);
        }

        if (!out.hasNext()) {
            return in.next();
        }

        if (!in.hasNext()) {
            skipGraves(out);
            return out.hasNext() ? out.next() : null;
        }

        return out.peek().id() < in.peek().id() ? out.next() : in.next();
    }

    private static void skipGraves(PeekIterator iter) {
        while (iter.hasNext() && StorageManager.isGrave(iter.peek())) {
            iter.next();
        }
    }
}
